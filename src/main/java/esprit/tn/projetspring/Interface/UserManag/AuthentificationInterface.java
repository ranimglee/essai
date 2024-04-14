package esprit.tn.projetspring.Interface.UserManag;

import esprit.tn.projetspring.Entity.UserManag.User;
import esprit.tn.projetspring.Entity.dto.request.ChangePasswordRequest;
import esprit.tn.projetspring.Entity.dto.request.RegisterRequest;
import esprit.tn.projetspring.Entity.dto.request.UpdateUserRequest;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface AuthentificationInterface {
     User register(RegisterRequest registerRequest, MultipartFile imageFile, List<String> roleNames) ;
     Map<String, String> jwtToken(String username, String password);
     void handleSuccessfulLogin(String username);
     boolean isUserSuspended(String username);
     void handleFailedLogin(String username);
      boolean confirmAccount(String confirmationToken) ;

      void updateCurrentUser(Principal connectedUser, UpdateUserRequest updatedUser) ;

      void changePassword(ChangePasswordRequest request, Principal connectedUser);
     User getCurrentUser(Principal connectedUser) ;

    }
