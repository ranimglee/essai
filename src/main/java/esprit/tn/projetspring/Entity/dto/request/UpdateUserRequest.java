package esprit.tn.projetspring.Entity.dto.request;

import esprit.tn.projetspring.Entity.FuneralManag.TypeReligion;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {

    private String firstName;

    private String lastName;
    private String mobileNumber;
    private String email;
    private String adressUser;
    private MultipartFile imageUser;
    private Date dateNaiss;
    private Date dateMort;
    private TypeReligion religion;
    private String sexe;

}
