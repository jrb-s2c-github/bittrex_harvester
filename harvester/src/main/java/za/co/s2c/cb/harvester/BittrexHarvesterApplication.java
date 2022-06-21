package za.co.s2c.cb.harvester;

import com.hazelcast.config.Config;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import za.co.s2c.cb.bittrex_client.Streamer;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@SpringBootApplication
@EnableCaching
@Slf4j
public class BittrexHarvesterApplication {

	public static void main(String[] args) throws Exception {

		log.error ("bingo1");

		SpringApplication application = new SpringApplication(BittrexHarvesterApplication.class);

		application.run(args);

		Streamer ethBct = new Streamer();
		ethBct.start(new String[] {"candle_ETH-BTC_MINUTE_1", "heartbeat"});
		Streamer adaBtc = new Streamer();
		adaBtc.start(new String[] {"candle_ADA-BTC_MINUTE_1", "heartbeat"});
		Streamer btcUsdt = new Streamer();
		btcUsdt.start(new String[] {"candle_BTC-USDT_MINUTE_1", "heartbeat"});
		Streamer xrpBtc = new Streamer();
		xrpBtc.start(new String[] {"candle_XRP-BTC_MINUTE_1", "heartbeat"});
		Streamer ltcBtc = new Streamer();
		ltcBtc.start(new String[] {"candle_LTC-BTC_MINUTE_1", "heartbeat"});

		TimerTask timerTask = new TimerTask() {


			@Override
			public void run() {
				try {
				long streamerRestartTime = 15000l;
				long now = new Date().getTime();
				if (now - ethBct.lastReception > streamerRestartTime) {
					ethBct.start(new String[]{"candle_ETH-BTC_MINUTE_1", "heartbeat"});
					log.trace("ETH-BTC restarted");
				}

				if (now - adaBtc.lastReception > streamerRestartTime) {
					adaBtc.start(new String[]{"candle_ADA-BTC_MINUTE_1", "heartbeat"});
					log.trace("ADA-BTC restarted");
				}

				if (now - btcUsdt.lastReception > streamerRestartTime) {
					btcUsdt.start(new String[]{"candle_BTC-USDT_MINUTE_1", "heartbeat"});
					log.trace("BTC-USDT restarted");
				}

				if (now - xrpBtc.lastReception > streamerRestartTime) {
					xrpBtc.start(new String[]{"candle_XRP-BTC_MINUTE_1", "heartbeat"});
					log.trace("XRP-BTC restarted");
				}

				if (now - ltcBtc.lastReception > streamerRestartTime) {
					ltcBtc.start(new String[]{"candle_LTC-BTC_MINUTE_1", "heartbeat"});
					log.trace("LTC-BTC restarted");
				}
				} catch (Exception e) {
					// do nothing and wait ?
				}
			}
		};
		Timer timer = new Timer();
		timer.schedule(timerTask, 10000, 20000);
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
