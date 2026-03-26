package cms.cdl.com.mailingService.mailingService.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor

public class IdeaCommentRequest {
    private String commenterEcode;
    private String commenterName;
    private String commentText;


    public IdeaCommentRequest(String commenterEcode, String commenterName, String commentText) {
        this.commenterEcode = commenterEcode;
        this.commenterName = commenterName;
        this.commentText = commentText;
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
}
