package com.commigo.metaclass.MetaClass.gestionemeeting.controller;

import com.commigo.metaclass.MetaClass.entity.Meeting;
import com.commigo.metaclass.MetaClass.exceptions.RuntimeException403;
import com.commigo.metaclass.MetaClass.exceptions.ServerRuntimeException;
import com.commigo.metaclass.MetaClass.gestionemeeting.service.GestioneMeetingService;
import com.commigo.metaclass.MetaClass.utility.multipleid.UtenteInMeetingID;
import com.commigo.metaclass.MetaClass.utility.request.RequestUtils;
import com.commigo.metaclass.MetaClass.utility.response.ResponseUtils;
import com.commigo.metaclass.MetaClass.utility.response.types.Response;
import com.commigo.metaclass.MetaClass.webconfig.JwtTokenUtil;
import com.commigo.metaclass.MetaClass.webconfig.ValidationToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.apache.catalina.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GestioneMeetingController {

    @Autowired
    private GestioneMeetingService meetingService;

    @Autowired
    private ValidationToken validationToken;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /*@PostMapping(value = "/meetingStatus/{id}")
    public ResponseEntity<Response<Boolean>> meetingStatus(@RequestBody String action, @PathVariable("id") Long idMeeting)
    {
        boolean value;
        String message;

        Meeting meeting = meetingService.findMeetingById(idMeeting);

        if(meeting == null)
        {
            message = "Meeting non trovato";
            return ResponseUtils.getResponseError(HttpStatus.INTERNAL_SERVER_ERROR,message);
        }

        switch (action)
        {
            case "start":
                message = "Meeting avviato";
                value = true;
                //inserire la data di inizio meeting
                break;
            case "stop":
                message = "Meeting terminato";
                value = true;
                //inserire la data di fine meeting
                break;
            default:
                message = "Azione non trovata";
                value = false;
        }
        if(value) return ResponseUtils.getResponseOk(message);
        return ResponseUtils.getResponseError(HttpStatus.INTERNAL_SERVER_ERROR,message);

    }*/

    @PostMapping(value = "/schedulingMeeting")
    public ResponseEntity<Response<Boolean>> schedulingMeeting(@Valid @RequestBody Meeting m,
                                                               BindingResult result,
                                                               HttpServletRequest request) {
        try {

            //controllo token
            if (!validationToken.isTokenValid(request)) {
                throw new RuntimeException403("Token non valido");
            }

            //controlla se i parametri passati al meeting sono corretti
            if(result.hasErrors()) {
                throw new RuntimeException403(RequestUtils.errorsRequest(result));
            }

            String metaID = jwtTokenUtil.getMetaIdFromToken(validationToken.getToken());

            meetingService.creaScheduling(m,metaID);
            return ResponseEntity.ok(new Response<>(true, "Meeting schedulato con successo"));

        } catch (RuntimeException403 e) {
            return ResponseEntity.status(403)
                    .body(new Response<>(false, "Errore durante la schedulazione del meeting: " + e.getMessage()));
        } catch (ServerRuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response<>(false, "Errore durante la schedulazione del meeting: " + e.getMessage()));

        }
    }

    @PostMapping(value = "/modificaScheduling")
    public ResponseEntity<Response<Boolean>> modificaScheduling
            (@Valid @RequestBody Meeting m, BindingResult result, HttpServletRequest request) {

        try {
            //controllo token
            if (!validationToken.isTokenValid(request)) {
                throw new RuntimeException403("Token non valido");
            }

            //controllo errori di validazione
            if(result.hasErrors())
            {
                return ResponseEntity.status(403)
                        .body(new Response<>(false, RequestUtils.errorsRequest(result)));
            }

            if (!meetingService.modificaScheduling(m)) {
                throw new ServerRuntimeException("modifica non effettuata");
            } else {
                return ResponseEntity.ok(new Response<>(true, "Meeting schedulato con successo"));
            }

        } catch (RuntimeException403 e) {
            return ResponseEntity.status(403)
                    .body(new Response<>(false, e.getMessage()));
        } catch (ServerRuntimeException se) {
            return ResponseEntity.status(500)
                    .body(new Response<>(false, se.getMessage()));
        }
    }

    @PostMapping(value = "/avviaMeeting/{id_meeting}")
    public ResponseEntity<Response<Boolean>> avviaMeeting (@PathVariable Long id_meeting,
                                                            HttpServletRequest request) {
        try {
            //controllo token
            if (!validationToken.isTokenValid(request)) {
                throw new RuntimeException403("Token non valido");
            }

            String metaID = jwtTokenUtil.getMetaIdFromToken(validationToken.getToken());

            if(meetingService.avviaMeeting(metaID, id_meeting)){
                return ResponseEntity.ok(new Response<>(true,
                        "Avvio meeting avvenuto con successo"));
            }else{
                throw new ServerRuntimeException("Errore nell'avvio del meeting");
            }


        }catch (RuntimeException403 e) {
            return ResponseEntity.status(403)
                    .body(new Response<>(false, e.getMessage()));
        } catch (ServerRuntimeException se) {
            return ResponseEntity.status(500)
                    .body(new Response<>(false, se.getMessage()));
        }
    }

    @PostMapping(value = "/visualizzaSchedulingMeeting/{Id}")
    public ResponseEntity<Response<List<Meeting>>> visualizzaSchedulingMeeting(@PathVariable Long Id,
                                                                               HttpServletRequest request) {
        try {
            if (!validationToken.isTokenValid(request)) {
                throw new RuntimeException403("Token non valido");
            }

            return meetingService.visualizzaSchedulingMeeting(Id);

        }catch (Exception e) {
            return ResponseEntity.ok (new Response<>(null, "Errore visualizzazione Scheduling dei meeting per la stanza"));
        }

    }
        @PostMapping(value = "/accediMeeting/{id_meeting}")
    public ResponseEntity<Response<Boolean>> accediMeeting (@PathVariable Long id_meeting,
                                                            HttpServletRequest request) {
        try {

            //controllo token
            if (!validationToken.isTokenValid(request)) {
                throw new RuntimeException403("Token non valido");
            }

            String metaID = jwtTokenUtil.getMetaIdFromToken(validationToken.getToken());

            if(meetingService.accediMeeting(metaID, id_meeting)){
                 return ResponseEntity.ok(new Response<>(true,
                         "Accesso avvenuto con successo"));
            }else{
                throw new ServerRuntimeException("Errore nell'accesso al meeting");
            }
        }catch (RuntimeException403 e) {
            return ResponseEntity.status(403)
                    .body(new Response<>(false, e.getMessage()));
        } catch (ServerRuntimeException se) {
            return ResponseEntity.status(500)
                    .body(new Response<>(false, se.getMessage()));
        }

    }

    @PostMapping(value = "/terminaMeeting/{id_meeting}")
    public ResponseEntity<Response<Boolean>> terminaMeeting (@PathVariable Long id_meeting,
                                                           HttpServletRequest request) {
        try {
            //controllo token
            if (!validationToken.isTokenValid(request)) {
                throw new RuntimeException403("Token non valido");
            }

            String metaID = jwtTokenUtil.getMetaIdFromToken(validationToken.getToken());

            if(meetingService.terminaMeeting(metaID, id_meeting)){
                return ResponseEntity.ok(new Response<>(true,
                        "Meeting terminato con successo"));
            }else{
                throw new ServerRuntimeException("Errore nella terminazione del meeting");
            }

        }catch (RuntimeException403 e) {
            return ResponseEntity.status(403)
                    .body(new Response<>(false, e.getMessage()));
        }catch (ServerRuntimeException se) {
            return ResponseEntity.status(500)
                    .body(new Response<>(false, se.getMessage()));
        }
    }

    @PostMapping(value = "/uscitaMeeting/{id_meeting}")
    public ResponseEntity<Response<Boolean>> uscitaMeeting (@PathVariable Long id_meeting,
                                                             HttpServletRequest request) {
        try {
            //controllo token
            if (!validationToken.isTokenValid(request)) {
                throw new RuntimeException403("Token non valido");
            }

            String metaID = jwtTokenUtil.getMetaIdFromToken(validationToken.getToken());

            if(meetingService.uscitaMeeting(metaID, id_meeting)){
                return ResponseEntity.ok(new Response<>(true,
                        "Uscita avvenuta con successo"));
            }else{
                throw new ServerRuntimeException("Errore nell'uscita dell'utente del meeting");
            }

        }catch (RuntimeException403 e) {
            return ResponseEntity.status(403)
                    .body(new Response<>(false, e.getMessage()));
        }catch (ServerRuntimeException se) {
            return ResponseEntity.status(500)
                    .body(new Response<>(false, se.getMessage()));
        }
    }

    @GetMapping("/visualizzaQuestionari")
    public ResponseEntity<Response<List<Meeting>>> visualizzaQuestionario (
            HttpServletRequest request) {

        try {
            //controllo token
            if (!validationToken.isTokenValid(request)) {
                throw new RuntimeException403("Token non valido");
            }

            String metaID = jwtTokenUtil.getMetaIdFromToken(validationToken.getToken());
            List<Meeting> meetingToQuest = meetingService.visualizzaQuestionari(metaID);
                return ResponseEntity.ok(new Response<>(meetingToQuest,
                        "La stampa dei meeting su cui bisogna compilare il questionario è" +
                                " avvenuto con successo"));

        }catch (RuntimeException403 e) {
            return ResponseEntity.status(403)
                    .body(new Response<>(null, e.getMessage()));
        }catch (ServerRuntimeException se) {
            return ResponseEntity.status(500)
                    .body(new Response<>(null, se.getMessage()));
        }
    }

    @PostMapping("/compilaQuestionario/{id_meeting}")
    public ResponseEntity<Response<Boolean>> compilaQuestionario(@RequestBody String JSONvalue,
                                                                 @PathVariable Long id_meeting,
                                                                 HttpServletRequest request) {

        try {
            //controllo token
            if (!validationToken.isTokenValid(request)) {
                throw new RuntimeException403("Token non valido");
            }

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(JSONvalue);
            JsonNode valutazioneNode = jsonNode.get("valutazione");

            int value = (valutazioneNode != null && !valutazioneNode.isNull()) ? valutazioneNode.asInt() : 0;
            if(value==0)
                throw new RuntimeException403("inserire 'valutazione' come attributo");

            String metaID = jwtTokenUtil.getMetaIdFromToken(validationToken.getToken());

            meetingService.compilaQuestionario(value, metaID, id_meeting);
            return ResponseEntity.ok(new Response<>
                    (true,"questionario compilato con successo"));

        }catch (RuntimeException403 e) {
            return ResponseEntity.status(403)
                    .body(new Response<>(null, e.getMessage()));
        }catch (ServerRuntimeException se) {
            return ResponseEntity.status(500)
                    .body(new Response<>(null, se.getMessage()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/visualizzaMeetingPrecedenti")
    public ResponseEntity<Response<List<Meeting>>> visualizzaMeetingPrecedeni (
            HttpServletRequest request) {

        try {
            //controllo token
            if (!validationToken.isTokenValid(request)) {
                throw new RuntimeException403("Token non valido");
            }

            String metaID = jwtTokenUtil.getMetaIdFromToken(validationToken.getToken());

            List<Meeting> meetingToQuest = meetingService.getMeetingPrecedenti(metaID);
            return ResponseEntity.ok(new Response<>(meetingToQuest,
                    "Operazione avvenuta con successo"));

        }catch (RuntimeException403 e) {
            return ResponseEntity.status(403)
                    .body(new Response<>(null, e.getMessage()));
        }catch (ServerRuntimeException se) {
            return ResponseEntity.status(500)
                    .body(new Response<>(null, se.getMessage()));
        }
    }


}

