package luca.vigano.gestioneEventi.services;

import luca.vigano.gestioneEventi.entities.Evento;
import luca.vigano.gestioneEventi.entities.Prenotazione;
import luca.vigano.gestioneEventi.entities.User;
import luca.vigano.gestioneEventi.exceptions.BadRequestException;
import luca.vigano.gestioneEventi.exceptions.NotFoundException;
import luca.vigano.gestioneEventi.payloads.PrenotazioneDTO;
import luca.vigano.gestioneEventi.repository.EventoRepository;
import luca.vigano.gestioneEventi.repository.PrenotazioneRepository;
import luca.vigano.gestioneEventi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PrenotazioneService {
    @Autowired
    private PrenotazioneRepository prenotazioneRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventoRepository eventoRepository;

    public Prenotazione save (PrenotazioneDTO body){
        User userfound = userRepository.findById(body.userId()).orElseThrow(()->new NotFoundException(body.userId()));

        Evento eventoFound = eventoRepository.findById(body.eventoId()).orElseThrow(()-> new NotFoundException(body.eventoId()));

        List<Prenotazione> prenotazioniEsistenti = prenotazioneRepository.findByUser_EmailAndEvento_Dataevento(userfound.getEmail(),eventoFound.getDataevento());

        if(!prenotazioniEsistenti.isEmpty()){
            throw new BadRequestException("l'utente ha già una prenotazione in questa data");
        }
        if(eventoFound.getPostidisponibili()< body.numeroposti()){
            throw new BadRequestException("non ci sono abbastanza posti disponibili all'evento");
        }
        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setEvento(eventoFound);
        prenotazione.setUser(userfound);
        prenotazione.setNumeropostiprenotati(body.numeroposti());
        eventoFound.setPostidisponibili(eventoFound.getPostidisponibili() - body.numeroposti());
        return prenotazioneRepository.save(prenotazione);
    }

    public Page<Prenotazione> findAll(int page, int size, String sortBy){
        if (size > 100) size = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.prenotazioneRepository.findAll(pageable);
    }

    public Prenotazione findById(UUID prenotazioneId){
        return this.prenotazioneRepository.findById(prenotazioneId).orElseThrow(() -> new NotFoundException(prenotazioneId));
    }

    public Prenotazione findByIdAndUpdate(UUID prenotazioneId, PrenotazioneDTO prenotazioneDTO) {
        Prenotazione prenotazione = prenotazioneRepository.findById(prenotazioneId)
                .orElseThrow(() -> new NotFoundException(prenotazioneId));

        if (prenotazioneDTO.userId() != null) {
            User user = userRepository.findById(prenotazioneDTO.userId())
                    .orElseThrow(() -> new NotFoundException(prenotazioneId));
            prenotazione.setUser(user);
        }

        if (prenotazioneDTO.eventoId() != null) {
            Evento evento = eventoRepository.findById(prenotazioneDTO.eventoId())
                    .orElseThrow(() -> new NotFoundException(prenotazioneId));
            prenotazione.setEvento(evento);
        }

        if (prenotazioneDTO.numeroposti() != 0) {
            prenotazione.setNumeropostiprenotati(prenotazioneDTO.numeroposti());
        }

        return prenotazioneRepository.save(prenotazione);
    }

    public void findByIdAndDelete (UUID prenotazioneId){
        Prenotazione prenotazioneFound = this.findById(prenotazioneId);
        this.prenotazioneRepository.delete(prenotazioneFound);
    }
}
