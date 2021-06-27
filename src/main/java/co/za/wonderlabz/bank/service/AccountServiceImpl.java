package co.za.wonderlabz.bank.service;

import co.za.wonderlabz.bank.domain.Account;
import co.za.wonderlabz.bank.dtos.CreateAccountRequestDto;
import co.za.wonderlabz.bank.dtos.CreateAccountResponseDto;
import co.za.wonderlabz.bank.enums.AccountType;
import co.za.wonderlabz.bank.repo.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {


    @Autowired
    private AccountRepository accountRepository;
    @Value("${initial_deposit:1000}")
    private Double initialDeposit;
    @Autowired
    private Mapper mapper;

    @Override
    public ResponseEntity<CreateAccountResponseDto> create(CreateAccountRequestDto createAccountRequest) {

        Account account = mapper.map(createAccountRequest, Account.class);
        account.setAccountNumber(Instant.now().getEpochSecond() + "");

        if (createAccountRequest.getAccountType() == AccountType.SAVINGS) {
            if (createAccountRequest.getInitialDeposit() == null || createAccountRequest.getInitialDeposit().doubleValue() < initialDeposit) {
                String message = "Saving account must have an initial deposit of R" + initialDeposit;
                log.error(message);
                CreateAccountResponseDto createAccountResponseDto = CreateAccountResponseDto.builder()
                        .message(message)
                        .build();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createAccountResponseDto);
            }
            account.setBalance(createAccountRequest.getInitialDeposit());
        }
        account = accountRepository.save(account);
        String message = "Account created successfully";
        log.info("{} {}", message, account);
        CreateAccountResponseDto createAccountResponseDto = CreateAccountResponseDto.builder()
                .message(message)
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(createAccountResponseDto);
    }
}
