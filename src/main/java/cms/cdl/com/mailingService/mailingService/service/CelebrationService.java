package cms.cdl.com.mailingService.mailingService.service;


import cms.cdl.com.mailingService.mailingService.Repository.IdeaCommentRepository;
import cms.cdl.com.mailingService.mailingService.Repository.IdeaLikeRepository;
import cms.cdl.com.mailingService.mailingService.dto.CelebrationWishRequest;
import cms.cdl.com.mailingService.mailingService.dto.IdeaResponse;
import cms.cdl.com.mailingService.mailingService.dto.UpdateIdeaRequest;
import cms.cdl.com.mailingService.mailingService.model.Idea;
import cms.cdl.com.mailingService.mailingService.repo.IdeaRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.transaction.annotation.Transactional; // Import for transactional methods

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// NEW IMPORTS FOR LIKES AND COMMENTS
import cms.cdl.com.mailingService.mailingService.model.IdeaLike;
import cms.cdl.com.mailingService.mailingService.model.IdeaComment;

import cms.cdl.com.mailingService.mailingService.dto.IdeaLikeRequest;
import cms.cdl.com.mailingService.mailingService.dto.IdeaCommentRequest;
import cms.cdl.com.mailingService.mailingService.dto.IdeaCommentResponse;

@Service
public class CelebrationService {

    private static final Logger logger = LoggerFactory.getLogger(CelebrationService.class);

    private final EmailService emailService;
    private final TamplateService templateService;
    private final IdeaRepository ideaRepository;
    private final IdeaLikeRepository ideaLikeRepository; // NEW: Injected IdeaLikeRepository
    private final IdeaCommentRepository ideaCommentRepository; // NEW: Injected IdeaCommentRepository

    @Value("${spring.mail.username}")
    private String smtpFromEmail;

    @Value("${default.sender.name:CMS Portal}")
    private String defaultSenderName;

    @Autowired
    public CelebrationService(EmailService emailService, TamplateService templateService,
                              IdeaRepository ideaRepository, IdeaLikeRepository ideaLikeRepository,
                              IdeaCommentRepository ideaCommentRepository) {
        this.emailService = emailService;
        this.templateService = templateService;
        this.ideaRepository = ideaRepository;
        this.ideaLikeRepository = ideaLikeRepository; // Initialize IdeaLikeRepository
        this.ideaCommentRepository = ideaCommentRepository; // Initialize IdeaCommentRepository
    }

