package za.co.momentummetropolitan;

import static org.hamcrest.CoreMatchers.is;
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
public class ClientInfoTest {

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer POSTGRES = new PostgreSQLContainer(TestConst.POSTGRES_IMAGE);

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(username = "philip.lourandos", authorities = {AuthoritiesConst.CLIENT})
    public void givenAuthenticatedUser_whenRequestingExistingClientInfo_thenReturnDataWith200()
            throws Exception {
        mvc.perform(get("/client/{id}", TestConst.VALID_CLIENT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Philip Lourandos")))
                .andExpect(jsonPath("$.mobile", is("0835550001")))
                .andExpect(jsonPath("$.email", is("philip.lourandos@gmail.com")))
                .andExpect(jsonPath("$.address", is("235 Beach Rd, Sea Point, Cape Town, 8005")));
    }

    @Test
    @WithMockUser(username = "john.smith", authorities = {AuthoritiesConst.SALES})
    public void givenAuthenicatedUserWithSalesRole_whenRequestingValidExistingClientInfo_thenFailWith403() 
            throws Exception {
        mvc.perform(get("/client/{id}", TestConst.VALID_CLIENT_ID))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "philip.lourandos", authorities = {AuthoritiesConst.BROKER})
    public void givenAuthenticatedUser_whenSubmittingInvalidId_thenFailWith404() throws Exception {
        mvc.perform(get("/client/{id}", "445566"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenAnonymousUser_whenSubmittingValidId_thenFailWith403() throws Exception {
        mvc.perform(get("/client/{id}", TestConst.VALID_CLIENT_ID))
                .andExpect(status().isForbidden());
    }
}
