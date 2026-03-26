package cms.cdl.com.mailingService.mailingService.dto;



import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



public class UpdateIdeaRequest {
    private String messageBody;
    private String status;
    private Integer likes;
    private Integer commentsCount;
    private String eCode;    // Allow updating eCode
    private String occasion; // Allow updating occasion
    private String question; // Allow updating question


    public UpdateIdeaRequest() {
    }

    public UpdateIdeaRequest(String messageBody, String status, Integer likes, Integer commentsCount, String eCode, String occasion, String question) {
        this.messageBody = messageBody;
        this.status = status;
        this.likes = likes;
        this.commentsCount = commentsCount;
        this.eCode = eCode;
        this.occasion = occasion;
        this.question = question;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(Integer commentsCount) {
        this.commentsCount = commentsCount;
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