package luca.vigano.gestioneEventi.services;

import luca.vigano.gestioneEventi.entities.User;
import luca.vigano.gestioneEventi.exceptions.BadRequestException;
import luca.vigano.gestioneEventi.exceptions.NotFoundException;
import luca.vigano.gestioneEventi.payloads.UserDTO;
import luca.vigano.gestioneEventi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder bcryptencoder;

    public User save(UserDTO body) {
        this.userRepository.findByEmail(body.email()).ifPresent(dipendente -> {
                    throw new BadRequestException("Email " + body.email() + " già in uso");
                }
        );
        User newUser = new User(body.nome(), body.cognome(), body.email(), bcryptencoder.encode(body.password()));
        return this.userRepository.save(newUser);
    }

    public Page<User> findAll(int page, int size, String sortBy){
        if (size > 100) size = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.userRepository.findAll(pageable);
    }

    public User findById(UUID userId) {
        return this.userRepository.findById(userId).orElseThrow(() -> new NotFoundException(userId));
    }

    public User findByIdAndUpdate(UUID userId, UserDTO body) {
        User userFound = this.findById(userId);

        if (!userFound.getEmail().equals(body.email())) {
            this.userRepository.findByEmail(body.email()).ifPresent(
                    user -> {
                        throw new BadRequestException("Email " + body.email() + " già in uso!");
                    }
            );
        }

        userFound.setNome(body.nome());
        userFound.setCognome(body.cognome());
        userFound.setEmail(body.email());
        return this.userRepository.save(userFound);
    }

    public void findByIdAndDelete(UUID userId) {
        User userFound = this.findById(userId);
        this.userRepository.delete(userFound);
    }

    public User finByEmail(String email){
        return this.userRepository.findByEmail(email).orElseThrow(()-> new NotFoundException("l'untente con la mail " + email + " non è stato trovato"));
    }


}
