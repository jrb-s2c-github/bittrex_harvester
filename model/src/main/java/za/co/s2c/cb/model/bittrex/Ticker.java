package za.co.s2c.cb.model.bittrex;

import lombok.Data;

@Data
public class Ticker {
    private String symbol;
    double lastTradeRate;
    double bidRate;
    double askRate;
}
