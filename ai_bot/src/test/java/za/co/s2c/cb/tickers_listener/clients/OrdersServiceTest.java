package za.co.s2c.cb.tickers_listener.clients;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import za.co.s2c.cb.bittrex_client.OrdersService;
import za.co.s2c.cb.bittrex_model.NewOrder;
import za.co.s2c.cb.bittrex_model.Order;

// TODO: 2022/03/15  uncomment below cases
@ActiveProfiles("test")
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringBootTest
//@AutoConfigureWireMock(port = 9091)
public class OrdersServiceTest {
    @Autowired
    OrdersService ordersService;

//    @Test
    public void testGetClosedOrders() {
        ordersService.getClosedOrders();
    }

//    @Test
    public void testGetOpenOrders() {
        ordersService.getOpenOrders();
    }

//    @Test
    public void testNewOrder() {
        NewOrder newOrder = new NewOrder();
        newOrder.setMarketSymbol("EUR-USDT");
        Order orderCreated = ordersService.newOrders(newOrder);
    }
}
