package in.pinnacle.apps.wallet.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class WalletAPIProducer {

	public static void main(String[] args) {
		SpringApplication.run(WalletAPIProducer.class, args);
	}

}