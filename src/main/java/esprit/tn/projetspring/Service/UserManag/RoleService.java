package esprit.tn.projetspring.Service.UserManag;


import esprit.tn.projetspring.Entity.UserManag.Role;
import esprit.tn.projetspring.Interface.UserManag.RoleInterface;
import esprit.tn.projetspring.Repository.UserManag.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor

public class RoleService implements RoleInterface {

    RoleRepository roleRepository;

    @Override
    public Role retrieveRole(Long idRole) {
        return roleRepository.findById(idRole).orElse(null);
    }

    @Override
    public List<Role> retrieveRole() {
        return  roleRepository.findAll();

    }





}
