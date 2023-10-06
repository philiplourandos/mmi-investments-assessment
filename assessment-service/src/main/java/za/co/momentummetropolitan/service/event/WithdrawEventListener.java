package za.co.momentummetropolitan.service.event;

import java.math.BigDecimal;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import za.co.momentummetropolitan.entities.ClientProduct;
import za.co.momentummetropolitan.entities.Withdraw;
import za.co.momentummetropolitan.entities.WithdrawAuditTracking;
import za.co.momentummetropolitan.enums.WithdrawStatusEnum;
import za.co.momentummetropolitan.repository.ClientFinancialProductRepository;
import za.co.momentummetropolitan.repository.WithdrawAuditRepository;
import za.co.momentummetropolitan.repository.WithdrawRepository;

@Component
public class WithdrawEventListener implements ApplicationListener<WithdrawEvent> {

    private final ClientFinancialProductRepository clientProductRepo;
    private final WithdrawRepository withdrawRepo;
    private final WithdrawAuditRepository withdrawAuditRepo;
    private final ApplicationEventPublisher appEventPub;

    public WithdrawEventListener(final ClientFinancialProductRepository clientFinProdRepo,
            final WithdrawRepository withdrawRepo, final WithdrawAuditRepository withdrawAuditRepo,
            final ApplicationEventPublisher appEventPub) {
        this.clientProductRepo = clientFinProdRepo;
        this.withdrawRepo = withdrawRepo;
        this.withdrawAuditRepo = withdrawAuditRepo;
        this.appEventPub = appEventPub;
    }

    @Override
    public void onApplicationEvent(final WithdrawEvent event) {
        final Long withdrawId = event.getWithdrawId();

        final Withdraw clientWithdrawRequest = withdrawRepo.findById(withdrawId).get();
        final WithdrawStatusEnum status = clientWithdrawRequest.getStatus();
        final ClientProduct clientProduct = clientProductRepo.findById(
                clientWithdrawRequest.getClientProductId()).get();
        final BigDecimal clientProductBalance = clientProduct.getBalance();

        logAudit(status, clientProductBalance, withdrawId);

        switch (status) {
            case STARTED -> {
                clientWithdrawRequest.setStatus(WithdrawStatusEnum.EXECUTING);
                withdrawRepo.save(clientWithdrawRequest);

                appEventPub.publishEvent(new WithdrawEvent(this, withdrawId));
            }
            case EXECUTING -> {
                clientProduct.setBalance(clientProductBalance.subtract(
                        clientWithdrawRequest.getAmount()));
                clientProductRepo.save(clientProduct);

                clientWithdrawRequest.setStatus(WithdrawStatusEnum.DONE);
                withdrawRepo.save(clientWithdrawRequest);

                appEventPub.publishEvent(new WithdrawEvent(this, withdrawId));
            }
            case DONE -> {
            }
        }
    }

    private void logAudit(final WithdrawStatusEnum status, final BigDecimal previousBalance,
            final Long withdrawId) {
        final WithdrawAuditTracking tracking = new WithdrawAuditTracking();
        tracking.setPreviousBalance(previousBalance);
        tracking.setWithdrawId(withdrawId);
        tracking.setWithdrawStatus(status);

        withdrawAuditRepo.save(tracking);
    }
}
