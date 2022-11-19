package ma.octo.assignement.mapper;

import ma.octo.assignement.domain.MoneyDeposit;
import ma.octo.assignement.dto.DepositDto;

public class DepositMapper {

    private static DepositDto DepositDto;

    public static DepositDto map(MoneyDeposit moneyDeposit) {
        DepositDto = new DepositDto();
        DepositDto.setRib(moneyDeposit.getCompteBeneficiaire().getRib());
        DepositDto.setNom_prenom_emetteur(moneyDeposit.getNom_prenom_emetteur());
        DepositDto.setDateExecution(moneyDeposit.getDateExecution());
        DepositDto.setMotifDeposit(moneyDeposit.getMotifDeposit());

        return DepositDto;

    }
}
