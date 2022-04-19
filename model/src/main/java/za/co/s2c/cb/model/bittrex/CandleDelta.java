package za.co.s2c.cb.model.bittrex;

import lombok.Data;

import java.util.Date;

@Data
public class CandleDelta {
    private Date startsAt;
    private Double open;
    private Double close;
    private Double high;
    private Double low;
    private Double volume;
    private Double quoteVolume;
}
