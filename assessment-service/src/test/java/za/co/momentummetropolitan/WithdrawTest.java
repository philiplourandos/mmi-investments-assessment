package za.co.momentummetropolitan;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import za.co.momentummetropolitan.entities.ClientProduct;
import za.co.momentummetropolitan.entities.Withdraw;
import za.co.momentummetropolitan.entities.WithdrawAuditTracking;
import za.co.momentummetropolitan.enums.AuthoritiesConst;
import za.co.momentummetropolitan.enums.FinancialProductsEnum;
import za.co.momentummetropolitan.enums.WithdrawStatusEnum;
import za.co.momentummetropolitan.repository.ClientFinancialProductRepository;
import za.co.momentummetropolitan.repository.WithdrawAuditRepository;
import za.co.momentummetropolitan.repository.WithdrawRepository;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles({"test"})
public class WithdrawTest {
    private static final String RETIREMENT_STARTING_BALANCE = "500000.00";
    private static final String RETIREMENT_DONE_BALANCE = "450000.00";
    private static final String WITHDRAW_AMOUNT = "50000.00";
    
    @Container
    @ServiceConnection
    private static final PostgreSQLContainer POSTGRES = new PostgreSQLContainer(TestConst.POSTGRES_IMAGE);

    @MockBean
    private Supplier<LocalDate> mockDateSupplier;

    @Autowired
    private MockMvc mvc;
    
    @Autowired
    private ClientFinancialProductRepository clientFinProdRepo;
    
    @Autowired
    private WithdrawAuditRepository withdrawAuditRepo;

    @Autowired
    private WithdrawRepository withdrawRepo;

    @Test
    @WithMockUser(username = "philip.lourandos", authorities = {AuthoritiesConst.CLIENT})
    public void givenAuthenticatedClient_whenSubmittingWithdrawThatPassesValidation_thenPassWith204() throws Exception {
        // given
        when(mockDateSupplier.get()).thenReturn(LocalDate.of(2050, Month.JUNE, 4));
        final ClientProduct clientsRetirementProduct = clientFinProdRepo.findByClientIdAndType(
                Long.valueOf(TestConst.VALID_CLIENT_ID), FinancialProductsEnum.RETIREMENT)
                .orElseThrow();

        // when
        mvc.perform(post("/client/withdraw")
                .accept(MediaType.APPLICATION_JSON)
                .content("""
                         {
                            "clientProductId": "%s",
                            "amount": "%s"
                         }
                         """.formatted(clientsRetirementProduct.getId(), WITHDRAW_AMOUNT))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // then
        verify(mockDateSupplier, times(1)).get();

        await().atMost(10, TimeUnit.SECONDS).until(() -> {
            final ClientProduct updatedClientProduct = clientFinProdRepo.findByClientIdAndType(
                Long.valueOf(TestConst.VALID_CLIENT_ID), FinancialProductsEnum.RETIREMENT)
                .orElseThrow();
            assertEquals(RETIREMENT_DONE_BALANCE, updatedClientProduct.getBalance().toString());

            final Optional<Withdraw> withdrawOpt = withdrawRepo.findByClientProductId(
                    clientsRetirementProduct.getId());
            assertTrue(withdrawOpt.isPresent());
            final Withdraw withdraw = withdrawOpt.get();

            assertEquals(WITHDRAW_AMOUNT, withdraw.getAmount().toString());
            assertEquals(WithdrawStatusEnum.DONE, withdraw.getStatus());

            final List<WithdrawAuditTracking> auditEvents =
                    withdrawAuditRepo.findByWithdrawIdOrderByEventCreatedAsc(withdraw.getId());
            assertEquals(3, auditEvents.size());

            final WithdrawAuditTracking startedEvent = auditEvents.get(0);
            assertEquals(RETIREMENT_STARTING_BALANCE, startedEvent.getPreviousBalance().toString());
            assertEquals(WithdrawStatusEnum.STARTED, startedEvent.getWithdrawStatus());

            final WithdrawAuditTracking executingEvent = auditEvents.get(1);
            assertEquals(RETIREMENT_STARTING_BALANCE, executingEvent.getPreviousBalance().toString());
            assertEquals(WithdrawStatusEnum.EXECUTING, executingEvent.getWithdrawStatus());

            final WithdrawAuditTracking doneEvent = auditEvents.get(2);
            assertEquals(RETIREMENT_DONE_BALANCE, doneEvent.getPreviousBalance().toString());
            assertEquals(WithdrawStatusEnum.DONE, doneEvent.getWithdrawStatus());

            return true;
        });
    }
}
