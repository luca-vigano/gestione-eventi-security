package luca.vigano.gestioneEventi.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "prenotazione")
@Getter
@Setter
@NoArgsConstructor
@ToString

public class Prenotazione {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Evento evento;

    private String numeropostiprenotati;

    public Prenotazione(User user, Evento evento, String numeropostiprenotati) {
        this.user = user;
        this.evento = evento;
        this.numeropostiprenotati = numeropostiprenotati;
    }
}
