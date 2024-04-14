package esprit.tn.projetspring.Service.UserManag;


import esprit.tn.projetspring.Entity.UserManag.User;
import esprit.tn.projetspring.Entity.dto.request.ProfileRequest;
import esprit.tn.projetspring.Entity.exception.NotFoundExecption;
import esprit.tn.projetspring.Interface.UserManag.UserInterface;
import esprit.tn.projetspring.Repository.UserManag.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;


import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service
@AllArgsConstructor

public class UserService implements UserInterface {


    UserRepository userRepository;
//     BCryptPasswordEncoder passwordEncoder;


    /*  @Override
      public User addUser(User user) {
         String encodedPassword = passwordEncoder.encode(user.getMdpUser());
          user.setMdpUser(encodedPassword);
          return userRepository.save(user);
      }

  */
    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User retrieveUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public List<User> retrieveUser() {
        return userRepository.findAll();
    }

    @Override
    public void removeUser(long id) {
        userRepository.deleteById(id);

    }

    public User manageProfile(Long idUser, ProfileRequest profile) {
        Optional<User> findUser = userRepository.findById(idUser);
        if (!findUser.isPresent()) {
            throw new NotFoundExecption("no user found");
        }
        User user = findUser.get();
        user.setPrenomUser(profile.getFirstName());
        user.setNomUser(profile.getLastName());
        user.setNumTel(profile.getMobileNumber());
        user.setAdressUser(profile.getAdressUser());
        user.setImageUser(profile.getImageUser());
        user.setReligion(profile.getReligion());
        user.setDateNaiss(profile.getDateNaiss());

        return userRepository.save(user);
    }

    public User findUserById(Long idUser) {
        Optional<User> user = userRepository.findById(idUser);
        if (!user.isPresent()) {
            throw new NotFoundExecption("no user found");
        }
        return user.get();
    }

    @Override
    public Page<User> findUserswithPaginationAndSorting(int offset, int pageSige, String field) {
        Page<User> users = userRepository.findAll(PageRequest.of(offset, pageSige).withSort(Sort.by(field)));
        return users;
    }

    public User getProfile(Long idUser) {
        // Find the user by ID
        Optional<User> findUser = userRepository.findById(idUser);

        // Check if the user exists
        if (!findUser.isPresent()) {
            throw new NotFoundExecption("No user found");
        }

        // Return the user object
        return findUser.get();
    }


    EmailServiceImp emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    private String generateCode() {
        return UUID.randomUUID().toString().substring(0, 6); // Generate a 6-character code
    }

    // Method to send an email with the code to reset password
    public void sendResetPasswordEmail(String userEmail) {
        // Find the user by email
        User user = userRepository.findByEmailUser(userEmail);
        if (user != null) {
            // Generate a code
            String resetCode = generateCode();
            // Save the code to the user entity (you might have to add a field for this in your User entity)
            user.setResetCode(resetCode);
            userRepository.save(user);
            // Send email with the code
            String emailSubject = "Password Reset Code";
            String emailBody = "Your password reset code is: " + resetCode;
            emailService.sendEmail(user.getEmailUser(), emailSubject, emailBody);
        } else {
            throw new IllegalArgumentException("User not found with email: " + userEmail);
        }
    }

    @Override
    // Method to reset password with the received code
    public void resetPassword(String userEmail, String resetCode, String newPassword) {
        // Find the user by email
        User user = userRepository.findByEmailUser(userEmail);
        if (user != null && user.getResetCode() != null && user.getResetCode().equals(resetCode)) {
            // Update user's password
            String encodedPassword = passwordEncoder.encode(newPassword);
            user.setMdpUser(encodedPassword);
            // Clear the reset code after password is reset
            user.setResetCode(null);
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("Invalid reset code or user not found.");
        }
    }




    /* private String generateNewPassword() {
         // Implement your logic to generate a new password (e.g., using random characters or tokens)
         // For demonstration, let's generate a simple random alphanumeric password
         String upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
         String lowerAlphabet = upperAlphabet.toLowerCase();
         String numbers = "0123456789";
         String allCharacters = upperAlphabet + lowerAlphabet + numbers;

         StringBuilder newPassword = new StringBuilder();
         for (int i = 0; i < 8; i++) { // Generating an 8-character password
             int index = (int) (allCharacters.length() * Math.random());
             newPassword.append(allCharacters.charAt(index));
         }
         return newPassword.toString();
     }


 */

    public User findByUsername(String username) {
        return userRepository.findByEmailUser(username);
    }

    @Override
    public Map<String, Double> calculateUserGenderRate() {
        // Retrieve all users from the database
        List<User> users = userRepository.findAll();

        // Count the number of users for each gender using Java Streams
        Map<String, Long> genderCount = users.stream()
                .collect(Collectors.groupingBy(User::getSexe, Collectors.counting()));

        // Calculate the rate or percentage of users for each gender relative to the total number of users
        long totalUsers = users.size();
        Map<String, Double> genderRate = genderCount.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> (double) entry.getValue() / totalUsers * 100
                ));

        return genderRate;
    }
@Override
public long getTotalNumberOfUsers() {
    return userRepository.count();
}
@Override
public Map<String, Long> calculateUsersPerRole() {
    List<User> users = userRepository.findAll();

    Map<String, Long> usersPerRole = users.stream()
            .flatMap(user -> user.getRoles().stream())
            .collect(Collectors.groupingBy(
                    role -> role.getRoleName().toString(),
                    Collectors.counting()
            ));

    return usersPerRole;
}

    // Methodto search users by multiple properties

    public int calculateAge(LocalDate dateNaissance) {
        LocalDate currentDate = LocalDate.now();
        int age = currentDate.getYear() - dateNaissance.getYear();
        if (dateNaissance.getMonthValue() > currentDate.getMonthValue() ||
                (dateNaissance.getMonthValue() == currentDate.getMonthValue() &&
                        dateNaissance.getDayOfMonth() > currentDate.getDayOfMonth())) {
            age--;
        }
        return age;
    }
   /* @Override
    public void banUser(Long userId) {
        Optional<User> optionalUser =userRepository.findById(userId);
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            user.setBanni(true);
            userRepository.save(user);
        }
        else {
            throw new EntityNotFoundException("Utilisateur inexistant : " + userId);
        }
    }*/

  /*  @Override
    public void debanUser(Long idUser) {

        Optional<User> optionalUser =userRepository.findById(idUser);
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            user.setBanni(false);
            userRepository.save(user);
        }
        else {
            throw new EntityNotFoundException("Utilisateur inexistant " );
        }

    }
*/
}