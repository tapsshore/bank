package co.za.wonderlabz.bank.service;

import co.za.wonderlabz.bank.dtos.CreateAccountRequestDto;
import co.za.wonderlabz.bank.dtos.CreateAccountResponseDto;
import co.za.wonderlabz.bank.repo.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {


    @Autowired
    private AccountRepository accountRepository;


    @Override
    public ResponseEntity<CreateAccountResponseDto> create(CreateAccountRequestDto createAccountRequest) {

        return null;

    }
}