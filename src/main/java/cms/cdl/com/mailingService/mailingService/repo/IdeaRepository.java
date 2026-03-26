package cms.cdl.com.mailingService.mailingService.repo;

import org.springframework.data.jpa.repository.JpaRepository;



import cms.cdl.com.mailingService.mailingService.model.Idea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IdeaRepository extends JpaRepository<Idea, Long> {
    // You can add custom query methods here if needed, e.g.:
    List<Idea> findByStatus(String status);
}