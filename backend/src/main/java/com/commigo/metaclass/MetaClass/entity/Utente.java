package com.commigo.metaclass.MetaClass.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Utente {

    /**
     * Costante per valore intero di 254.
     */
    public static final int MAX_NAME_LENGTH = 254;

    /**
     * Costante per valore intero di 1.
     */
    public static final int MIN_NAME_LENGTH = 1;

    /**
     * Lunghezza campo sesso.
     */
    public static final int SEX_LENGTH = 1;

    /**
     * Costante per valore intero di 114.
     */
    public static final int MAX_ETA_LENGTH = 114;

    /**
     * Costante per valore intero di 10.
     */
    public static final int MIN_ETA_LENGTH = 10;

    /**
     * Costante per valore intero di 10.
     */
    private static final int MAX_PHONE_LENGTH = 10;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private long Id;


    @NotNull(message = "Il nome non può essere nullo")
    @Column(length = MAX_NAME_LENGTH)
    @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH,
            message = "Lunghezza nome non valida")
    @NotBlank(message = "Il nome non può essere vuoto")
    private String Nome;

    @NotNull(message = "Il cognome non può essere nullo")
    @Column(length = MAX_NAME_LENGTH)
    @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH,
            message = "Lunghezza cognome non valida")
    @NotBlank(message = "Il cognome non può essere vuoto")
    private String Cognome;

    @NotNull(message = "Il sesso non può essere nullo")
    @Column(length = SEX_LENGTH)
    @Size(min = SEX_LENGTH, max = SEX_LENGTH,
            message = "Lunghezza sesso non valida")
    @NotBlank(message = "Il sesso non può essere vuoto")
    @Pattern(regexp = "^[MFO]$", message = "Il genere deve essere 'M', 'F' o 'O'")
    private String Sesso;

    @NotNull(message = "L'età non può essere nulla")
    @Column(length = MAX_ETA_LENGTH)
    @Min(value = MIN_ETA_LENGTH, message = "L'età deve essere maggiore o uguale a 10")
    @Max(value = MAX_ETA_LENGTH, message = "L'età deve essere minore o uguale a 114")
    private int Età;

    @NotNull(message = "IsAdmin non può essere nullo")
    private boolean IsAdmin;

    @NotNull(message = "L'email non può essere nulla")
    @Email(message = "Formato email non valida")
    private String Email;

    @Column(length = MAX_PHONE_LENGTH)
    @Size(min = MAX_PHONE_LENGTH, max = MAX_PHONE_LENGTH,
            message = "Lunghezza telefono non valida")
    @Pattern(regexp = "^[0-9]{10}$",
            message = "Formato telefono non valido")
    private String Telefono;

    //da valutare la lunghezza della stringa
    @NotNull(message = "IdMeta non può essere nulla")
    @Column(length = MAX_NAME_LENGTH, unique = true)
    @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH,
            message = "Lunghezza IdMeta non valida")
    @NotBlank(message = "Il IdMeta non può essere vuoto")
    private String metaId;

    //da valutare la lunghezza della stringa
    @NotNull(message = "TokenAuth non può essere nulla")
    @Column(length = MAX_NAME_LENGTH)
    @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH,
            message = "Lunghezza TokenAuth non valida")
    @NotBlank(message = "Il TokenAuth non può essere vuoto")
    private String TokenAuth;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime DataCreazione;

    @UpdateTimestamp
    private LocalDateTime DataAggiornamento;

    @ManyToOne
    @JoinColumn(name = "report_id")
    private Report report;

    @JsonCreator
    public Utente(@JsonProperty("Nome") String Nome,
                  @JsonProperty("Cognome") String Cognome,
                  @JsonProperty("Sesso") String Sesso,
                  @JsonProperty("Età") int Età,
                  @JsonProperty("Email") String Email,
                  @JsonProperty("metaId") String IdMeta,
                  @JsonProperty("TokenAuth") String TokenAuth) {
        this.Nome = Nome;
        this.Cognome = Cognome;
        this.Sesso = Sesso;
        this.Età = Età;
        this.Email = Email;
        this.metaId = IdMeta;
        this.TokenAuth = TokenAuth;
    }
}
