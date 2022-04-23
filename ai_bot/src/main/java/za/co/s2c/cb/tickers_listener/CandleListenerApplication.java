package za.co.s2c.cb.tickers_listener;

import com.hazelcast.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import javax.annotation.PostConstruct;


@SpringBootApplication
@EnableCaching
@Slf4j
@EnableCassandraRepositories(
        basePackages = "za.co.s2c.cb.model.linqua_franca")
//@ComponentScan(basePackages = {"", "za.co.s2c.cb.tickers_listener"})
@ComponentScan(basePackages = {"za.co.s2c.cb.tickers_listener"})
public class CandleListenerApplication {
    @Autowired
    private CandleListener candleListener;

    public static void main(String[] args) {
        SpringApplication.run(CandleListenerApplication.class, args);
    }

    @PostConstruct
    void postBuild() {
        candleListener.init();
    }

    @ConditionalOnProperty(name="is_local", havingValue = "false")
    @Bean
    Config config() {
        log.error ("bingo2");
        Config config = new Config();
        config.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(false);
        config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
        config.getNetworkConfig().getJoin().getKubernetesConfig().setEnabled(true)
                .setProperty("namespace", "default")
                .setProperty("service-name", "hazelcast-service");
        return config;
    }
}
