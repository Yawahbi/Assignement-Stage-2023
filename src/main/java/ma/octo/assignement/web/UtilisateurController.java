package ma.octo.assignement.web;

import ma.octo.assignement.domain.Utilisateur;
import ma.octo.assignement.service.UtilisateurService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path="/utilisateurs")
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @GetMapping("lister_utilisateurs")
    @ResponseStatus(HttpStatus.OK)
    List<Utilisateur> loadAllUtilisateur() {
        return utilisateurService.getAll();
    }
}
