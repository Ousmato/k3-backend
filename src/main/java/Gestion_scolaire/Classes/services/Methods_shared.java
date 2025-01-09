package Gestion_scolaire.Classes.services;

import Gestion_scolaire.Services.Modules_service;
import Gestion_scolaire.Services.Note_service;
import Gestion_scolaire.Services.Semestre_service;
import Gestion_scolaire.students.services.Inscription_service;
import Gestion_scolaire.students.services.Student_service;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Data
@Component
public  class  Methods_shared {

    @Autowired
    private Semestre_service semestre_service;


    @Autowired
    private  Modules_service modules_service;

    @Autowired
    private Inscription_service inscription_service;

    @Autowired
    private Student_service student_service;

//    @Autowired
//    private Note_service note_service;


}