    /**
     * Prepares and sends a celebration wish, suggestion, or idea email using HTML templates.
     * For 'IDEA' type, it also saves the idea to the database.
     * @param request The CelebrationWishRequest DTO.
     * @return true if the email was successfully delegated and sent, false otherwise.
     */
    @Transactional // Ensure this method is transactional for DB operations
    public boolean sendCelebrationWish(CelebrationWishRequest request) {
        logger.info("Processing {} wish/submission for: {} ({}) from {}",
                request.getWishType(), request.getRecipientFullName(), request.getRecipientEmail(), request.getSenderEmail());

        String subject;
        String emailHtmlBody;
        String finalRecipientEmail = request.getRecipientEmail();
        String[] ccRecipients;

        switch (request.getWishType().toUpperCase()) {
            case "BIRTHDAY":
                subject = "Happy Birthday " + request.getRecipientFullName() + "!";
                emailHtmlBody = templateService.buildBirthdayTemplate(
                        request.getRecipientFullName(),
                        request.getSenderName(),
                        request.getMessageBody()
                );
                ccRecipients = new String[]{smtpFromEmail};
                if (StringUtils.hasText(request.getCcEmail())) {
                    ccRecipients = new String[]{smtpFromEmail, request.getCcEmail()};
                }
                break;
            case "ANNIVERSARY":
                subject = String.format("Happy Work Anniversary %s!", request.getRecipientFullName());
                emailHtmlBody = templateService.buildAnniversaryTemplate(
                        request.getRecipientFullName(),
                        request.getSenderName(),
                        request.getYearsOfService(),
                        request.getMessageBody()
                );
                ccRecipients = new String[]{smtpFromEmail};
                if (StringUtils.hasText(request.getCcEmail())) {
                    ccRecipients = new String[]{smtpFromEmail, request.getCcEmail()};
                }
                break;
            case "SUGGESTION":
                subject = "New Employee Suggestion from " + request.getSenderName();
                finalRecipientEmail = "ceo@cms.co.in";
                emailHtmlBody = templateService.buildSuggestionTemplate(
                        request.getSenderName(),
                        request.getMessageBody()
                );
                if (StringUtils.hasText(request.getCcEmail())) {
                    ccRecipients = new String[]{smtpFromEmail, request.getCcEmail()};
                } else {
                    ccRecipients = new String[]{smtpFromEmail};
                }
                break;
            case "IDEA":
                subject = "New Idea Submitted by " + request.getSenderName();
                finalRecipientEmail = "happitude@cms.co.in";
                emailHtmlBody = templateService.buildIdeaTemplate(
                        request.getSenderName(),
                        request.getMessageBody()
                );
                if (StringUtils.hasText(request.getCcEmail())) {
                    ccRecipients = new String[]{smtpFromEmail, request.getCcEmail()};
                } else {
                    ccRecipients = new String[]{smtpFromEmail};
                }

                // ✨ NEW: Save the idea to the database ✨
                try {
                    // Idea constructor needs to match the AllArgsConstructor or a custom one
                    Idea newIdea = new Idea(
                            null, // id will be generated
                            request.getSenderEmail(),
                            request.getSenderName(),
                            request.geteCode(),
                            request.getOccasion(),
                            request.getQuestion(),
                            request.getMessageBody(),
                            LocalDateTime.now(), // submissionDate
                            "PENDING", // Initial status for a new idea
                            0, // Initial likes
                            0  // Initial commentsCount
                    );
                    ideaRepository.save(newIdea);
                    logger.info("Idea from {} saved to database with ID: {}", request.getSenderEmail(), newIdea.getId());
                } catch (Exception e) {
                    logger.error("Failed to save idea to database from {}: {}", request.getSenderEmail(), e.getMessage(), e);
                    // Decide if email should still be sent if DB save fails. For now, it proceeds.
                }
                break;
            default:
                logger.warn("Unknown wish type received: {}. Falling back to plain text message.", request.getWishType());
                subject = "A Special Message from " + request.getSenderName();
                emailHtmlBody = "Dear " + request.getRecipientFullName() + ",\n\n" +
                        (request.getMessageBody() != null && !request.getMessageBody().isEmpty() ? request.getMessageBody() : "A message from an employee.") +
                        "\n\nBest regards,\n" + (request.getSenderName() != null && !request.getSenderName().isEmpty() ? request.getSenderName() : defaultSenderName);
                ccRecipients = new String[]{smtpFromEmail};
                if (StringUtils.hasText(request.getCcEmail())) {
                    ccRecipients = new String[]{smtpFromEmail, request.getCcEmail()};
                }
                break;
        }

        boolean sent = emailService.sendHtmlEmail(
                request.getSenderName(),
                finalRecipientEmail,
                subject,
                emailHtmlBody,
                ccRecipients
        );

        if (sent) {
            logger.info("{} delegation successful for: {} with CC: {}", request.getWishType(), finalRecipientEmail, String.join(", ", ccRecipients));
        } else {
            logger.warn("{} delegation failed for: {} with CC: {}. Check EmailService logs for details.", request.getWishType(), finalRecipientEmail, String.join(", ", ccRecipients));
        }
        return sent;
    }


    /**
     * Fetches all ideas from the database.
     * @return A list of IdeaResponse DTOs.
     */
    public List<IdeaResponse> getAllIdeas() {
        return ideaRepository.findAll().stream()
                .map(this::convertToIdeaResponse)
                .collect(Collectors.toList());
    }

    /**
     * Fetches a single idea by its ID.
     * @param id The ID of the idea.
     * @return An Optional containing the IdeaResponse DTO if found, empty otherwise.
     */
    public Optional<IdeaResponse> getIdeaById(Long id) {
        return ideaRepository.findById(id)
                .map(this::convertToIdeaResponse);
    }

    /**
     * Updates an existing idea.
     * @param id The ID of the idea to update.
     * @param request The UpdateIdeaRequest DTO containing fields to update.
     * @return An Optional containing the updated IdeaResponse DTO if found and updated, empty otherwise.
     */
    @Transactional
    public Optional<IdeaResponse> updateIdea(Long id, UpdateIdeaRequest request) {
        return ideaRepository.findById(id).map(idea -> {
            if (request.getMessageBody() != null) {
                idea.setMessageBody(request.getMessageBody());
            }
            if (request.getStatus() != null) {
                idea.setStatus(request.getStatus());
            }
            // Likes and commentsCount are updated via specific like/comment endpoints,
            // so they are generally not updated directly via UpdateIdeaRequest
            // if (request.getLikes() != null) {
            //     idea.setLikes(request.getLikes());
            // }
            // if (request.getCommentsCount() != null) {
            //     idea.setCommentsCount(request.getCommentsCount());
            // }
            if (request.geteCode() != null) {
                idea.seteCode(request.geteCode());
            }
            if (request.getOccasion() != null) {
                idea.setOccasion(request.getOccasion());
            }
            if (request.getQuestion() != null) {
                idea.setQuestion(request.getQuestion());
            }
            Idea updatedIdea = ideaRepository.save(idea);
            logger.info("Idea with ID {} updated.", updatedIdea.getId());
            return convertToIdeaResponse(updatedIdea);
        });
    }

