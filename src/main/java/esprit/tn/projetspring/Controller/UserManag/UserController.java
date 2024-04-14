package esprit.tn.projetspring.Controller.UserManag;


import esprit.tn.projetspring.Entity.UserManag.ForgotPasswordRequest;
import esprit.tn.projetspring.Entity.UserManag.User;
import esprit.tn.projetspring.Entity.dto.request.ProfileRequest;
import esprit.tn.projetspring.Entity.exception.NotFoundExecption;
import esprit.tn.projetspring.Interface.UserManag.EmailInterface;
import esprit.tn.projetspring.Interface.UserManag.UserInterface;
import esprit.tn.projetspring.Repository.UserManag.UserRepository;
import esprit.tn.projetspring.Service.UserManag.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@CrossOrigin("*")

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("api/users")
public class UserController {
    UserInterface userInterface;
    EmailInterface emailInterface;
    UserRepository userRepository;

    @GetMapping("/retrieveUsers")
    public List<User> retrieveUsers() {
        return userInterface.retrieveUser();
    }


    @GetMapping("/retrieveUser/{id}")
    public User retrieveUser(@PathVariable("id") long id) {
        return userInterface.retrieveUser(id);
    }


   /* @PostMapping("/addUser")
    public User addUser(@RequestBody User user) {
        return userInterface.addUser(user);
    }*/

    @PutMapping("/updateUser")
    public User updateUser(@RequestBody User user) {
        return userInterface.updateUser(user);
    }


    @DeleteMapping("/removeUser/{id}")
    public void removeUser(@PathVariable("id") long id) {
        userInterface.removeUser(id);
    }


    @GetMapping("/all/{offset}/{pageSige}/{field}")
    public ResponseEntity<Page<User>> findUserswithPaginationAndSorting(@PathVariable int offset, @PathVariable int pageSige, @PathVariable String field) {
        return new ResponseEntity<>(userInterface.findUserswithPaginationAndSorting(offset, pageSige, field), HttpStatus.OK);
    }

    @GetMapping("/findById/{idUser}")
    public ResponseEntity<?> findUserById(@PathVariable Long idUser) {
        try {
            return new ResponseEntity<>(userInterface.findUserById(idUser), HttpStatus.OK);
        } catch (NotFoundExecption execption) {
            return new ResponseEntity<>(execption.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/modify/{idUser}")
    public ResponseEntity<?> manageProfile(@PathVariable Long idUser, @RequestBody ProfileRequest profile) {
        try {
            return new ResponseEntity<>(userInterface.manageProfile(idUser, profile), HttpStatus.OK);
        } catch (NotFoundExecption execption) {
            return new ResponseEntity<>(execption.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<User> getUserProfile(@PathVariable Long userId) {
        try {
            User user = userInterface.getProfile(userId);
            return ResponseEntity.ok(user);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /* @PostMapping("/forgot-password")
     public String forgotPassword(@RequestParam("email") String userEmail) {
         userInterface.sendResetPasswordEmail(userEmail);
         return "Password reset email sent successfully";
     }
 */
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        try {
            // Check if the user with the provided email exists
            User user = userRepository.findByEmailUser(forgotPasswordRequest.getEmail());
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User not found with email: " + forgotPasswordRequest.getEmail());
            }

            // Generate a reset code (you can adjust this logic based on your requirements)
            String resetCode = generateCode();

            // Update user's reset code in the database
            user.setResetCode(resetCode);
            userInterface.updateUser(user);

            // Send an email with the reset code
            String emailSubject = "Password Reset";
            String emailBody = "Your password reset code is: " + resetCode;
            emailInterface.sendEmail(user.getEmailUser(), emailSubject, emailBody);

            // Return a JSON response
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("message", "Password reset code sent successfully to " + user.getEmailUser());
            return ResponseEntity.ok(jsonResponse.toString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }


    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    private String generateCode() {
        return UUID.randomUUID().toString().substring(0, 6); // Generate a 6-character code
    }


    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam("email") String userEmail,
                                           @RequestParam("code") String resetCode,
                                           @RequestParam("newPassword") String newPassword) {
        try {
            // Check if any of the required parameters are missing
            if (userEmail == null || resetCode == null || newPassword == null) {
                // Return a JSON response with a bad request status and error message
                return ResponseEntity.badRequest().body(new ErrorResponse("Missing required parameters."));
            }

            // Find the user by email
            User user = userRepository.findByEmailUser(userEmail);
            if (user != null && user.getResetCode() != null && user.getResetCode().equals(resetCode)) {
                // Update user's password
                String encodedPassword = bCryptPasswordEncoder.encode(newPassword);
                user.setMdpUser(encodedPassword);
                // Clear the reset code after password is reset
                user.setResetCode(null);
                userRepository.save(user); // Save the updated user object
            } else {
                // If the user is not found or the reset code is invalid, return an error response
                return ResponseEntity.badRequest().body(new ErrorResponse("Invalid reset code or user not found."));
            }

            // Return a JSON response with a success message
            return ResponseEntity.ok(new SuccessResponse("Password reset successfully."));
        } catch (Exception e) {
            // Return a JSON response with an error message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An error occurred: " + e.getMessage()));
        }
    }

    // Define a class for success response
    private static class SuccessResponse {
        private String message;

        public SuccessResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    // Define a class for error response
    private static class ErrorResponse {
        private String error;

        public ErrorResponse(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }
    }



    @GetMapping("/users-per-role")
    public ResponseEntity<Map<String, Long>> getUsersPerRole() {
        Map<String, Long> usersPerRole = userInterface.calculateUsersPerRole();
        return ResponseEntity.ok(usersPerRole);
    }

    @GetMapping("/total")
    public ResponseEntity<Long> getTotalNumberOfUsers() {
        long totalUsers = userInterface.getTotalNumberOfUsers();
        return ResponseEntity.ok(totalUsers);
    }

    @GetMapping("/search")
    List<User> searchUser(@RequestParam(value = "keyword") String keyword) {
        List<User> allUsers = userRepository.findAll();
        // Effectue la recherche en filtrant les posts qui contiennent le mot-clé dans leur description
        return allUsers.stream()
                .filter(user ->
                                user.getNomUser().toLowerCase().contains(keyword.toLowerCase()) ||
                                        user.getPrenomUser().toLowerCase().contains(keyword.toLowerCase()) ||
                                        user.getEmailUser().toLowerCase().contains(keyword.toLowerCase()) ||
                                        user.getNumTel().toLowerCase().contains(keyword.toLowerCase())
                        // Add more conditions for other properties as needed
                )
                .collect(Collectors.toList());
    }
}