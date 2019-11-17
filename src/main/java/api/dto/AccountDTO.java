package api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class AccountDTO {
    private Long accountNumber;
    private BigDecimal balanceAmount;
    private String currencyCode;
}
