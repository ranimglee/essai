package esprit.tn.projetspring.Controller.UserManag;


import esprit.tn.projetspring.Entity.UserManag.Role;
import esprit.tn.projetspring.Interface.UserManag.RoleInterface;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin("*")
@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("api/roles")
public class RoleController {

    RoleInterface roleInterface;

    @GetMapping("/get")
    public List<Role> retrieveRoles() {
        return roleInterface.retrieveRole();
    }


    @GetMapping("/retrieveRole/{idRole}")
    public Role retrieveRole(@PathVariable("idRole") long idRole) {
        return roleInterface.retrieveRole(idRole);
    }


}
