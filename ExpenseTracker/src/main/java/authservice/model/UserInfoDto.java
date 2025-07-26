package authservice.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoDto {

    private String username;
    private String password;

    private String firstName;
    private String lastName;
    private Long phoneNumber;

    private String email;




}
