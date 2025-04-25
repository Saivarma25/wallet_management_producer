package in.pinnacle.apps.wallet.api.config.kafka;

import in.pinnacle.apps.wallet.api.util.dto.WalletTransactionDTO;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootStrapServers;

    // Wallet Transaction DTO Producer
    @Bean
    public ProducerFactory<Long, WalletTransactionDTO> walletTransactionProducerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.ACKS_CONFIG, "0");
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean(name = "walletTransactionKafkaTemplate")
    public KafkaTemplate<Long, WalletTransactionDTO> walletTransactionKafkaTemplate() {
        return new KafkaTemplate<>(walletTransactionProducerFactory());
    }

    // Wallet DTO Producer
    @Bean
    public ProducerFactory<Long, Object> walletProducerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean(name = "walletKafkaTemplate")
    public KafkaTemplate<Long, Object> walletKafkaTemplate() {
        return new KafkaTemplate<>(walletProducerFactory());
    }

}
