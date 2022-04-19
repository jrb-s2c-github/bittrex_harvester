package za.co.s2c.cb.bittrex_client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import za.co.s2c.cb.bittrex_model.Balance;

import java.util.Set;

@FeignClient(value = "bittrexBalancesClient", url = "https://api.bittrex.com/v3")
public interface BalancesClient {
    @RequestMapping(method = RequestMethod.GET, value = "/balances")
    public Set<Balance> getBalances(@RequestHeader("Api-Key") String apiKey,
                                    @RequestHeader("Api-Timestamp") String apiTimestamp,
                                    @RequestHeader("Api-Content-Hash") String contentHash,
                                    @RequestHeader("Api-Signature") String signature);
}
