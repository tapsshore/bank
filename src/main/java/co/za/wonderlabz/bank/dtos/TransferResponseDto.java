package co.za.wonderlabz.bank.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransferResponseDto {
    private String message;

}
