package ma.octo.assignement.web;

import ma.octo.assignement.domain.Compte;
import ma.octo.assignement.service.CompteService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path="/accounts")
public class CompteController {


    private final CompteService compteService;

    public CompteController(CompteService compteService) {
        this.compteService = compteService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<Compte> loadAllCompte() {
        return compteService.getAll();
    }
}
