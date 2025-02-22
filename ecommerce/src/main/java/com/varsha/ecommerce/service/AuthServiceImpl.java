package com.varsha.ecommerce.service;

import com.varsha.ecommerce.dto.SignUpDTO;
import com.varsha.ecommerce.dto.UserDTO;
import com.varsha.ecommerce.entity.User;
import com.varsha.ecommerce.enums.UserRole;
import com.varsha.ecommerce.mapper.UserMapper;
import com.varsha.ecommerce.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder;

    private final UserMapper userMapper;

    public UserDTO createUser(SignUpDTO signUpDTO){

        User user = userMapper.toUserEntity(signUpDTO);
        user.setPassword(encoder.encode(signUpDTO.getPassword()));

        User savedUser = userRepository.save(user);

        return userMapper.toUserDTO(savedUser);
    }

    public Boolean hasUserWithEmail(String email){
        return userRepository.findByEmail(email).isPresent();
    }

    @PostConstruct
    public void createAdminAccount(){
        User adminAccount = userRepository.findByRole(UserRole.ADMIN);

        if(adminAccount==null){
            User user = new User();
            user.setEmail("admin@test.com");
            user.setName("admin");
            user.setRole(UserRole.ADMIN);
            user.setPassword(encoder.encode("admin"));
            userRepository.save(user);
        }
    }
}
