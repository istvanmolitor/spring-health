package molitor.springhealth.exercise;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseGroupRepository extends JpaRepository<ExerciseGroup, Long> {

    Page<ExerciseGroup> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
