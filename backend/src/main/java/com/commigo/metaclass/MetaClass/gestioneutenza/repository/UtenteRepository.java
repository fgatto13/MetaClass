package com.commigo.metaclass.MetaClass.gestioneutenza.repository;

import com.commigo.metaclass.MetaClass.entity.Utente;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository("UtenteRepository")
public interface UtenteRepository extends JpaRepository<Utente, Long> {

    /**
     *
     * @param idMeta
     * @return
     */
    Utente findFirstByMetaId(String idMeta);

    /**
     *
     * @param id
     * @return
     */
    Utente findUtenteById(long id);

    //il valore restituito indica il numero di righe modificate, quindi:
    // 1 se tutto ok
    // 0 se non è ok
    /**
     *
     * @param SessionID
     * @param attributes
     * @return
     */
    @Modifying
    @Transactional
    @Query("UPDATE Utente u SET " +
            "u.nome = COALESCE(:#{#attributes['nome']}, u.nome), " +
            "u.cognome = COALESCE(:#{#attributes['cognome']}, u.cognome), " +
            "u.sesso = COALESCE(:#{#attributes['sesso']}, u.sesso), " +
            "u.dataDiNascita = COALESCE(:#{#attributes['dataDiNascita']}, u.dataDiNascita), " +
            "u.email = COALESCE(:#{#attributes['email']}, u.email), " +
            "u.telefono = COALESCE(:#{#attributes['telefono']}, u.telefono), " +
            "u.metaId = COALESCE(:#{#attributes['metaId']}, u.metaId), " +
            "u.tokenAuth = COALESCE(:#{#attributes['tokenAuth']}, u.tokenAuth) " +
            "WHERE u.metaId = :SessionID")
    int updateAttributes(@Param("SessionID") String SessionID, @Param("attributes") Map<String, Object> attributes);

}
