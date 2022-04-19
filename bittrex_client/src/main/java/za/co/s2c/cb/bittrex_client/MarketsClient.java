package za.co.s2c.cb.bittrex_client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import za.co.s2c.cb.bittrex_model.Market;

import java.util.Set;

@FeignClient(value = "bittrexMarketsClient", url = "https://api.bittrex.com/v3")
@Component
public interface MarketsClient {
    @RequestMapping(method = RequestMethod.GET, value = "/markets")
    public Set<Market> getMarkets();
}
