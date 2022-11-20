package ma.octo.assignement.service;

import ma.octo.assignement.domain.Compte;
import ma.octo.assignement.dto.DepositDto;
import ma.octo.assignement.exceptions.CompteNonExistantException;
import ma.octo.assignement.exceptions.TransactionException;
import ma.octo.assignement.repository.CompteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

@SpringBootTest
class DepositServiceTest {
    
    @Autowired
    DepositService depositService;

    @Autowired
    CompteRepository compteRepository;

    @Test
    void shouldThrowCompteNonExistantExceptionWhenRibIsEmpty(){
        DepositDto depositDto = new DepositDto();
        Exception thrown = assertThrows(
            TransactionException.class, 
            ()-> depositService.createDeposit(depositDto)
        );
        assertEquals("RIB vide", thrown.getMessage());
    }
    @Test
    void shouldThrowTransactionExceptionWhenRibIsNotValid(){
        DepositDto depositDto = new DepositDto();
        depositDto.setRib("839403444");
        Exception thrown = assertThrows(
                TransactionException.class,
                ()-> depositService.createDeposit(depositDto)
        );
        assertEquals("RIB non valide", thrown.getMessage());
    }
    @Test
    void shouldThrowCompteNonExistantExceptionWhenNoAccountNotExists(){
        DepositDto depositDto = new DepositDto();
        String rib = "263748594738405746395683";
        depositDto.setRib(rib);
        Exception thrown = assertThrows(
                CompteNonExistantException.class,
                ()-> depositService.createDeposit(depositDto)
        );
        assertEquals("Compte Non existant", thrown.getMessage());
    }
    @Test
    void shouldThrewTransactionExceptionWhenMontantIsNull(){

        DepositDto depositDto = new DepositDto();
        String rib = "127384950695847364758697";
        depositDto.setRib(rib);
        Exception thrown = assertThrows(
                TransactionException.class,
                ()-> depositService.createDeposit(depositDto)
        );
        assertEquals("Montant vide", thrown.getMessage());
    }

    @Test
    void shouldThrewTransactionExceptionWhenSoldIs0(){
        DepositDto depositDto = new DepositDto();
        String rib = "127384950695847364758697";
        depositDto.setRib(rib);
        depositDto.setMontant(new BigDecimal(0));
        Exception thrown = assertThrows(
                TransactionException.class,
                ()-> depositService.createDeposit(depositDto)
        );
        assertEquals("Montant vide", thrown.getMessage());
    }
    @Test
    void shouldThrewTransactionExceptionWhenMontantBelow10(){
        DepositDto depositDto = new DepositDto();
        String rib = "127384950695847364758697";
        depositDto.setRib(rib);
        depositDto.setMontant(new BigDecimal(4));
        Exception thrown = assertThrows(
                TransactionException.class,
                ()-> depositService.createDeposit(depositDto)
        );
        assertEquals("Montant minimal de deposit non atteint", thrown.getMessage());
    }
    @Test
    void shouldThrewTransactionExceptionWhenMontantAbove1000(){
        DepositDto depositDto = new DepositDto();
        String rib = "127384950695847364758697";
        depositDto.setRib(rib);
        depositDto.setMontant(new BigDecimal("30000.34"));
        Exception thrown = assertThrows(
                TransactionException.class,
                ()-> depositService.createDeposit(depositDto)
        );
        assertEquals("Montant dépasse le montant maximal de deposit", thrown.getMessage());
    }
@Test
    void shouldThrewTransactionExceptionWhenEmmeteurNameIsNullOrEmpty(){
        DepositDto depositDto = new DepositDto();
        String rib = "127384950695847364758697";
        depositDto.setRib(rib);
        depositDto.setMontant(new BigDecimal("400"));
        Exception thrown = assertThrows(
                TransactionException.class,
                ()-> depositService.createDeposit(depositDto)
        );
        assertEquals("Emetteur Nom et Prenom vide", thrown.getMessage());
    }

    @Test
    void shouldThrewTransactionExceptionWhenMotifIsNullOrEmpty(){
        DepositDto depositDto = new DepositDto();
        String user1Rib = "127384950695847364758697";
        depositDto.setRib(user1Rib);
        depositDto.setMontant(new BigDecimal("400"));
        depositDto.setNom_prenom_emetteur("wahbi yassine");
        Exception thrown = assertThrows(
                TransactionException.class,
                ()-> depositService.createDeposit(depositDto)
        );
        assertEquals("Motif vide", thrown.getMessage());
    }
}

