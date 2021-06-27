package co.za.wonderlabz.bank.controller;

import co.za.wonderlabz.bank.dtos.*;
import co.za.wonderlabz.bank.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("v1/transact")
public class TransactionController {

    private TransactionService transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<DepositResponseDto> deposit(@RequestBody DepositRequestDto depositRequestDto){
        return transactionService.deposit(depositRequestDto);
    }
    @PostMapping("/withdraw")
    public ResponseEntity<WithdrawResponseDto> withdraw(@RequestBody WithdrawRequestDto withdrawRequestDto){
        return transactionService.withdraw(withdrawRequestDto);
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransferResponseDto> withdraw(@RequestBody TransferRequestDto transferRequestDto){
        return transactionService.transfer(transferRequestDto);
    }

    ///TODO ADD BALANCE INQUIRY

    ///TODO Add endpoints for listing transaction history
}
