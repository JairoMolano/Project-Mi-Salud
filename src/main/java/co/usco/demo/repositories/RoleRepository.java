package co.usco.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import co.usco.demo.models.RoleModel;

@Repository
public interface RoleRepository extends JpaRepository<RoleModel, Long>{

    Optional<RoleModel> findByRoleName(String roleName);
    
}
