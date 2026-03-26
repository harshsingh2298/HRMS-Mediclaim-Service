package cms.cdl.com.mailingService.mailingService.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor; // Explicitly add NoArgsConstructor for clarity, though @Data covers it implicitly
import lombok.AllArgsConstructor; // Explicitly add AllArgsConstructor for convenience

@Data // Generates getters, setters, toString, equals, hashCode, and a no-arg constructor

public class CelebrationWishRequest {
    @NotBlank(message = "Sender email cannot be blank")
    @Email(message = "Invalid sender email format")
    private String senderEmail; // This should match spring.mail.username

    @NotBlank(message = "Sender name cannot be blank")
    private String senderName; // Your name from frontend

    @NotBlank(message = "Recipient email cannot be blank")
    @Email(message = "Invalid recipient email format")
    private String recipientEmail;

    @NotBlank(message = "Recipient full name cannot be blank")
    private String recipientFullName;

    @NotBlank(message = "Message body cannot be blank")
    private String messageBody;

    @NotBlank(message = "Wish type cannot be blank")
    private String wishType; // e.g., "BIRTHDAY", "ANNIVERSARY", "SUGGESTION", "IDEA"

    // Optional: For anniversary, if you want to include years of service
    private Integer yearsOfService;

    // Optional: For custom image URL in email (if you decide to use it from frontend)
    private String imageUrl;

    // NEW: For custom CC email from frontend (e.g., user's email)
    private String ccEmail;


    private String eCode;
    private String occasion;
    private String question;

    public CelebrationWishRequest() {
    }

    public CelebrationWishRequest(String senderEmail, String senderName, String recipientEmail, String recipientFullName, String messageBody, String wishType, Integer yearsOfService, String imageUrl, String ccEmail, String eCode, String occasion, String question) {
        this.senderEmail = senderEmail;
        this.senderName = senderName;
        this.recipientEmail = recipientEmail;
        this.recipientFullName = recipientFullName;
        this.messageBody = messageBody;
        this.wishType = wishType;
        this.yearsOfService = yearsOfService;
        this.imageUrl = imageUrl;
        this.ccEmail = ccEmail;
        this.eCode = eCode;
        this.occasion = occasion;
        this.question = question;
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

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public String getRecipientFullName() {
        return recipientFullName;
    }

    public void setRecipientFullName(String recipientFullName) {
        this.recipientFullName = recipientFullName;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public String getWishType() {
        return wishType;
    }

    public void setWishType(String wishType) {
        this.wishType = wishType;
    }

    public Integer getYearsOfService() {
        return yearsOfService;
    }

    public void setYearsOfService(Integer yearsOfService) {
        this.yearsOfService = yearsOfService;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCcEmail() {
        return ccEmail;
    }

    public void setCcEmail(String ccEmail) {
        this.ccEmail = ccEmail;
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
}