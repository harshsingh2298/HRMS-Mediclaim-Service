package cms.cdl.com.mailingService.mailingService.Repository;



import cms.cdl.com.mailingService.mailingService.model.IdeaComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IdeaCommentRepository extends JpaRepository<IdeaComment, Long> {
    // Find all comments for a specific idea ID, ordered by creation time
    List<IdeaComment> findByIdeaIdOrderByCommentedAtDesc(Long ideaId);

    // Count comments for a specific idea post
    int countByIdeaId(Long ideaId);

    // Find all comments for a specific idea ID (for cascade deletion if idea is deleted)
    List<IdeaComment> findByIdeaId(Long ideaId);
}
