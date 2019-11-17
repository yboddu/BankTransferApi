package api.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MoneyTransferDTO {
    private Long fromAccountNumber;
    private Long toAccountNumber;
    private BigDecimal amount;
}
