package luca.vigano.gestioneEventi.payloads;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PrenotazioneDTO (
        @NotNull(message = "l'user id è obbligatorio!")
        UUID userId,
        @NotNull(message = "l'Id dell'evento è obbligatorio!")
        UUID eventoId,
        @NotNull(message = "Numero posti prenotati è obbligatorio!")
        int numeroposti
) {
}
