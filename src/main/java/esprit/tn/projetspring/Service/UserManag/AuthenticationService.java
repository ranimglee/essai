package esprit.tn.projetspring.Service.UserManag;

import esprit.tn.projetspring.Entity.UserManag.Role;
import esprit.tn.projetspring.Entity.UserManag.TypeRole;
import esprit.tn.projetspring.Entity.dto.request.ChangePasswordRequest;
import esprit.tn.projetspring.Entity.dto.request.UpdateUserRequest;
import esprit.tn.projetspring.Repository.UserManag.RoleRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import esprit.tn.projetspring.Entity.UserManag.User;
import esprit.tn.projetspring.Entity.dto.request.RegisterRequest;
import esprit.tn.projetspring.Entity.exception.EmailExistsExecption;
import esprit.tn.projetspring.Interface.UserManag.AuthentificationInterface;
import esprit.tn.projetspring.Repository.UserManag.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.management.relation.RoleNotFoundException;
import java.io.IOException;
import java.security.Principal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service


public class AuthenticationService implements AuthentificationInterface {


    private final FileNamingUtil fileNamingUtil;
    private final String uploadUserImages;

    public AuthenticationService(FileNamingUtil fileNamingUtil, @Value("${uploadUserImages}") String uploadUserImages) {
        this.fileNamingUtil = fileNamingUtil;
        this.uploadUserImages = uploadUserImages;
    }


    @Autowired
   private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;


    @Autowired
     private JwtEncoder jwtEncoder;

    @Autowired
      private JwtDecoder jwtDecoder;
    @Autowired
     private UserDetailsService userDetailsService;

      @Autowired
     private PasswordEncoder bCryptPasswordEncoder;
@Autowired
private RoleRepository roleRepository;


    @Override
    public User register(RegisterRequest registerRequest, MultipartFile imageFile, List<String> roleNames
) {
        Optional<User> existingUser = Optional.ofNullable(userRepository.findByEmailUser(registerRequest.getEmail()));
        if (existingUser.isPresent()) {
            throw new EmailExistsExecption("Email already exists");
        }
        // Generate confirmation token
        String confirmationToken = generateConfirmationToken();

        User user = new User();
        user.setPrenomUser(registerRequest.getFirstName());
        user.setNomUser(registerRequest.getLastName());
        user.setNumTel(registerRequest.getMobileNumber());
        user.setMdpUser(bCryptPasswordEncoder.encode(registerRequest.getPassword()));
        user.setEmailUser(registerRequest.getEmail());
        user.setAdressUser(registerRequest.getAdressUser());
        user.setDateNaiss(registerRequest.getDateNaiss());
        user.setDateMort(registerRequest.getDateMort());
        user.setReligion(registerRequest.getReligion());
        user.setSexe(registerRequest.getSexe());
        user.setConfirmationToken(confirmationToken); // Set confirmation token
        // Fetch role based on role ID
         List<Role> roles = new ArrayList<>();
        for (String roleName : roleNames) {
            TypeRole typeRole = TypeRole.valueOf(roleName);
            Role role = roleRepository.findByRoleName(typeRole)
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            roles.add(role);
        }

        user.setRoles(roles);


        // Handle image upload
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String filename = fileNamingUtil.nameFile(imageFile);
                String uploadDir = uploadUserImages; // Upload directory configured in application properties
                fileNamingUtil.saveNewFile(uploadDir, filename, imageFile);
                user.setImageUser(filename); // Set the image filename to the user entity
            } catch (IOException e) {
                // Handle file upload error
                e.printStackTrace(); // Log the error or handle it as needed
            }
        }

        // Save user to repository
        userRepository.save(user);

        // Send confirmation email
        sendConfirmationEmail(user.getEmailUser(), confirmationToken);

        return user;
    }
    private String generateConfirmationToken() {
        // Generate a random confirmation token logic here
        // You can use UUID.randomUUID() or any other method to generate a unique token
        return UUID.randomUUID().toString();
    }
    @Value("${spring.mail.username}") // Assuming you have the username configured in your application.properties file
    private String emailUsername;

    @Value("${spring.mail.app-password}") // Assuming you have configured the app password in your application.properties file
    private String emailAppPassword;
    @Autowired
    private JavaMailSender emailSender;
    private void sendConfirmationEmail(String email, String confirmationToken) {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            // Configure the helper with the sender email properties
            helper.setFrom(emailUsername); // Set the sender email address
            helper.setTo(email);
            helper.setSubject("Confirm your account");
            helper.setText("To confirm your account, please click the link below:\n"
                    + "http://localhost:8082/test/auth/confirm-account?token=" + confirmationToken);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        if (emailSender instanceof JavaMailSenderImpl) {
            JavaMailSenderImpl mailSenderImpl = (JavaMailSenderImpl) emailSender;
            mailSenderImpl.setUsername(emailUsername);
            mailSenderImpl.setPassword(emailAppPassword);
        }


        emailSender.send(message);
    }


    private Map<String, Integer> failedLoginAttempts = new HashMap<>();
    private Map<String, Long> suspendedUsers = new HashMap<>();
    private static final int MAX_FAILED_ATTEMPTS = 2;
    private static final long SUSPENSION_DURATION = 3600000; // 1 hour in milliseconds

    // Method to check if the user account is suspended
    public boolean isUserSuspended(String username) {
        Long suspensionEndTime = suspendedUsers.get(username);
        return suspensionEndTime != null && suspensionEndTime > System.currentTimeMillis();
    }

    // Method to handle a failed login attempt
    public void handleFailedLogin(String username) {
        int failedAttempts = failedLoginAttempts.getOrDefault(username, 0);
        failedAttempts++;
        failedLoginAttempts.put(username, failedAttempts);

        if (failedAttempts > MAX_FAILED_ATTEMPTS) {
            // Suspend the user for one hour
            suspendedUsers.put(username, System.currentTimeMillis() + SUSPENSION_DURATION);
            // Reset the failed login attempts count
            failedLoginAttempts.put(username, 0);
        }
    }

    // Method to handle a successful login attempt
    public void handleSuccessfulLogin(String username) {
        // Reset the failed login attempts count
        failedLoginAttempts.put(username, 0);
        // Remove the user from suspended users (if previously suspended)
        suspendedUsers.remove(username);
    }

