package co.za.wonderlabz.bank.service;


import co.za.wonderlabz.bank.dtos.*;
import org.springframework.http.ResponseEntity;

public interface TransactionService {

    ResponseEntity<DepositResponseDto> deposit(DepositRequestDto depositDto);

    ResponseEntity<WithdrawResponseDto> withdraw(WithdrawRequestDto withdrawDto);

    ResponseEntity<TransferResponseDto> transfer(TransferRequestDto transferRequestDto);
}
