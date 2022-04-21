package za.co.s2c.cb.harvester;

import com.hazelcast.config.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import za.co.s2c.cb.bittrex_client.Streamer;

@SpringBootApplication
@EnableCaching
public class BittrexHarvesterApplication {

	public static void main(String[] args) throws Exception {

		SpringApplication application = new SpringApplication(BittrexHarvesterApplication.class);
//		application.setWebApplicationType(WebApplicationType.NONE);
		application.run(args);
//		SpringApplication.run(BittrexTickersApplication.class, args);
//		new Streamer().start(new String[] {"tickers"});
		new Streamer().start(new String[] {"candle_ETH-BTC_MINUTE_1"});
		new Streamer().start(new String[] {"candle_ADA-BTC_MINUTE_1"});

	}

	@ConditionalOnProperty(name="is_local", havingValue = "false")
	@Bean
	Config config() {
		Config config = new Config();
		config.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(false);
		config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
		config.getNetworkConfig().getJoin().getKubernetesConfig().setEnabled(true)
				.setProperty("namespace", "default")
				.setProperty("service-name", "hazelcast-service");
		return config;
	}
}
