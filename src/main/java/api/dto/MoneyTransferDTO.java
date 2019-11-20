package api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class MoneyTransferDTO {
    private Long fromAccountNumber;
    private Long toAccountNumber;
    private BigDecimal amount;
}
