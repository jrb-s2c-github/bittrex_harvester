package za.co.s2c.cb.bittrex_model;

import lombok.Data;

@Data
public class Market {
    private String symbol;
    private String baseCurrencySymbol;
    private String quoteCurrencySymbol;
    private double minTradeSize;
    private int precision;
    private String status;
    private String createdAt;
    private String notice;
}
