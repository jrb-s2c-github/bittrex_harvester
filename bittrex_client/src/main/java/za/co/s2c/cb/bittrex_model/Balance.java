package za.co.s2c.cb.bittrex_model;

import lombok.Data;

@Data
public class Balance {
    private String currencySymbol;
    private double total;
    private double available;
    private String updatedAt;
}
