package co.za.wonderlabz.bank.controller;

import co.za.wonderlabz.bank.dtos.CreateAccountRequestDto;
import co.za.wonderlabz.bank.dtos.CreateAccountResponseDto;
import co.za.wonderlabz.bank.service.AccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("v1/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<CreateAccountResponseDto> createAccount(@RequestBody CreateAccountRequestDto createAccountRequestDto){
        log.info("{}", createAccountRequestDto);
        return accountService.create(createAccountRequestDto);
    }

}
