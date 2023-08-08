package poc.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poc.base.entity.Entity;

@Repository
public interface EntityRepository extends JpaRepository<Entity, Integer> {
}