    /**
     * Deletes an idea by its ID.
     * @param id The ID of the idea to delete.
     * @return true if deleted, false if not found.
     */
    @Transactional
    public boolean deleteIdea(Long id) {
        if (ideaRepository.existsById(id)) {
            // NEW: Delete associated likes and comments to maintain data integrity
            ideaLikeRepository.deleteAll(ideaLikeRepository.findByIdeaId(id));
            ideaCommentRepository.deleteAll(ideaCommentRepository.findByIdeaId(id));

            ideaRepository.deleteById(id);
            logger.info("Idea with ID {} deleted.", id);
            return true;
        }
        logger.warn("Attempted to delete non-existent idea with ID: {}", id);
        return false;
    }

    // NEW: Toggle like/unlike functionality for an idea
    @Transactional
    public boolean toggleIdeaLike(Long ideaId, IdeaLikeRequest likeRequest) {
        Optional<Idea> ideaOptional = ideaRepository.findById(ideaId);
        if (ideaOptional.isEmpty()) {
            logger.warn("Attempted to like/unlike non-existent idea with ID: {}", ideaId);
            return false; // Idea not found
        }

        Optional<IdeaLike> existingLike = ideaLikeRepository.findByIdeaIdAndLikerEcode(ideaId, likeRequest.getLikerEcode());

        if (existingLike.isPresent()) {
            // User already liked, so unlike
            ideaLikeRepository.delete(existingLike.get());
            Idea idea = ideaOptional.get();
            idea.setLikes(idea.getLikes() - 1); // Decrement count
            ideaRepository.save(idea); // Save updated idea
            logger.info("Idea ID {} unliked by eCode {}", ideaId, likeRequest.getLikerEcode());
            return false; // Indicates unliked
        } else {
            // User has not liked, so add like
            IdeaLike newLike = new IdeaLike(null, ideaId, likeRequest.getLikerEcode(), likeRequest.getLikerName(), null);
            ideaLikeRepository.save(newLike);
            Idea idea = ideaOptional.get();
            idea.setLikes(idea.getLikes() + 1); // Increment count
            ideaRepository.save(idea); // Save updated idea
            logger.info("Idea ID {} liked by eCode {}", ideaId, likeRequest.getLikerEcode());
            return true; // Indicates liked
        }
    }

    // NEW: Check if user has liked an idea
    public boolean hasUserLikedIdea(Long ideaId, String likerEcode) {
        return ideaLikeRepository.findByIdeaIdAndLikerEcode(ideaId, likerEcode).isPresent();
    }

    // NEW: Get comments for an idea post
    public List<IdeaCommentResponse> getCommentsForIdea(Long ideaId) {
        return ideaCommentRepository.findByIdeaIdOrderByCommentedAtDesc(ideaId).stream()
                .map(this::convertToIdeaCommentResponse)
                .collect(Collectors.toList());
    }

    // NEW: Add a comment to an idea post
    @Transactional
    public Optional<IdeaCommentResponse> addCommentToIdea(Long ideaId, IdeaCommentRequest commentRequest) {
        Optional<Idea> ideaOptional = ideaRepository.findById(ideaId);
        if (ideaOptional.isEmpty()) {
            logger.warn("Attempted to add comment to non-existent idea with ID: {}", ideaId);
            return Optional.empty(); // Idea not found
        }

        IdeaComment newComment = new IdeaComment(
                null, // ID will be generated
                ideaId,
                commentRequest.getCommenterEcode(),
                commentRequest.getCommenterName(),
                commentRequest.getCommentText(),
                null // Timestamp handled by @PrePersist
        );
        IdeaComment savedComment = ideaCommentRepository.save(newComment);

        // Update comment count on the idea
        Idea idea = ideaOptional.get();
        idea.setCommentsCount(idea.getCommentsCount() + 1);
        ideaRepository.save(idea);
        logger.info("Comment added to idea ID {} by eCode {}", ideaId, commentRequest.getCommenterEcode());

        return Optional.of(convertToIdeaCommentResponse(savedComment));
    }

    // Helper method to convert Idea entity to IdeaResponse DTO
    private IdeaResponse convertToIdeaResponse(Idea idea) {
        return new IdeaResponse(
                idea.getId(),
                idea.getSenderEmail(),
                idea.getSenderName(),
                idea.geteCode(),
                idea.getOccasion(),
                idea.getQuestion(),
                idea.getMessageBody(),
                idea.getSubmissionDate(),
                idea.getStatus(),
                idea.getLikes(),
                idea.getCommentsCount()
        );
    }

    // Helper method to convert IdeaComment entity to IdeaCommentResponse DTO
    private IdeaCommentResponse convertToIdeaCommentResponse(IdeaComment comment) {
        return new IdeaCommentResponse(
                comment.getId(),
                comment.getIdeaId(),
                comment.getCommenterEcode(),
                comment.getCommenterName(),
                comment.getCommentText(),
                comment.getCommentedAt()
        );
    }
}
