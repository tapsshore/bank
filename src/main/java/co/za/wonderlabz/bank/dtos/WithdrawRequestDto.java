package co.za.wonderlabz.bank.dtos;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class WithdrawRequestDto {
    private String accountNumber;
    private BigDecimal amount;
}
