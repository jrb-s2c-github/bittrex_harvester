package za.co.s2c.cb.model.bittrex;

import lombok.Data;

import java.util.Set;

@Data
public class TickersResult {
    private int sequence;
    private Set<Ticker> deltas;
}
