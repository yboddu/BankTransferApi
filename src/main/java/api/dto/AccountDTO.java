package api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@ToString
public class AccountDTO {
    private Long accountNumber;
    private BigDecimal balanceAmount;
    private String currencyCode;
}
