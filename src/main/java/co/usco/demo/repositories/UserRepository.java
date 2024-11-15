package co.usco.demo.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import co.usco.demo.models.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long>{
    Optional<UserModel> findByUsername(String username);

    Optional<UserModel> findByDocumentNumber(String documentNumber);
}