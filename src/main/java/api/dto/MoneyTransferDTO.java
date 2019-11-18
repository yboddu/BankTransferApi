package api.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class MoneyTransferDTO {
    private Long fromAccountNumber;
    private Long toAccountNumber;
    private BigDecimal amount;
}
