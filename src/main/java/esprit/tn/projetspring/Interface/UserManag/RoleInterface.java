package esprit.tn.projetspring.Interface.UserManag;



import esprit.tn.projetspring.Entity.UserManag.Role;

import java.util.List;

public interface RoleInterface {

    Role retrieveRole(Long idRole);
    List<Role> retrieveRole();

}
