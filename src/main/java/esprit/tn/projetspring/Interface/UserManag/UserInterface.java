package esprit.tn.projetspring.Interface.UserManag;


import esprit.tn.projetspring.Entity.UserManag.User;
import esprit.tn.projetspring.Entity.dto.request.ProfileRequest;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Map;

public interface UserInterface {

 // User addUser(User user);
 User updateUser(User user);

 User retrieveUser(Long id);

 List<User> retrieveUser();

 void removeUser(long id);

 User findUserById(Long idUser);

 Page<User> findUserswithPaginationAndSorting(int offset, int pageSige, String field);

 User manageProfile(Long idUser, ProfileRequest profile);

 User getProfile(Long idUser);

 void sendResetPasswordEmail(String userEmail);

 void resetPassword(String userEmail, String resetCode, String newPassword);


  Map<String, Double> calculateUserGenderRate() ;
  Map<String, Long> calculateUsersPerRole() ;
  long getTotalNumberOfUsers() ;

 // void banUser(Long userId) ;
 // void debanUser(Long userId) ;


  User findByUsername(String username);


}

