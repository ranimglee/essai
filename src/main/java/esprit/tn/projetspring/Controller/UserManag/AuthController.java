package esprit.tn.projetspring.Controller.UserManag;

import esprit.tn.projetspring.Entity.ErrorResponse;
import esprit.tn.projetspring.Entity.FuneralManag.TypeReligion;
import esprit.tn.projetspring.Entity.UserManag.Role;
import esprit.tn.projetspring.Entity.UserManag.TypeRole;
import esprit.tn.projetspring.Entity.UserManag.User;
import esprit.tn.projetspring.Entity.dto.request.ChangePasswordRequest;
import esprit.tn.projetspring.Entity.dto.request.RegisterRequest;
import esprit.tn.projetspring.Entity.dto.request.UpdateUserRequest;
import esprit.tn.projetspring.Entity.exception.EmailExistsExecption;
import esprit.tn.projetspring.Interface.UserManag.AuthentificationInterface;
import esprit.tn.projetspring.Interface.UserManag.RoleInterface;
import esprit.tn.projetspring.Service.UserManag.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
  private AuthentificationInterface authentificationInterface;
    @Autowired
    private UserDetailsService userDetailsService;
@Autowired
private RoleInterface roleInterface;

    @GetMapping("/roles")
    public List<Role> getAllRoles() {
        return roleInterface.retrieveRole();
    }
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("password") String password,
            @RequestParam("mobileNumber") String mobileNumber,
            @RequestParam("email") String email,
            @RequestParam("adressUser") String address,
            @RequestParam("dateNaiss") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateNaiss,
            @RequestParam("religion") TypeReligion religion,
            @RequestParam("sexe") String sexe,
            @RequestParam("roleNames") List<String> roleNames,
            @RequestParam("imageFile") MultipartFile imageFile
    ) {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setFirstName(firstName);
        registerRequest.setLastName(lastName);
        registerRequest.setPassword(password);
        registerRequest.setMobileNumber(mobileNumber);
        registerRequest.setEmail(email);
        registerRequest.setAdressUser(address);
        registerRequest.setDateNaiss(dateNaiss);
        registerRequest.setReligion(religion);
        registerRequest.setSexe(sexe);

        try {
            User newUser = authentificationInterface.register(registerRequest, imageFile,roleNames);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (EmailExistsExecption e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    @PostMapping("/token")
    public ResponseEntity<Map<String, String>> jwtToken(
            @RequestParam String username,
            @RequestParam String password) {

        if (authentificationInterface.isUserSuspended(username)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("errorMessage", "Your account is suspended. Please try again later."));
        }

        Map<String, String> response = authentificationInterface.jwtToken(username, password);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/africa")
    @PreAuthorize("hasAuthority('SCOPE_PATIENT')")
    public String displayData(){
        return "hello my friend";
    }



    @GetMapping("/logout")
    public ResponseEntity<String> logout() {
        // Invalidate current authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        return ResponseEntity.ok("Logout successful");
    }

    @GetMapping("/current-user")
    public <T> ResponseEntity<T> getCurrentUser(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(null); // or any appropriate response
        }

        try {
            User currentUser = (User) this.userDetailsService.loadUserByUsername(principal.getName());
            return ResponseEntity.ok((T) currentUser); // Cast to generic type T
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body((T) new ErrorResponse("User not found")); // Cast to generic type T
        }
    }
    @PutMapping("/updateCurrent")
    public ResponseEntity<String> updateCurrentUser(Principal principal, @RequestBody UpdateUserRequest updatedUser) {
        try {
            authentificationInterface.updateCurrentUser(principal, updatedUser);
            return ResponseEntity.ok("User updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update user: " + e.getMessage());
        }
    }
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request, Principal principal) {
        try {
            authentificationInterface.changePassword(request, principal);
            return ResponseEntity.ok("Password changed successfully");
        } catch (UsernameNotFoundException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to change password: " + e.getMessage());
        }
    }

    @GetMapping("/confirm-account")
    public ResponseEntity<String> confirmAccount(@RequestParam("token") String confirmationToken) {
        // Call the authentication service method to confirm the account
        boolean isAccountConfirmed = authentificationInterface.confirmAccount(confirmationToken);

        if (isAccountConfirmed) {
            return ResponseEntity.ok("Your account has been successfully confirmed.");
        } else {
            return ResponseEntity.badRequest().body("Invalid confirmation token.");
        }
    }

    @GetMapping("/facebook")
    public String facebookLogin() {
        return "redirect:/oauth2/authorization/facebook";
    }
}
