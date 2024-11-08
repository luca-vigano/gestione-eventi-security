package luca.vigano.gestioneEventi.repository;

import luca.vigano.gestioneEventi.entities.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface PrenotazioneRepository extends JpaRepository<Prenotazione, UUID> {
    List<Prenotazione> findByUser_EmailAndEvento_Dataevento(String user, LocalDate data);
}
