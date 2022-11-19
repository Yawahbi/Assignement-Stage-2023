package ma.octo.assignement.service;

import ma.octo.assignement.domain.Compte;
import ma.octo.assignement.domain.MoneyDeposit;
import ma.octo.assignement.dto.DepositDto;
import ma.octo.assignement.exceptions.CompteNonExistantException;
import ma.octo.assignement.exceptions.TransactionException;
import ma.octo.assignement.repository.CompteRepository;
import ma.octo.assignement.repository.DepositRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DepositService {

    public static final int MONTANT_MAXIMAL = 1000;

    Logger LOGGER = LoggerFactory.getLogger(DepositService.class);

    private final DepositRepository depositRepository;

    private final CompteRepository compteRepository;

    private final AuditService auditService;

    public DepositService(DepositRepository depositRepository, CompteRepository compteRepository, AuditService auditService) {
        this.depositRepository = depositRepository;
        this.compteRepository = compteRepository;
        this.auditService = auditService;
    }

    public List<MoneyDeposit> getAll(){
        LOGGER.info("Lister tous les deposits");
        var all = depositRepository.findAll();
        return all;
    }

    public void createDeposit(DepositDto depositDto) throws CompteNonExistantException, TransactionException {
        if(depositDto.getRib() == null){
            LOGGER.error("RIB vide");
            throw new TransactionException("RIB vide");
        } else if (depositDto.getRib().length() != 24){
            LOGGER.error("RIB non valide");
            throw new TransactionException("RIB non valide");
        }
        Compte compte = compteRepository.findByRib(depositDto.getRib());
        if (compte==null) {
            LOGGER.error("Compte Non existant");
            throw new CompteNonExistantException("Compte Non existant");
        }
        if (depositDto.getMontant() == null) {
            LOGGER.error("Montant vide");
            throw new TransactionException("Montant vide");
        }else if (depositDto.getMontant().intValue() == 0) {
            LOGGER.error("Montant vide");
            throw new TransactionException("Montant vide");
        } else if (depositDto.getMontant().intValue() < 10) {
            LOGGER.error("Montant minimal de deposit non atteint");
            throw new TransactionException("Montant minimal de deposit non atteint");
        } else if (depositDto.getMontant().intValue() > MONTANT_MAXIMAL) {
            LOGGER.error("Montant dépasse le montant maximal de deposit");
            throw new TransactionException("Montant maximal de deposit dépassé");
        }

        if( depositDto.getNom_prenom_emetteur() == null || depositDto.getNom_prenom_emetteur().length() < 0 ){
            LOGGER.error("Emetteur Nom et Prenom vide");
            throw new TransactionException(("Emetteur Nom et Prenom vide"));
        }
        if (depositDto.getMotifDeposit()==null || depositDto.getMotifDeposit().length() < 0) {
            LOGGER.error("Motif vide");
            throw new TransactionException("Motif vide");
        }

        compte.setSolde(compte.getSolde().add(depositDto.getMontant()));
        compteRepository.save(compte);

        MoneyDeposit deposit = new MoneyDeposit();
        deposit.setMontant(depositDto.getMontant());
        deposit.setCompteBeneficiaire(compte);
        deposit.setMotifDeposit(depositDto.getMotifDeposit());
        deposit.setDateExecution(new Date());
        deposit.setNom_prenom_emetteur(depositDto.getNom_prenom_emetteur());

        depositRepository.save(deposit);

        auditService.auditDeposit("Deposit vers le compte de RIB"+ depositDto.getRib()+" par "+ depositDto.getNom_prenom_emetteur());

    }
}
