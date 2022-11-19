package ma.octo.assignement.service;

import ma.octo.assignement.domain.Compte;
import ma.octo.assignement.domain.Transfer;
import ma.octo.assignement.dto.TransferDto;
import ma.octo.assignement.exceptions.CompteNonExistantException;
import ma.octo.assignement.exceptions.SoldeDisponibleInsuffisantException;
import ma.octo.assignement.exceptions.TransactionException;
import ma.octo.assignement.repository.CompteRepository;
import ma.octo.assignement.repository.TransferRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransferService {

    public static final int MONTANT_MAXIMAL = 10000;

    Logger LOGGER = LoggerFactory.getLogger(TransferService.class);

    private final TransferRepository transferRepository;

    private final CompteRepository compteRepository;

    private final AuditService auditService;


    public TransferService(TransferRepository transferRepository, CompteRepository compteRepository, AuditService auditService) {
        this.transferRepository = transferRepository;
        this.compteRepository = compteRepository;
        this.auditService = auditService;
    }

    public List<Transfer> getAll(){
        LOGGER.info("Lister tous les transfers");
        var all = transferRepository.findAll();
        return all;
    }

    public void createTransaction(TransferDto transferDto) throws CompteNonExistantException, SoldeDisponibleInsuffisantException, TransactionException {
        Compte emetteur = compteRepository.findByNrCompte(transferDto.getNrCompteEmetteur());
        Compte beneficaire = compteRepository.findByNrCompte(transferDto.getNrCompteBeneficiaire());

        if (emetteur == null || beneficaire == null) {
            LOGGER.error("Compte Non existant");
            throw new CompteNonExistantException("Compte Non existant");
        }

        if (transferDto.getMontant().equals(null)) {
            LOGGER.error("Montant vide");
            throw new TransactionException("Montant vide");
        } else if (transferDto.getMontant().intValue() == 0) {
            LOGGER.error("Montant vide");
            throw new TransactionException("Montant vide");
        } else if (transferDto.getMontant().intValue() < 10) {
            LOGGER.error("Montant minimal de transfer non atteint");
            throw new TransactionException("Montant minimal de transfer non atteint");
        } else if (transferDto.getMontant().intValue() > MONTANT_MAXIMAL) {
            LOGGER.error("Montant maximal de transfer dépassé");
            throw new TransactionException("Montant maximal de transfer dépassé");
        }

        if (transferDto.getMotif().length() < 0) {
            LOGGER.error("Motif vide");
            throw new TransactionException("Motif vide");
        }

        if (emetteur.getSolde().intValue() - transferDto.getMontant().intValue() < 0) {
            LOGGER.error("Solde insuffisant pour l'utilisateur");
            throw new SoldeDisponibleInsuffisantException("Solde insuffisant pour l'utilisateur");
        }

        emetteur.setSolde(emetteur.getSolde().subtract(transferDto.getMontant()));
        compteRepository.save(emetteur);

        beneficaire.setSolde(beneficaire.getSolde().add(transferDto.getMontant()));
        compteRepository.save(beneficaire);

        Transfer transfer = new Transfer();
        transfer.setDateExecution(transferDto.getDate());
        transfer.setCompteBeneficiaire(beneficaire);
        transfer.setCompteEmetteur(emetteur);
        transfer.setMontantTransfer(transferDto.getMontant());

        transferRepository.save(transfer);

        auditService.auditTransfer("Transfer depuis " + transferDto.getNrCompteEmetteur() + " vers " + transferDto
                .getNrCompteBeneficiaire() + " d'un montant de " + transferDto.getMontant()
                .toString());
    }
}
