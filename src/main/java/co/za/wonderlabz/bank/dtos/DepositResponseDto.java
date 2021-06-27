package co.za.wonderlabz.bank.dtos;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DepositResponseDto {
    private String message;
    private BigDecimal newBalance;
}
