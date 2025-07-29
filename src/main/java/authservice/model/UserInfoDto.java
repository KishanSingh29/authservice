package authservice.model;

import authservice.entities.UserInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoDto  {


    private String username;
    private String password;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("phone_number")
    private Long phoneNumber;

    private String email;



}
