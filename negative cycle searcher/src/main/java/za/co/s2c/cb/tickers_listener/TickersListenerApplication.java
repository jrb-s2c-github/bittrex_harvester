package za.co.s2c.cb.tickers_listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PostConstruct;


@SpringBootApplication
@EnableCaching
@Slf4j
@ComponentScan(basePackages = {"za.co.s2c.cb.bittrex_client", "za.co.s2c.cb.tickers_listener"})
public class TickersListenerApplication {
    @Autowired
    private TickersListener tickersListener;

    public static void main(String[] args) {
        SpringApplication.run(TickersListenerApplication.class, args);
    }

    @PostConstruct
    void postBuild() {
        tickersListener.init();
    }
}
