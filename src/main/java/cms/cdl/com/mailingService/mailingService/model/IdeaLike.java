package cms.cdl.com.mailingService.mailingService.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "idea_likes") // Table to store individual likes for ideas
@Data

public class IdeaLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long ideaId; // ID of the idea being liked

    @Column(nullable = false)
    private String likerEcode; // Employee code of the user who liked

    @Column(nullable = false)
    private String likerName; // Name of the user who liked

    @Column(nullable = false)
    private LocalDateTime likedAt; // Timestamp of when the like occurred

    public IdeaLike() {
    }

    public IdeaLike(Long id, Long ideaId, String likerEcode, String likerName, LocalDateTime likedAt) {
        this.id = id;
        this.ideaId = ideaId;
        this.likerEcode = likerEcode;
        this.likerName = likerName;
        this.likedAt = likedAt;
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

    public String getLikerEcode() {
        return likerEcode;
    }

    public void setLikerEcode(String likerEcode) {
        this.likerEcode = likerEcode;
    }

    public String getLikerName() {
        return likerName;
    }

    public void setLikerName(String likerName) {
        this.likerName = likerName;
    }

    public LocalDateTime getLikedAt() {
        return likedAt;
    }

    public void setLikedAt(LocalDateTime likedAt) {
        this.likedAt = likedAt;
    }

    @PrePersist
    protected void onCreate() {
        likedAt = LocalDateTime.now();
    }
}
