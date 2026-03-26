package cms.cdl.com.mailingService.mailingService.Repository;

import cms.cdl.com.mailingService.mailingService.model.IdeaLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional; // Import for transactional operations

import java.util.Optional;
import java.util.List;

@Repository
public interface IdeaLikeRepository extends JpaRepository<IdeaLike, Long> {
    // Find a like by idea ID and liker's employee code (for checking if already liked)
    Optional<IdeaLike> findByIdeaIdAndLikerEcode(Long ideaId, String likerEcode);

    // Count likes for a specific idea post
    int countByIdeaId(Long ideaId);

    // Delete a like by idea ID and liker's employee code (for unliking)
    @Transactional
    void deleteByIdeaIdAndLikerEcode(Long ideaId, String likerEcode);

    // Find all likes for a specific idea ID (for cascade deletion if idea is deleted)
    List<IdeaLike> findByIdeaId(Long ideaId);
}
