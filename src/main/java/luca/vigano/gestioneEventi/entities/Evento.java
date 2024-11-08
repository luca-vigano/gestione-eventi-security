package luca.vigano.gestioneEventi.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "eventi")
@Getter
@Setter
@ToString
@NoArgsConstructor

public class Evento {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;
    private String titolo;
    private String descrizione;
    private LocalDate dataevento;
    private String luogo;
    private int postidisponibili;
    @ManyToOne
    private User user;


    public Evento(String titolo, String descrizione, LocalDate dataevento, String luogo, int postidisponibili,User user) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.dataevento = dataevento;
        this.luogo = luogo;
        this.postidisponibili = postidisponibili;
        this.user = user;
    }
}
