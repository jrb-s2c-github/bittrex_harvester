package za.co.s2c.cb.model.linqua_franca;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Builder
@EqualsAndHashCode
@Table("candles")
public class Candle implements Serializable {
    @PrimaryKey
    @Getter
    private UUID id;
    private Integer sequence;
    @Getter
    private String marketSymbol;
    private String interval;
    private Integer decision; // -1 for short, 0 no and 1 for buy
    @Getter
    private Date startsAt;
    @Getter
    private Double open;
    @Getter
    private Double close;
    @Getter
    private Double high;
    @Getter
    private Double low;
    private Double volume;
    private Double quoteVolume;
    private Date tradeFulfilledTS;
    private UUID fulfillingCandleID;

    public Candle replicateWithDifferentDecision(Integer newDecision, UUID fulfillingCandleID, Date tradeFulfilledTS) {
        za.co.s2c.cb.model.linqua_franca.Candle result = Candle.builder().
                id(id).
                close(close).
                decision(newDecision).
                high(high).
                interval(interval).
                low(low).
                marketSymbol(marketSymbol).
                open(open).
                quoteVolume(quoteVolume).
                sequence(sequence).
                volume(volume).
                startsAt(startsAt).
                fulfillingCandleID(fulfillingCandleID).
                tradeFulfilledTS(tradeFulfilledTS).
                build();

        return result;
    }
}
