package co.za.wonderlabz.bank.service;


import co.za.wonderlabz.bank.dtos.CreateAccountRequestDto;
import co.za.wonderlabz.bank.dtos.CreateAccountResponseDto;
import org.springframework.http.ResponseEntity;

public interface AccountService {
    ResponseEntity<CreateAccountResponseDto> create(CreateAccountRequestDto createAccountRequest);

}
