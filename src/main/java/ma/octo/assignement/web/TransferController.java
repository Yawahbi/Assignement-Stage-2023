package ma.octo.assignement.web;

import ma.octo.assignement.domain.Compte;
import ma.octo.assignement.domain.Utilisateur;
import ma.octo.assignement.domain.Transfer;
import ma.octo.assignement.dto.TransferDto;
import ma.octo.assignement.exceptions.CompteNonExistantException;
import ma.octo.assignement.exceptions.SoldeDisponibleInsuffisantException;
import ma.octo.assignement.exceptions.TransactionException;
import ma.octo.assignement.repository.CompteRepository;
import ma.octo.assignement.repository.UtilisateurRepository;
import ma.octo.assignement.repository.TransferRepository;
import ma.octo.assignement.service.AuditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(path="/transfers")
class TransferController {

    public static final int MONTANT_MAXIMAL = 10000;

    Logger LOGGER = LoggerFactory.getLogger(TransferController.class);

    private final CompteRepository compteRepository;

    private final TransferRepository transferRepository;

    private final AuditService auditService;

    private final UtilisateurRepository utilisateurRepository;

    @Autowired
    TransferController(UtilisateurRepository utilisateurRepository,CompteRepository compteRepository,TransferRepository transferRepository,AuditService auditService) {
        this.utilisateurRepository = utilisateurRepository;
        this.compteRepository = compteRepository;
        this.transferRepository = transferRepository;
        this.auditService = auditService;
    }

    @GetMapping("listDesTransferts")
    List<Transfer> loadAll() {
        LOGGER.info("Lister des utilisateurs");
        var all = transferRepository.findAll();

        if (CollectionUtils.isEmpty(all)) {
            return null;
        } else {
            return CollectionUtils.isEmpty(all) ? all : null;
        }
    }

    @GetMapping("listOfAccounts")
    List<Compte> loadAllCompte() {
        List<Compte> all = compteRepository.findAll();

        if (CollectionUtils.isEmpty(all)) {
            return null;
        } else {
            return all;
        }
    }

    @GetMapping("lister_utilisateurs")
    List<Utilisateur> loadAllUtilisateur() {
        List<Utilisateur> all = utilisateurRepository.findAll();

        if (CollectionUtils.isEmpty(all)) {
            return null;
        } else {
            return all;
        }
    }

    @PostMapping("/executerTransfers")
    @ResponseStatus(HttpStatus.CREATED)
    public void createTransaction(@RequestBody TransferDto transferDto)
            throws SoldeDisponibleInsuffisantException, CompteNonExistantException, TransactionException {
        Compte c1 = compteRepository.findByNrCompte(transferDto.getNrCompteEmetteur());
        Compte f12 = compteRepository
                .findByNrCompte(transferDto.getNrCompteBeneficiaire());

        if (c1 == null) {
            System.out.println("Compte Non existant");
            throw new CompteNonExistantException("Compte Non existant");
        }

        if (f12 == null) {
            System.out.println("Compte Non existant");
            throw new CompteNonExistantException("Compte Non existant");
        }

        if (transferDto.getMontant().equals(null)) {
            System.out.println("Montant vide");
            throw new TransactionException("Montant vide");
        } else if (transferDto.getMontant().intValue() == 0) {
            System.out.println("Montant vide");
            throw new TransactionException("Montant vide");
        } else if (transferDto.getMontant().intValue() < 10) {
            System.out.println("Montant minimal de transfer non atteint");
            throw new TransactionException("Montant minimal de transfer non atteint");
        } else if (transferDto.getMontant().intValue() > MONTANT_MAXIMAL) {
            System.out.println("Montant maximal de transfer dépassé");
            throw new TransactionException("Montant maximal de transfer dépassé");
        }

        if (transferDto.getMotif().length() < 0) {
            System.out.println("Motif vide");
            throw new TransactionException("Motif vide");
        }

        if (c1.getSolde().intValue() - transferDto.getMontant().intValue() < 0) {
            LOGGER.error("Solde insuffisant pour l'utilisateur");
        }

        if (c1.getSolde().intValue() - transferDto.getMontant().intValue() < 0) {
            LOGGER.error("Solde insuffisant pour l'utilisateur");
        }

        c1.setSolde(c1.getSolde().subtract(transferDto.getMontant()));
        compteRepository.save(c1);

        f12
                .setSolde(new BigDecimal(f12.getSolde().intValue() + transferDto.getMontant().intValue()));
        compteRepository.save(f12);

        Transfer transfer = new Transfer();
        transfer.setDateExecution(transferDto.getDate());
        transfer.setCompteBeneficiaire(f12);
        transfer.setCompteEmetteur(c1);
        transfer.setMontantTransfer(transferDto.getMontant());

        transferRepository.save(transfer);

        auditService.auditTransfer("Transfer depuis " + transferDto.getNrCompteEmetteur() + " vers " + transferDto
                        .getNrCompteBeneficiaire() + " d'un montant de " + transferDto.getMontant()
                        .toString());
    }

    private void save(Transfer Transfer) {
        transferRepository.save(Transfer);
    }
}
