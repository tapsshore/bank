package co.za.wonderlabz.bank.service;

import co.za.wonderlabz.bank.domain.Account;
import co.za.wonderlabz.bank.domain.TransactionHistory;
import co.za.wonderlabz.bank.dtos.*;
import co.za.wonderlabz.bank.enums.TransactionType;
import co.za.wonderlabz.bank.exceptions.AccountDoesNotExistException;
import co.za.wonderlabz.bank.repo.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private AccountRepository accountRepository;

    @Value("${overdraft_amount:100000}")
    private BigDecimal overdraftAmount;

    @Autowired
    private TransactionHistoryService historyService;

    @Override
    @Transactional
    public ResponseEntity<DepositResponseDto> deposit(DepositRequestDto depositDto) {
        ResponseEntity<DepositResponseDto> result = null;
        boolean finished = false;

        if (depositDto.getAmount() == null || depositDto.getAmount().compareTo(BigDecimal.ZERO) < 1) {
            String message = "Invalid deposit amount";
            log.error(message);
            result = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(DepositResponseDto.builder().message(message).build());
        } else {
            Account account = null;
            try {
                account = getAccount(depositDto.getAccountNumber());
            } catch (AccountDoesNotExistException e) {
                log.error("", e);
                result = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(DepositResponseDto.builder().message(e.getMessage()).build());
                finished = true;
            }
            if (!finished) {
                if (account.getBalance() == null) {
                    account.setBalance(BigDecimal.ZERO);
                }
                account.setBalance(account.getBalance().add(depositDto.getAmount()));
                account = accountRepository.save(account);
                String message = "Deposited successfully " + depositDto.getAccountNumber() + " New Balance is now " + account.getBalance();
                saveHistrory(depositDto.getAccountNumber(), depositDto.getAmount(), message, TransactionType.DEPOSIT);
                log.info(message);
                DepositResponseDto responseDto = DepositResponseDto.builder()
                        .newBalance(account.getBalance())
                        .message(message)
                        .build();
                result = ResponseEntity.status(HttpStatus.OK).body(responseDto);
            }
        }

        return result;
    }


    @Override
    @Transactional
    public ResponseEntity<WithdrawResponseDto> withdraw(WithdrawRequestDto withdrawDto) {

        if (withdrawDto.getAmount()==null || withdrawDto.getAmount().compareTo(BigDecimal.ZERO)<1) {
            String message = "Invalid withdrawal amount!";
            log.error(message);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(WithdrawResponseDto.builder().message(message).build());
        }
        Account account;
        try {
            account = getAccount(withdrawDto.getAccountNumber());
        } catch (AccountDoesNotExistException e) {
            log.error("",e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(WithdrawResponseDto.builder().message(e.getMessage()).build());
        }

        if(isOverdraft(account, withdrawDto.getAmount())) {
            String message = "Amount " + withdrawDto.getAmount() + " above withdrawal limit!";
            log.error(message);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(WithdrawResponseDto.builder().message(message).build());
        }

        account.setBalance(account.getBalance().subtract(withdrawDto.getAmount()));
        account = accountRepository.save(account);
        String message = "Withdraw of " +withdrawDto.getAmount() +" from " + withdrawDto.getAccountNumber()
                + " successful. New Balance is now R" + account.getBalance();
        WithdrawResponseDto responseDto = WithdrawResponseDto.builder().message(message)
                .newBalance(account.getBalance())
                .build();
        log.info("{}, {}", message, account);
        saveHistrory(withdrawDto.getAccountNumber(), withdrawDto.getAmount(), message, TransactionType.WITHDRAWAL);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    private boolean isOverdraft(Account account, BigDecimal withdrawalAmount){
        BigDecimal maximumWithdrawal = account.getBalance().add(overdraftAmount);
        if (maximumWithdrawal.compareTo(withdrawalAmount) < 0) {
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public ResponseEntity<TransferResponseDto> transfer(TransferRequestDto dto) {

        if (dto.getSourceAccount().equalsIgnoreCase(dto.getDestinationAccount())) {
            String message = "Cannot transfer to the same account "+ dto.getSourceAccount() +"!";
            log.error(message);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(TransferResponseDto.builder().message(message).build());
        }
        Account sourceAccount;
        Account destinationAccount;
        try {
            sourceAccount = getAccount(dto.getSourceAccount());
            destinationAccount = getAccount(dto.getDestinationAccount());
        } catch (AccountDoesNotExistException ex) {
            log.error("",ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(TransferResponseDto.builder().message(ex.getMessage()).build());
        }

        //TODO can a customer transfer more than balance?

        if (sourceAccount.getBalance().compareTo(dto.getAmount()) < 0) {
            String message  = "Amount greater than account balance";
            log.error(message);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(TransferResponseDto.builder().message(message).build());
        }

        sourceAccount.setBalance(sourceAccount.getBalance().subtract(dto.getAmount()));
        destinationAccount.setBalance(destinationAccount.getBalance().add(dto.getAmount()));

        ///TODO add reference generator
        String reference = UUID.randomUUID().toString();
        sourceAccount = accountRepository.save(sourceAccount);
        destinationAccount = accountRepository.save(destinationAccount);

        String message = "Transfer of " + dto.getAmount() +" from " + dto.getSourceAccount() + " to "
                + dto.getDestinationAccount() +" successful. Transfer reference " + reference;
        log.info("{}, {}, {},",message, sourceAccount, destinationAccount);

        saveHistrory(dto.getSourceAccount(), dto.getAmount(), message, TransactionType.TRANSFER);
        saveHistrory(dto.getDestinationAccount(), dto.getAmount(), message, TransactionType.TRANSFER);
        return ResponseEntity.status(HttpStatus.OK).body(TransferResponseDto.builder().message(message).build());
    }

    private Account getAccount(String accountNumber) throws AccountDoesNotExistException {
        Optional<Account> optionalAccount = accountRepository.findByAccountNumber(accountNumber);
        if (!optionalAccount.isPresent()) {
            String message = "Account " + accountNumber + " not found!";
            throw new AccountDoesNotExistException(message);
        }
        return optionalAccount.get();
    }

    private void saveHistrory(String accountNum, BigDecimal amount, String description, TransactionType transactionType){
        historyService.save(TransactionHistory.builder()
                .account(accountNum)
                .amount(amount)
                .transactionType(transactionType)
                .description(description)
                .build());
    }


}
