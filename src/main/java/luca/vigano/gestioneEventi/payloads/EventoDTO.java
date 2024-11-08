package luca.vigano.gestioneEventi.payloads;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record EventoDTO(
        @NotNull(message = "Titolo evento obbligatorio!")
        String titolo,
        @NotNull(message = "Luogo evento obbligatorio!")
        String luogo,
        @NotNull(message = "Data evento obbligatoria!")
        LocalDate dataevento,
        @NotNull(message = "Descrizione obbligatoria!")
        String descrizione,
        @NotNull(message = "Numero di posti disponibili è obbligatorio!")
        int postimax,
        @NotNull(message = "Id organizzatore obbligatorio!")
        UUID userId

) {
}
