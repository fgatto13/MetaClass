package com.commigo.metaclass.MetaClass.entity;

import com.commigo.metaclass.MetaClass.exceptions.MismatchJsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Categoria {

    
    public static final int MAX_NAME_LENGTH = 254;
    public static final int MIN_NAME_LENGTH = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "Il nome non può essere nullo")
    @Column(unique = true)
    @Size(min = MIN_NAME_LENGTH,
            max = MAX_NAME_LENGTH,
            message = "Lunghezza nome errata")
    @Pattern(regexp = "^[A-Z][A-Za-z0-9\\s]*$",
            message = "Formato nome errato")
    @NotBlank (message ="Il nome della categoria non può essere vuoto")
    private String nome;


    @NotNull(message = "La descrizione non può essere nulla")
    @Column
    @Size(min = MIN_NAME_LENGTH,
            max = MAX_NAME_LENGTH,
            message = "Lunghezza descrizione errata")
    @Pattern(regexp="^[a-zA-Z0-9.,!?()'\"\\-\\s]+$",
            message = "Formato descrizione errato")
    @NotBlank (message ="La descrizione della categoria non può essere vuoto")
    private String descrizione_categoria;


    @JsonCreator
    public Categoria(@JsonProperty("nome") String nome,
                     @JsonProperty("descrizione") String descrizione_categoria) throws MismatchJsonProperty {

        if (nome == null || descrizione_categoria == null) {
            throw new MismatchJsonProperty("gli attributi non sono corretti");
        }
        this.nome = nome;
        this.descrizione_categoria = descrizione_categoria;

    }
}
