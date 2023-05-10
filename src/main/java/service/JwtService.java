package service;

import models.user.User;
import jwt.config.JwtTokenUtil;
import org.springframework.stereotype.Service;
import repo.UserRepository;

@Service
public class JwtService {
    protected final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;

    public JwtService(UserRepository userRepository, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public User getUserFromJwt(String jwt) {
        jwt = jwt.replace("Bearer ", "");
        String userName = jwtTokenUtil.getUsernameFromToken(jwt);
        return userRepository.findByLogin(userName);
    }

    public boolean isBaseUser(User user) {
        return user.getLogin().equals("admin") || user.getLogin().equals("demo") || user.getLogin().equals("threadqa");
    }
}
