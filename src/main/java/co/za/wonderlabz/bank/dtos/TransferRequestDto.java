package co.za.wonderlabz.bank.dtos;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TransferRequestDto {

    private String sourceAccount;
    private String destinationAccount;
    private String reason;
    private BigDecimal amount;

}
