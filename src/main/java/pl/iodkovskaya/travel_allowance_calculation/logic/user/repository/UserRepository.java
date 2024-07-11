package pl.iodkovskaya.travel_allowance_calculation.logic.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.iodkovskaya.travel_allowance_calculation.logic.user.model.entity.UserEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByPesel(Long Pesel);
}
