package luca.vigano.gestioneEventi.controller;

import luca.vigano.gestioneEventi.entities.Evento;
import luca.vigano.gestioneEventi.exceptions.BadRequestException;
import luca.vigano.gestioneEventi.payloads.EventoDTO;
import luca.vigano.gestioneEventi.services.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/eventi")
public class EventiController {
    @Autowired
    private EventoService eventoService;

    @GetMapping
    public Page<Evento> findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
                                @RequestParam(defaultValue = "id") String sortBy) {
        return this.eventoService.findAll(page, size, sortBy);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('MOD_EVENTI') or hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Evento save(@RequestBody @Validated EventoDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("Ci sono stati errori nel payload! " + message);
        }
        return this.eventoService.save(body);
    }

    @GetMapping("/{eventoId}")
    public Evento findById(@PathVariable UUID eventoId) {
        return this.eventoService.findById(eventoId);
    }

    @PutMapping("/{eventoId}")
    @PreAuthorize("hasAuthority('MOD_EVENTI') or hasAuthority('ADMIN')")
    public Evento findByIdAndUpdate(@PathVariable UUID eventoId, @RequestBody EventoDTO body) {

        return this.eventoService.findByIdAndUpdate(eventoId, body);
    }

    @DeleteMapping("/{eventoId}")
    @PreAuthorize("hasAuthority('MOD_EVENTI') or hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable UUID eventoId) {
        this.eventoService.findByIdAndDelete(eventoId);
    }


}
