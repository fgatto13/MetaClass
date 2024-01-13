package com.commigo.metaclass.MetaClass.gestionestimaduratameeting.controller;

import com.commigo.metaclass.MetaClass.exceptions.RuntimeException403;
import com.commigo.metaclass.MetaClass.exceptions.ServerRuntimeException;
import com.commigo.metaclass.MetaClass.gestionestimaduratameeting.service.GestioneStimaMeetingService;
import com.commigo.metaclass.MetaClass.utility.response.types.Response;
import com.commigo.metaclass.MetaClass.webconfig.JwtTokenUtil;
import com.commigo.metaclass.MetaClass.webconfig.ValidationToken;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StimaDurataMeetingController {

    @Autowired
    private ValidationToken validationToken;

    @Autowired
    private GestioneStimaMeetingService gestioneStimaMeetingService;
    @GetMapping(value="/stimaMeeting/{id_stanza}")
    public ResponseEntity<Response<Double>> visualizzaStimaDurataMeeting
            (@PathVariable Long id_stanza, HttpServletRequest request){

        try{

           //validazione dl token
           if (!validationToken.isTokenValid(request)) {
               throw new RuntimeException403("Token non valido");
           }

           double durata = gestioneStimaMeetingService.getDurataMeeting(id_stanza);
           return ResponseEntity.ok(new Response<>(durata, "Stima effettuata con successo"));

        } catch (RuntimeException403 re) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new Response<>(null, "Errore durante la richiesta: " + re.getMessage()));
        } catch (ServerRuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response<>(null, "Errore durante la richiesta: " + e.getMessage()));

        }
    }
}