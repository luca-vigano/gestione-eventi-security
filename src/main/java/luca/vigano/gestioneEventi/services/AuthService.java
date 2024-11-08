package luca.vigano.gestioneEventi.services;

import luca.vigano.gestioneEventi.entities.User;
import luca.vigano.gestioneEventi.exceptions.UnauthorizedException;
import luca.vigano.gestioneEventi.payloads.UserLoginDTO;
import luca.vigano.gestioneEventi.tools.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserService userService;
    @Autowired
    private JWT jwt;
    @Autowired
    private PasswordEncoder bcryptencoder;

    public String checkCredenzialiAndToken(UserLoginDTO body){

        User userFound = this.userService.finByEmail(body.email());
        if (bcryptencoder.matches(body.password(), userFound.getPassword())){
            String accessToken = jwt.createToken(userFound);
            return accessToken;
        }else {
            throw new UnauthorizedException("credenziali inserite errate");
        }
    }

}
