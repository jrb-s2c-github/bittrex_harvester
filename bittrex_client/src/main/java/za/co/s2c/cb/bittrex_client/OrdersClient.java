package za.co.s2c.cb.bittrex_client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import za.co.s2c.cb.bittrex_model.NewOrder;
import za.co.s2c.cb.bittrex_model.Order;

import java.util.List;

@FeignClient(value = "bittrexOrdersClient", url = "https://api.bittrex.com/v3")
@Component
public interface OrdersClient {
    @RequestMapping(method = RequestMethod.GET, value = "/orders/closed")
    List<Order> getClosedOrders(@RequestHeader("Api-Key") String apiKey,
                                 @RequestHeader("Api-Timestamp") String apiTimestamp,
                                 @RequestHeader("Api-Content-Hash") String contentHash,
                                 @RequestHeader("Api-Signature") String signature,
                                 @RequestParam String marketSymbol);

    @RequestMapping(method = RequestMethod.GET, value = "/orders/open")
    List<Order> getOpenOrders(@RequestHeader("Api-Key") String apiKey,
                              @RequestHeader("Api-Timestamp") String apiTimestamp,
                              @RequestHeader("Api-Content-Hash") String contentHash,
                              @RequestHeader("Api-Signature") String signature,
                              @RequestParam String marketSymbol);

    @RequestMapping(method = RequestMethod.POST, value = "/orders", consumes = "application/json")
    Order newOrder(@RequestHeader("Api-Key") String apiKey,
                              @RequestHeader("Api-Timestamp") String apiTimestamp,
                              @RequestHeader("Api-Content-Hash") String contentHash,
                              @RequestHeader("Api-Signature") String signature,
                              @RequestBody String newOrder);
}
