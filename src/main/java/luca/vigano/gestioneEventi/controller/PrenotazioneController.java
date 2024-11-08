package luca.vigano.gestioneEventi.controller;

import luca.vigano.gestioneEventi.entities.Prenotazione;
import luca.vigano.gestioneEventi.exceptions.BadRequestException;
import luca.vigano.gestioneEventi.payloads.PrenotazioneDTO;
import luca.vigano.gestioneEventi.services.PrenotazioneService;
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
@RequestMapping("/prenotazioni")
public class PrenotazioneController {

    @Autowired
    private PrenotazioneService prenotazioneService;

    @GetMapping
    @PreAuthorize("hasAuthority('MOD_EVENTI') or hasAuthority('ADMIN')")
    public Page<Prenotazione> findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
                                      @RequestParam(defaultValue = "id") String sortBy) {
        return this.prenotazioneService.findAll(page, size, sortBy);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Prenotazione save(@RequestBody @Validated PrenotazioneDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("Ci sono stati errori nel payload! " + message);
        }

        return this.prenotazioneService.save(body);
    }

    @GetMapping("/{prenotazioneId}")
    public Prenotazione findById(@PathVariable UUID prenotazioneId) {
        return this.prenotazioneService.findById(prenotazioneId);
    }

    @PutMapping("/{prenotazioneId}")
    public Prenotazione findByIdAndUpdate(@PathVariable UUID prenotazioneId, @RequestBody @Validated PrenotazioneDTO prenotazioneDTO,
                                          BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream()
                    .map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("Ci sono stati errori nel payload! " + message);
        }

        return this.prenotazioneService.findByIdAndUpdate(prenotazioneId, prenotazioneDTO);
    }


    @DeleteMapping("/{prenotazioneId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable UUID prenotazioneId) {
        this.prenotazioneService.findByIdAndDelete(prenotazioneId);
    }

}
