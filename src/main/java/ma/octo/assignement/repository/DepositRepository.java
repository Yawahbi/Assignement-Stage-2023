package ma.octo.assignement.repository;

import ma.octo.assignement.domain.MoneyDeposit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepositRepository extends JpaRepository<MoneyDeposit, Long> {
}
