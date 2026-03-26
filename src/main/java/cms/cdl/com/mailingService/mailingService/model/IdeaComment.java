package cms.cdl.com.mailingService.mailingService.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "idea_comments") // Table to store individual comments for ideas
@Data

public class IdeaComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long ideaId; // ID of the idea being commented on

    @Column(nullable = false)
    private String commenterEcode; // Employee code of the user who commented

    @Column(nullable = false)
    private String commenterName; // Name of the user who commented

    @Column(nullable = false, columnDefinition = "TEXT")
    private String commentText; // The actual comment text

    @Column(nullable = false)
    private LocalDateTime commentedAt; // Timestamp of when the comment was made

    public IdeaComment(Long id, Long ideaId, String commenterEcode, String commenterName, String commentText, LocalDateTime commentedAt) {
        this.id = id;
        this.ideaId = ideaId;
        this.commenterEcode = commenterEcode;
        this.commenterName = commenterName;
        this.commentText = commentText;
        this.commentedAt = commentedAt;
    }

    public IdeaComment() {
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

    @PrePersist
    protected void onCreate() {
        commentedAt = LocalDateTime.now();
    }
}
