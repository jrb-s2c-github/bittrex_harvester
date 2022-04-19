package za.co.s2c.cb.bittrex_client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.stereotype.Component;
import za.co.s2c.cb.bittrex_model.Market;

import java.util.Set;

@Component
@EnableFeignClients
public class MarketsService {
    @Autowired
    private MarketsClient marketsClient;

    public Set<Market> getMarkets() {
        return marketsClient.getMarkets();
    }
}
