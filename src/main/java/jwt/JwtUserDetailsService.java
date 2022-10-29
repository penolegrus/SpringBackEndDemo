package jwt;

import app.UserDataBase;
import models.user.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user;
        if(UserDataBase.isLoginExists(username)){
            user = UserDataBase.getUser(username);
            return new org.springframework.security.core.userdetails.User(username, user.getPass(), new ArrayList<>());
        }
        else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

}
