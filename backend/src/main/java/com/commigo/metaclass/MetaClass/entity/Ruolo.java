package com.commigo.metaclass.MetaClass.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ruolo {

    public static final int MAX_NAME_LENGTH = 254;
    public static final int MIN_NAME_LENGTH = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @NotNull(message = "Il nome non può essere nullo")
    @Column
    @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH, message = "Lunghezza del nome non valida")
    @NotBlank
    private String Nome;

    @Column(name = "Data_Creazione", updatable = false)
    @CreationTimestamp
    private LocalDateTime Data_Creazione;

    @Column(name = "Data_Aggiornamento")
    @UpdateTimestamp
    private LocalDateTime Data_Aggiornamento;

}