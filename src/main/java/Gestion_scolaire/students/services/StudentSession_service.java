package Gestion_scolaire.students.services;

import Gestion_scolaire.Classes.entity.Modules;
import Gestion_scolaire.Dto_classe.DTO_response_string;
import Gestion_scolaire.Models.Semestres;
import Gestion_scolaire.Repositories.Modules_repositories;
import Gestion_scolaire.Repositories.Semestre_repositorie;
import Gestion_scolaire.configuration.NoteFundException;
import Gestion_scolaire.students.entity.Inscription;
import Gestion_scolaire.students.entity.StudentSession;
import Gestion_scolaire.students.entity.StudentsClasse;
import Gestion_scolaire.students.repositories.Inscription_repositorie;
import Gestion_scolaire.students.repositories.Sessions_repositorie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentSession_service {

    @Autowired
    private Sessions_repositorie sessions_repositorie;

    @Autowired
    private Inscription_repositorie inscription_repositorie;

    @Autowired
    private Semestre_repositorie semestre_repositorie;

    @Autowired
    private Modules_repositories modules_repositories;

    public Object addSessionNote(long idInscrit, long idSemestre, long idModule,  double note ){
        System.out.println("-------------" + note);
        Inscription inscription = inscription_repositorie.findById(idInscrit);
        if (inscription == null){
            throw new NoteFundException("L'étudiant es introuvable");
        }
        Modules module = modules_repositories.findById(idModule);
        if (module == null){
            throw new NoteFundException("Le module est introuvable");
        }
        Semestres semestre = semestre_repositorie.findById(idSemestre);
        if (semestre == null){
            throw new NoteFundException("Veillez choisir un semestre");
        }
        StudentSession session = sessions_repositorie.findByIdInscritIdAndIdSemestreIdAndIdModuleId(idInscrit,idSemestre,idModule);

        if (session != null) {

            System.out.println("++++++++++++++++++++++" + note);
            session.setNoteSession(note);
            if (note == 0) {
                session.setNbreSession(1);
            } else {
                // Si la note n'est pas 0, on garde la valeur existante de nbreSession
                session.setNbreSession(session.getNbreSession());
            }
            sessions_repositorie.save(session);

            return DTO_response_string.updateMessage();
        }else {
          StudentSession  newsession = new StudentSession();
          newsession.setNoteSession(note);
          newsession.setNbreSession(newsession.getNbreSession() + 1);
          newsession.setIdModule(module);
          newsession.setIdSemestre(semestre);
          newsession.setIdInscrit(inscription);
          sessions_repositorie.save(newsession);
          return DTO_response_string.addMessage();
        }
    }

}
