package esprit.tn.projetspring.Repository.UserManag;


import esprit.tn.projetspring.Entity.UserManag.Role;
import esprit.tn.projetspring.Entity.UserManag.TypeRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByRoleName(TypeRole roleName);
}
