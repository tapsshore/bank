package co.za.wonderlabz.bank.dtos;

import co.za.wonderlabz.bank.enums.AccountType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateAccountResponseDto {
    private String accountNumber;
    private String message;
    private AccountType accountType;

}
