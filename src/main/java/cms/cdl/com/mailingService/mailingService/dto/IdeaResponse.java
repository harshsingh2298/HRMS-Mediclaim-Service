package cms.cdl.com.mailingService.mailingService.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter

public class IdeaResponse {
    private Long id;
    private String senderEmail;
    private String senderName;
    private String eCode;
    private String occasion;
    private String question;
    private String messageBody;
    private LocalDateTime submissionDate;
    private String status;
    private int likes;
    private int commentsCount;


    public IdeaResponse() {
    }

    public IdeaResponse(Long id, String senderEmail, String senderName, String eCode, String occasion, String question, String messageBody, LocalDateTime submissionDate, String status, int likes, int commentsCount) {
        this.id = id;
        this.senderEmail = senderEmail;
        this.senderName = senderName;
        this.eCode = eCode;
        this.occasion = occasion;
        this.question = question;
        this.messageBody = messageBody;
        this.submissionDate = submissionDate;
        this.status = status;
        this.likes = likes;
        this.commentsCount = commentsCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String geteCode() {
        return eCode;
    }

    public void seteCode(String eCode) {
        this.eCode = eCode;
    }

    public String getOccasion() {
        return occasion;
    }

    public void setOccasion(String occasion) {
        this.occasion = occasion;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public LocalDateTime getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(LocalDateTime submissionDate) {
        this.submissionDate = submissionDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }
}
