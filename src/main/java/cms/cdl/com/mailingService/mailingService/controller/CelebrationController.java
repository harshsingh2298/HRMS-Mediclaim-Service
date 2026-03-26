package cms.cdl.com.mailingService.mailingService.controller;


import cms.cdl.com.mailingService.mailingService.dto.CelebrationWishRequest;
import cms.cdl.com.mailingService.mailingService.dto.IdeaResponse;
import cms.cdl.com.mailingService.mailingService.dto.UpdateIdeaRequest;
import cms.cdl.com.mailingService.mailingService.service.CelebrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

// NEW IMPORTS FOR LIKES AND COMMENTS
import cms.cdl.com.mailingService.mailingService.dto.IdeaLikeRequest;
import cms.cdl.com.mailingService.mailingService.dto.IdeaCommentRequest;
import cms.cdl.com.mailingService.mailingService.dto.IdeaCommentResponse;


@RestController
@RequestMapping("/api/celebration")
@CrossOrigin(origins = "*") // Be specific about origins in production for security (e.g., "http://localhost:3000")
public class CelebrationController {

    private static final Logger logger = LoggerFactory.getLogger(CelebrationController.class);

    private final CelebrationService celebrationService;

    @Autowired
    public CelebrationController(CelebrationService celebrationService) {
        this.celebrationService = celebrationService;
    }

    /**
     * Endpoint for sending various types of wishes/submissions.
     * For 'IDEA' type, this will also trigger saving the idea to the database.
     * POST /api/celebration/send-wish
     * @param request The CelebrationWishRequest DTO.
     * @return ResponseEntity indicating success or failure.
     */
    @PostMapping("/send-wish")
    public ResponseEntity<String> sendWish(@Valid @RequestBody CelebrationWishRequest request) {
        logger.info("Received request to send a {} wish to {}", request.getWishType(), request.getRecipientEmail());
        // Basic validation (more robust validation can be in DTO with @Valid annotations)
        if (request.getRecipientEmail() == null || request.getRecipientEmail().isEmpty()) {
            logger.warn("Attempt to send wish with missing recipient email.");
            return new ResponseEntity<>("Recipient email is required.", HttpStatus.BAD_REQUEST);
        }
        if (request.getSenderEmail() == null || request.getSenderEmail().isEmpty()) {
            logger.warn("Attempt to send wish with missing sender email.");
            return new ResponseEntity<>("Sender email is required.", HttpStatus.BAD_REQUEST);
        }
        if (request.getMessageBody() == null || request.getMessageBody().isEmpty()) {
            logger.warn("Attempt to send wish with empty message body.");
            return new ResponseEntity<>("Message body cannot be empty.", HttpStatus.BAD_REQUEST);
        }
        if (request.getWishType() == null || request.getWishType().isEmpty()) {
            logger.warn("Attempt to send wish with missing wish type.");
            return new ResponseEntity<>("Wish type is required (e.g., BIRTHDAY, ANNIVERSARY, SUGGESTION, IDEA).", HttpStatus.BAD_REQUEST);
        }

        try {
            boolean sent = celebrationService.sendCelebrationWish(request);
            if (sent) {
                logger.info("Celebration wish successfully processed for {}", request.getRecipientEmail());
                return new ResponseEntity<>("Wish sent successfully!", HttpStatus.OK);
            } else {
                logger.error("Failed to send celebration wish for {}. Check service logs.", request.getRecipientEmail());
                return new ResponseEntity<>("Failed to send wish. Please try again.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            logger.error("An unexpected error occurred while sending wish to {}: {}", request.getRecipientEmail(), e.getMessage(), e);
            return new ResponseEntity<>("An internal server error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Fetches all submitted ideas.
     * GET /api/celebration/ideas
     * @return A list of IdeaResponse DTOs.
     */
    @GetMapping("/ideas")
    public ResponseEntity<List<IdeaResponse>> getAllIdeas() {
        logger.info("Fetching all ideas.");
        List<IdeaResponse> ideas = celebrationService.getAllIdeas();
        return new ResponseEntity<>(ideas, HttpStatus.OK);
    }

    /**
     * Fetches a single idea by its ID.
     * GET /api/celebration/ideas/{id}
     * @param id The ID of the idea.
     * @return The IdeaResponse DTO if found, or 404 Not Found.
     */
    @GetMapping("/ideas/{id}")
    public ResponseEntity<IdeaResponse> getIdeaById(@PathVariable Long id) {
        logger.info("Fetching idea with ID: {}", id);
        Optional<IdeaResponse> idea = celebrationService.getIdeaById(id);
        return idea.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> {
                    logger.warn("Idea with ID {} not found.", id);
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                });
    }

    /**
     * Updates an existing idea by its ID.
     * PUT /api/celebration/ideas/{id}
     * @param id The ID of the idea to update.
     * @param request The UpdateIdeaRequest DTO containing fields to update.
     * @return The updated IdeaResponse DTO if successful, or 404 Not Found if idea doesn't exist.
     */
    @PutMapping("/ideas/{id}")
    public ResponseEntity<IdeaResponse> updateIdea(@PathVariable Long id, @RequestBody UpdateIdeaRequest request) {
        logger.info("Updating idea with ID: {}", id);
        Optional<IdeaResponse> updatedIdea = celebrationService.updateIdea(id, request);
        return updatedIdea.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> {
                    logger.warn("Failed to update idea with ID {}: Not found.", id);
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                });
    }

    /**
     * Deletes an idea by its ID.
     * DELETE /api/celebration/ideas/{id}
     * @param id The ID of the idea to delete.
     * @return ResponseEntity indicating success or 404 Not Found.
     */
    @DeleteMapping("/ideas/{id}")
    public ResponseEntity<Void> deleteIdea(@PathVariable Long id) {
        logger.info("Deleting idea with ID: {}", id);
        boolean deleted = celebrationService.deleteIdea(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content for successful deletion
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found if idea doesn't exist
        }
    }

    // NEW: Endpoint to toggle a like on an idea post
    // Returns 200 OK and true if liked, 200 OK and false if unliked, 404 if idea not found
    @PostMapping("/ideas/{id}/like")
    public ResponseEntity<Boolean> toggleIdeaLike(@PathVariable Long id, @RequestBody IdeaLikeRequest likeRequest) {
        boolean liked = celebrationService.toggleIdeaLike(id, likeRequest);
        return new ResponseEntity<>(liked, HttpStatus.OK);
    }

    // NEW: Endpoint to check if a user has liked an idea post
    @GetMapping("/ideas/{id}/hasLiked")
    public ResponseEntity<Boolean> hasUserLikedIdea(@PathVariable Long id, @RequestParam String likerEcode) {
        boolean hasLiked = celebrationService.hasUserLikedIdea(id, likerEcode);
        return new ResponseEntity<>(hasLiked, HttpStatus.OK);
    }

    // NEW: Endpoint to get comments for an idea post
    @GetMapping("/ideas/{id}/comments")
    public ResponseEntity<List<IdeaCommentResponse>> getIdeaComments(@PathVariable Long id) {
        List<IdeaCommentResponse> comments = celebrationService.getCommentsForIdea(id);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    // NEW: Endpoint to add a comment to an idea post
    @PostMapping("/ideas/{id}/comments")
    public ResponseEntity<IdeaCommentResponse> addIdeaComment(@PathVariable Long id, @RequestBody IdeaCommentRequest commentRequest) {
        Optional<IdeaCommentResponse> savedComment = celebrationService.addCommentToIdea(id, commentRequest);
        return savedComment.map(value -> new ResponseEntity<>(value, HttpStatus.CREATED))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
