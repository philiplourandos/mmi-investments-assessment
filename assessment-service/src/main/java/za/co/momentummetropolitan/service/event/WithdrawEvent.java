package za.co.momentummetropolitan.service.event;

import org.springframework.context.ApplicationEvent;

public class WithdrawEvent extends ApplicationEvent {

    private final Long withdrawId;

    public WithdrawEvent(final Object source, final Long withdrawId) {
        super(source);

        this.withdrawId = withdrawId;
    }

    public Long getWithdrawId() {
        return withdrawId;
    }
}
