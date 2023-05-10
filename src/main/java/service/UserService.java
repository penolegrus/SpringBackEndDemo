package service;

import models.user.User;
import models.user.RegisterUserResponse;

import jwt.config.JwtTokenUtil;
import models.messages.InfoMessage;
import models.messages.Message;
import models.user.ChangeUserPass;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import repo.UserRepository;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserService extends JwtService {
    public UserService(UserRepository userRepository, JwtTokenUtil jwtTokenUtil) {
        super(userRepository, jwtTokenUtil);
    }

    public ResponseEntity<?> signUp(User userDTO) {
        if(userDTO.getLogin() == null || userDTO.getPass() == null){
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Missing login or password"));
        }
        if (!userRepository.existsByLogin(userDTO.getLogin())) {
            User user = new User(userDTO.getLogin(), userDTO.getPass(), userDTO.getGames());
            if(user.getGames().size() > 20){
                return ResponseEntity.status(201).body(new InfoMessage("fail", "User cant have more than 20 games on registration"));
            }
            userRepository.save(user);
            userRepository.flush();
            return ResponseEntity.status(201).body(new RegisterUserResponse(user,
                    new Message("success", "User created")));
        }
        return ResponseEntity.status(400).body(new InfoMessage("fail", "Login already exist"));
    }

    public ResponseEntity<InfoMessage> updateUserPassword(ChangeUserPass newPassword, String authHeader) {
        if (newPassword.getPassword() == null || newPassword.getPassword().isEmpty()) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Body has no password parameter"));
        }

        User user = getUserFromJwt(authHeader);
        if (isBaseUser(user)) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Cant update base users"));
        }

        user.setPass(newPassword.getPassword());
        userRepository.save(user);
        userRepository.flush();
        return ResponseEntity.status(200).body(new InfoMessage("success", "User password successfully changed"));
    }

    public ResponseEntity<User> getUser(String jwt) {
        User user = getUserFromJwt(jwt);
        return ResponseEntity.status(200).body(user);
    }

    public ResponseEntity<List<String>> getAllUsers() {
        return ResponseEntity.status(200).body(userRepository.findAll().stream().map(User::getLogin).collect(Collectors.toList()));
    }

    public ResponseEntity<InfoMessage> deleteUser(String jwt) {
        User user = getUserFromJwt(jwt);

        if (isBaseUser(user)) {
            return ResponseEntity.status(400).body(new InfoMessage("fail", "Cant delete base users"));
        }

        userRepository.deleteById(user.getId());
        userRepository.flush();
        return ResponseEntity.status(200).body(new InfoMessage("success", "User successfully deleted"));
    }


}
