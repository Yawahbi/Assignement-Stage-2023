package ma.octo.assignement.web;

import ma.octo.assignement.domain.Transfer;
import ma.octo.assignement.dto.TransferDto;
import ma.octo.assignement.exceptions.CompteNonExistantException;
import ma.octo.assignement.exceptions.SoldeDisponibleInsuffisantException;
import ma.octo.assignement.exceptions.TransactionException;
import ma.octo.assignement.service.TransferService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/transfers")
class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<Transfer> loadAll() {
        return transferService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createTransaction(@RequestBody TransferDto transferDto)
            throws SoldeDisponibleInsuffisantException, CompteNonExistantException, TransactionException {
        transferService.createTransaction(transferDto);
    }
}
