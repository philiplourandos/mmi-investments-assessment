package za.co.momentummetropolitan;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.Month;
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
import za.co.momentummetropolitan.enums.AuthoritiesConst;
import za.co.momentummetropolitan.enums.FinancialProductsEnum;
import za.co.momentummetropolitan.repository.ClientFinancialProductRepository;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles({"test"})
public class WithdrawNegativeScenarioTest {
    private static final String EXPECTED_RA_BALANCE = "500000.00";

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer POSTGRES = new PostgreSQLContainer(TestConst.POSTGRES_IMAGE);

    @MockBean
    private Supplier<LocalDate> mockDateSupplier;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ClientFinancialProductRepository clientFinProdRepo;

    @Test
    @WithMockUser(username = "jose", authorities = {AuthoritiesConst.CLIENT})
    public void givenAuthenticatedValidClient_whenWithdrawingBeforeRetirementFromRA_thenFailWith400() throws Exception {
        // given
        when(mockDateSupplier.get()).thenReturn(LocalDate.of(2010, Month.APRIL, 23));
        final ClientProduct clientsRetirementProduct = clientFinProdRepo.findByClientIdAndType(
                Long.valueOf(TestConst.VALID_CLIENT_ID), FinancialProductsEnum.RETIREMENT)
                .orElseThrow();
        final String expectedYearsTooEarlyForRetirement = "[31]";

        // when
        mvc.perform(post("/client/withdraw")
                .accept(MediaType.APPLICATION_JSON)
                .content("""
                         {
                            "clientProductId": "%s",
                            "amount": "50000"
                         }
                         """.formatted(clientsRetirementProduct.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail", containsString(expectedYearsTooEarlyForRetirement)));

        // then
        verify(mockDateSupplier, times(1)).get();

        final ClientProduct updatedClientProduct = clientFinProdRepo.findByClientIdAndType(
                Long.valueOf(TestConst.VALID_CLIENT_ID), FinancialProductsEnum.RETIREMENT)
                .orElseThrow();
        assertEquals(EXPECTED_RA_BALANCE, updatedClientProduct.getBalance().toString());
    }

    @Test
    @WithMockUser(username = "jose", authorities = {AuthoritiesConst.CLIENT})
    public void givenAuthenticatedValidClient_whenWithdrawingMoreThanTheAvailableBalanceFromRA_thenFailWith400()
            throws Exception {
        when(mockDateSupplier.get()).thenReturn(LocalDate.of(2050, Month.APRIL, 23));
        final ClientProduct clientsRetirementProduct = clientFinProdRepo.findByClientIdAndType(
                Long.valueOf(TestConst.VALID_CLIENT_ID), FinancialProductsEnum.RETIREMENT)
                .orElseThrow();
        final String invalidWithdrawAmount = "5000000";

        // when
        mvc.perform(post("/client/withdraw")
                .accept(MediaType.APPLICATION_JSON)
                .content("""
                         {
                            "clientProductId": "%s",
                            "amount": "%s"
                         }
                         """.formatted(clientsRetirementProduct.getId(), invalidWithdrawAmount))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail",
                        containsString("[%s]".formatted(invalidWithdrawAmount))))
                .andExpect(jsonPath("$.detail",
                        containsString("[%s]".formatted(clientsRetirementProduct.getBalance()))));

        // then
        verify(mockDateSupplier, times(1)).get();

        final ClientProduct updatedClientProduct = clientFinProdRepo.findByClientIdAndType(
                Long.valueOf(TestConst.VALID_CLIENT_ID), FinancialProductsEnum.RETIREMENT)
                .orElseThrow();
        assertEquals(EXPECTED_RA_BALANCE, updatedClientProduct.getBalance().toString());
    }

    @Test
    @WithMockUser(username = "jose", authorities = {AuthoritiesConst.CLIENT})
    public void givenAuthenticatedValidClient_whenWithdrawingMoreThan90PercentOfTheRA_thenFailWith400()
            throws Exception {
        // given
        when(mockDateSupplier.get()).thenReturn(LocalDate.of(2050, Month.APRIL, 23));
        final ClientProduct clientsRetirementProduct = clientFinProdRepo.findByClientIdAndType(
                Long.valueOf(TestConst.VALID_CLIENT_ID), FinancialProductsEnum.RETIREMENT)
                .orElseThrow();
        final String expectedExceededPercent = "96";
        
        // when
        mvc.perform(post("/client/withdraw")
                .accept(MediaType.APPLICATION_JSON)
                .content("""
                         {
                            "clientProductId": "%s",
                            "amount": "480000"
                         }
                         """.formatted(clientsRetirementProduct.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail", containsString("[%s]".format(expectedExceededPercent))));

        // then
        verify(mockDateSupplier, times(1)).get();

        final ClientProduct updatedClientProduct = clientFinProdRepo.findByClientIdAndType(
                Long.valueOf(TestConst.VALID_CLIENT_ID), FinancialProductsEnum.RETIREMENT)
                .orElseThrow();
        assertEquals(EXPECTED_RA_BALANCE, updatedClientProduct.getBalance().toString());
    }
}
