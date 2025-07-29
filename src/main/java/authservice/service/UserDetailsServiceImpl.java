package authservice.service;

import authservice.entities.UserInfo;

import authservice.eventProducer.UserInfoEvent;
import authservice.eventProducer.UserInfoProducer;
import authservice.model.UserInfoDto;
import authservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.slf4j.Logger;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;


@Component
@AllArgsConstructor
@Data

public class UserDetailsServiceImpl implements UserDetailsService {



    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private  PasswordEncoder passwordEncoder;

    @Autowired
    private UserInfoProducer userInfoProducer;


    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {

        log.debug("Entering in loadUserByUsername Method...");
        UserInfo user = userRepository.findByUsername(username);
        if(user == null){
            log.error("Username not found: " + username);
            throw new UsernameNotFoundException("could not found user..!!");
        }
        log.info("User Authenticated Successfully..!!!");
        return new CustomUserDetails(user);  // iski jaga builder bhi use ho sakta tha journal entry ki jaise or customuserdetails ke through hi kyu use huaa or uska use kya hai
    }

    public UserInfo checkIfUserAlreadyExist(UserInfoDto userInfoDto){
        return userRepository.findByUsername(userInfoDto.getUsername());
    }

    public Boolean signupUser(UserInfoDto userInfoDto){
        if(Objects.nonNull(checkIfUserAlreadyExist(userInfoDto))){
            return false;
        }

        String userId = UUID.randomUUID().toString();

        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        userInfo.setUsername(userInfoDto.getUsername());
        userInfo.setPassword(passwordEncoder.encode(userInfoDto.getPassword()));
        userInfo.setRoles(new HashSet<>()); // Optional for now

        userRepository.save(userInfo);

        userInfoProducer.sendEventToKafka(
                UserInfoEvent.builder()
                        .userId(userId)
                        .firstName(userInfoDto.getFirstName())
                        .lastName(userInfoDto.getLastName())
                        .email(userInfoDto.getEmail())
                        .phoneNumber(userInfoDto.getPhoneNumber())
                        .build()
        );

        return true;
    }


    private UserInfoEvent userInfoEventToPublish(UserInfoDto userInfoDto, String userId){
        return UserInfoEvent.builder()
                .userId(userId)
                .firstName(userInfoDto.getUsername())
                .lastName(userInfoDto.getLastName())
                .email(userInfoDto.getEmail())
                .phoneNumber(userInfoDto.getPhoneNumber()).build();

    }


}
