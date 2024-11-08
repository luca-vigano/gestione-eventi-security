package luca.vigano.gestioneEventi.services;

import luca.vigano.gestioneEventi.entities.Evento;
import luca.vigano.gestioneEventi.exceptions.BadRequestException;
import luca.vigano.gestioneEventi.exceptions.NotFoundException;
import luca.vigano.gestioneEventi.payloads.EventoDTO;
import luca.vigano.gestioneEventi.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    public Evento save(EventoDTO body){
        if(body.dataevento().isBefore(LocalDate.now())){
            throw new BadRequestException("La data inserita è già passata");
        }
        Evento newEvento = new Evento(body.titolo(), body.descrizione(), body.dataevento(), body.luogo(), body.postimax());
        return eventoRepository.save(newEvento);
    }

    public Page<Evento> findAll(int page, int size, String sortBy){
        if (size > 100) size = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.eventoRepository.findAll(pageable);
    }

    public Evento findById(UUID viaggioId){
        return this.eventoRepository.findById(viaggioId).orElseThrow(() -> new NotFoundException(viaggioId));
    }

    public Evento findByIdAndUpdate(UUID eventoId, EventoDTO body){

        if(body.dataevento().isBefore(LocalDate.now())){
            throw new BadRequestException("la data inserita è già passata");
        }

        Evento eventoFound = this.findById(eventoId);
        eventoFound.setDataevento(body.dataevento());
        eventoFound.setLuogo(body.luogo());
        eventoFound.setTitolo(body.titolo());
        eventoFound.setPostimax(body.postimax());
        eventoFound.setDescrizione(body.descrizione());
        return this.eventoRepository.save(eventoFound);
    }

    public void findByIdAndDelete (UUID eventoId){
        Evento eventoFound = this.findById(eventoId);
        this.eventoRepository.delete(eventoFound);
    }
}
