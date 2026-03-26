package cms.cdl.com.mailingService.mailingService.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class IdeaCommentResponse {
    private Long id;
    private Long ideaId;
    private String commenterEcode;
    private String commenterName;
    private String commentText;
    private LocalDateTime commentedAt;


    public IdeaCommentResponse(Long id, Long ideaId, String commenterEcode, String commenterName, String commentText, LocalDateTime commentedAt) {
        this.id = id;
        this.ideaId = ideaId;
        this.commenterEcode = commenterEcode;
        this.commenterName = commenterName;
        this.commentText = commentText;
        this.commentedAt = commentedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdeaId() {
        return ideaId;
    }

    public void setIdeaId(Long ideaId) {
        this.ideaId = ideaId;
    }

    public String getCommenterEcode() {
        return commenterEcode;
    }

    public void setCommenterEcode(String commenterEcode) {
        this.commenterEcode = commenterEcode;
    }

    public String getCommenterName() {
        return commenterName;
    }

    public void setCommenterName(String commenterName) {
        this.commenterName = commenterName;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public LocalDateTime getCommentedAt() {
        return commentedAt;
    }

    public void setCommentedAt(LocalDateTime commentedAt) {
        this.commentedAt = commentedAt;
    }
}
