package ma.octo.assignement.service;

import ma.octo.assignement.domain.Utilisateur;
import ma.octo.assignement.repository.UtilisateurRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtilisateurService {

    Logger LOGGER = LoggerFactory.getLogger(UtilisateurService.class);

    private final UtilisateurRepository utilisateurRepository;

    public UtilisateurService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    public List<Utilisateur> getAll() {
        LOGGER.info("List de tous les utilisateurs");
        List<Utilisateur> all = utilisateurRepository.findAll();
        return all;
    }
}