/*@Override
public Map<String, String> jwtToken(String username, String password) {
    // Check if the user is suspended
    if (isUserSuspended(username)) {
        return Map.of("errorMessage", "Your account is suspended. Please try again later.");
    } else {
        try {
            // Check if the user's account is confirmed
            User userOptional = userRepository.findByEmailUser(username);

                if (!userOptional.isAccountConfirmed()) {
                    return Map.of("errorMessage", "Your account is not confirmed. Please confirm your account first.");
                }

            // Perform authentication
            String subject = null;
            String scope = null;
            String grantType = "password";
            boolean withRefreshToken = true;
            String refreshToken = null;

            if (grantType.equals("password")) {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(username, password)
                );
                // If authentication successful, handle successful login
                handleSuccessfulLogin(username);

                subject = authentication.getName();
                scope = authentication.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(" "));
            } else if (grantType.equals("refreshToken")) {
                if (refreshToken == null) {
                    return Map.of("errorMessage", "Refresh Token is required");
                }

                Jwt decodeJWT = jwtDecoder.decode(refreshToken);
                subject = decodeJWT.getSubject();
                UserDetails userDetails = userDetailsService.loadUserByUsername(subject);
                scope = userDetails.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(" "));
            }

            Map<String, String> idToken = new HashMap<>();
            Instant instant = Instant.now();
            JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                    .subject(subject)
                    .issuedAt(instant)
                    .expiresAt(instant.plus(withRefreshToken ? 1 : 5, ChronoUnit.MINUTES))
                    .issuer("security-service")
                    .claim("scope", scope)
                    .build();

            String jwtAccessToken = jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
            idToken.put("accessToken", jwtAccessToken);

            if (withRefreshToken) {
                JwtClaimsSet jwtClaimsSetRefresh = JwtClaimsSet.builder()
                        .subject(subject)
                        .issuedAt(instant)
                        .expiresAt(instant.plus(5, ChronoUnit.MINUTES))
                        .issuer("security-service")
                        .build();

                String jwtRefreshToken = jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSetRefresh)).getTokenValue();
                idToken.put("refreshToken", jwtRefreshToken);
            }
            return idToken;
        } catch (AuthenticationException e) {
            // If authentication fails, handle failed login
            handleFailedLogin(username);
            // Return error message or handle it as needed
            return Map.of("errorMessage", "Invalid credentials");
        }
    }
}
*/
@Override
public Map<String, String> jwtToken(String username, String password) {
    // Check if the user is suspended
    if (isUserSuspended(username)) {
        return Map.of("errorMessage", "Your account is suspended. Please try again later.");
    } else {
        try {
            // Check if the user's account is confirmed
            User userOptional = userRepository.findByEmailUser(username);

            if (!userOptional.isAccountConfirmed()) {
                return Map.of("errorMessage", "Your account is not confirmed. Please confirm your account first.");
            }

            // Perform authentication
            String subject = null;
            String scope = null;
            String grantType = "password";
            boolean withRefreshToken = true;
            String refreshToken = null;

            if (grantType.equals("password")) {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(username, password)
                );

                // If authentication successful, handle successful login
                handleSuccessfulLogin(username);

                subject = authentication.getName();
                scope = authentication.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(" "));
            } else if (grantType.equals("refreshToken")) {
                if (refreshToken == null) {
                    return Map.of("errorMessage", "Refresh Token is required");
                }

                Jwt decodeJWT = jwtDecoder.decode(refreshToken);
                subject = decodeJWT.getSubject();
                UserDetails userDetails = userDetailsService.loadUserByUsername(subject);
                scope = userDetails.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(" "));
            }

            // Build JWT claims and encode into tokens
            Map<String, String> idToken = new HashMap<>();
            Instant instant = Instant.now();
            JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                    .subject(subject)
                    .issuedAt(instant)
                    .expiresAt(instant.plus(withRefreshToken ? 1 : 30, ChronoUnit.MINUTES))
                    .issuer("security-service")
                    .claim("scope", scope)
                    .build();

            // Encode access token
            String jwtAccessToken = jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
            idToken.put("accessToken", jwtAccessToken);

            // Generate refresh token if needed
            if (withRefreshToken) {
                JwtClaimsSet jwtClaimsSetRefresh = JwtClaimsSet.builder()
                        .subject(subject)
                        .issuedAt(instant)
                        .expiresAt(instant.plus(30, ChronoUnit.MINUTES))
                        .issuer("security-service")
                        .build();

                String jwtRefreshToken = jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSetRefresh)).getTokenValue();
                idToken.put("refreshToken", jwtRefreshToken);
            }

            return idToken;
        } catch (AuthenticationException e) {
            // If authentication fails, handle failed login
            handleFailedLogin(username);
            // Return error message or handle it as needed
            return Map.of("errorMessage", "Invalid credentials");
        }
    }
}



    @Override
    public boolean confirmAccount(String confirmationToken) {
        Optional<User> userOptional = userRepository.findByConfirmationToken(confirmationToken);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setAccountConfirmed(true);
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }
    @Override
    public User getCurrentUser(Principal connectedUser) {
        if (connectedUser instanceof UsernamePasswordAuthenticationToken) {
            Object principal = ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
            if (principal instanceof UserDetails) {
                return (User) principal;
            }
        }
        return null;
    }

    @Override
    public void updateCurrentUser(Principal connectedUser, UpdateUserRequest updatedUser) {
        User currentUser = getCurrentUser(connectedUser);

        if (updatedUser.getLastName() != null && !updatedUser.getLastName().isEmpty()) {
            currentUser.setNomUser(updatedUser.getLastName());
        }
        if (updatedUser.getFirstName() != null && !updatedUser.getFirstName().isEmpty()) {
            currentUser.setPrenomUser(updatedUser.getFirstName());
        }
        if (updatedUser.getEmail() != null && !updatedUser.getEmail().isEmpty()) {
            currentUser.setEmailUser(updatedUser.getEmail());
        }
        if (updatedUser.getDateNaiss() != null) {
            currentUser.setDateNaiss(updatedUser.getDateNaiss());
        }
        if (updatedUser.getMobileNumber() != null && !updatedUser.getMobileNumber().isEmpty()) {
            currentUser.setNumTel(updatedUser.getMobileNumber());
        }
        if (updatedUser.getAdressUser() != null && !updatedUser.getAdressUser().isEmpty()) {
            currentUser.setAdressUser(updatedUser.getAdressUser());
        }
   /*     if (updatedUser.getImageUser() != null) {
            // Handle updating profile image if needed
            // Example: currentUser.setImage(updatedUser.getImageUser().getBytes());
        }*/
        if (updatedUser.getDateMort() != null) {
            currentUser.setDateMort(updatedUser.getDateMort());
        }
        if (updatedUser.getReligion() != null) {
            currentUser.setReligion(updatedUser.getReligion());
        }
        if (updatedUser.getSexe() != null && !updatedUser.getSexe().isEmpty()) {
            currentUser.setSexe(updatedUser.getSexe());
        }

        userRepository.save(currentUser);
    }

    @Override
    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {
        var user= (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        if (!bCryptPasswordEncoder.matches(request.getCurrentPassword(), user.getPassword())){
            throw new IllegalStateException("Wrong Password");
        }
        if(!request.getNewPassword().equals(request.getConfirmationPassword())){
            throw new IllegalStateException("Password are not the same");
        }
        user.setMdpUser(bCryptPasswordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}
