package Gestion_scolaire.Controllers;

import Gestion_scolaire.Models.Notifications;
import Gestion_scolaire.Services.Notification_service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api-notification")
public class Notifications_controller {

    @Autowired
    private Notification_service notification_service;

    @PostMapping("/add")
    public Notifications addNotification(@RequestBody Notifications notifications) {
       return notification_service.addAmin(notifications);
    }
//    ---------------------------get all notification of the week
    @GetMapping("/list-week-notifs")
    public List<Notifications> getWeekNotification(){
       return notification_service.getListNotification();
    }
}
