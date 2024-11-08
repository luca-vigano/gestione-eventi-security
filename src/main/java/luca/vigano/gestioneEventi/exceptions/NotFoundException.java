package luca.vigano.gestioneEventi.exceptions;

import java.util.UUID;

public class NotFoundException extends RuntimeException {
    public NotFoundException(UUID id) {
        super("il record con id " + id + " non è stato trovato");
    }

    public NotFoundException (String message){
        super(message);
    }
}