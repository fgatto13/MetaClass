package com.commigo.metaclass.MetaClass.entity;

import com.commigo.metaclass.MetaClass.utility.multipleid.StatoPartecipazioneId;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;
import org.springframework.transaction.TransactionSystemException;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(StatoPartecipazioneId.class)
public class StatoPartecipazione implements Serializable {


    /**
     * Costante per valore intero di 50.
     */
    public static final int MAX_NAME_LENGTH = 50;

    /**
     *Chiave Esterna sulla stanza
     */
    @Id
    @NotNull(message = "La stanza non può essere nulla")
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_stanza")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Stanza stanza;

    /**
     *Chiave Esterna sull'utente
     */
    @Id
    @NotNull(message = "L'utente non può essere nullo")
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_utente")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Utente utente;

    /**
     *Chiave Esterna sulla ruolo dell'utente
     */
    @NotNull(message = "Il ruolo  non può essere nullo")
    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "id_ruolo")
    private Ruolo ruolo;

    /**
     *isInAttesa per verificare se l'utente è in attesa di entrare nella stanza
     */
    @NotNull(message = "isInAttesa non può essere nullo")
    private boolean isInAttesa;

    /**
     *isBannato per verificare se l'utente è stato bannato da una stanza
     */
    @NotNull(message = "isBannato non può essere nullo")
    private boolean isBannato;


    /**
     * isSilenziato per verificare se l'utente è silenziato in una stanza
     */

    @NotNull(message = "isSilenziato non può essere nullo")
    private boolean isSilenziato;

    /**
     *NomeInStanza identifica il nome dell'utente nella stanza specifica
     */
    @NotNull(message = "Il nome nella stanza non può essere nullo")
    @Column(length = MAX_NAME_LENGTH)
    @Pattern(regexp = "^[A-Z][A-Za-z0-9]*$")
    @Size(min = 1, max = MAX_NAME_LENGTH, message = "Lunghezza del NomeInStanza non valida")
    @NotBlank(message = "Il nome nella stanza non può essere vuota")
    private String nomeInStanza;

    //data creazione e aggiornamento dei dati
    @Column(name = "Data_Creazione", updatable = false)
    @CreationTimestamp
    private LocalDateTime data_Creazione;

    @Column(name = "Data_Aggiornamento")
    @UpdateTimestamp
    private LocalDateTime data_Aggiornamento;

    public void checkRule(){
        try{

            if(this.utente.isAdmin() && this.isBannato)
                throw new TransactionSystemException("un'amministratore se viene bannato "+
                        "se lo può recovare!");

            if(!this.ruolo.getNome().equalsIgnoreCase(Ruolo.PARTECIPANTE)){
                if(this.isInAttesa){

                    throw new TransactionSystemException("non puoi inserire un ruolo " +
                            "diverso da partecipante che sia in attesa");

                }else if(this.ruolo.getNome().equalsIgnoreCase(Ruolo.ORGANIZZATORE_MASTER) &&
                        this.isBannato){

                    throw new TransactionSystemException("L'organizzatore master non può " +
                            "essere inserito come bannato");

                }
            }
        }catch(TransactionSystemException e){
            System.err.println(e.getMessage());
        }
    }

    public StatoPartecipazione(Stanza stanza, Utente utente, Ruolo ruolo,
                               boolean isInAttesa, boolean isBannato, String nomeInStanza, boolean isSilenziato) throws TransactionSystemException {
        this.stanza = stanza;
        this.utente = utente;
        this.ruolo = ruolo;
        this.isInAttesa = isInAttesa;
        this.isBannato = isBannato;
        this.nomeInStanza = nomeInStanza;
        this.isSilenziato = isSilenziato;
        checkRule();
    }
}
