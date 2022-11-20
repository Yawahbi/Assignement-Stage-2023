package ma.octo.assignement.repository;

import ma.octo.assignement.domain.Compte;
import ma.octo.assignement.domain.Transfer;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Date;

@SpringBootTest
public class TransferRepositoryTest {

  @Autowired
  private TransferRepository transferRepository;

  @Autowired
  private CompteRepository compteRepository;

  @Test
  public void save() {

    Compte compte1 = compteRepository.findByNrCompte("010000A000001000");
    Compte compte2 = compteRepository.findByNrCompte("010000B025001000");

    Transfer transfer = new Transfer();
    transfer.setCompteEmetteur(compte1);
    transfer.setCompteBeneficiaire(compte2);
    transfer.setMotifTransfer("test motif 1");
    transfer.setMontantTransfer(new BigDecimal("60000"));
    transfer.setDateExecution(new Date());

    transferRepository.save(transfer);

    assertThat(transferRepository.findById(transfer.getId()).isPresent()).isTrue();
  }

  @Test
  public void findOne() {
    assertThat(transferRepository.findById(100L).isPresent()).isFalse();
  }

  @Test
  public void findAll() {
    assertThat(transferRepository.findAll().size()).isGreaterThanOrEqualTo(1);
  }


  @Test
  public void delete() {
    Compte compte1 = compteRepository.findByNrCompte("010000A000001000");
    Compte compte2 = compteRepository.findByNrCompte("010000B025001000");

    Transfer transfer = new Transfer();
    transfer.setCompteEmetteur(compte1);
    transfer.setCompteBeneficiaire(compte2);
    transfer.setMotifTransfer("test motif 1");
    transfer.setMontantTransfer(new BigDecimal("60000"));
    transfer.setDateExecution(new Date());

    transferRepository.save(transfer);

    transferRepository.deleteById(transfer.getId());
    assertThat(transferRepository.findById(transfer.getId()).isPresent()).isFalse();
  }
}