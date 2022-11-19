package ma.octo.assignement.web;

import ma.octo.assignement.domain.MoneyDeposit;
import ma.octo.assignement.dto.DepositDto;
import ma.octo.assignement.exceptions.CompteNonExistantException;
import ma.octo.assignement.exceptions.TransactionException;
import ma.octo.assignement.service.DepositService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/deposits")
class DepositController {

    private final DepositService depositService;

    public DepositController(DepositService depositService) {
        this.depositService = depositService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<MoneyDeposit> loadAll() {
        return depositService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createDeposit(@RequestBody DepositDto depositDto)
            throws CompteNonExistantException, TransactionException {
        depositService.createDeposit(depositDto);
    }
}
