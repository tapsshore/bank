package co.za.wonderlabz.bank.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountDto {
    private String fullName;
    private String address;
    private String accountType;
    private String idNumber;
    private String phoneNumber;
    private String emailAddress;
    private BigDecimal balance;


}
