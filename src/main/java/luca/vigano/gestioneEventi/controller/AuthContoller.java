package luca.vigano.gestioneEventi.controller;

import luca.vigano.gestioneEventi.entities.User;
import luca.vigano.gestioneEventi.exceptions.BadRequestException;
import luca.vigano.gestioneEventi.payloads.UserDTO;
import luca.vigano.gestioneEventi.payloads.UserLoginDTO;
import luca.vigano.gestioneEventi.payloads.UserLoginResponseDTO;
import luca.vigano.gestioneEventi.services.AuthService;
import luca.vigano.gestioneEventi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthContoller {

    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public UserLoginResponseDTO login(@RequestBody UserLoginDTO body){
        return new UserLoginResponseDTO(this.authService.checkCredenzialiAndToken(body));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User save(@RequestBody @Validated UserDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("Ci sono stati errori nel payload! " + message);
        }

        return this.userService.save(body);
    }
}
