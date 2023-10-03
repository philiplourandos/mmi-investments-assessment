package za.co.momentummetropolitan;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import za.co.momentummetropolitan.enums.AuthoritiesConst;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles({"test"})
public class ClientProductTest {
    @Container
    @ServiceConnection
    private static final PostgreSQLContainer POSTGRES = new PostgreSQLContainer(TestConst.POSTGRES_IMAGE);

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(username = "philip.lourandos", authorities = {AuthoritiesConst.CLIENT})
    public void givenAuthenticatedClient_whenSubmittingValidId_thenReturnProductsWith200() throws Exception {
        mvc.perform(get("/client/products/{id}", TestConst.VALID_CLIENT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$.[0].id", notNullValue()))
                .andExpect(jsonPath("$.[0].id", isA(Integer.class)))
                .andExpect(jsonPath("$.[0].productName", is("Momentum Money")))
                .andExpect(jsonPath("$.[0].balance", is(120300.00)))
                .andExpect(jsonPath("$.[0].type", is("SAVINGS")))
                .andExpect(jsonPath("$.[1].id", notNullValue()))
                .andExpect(jsonPath("$.[1].id", isA(Integer.class)))
                .andExpect(jsonPath("$.[1].productName", is("Momentum RA")))
                .andExpect(jsonPath("$.[1].balance", is(3000000.12)))
                .andExpect(jsonPath("$.[1].type", is("RETIREMENT")));
    }

    @Test
    @WithMockUser(username = "joe.smith", authorities = {AuthoritiesConst.BROKER})
    public void givenAuthenticatedBroker_whenSubmittingNonExistantId_thenFailWith404() throws Exception {
        mvc.perform(get("/client/products/{id}", "434"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "joe.smith", authorities = {AuthoritiesConst.SALES})
    public void givenAuthenticatedSales_whenSubmittingValidId_thenFailWith403() throws Exception {
        mvc.perform(get("/client/products/{id}", TestConst.VALID_CLIENT_ID))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "james", authorities = {AuthoritiesConst.CLIENT})
    public void givenAuthenticatedClient_whenClientIdWithNoProductsSubmitted_thenReturn204() throws Exception {
        mvc.perform(get("/client/products/{id}", TestConst.VALID_CLIENT_ID_WITH_NOT_PRODUCTS))
                .andExpect(status().isNoContent());
    }
}
