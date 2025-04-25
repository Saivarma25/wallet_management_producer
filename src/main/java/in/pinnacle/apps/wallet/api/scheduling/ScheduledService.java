package in.pinnacle.apps.wallet.api.scheduling;

import in.pinnacle.apps.wallet.api.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduledService {

    private final WalletService walletService;

    /**
     * Scheduler method to sync wallet balances for every 5 minutes
     */
    @Scheduled(fixedDelayString = "${wallet.sync.delay}", initialDelayString = "PT10M")
    public void doWalletSync() {
            walletService.sendWalletSyncFromRedis();
    }

}
