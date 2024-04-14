package esprit.tn.projetspring.Service.UserManag;

import esprit.tn.projetspring.Interface.UserManag.EmailInterface;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImp implements EmailInterface {

    @Autowired
    private JavaMailSender emailSender;

    @Value("${spring.mail.username}") // Assuming you have the username configured in your application.properties file
    private String emailUsername;

    @Value("${spring.mail.app-password}") // Assuming you have configured the app password in your application.properties file
    private String emailAppPassword;

    @Override
    public void sendEmail(String to, String subject, String body) {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        // Configure the JavaMailSenderImpl with the app password
        if (emailSender instanceof JavaMailSenderImpl) {
            JavaMailSenderImpl mailSenderImpl = (JavaMailSenderImpl) emailSender;
            mailSenderImpl.setUsername(emailUsername);
            mailSenderImpl.setPassword(emailAppPassword);
        }

        emailSender.send(message);
    }
}
