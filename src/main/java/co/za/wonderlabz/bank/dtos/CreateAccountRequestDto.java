package co.za.wonderlabz.bank.dtos;

import co.za.wonderlabz.bank.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountRequestDto {
    @NonNull
    private String fullName;
    @NonNull
    private String address;
    @NonNull
    private AccountType accountType;
    @NonNull
    private String idNumber;
    @NonNull
    private String phoneNumber;
    @NonNull
    private String emailAddress;
    @NonNull
    private BigDecimal initialDeposit;
}
