package za.co.s2c.cb.model.bittrex;

import lombok.Data;

@Data
public class Candle {
    private Integer sequence;
    private String marketSymbol;
    private String interval;
    private CandleDelta delta;
}
