package com.commigo.metaclass.MetaClass.gestionemeeting.controller;

import com.commigo.metaclass.MetaClass.entity.Meeting;
import com.commigo.metaclass.MetaClass.gestionemeeting.service.GestioneMeetingService;
import com.commigo.metaclass.MetaClass.utility.request.RequestUtils;
import com.commigo.metaclass.MetaClass.utility.response.ResponseUtils;
import com.commigo.metaclass.MetaClass.utility.response.types.Response;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GestioneMeetingController {

    @Autowired
    @Qualifier("GestioneMeetingService")
    private GestioneMeetingService meetingService;

    @PostMapping(value = "/meetingStatus/{id}")
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

    }

    @PostMapping(value = "/schedulingMeeting")
    public ResponseEntity<Response<Boolean>> schedulingMeeting(@Valid @RequestBody Meeting m, BindingResult result) {
        try {

            //controlla se i parametri passati al meeting sono corretti
            if(result.hasErrors())
            {
                return ResponseUtils.getResponseError(HttpStatus.INTERNAL_SERVER_ERROR, RequestUtils.errorsRequest(result));
            }

            if (!meetingService.creaScheduling(m)) {
                throw new RuntimeException("Meeting non effettuato");
            } else {
                return ResponseEntity.ok(new Response<>(true, "Meeting schedulato con successo"));
            }

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response<>(false, "Errore durante la schedulazione del meeting: " + e.getMessage()));
        }
    }
}

