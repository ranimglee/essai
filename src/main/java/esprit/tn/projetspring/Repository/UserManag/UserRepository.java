package esprit.tn.projetspring.Repository.UserManag;


import esprit.tn.projetspring.Entity.UserManag.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    User findByEmailUser(String EmailUser);
    @Query("SELECT u FROM User u WHERE u.id = :currentUserId") // Assuming you have a field 'id' in your User entity
    User getCurrentUser(Long currentUserId);

    Optional<User> findByConfirmationToken(String confirmationToken);
    long count(); // Spring Data JPA will automatically generate the SQL query to count all users

    // Method to search users by multiple properties



}
