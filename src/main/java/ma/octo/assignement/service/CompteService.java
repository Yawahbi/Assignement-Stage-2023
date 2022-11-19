package ma.octo.assignement.service;

import ma.octo.assignement.domain.Compte;
import ma.octo.assignement.repository.CompteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompteService {

    Logger LOGGER = LoggerFactory.getLogger(CompteService.class);

    private final CompteRepository compteRepository;


    public CompteService(CompteRepository compteRepository) {
        this.compteRepository = compteRepository;
    }

    public List<Compte> getAll() {
        LOGGER.info("Lister tous les comptes");
        List<Compte> all = compteRepository.findAll();
        return all;
    }

}
