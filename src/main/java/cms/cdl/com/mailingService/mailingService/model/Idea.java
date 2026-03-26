package cms.cdl.com.mailingService.mailingService.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor; // Added AllArgsConstructor for convenience

@Entity
@Table(name = "ideas") // Defines the table name in the database
// Generates a constructor with all fields
public class Idea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String senderEmail;

    @Column(nullable = false)
    private String senderName;

    @Column(nullable = true) // E. Code might be optional depending on your system
    private String eCode; // NEW: Employee Code

    @Column(nullable = true) // Occasion might be optional
    private String occasion; // NEW: e.g., "Independence Day"

    @Column(nullable = true, length = 5000) // Question might be optional, added length
    private String question; // NEW: e.g., "What IF" India in 2047 could see us today?"

    @Column(nullable = false, length = 10000) // Increased length for message body
    private String messageBody;

    @Column(nullable = false)
    private LocalDateTime submissionDate;

    @Column(nullable = false)
    private String status; // e.g., "PENDING", "REVIEWED", "PUBLISHED", "ARCHIVED"

    @Column(nullable = false) // Ensure likes is not null
    private int likes = 0; // Initialize to 0

    @Column(nullable = false) // Ensure commentsCount is not null
    private int commentsCount = 0; // Initialize to 0

    public Idea() {
    }

    public Idea(Long id, String senderEmail, String senderName, String eCode, String occasion, String question, String messageBody, LocalDateTime submissionDate, String status, int likes, int commentsCount) {
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

    @PrePersist
    protected void onCreate() {
        if (submissionDate == null) {
            submissionDate = LocalDateTime.now();
        }
        if (status == null) {
            status = "PENDING";
        }
        // Ensure likes and commentsCount are initialized to 0 for new ideas
        if (this.likes < 0) this.likes = 0;
        if (this.commentsCount < 0) this.commentsCount = 0;
    }
}
