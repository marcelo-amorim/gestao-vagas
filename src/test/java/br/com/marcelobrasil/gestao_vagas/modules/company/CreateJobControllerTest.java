package br.com.marcelobrasil.gestao_vagas.modules.company;


import br.com.marcelobrasil.gestao_vagas.modules.company.dto.CreateJobDTO;
import br.com.marcelobrasil.gestao_vagas.modules.company.entities.CompanyEntity;
import br.com.marcelobrasil.gestao_vagas.modules.company.repositories.CompanyRepository;
import br.com.marcelobrasil.gestao_vagas.modules.utils.TestUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

// @RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CreateJobControllerTest {
    @Value("${security.token.secret.company}")
    private String SECRET_KEY;

    private MockMvc mvc;

    @Autowired private WebApplicationContext context;

    @Autowired private CompanyRepository companyRepository;

    @BeforeEach
    public void setup() {
        mvc =
                MockMvcBuilders.webAppContextSetup(context)
                        .apply(SecurityMockMvcConfigurers.springSecurity())
                        .build();
    }

    @Test
    public void should_be_able_to_create_a_new_job() throws Exception {
        var companyDTO =
                CompanyEntity.builder()
                        .email("company@email.com")
                        .description("Company Description")
                        .name("Company Name")
                        .password(UUID.randomUUID().toString())
                        .build();

        var companyId = companyRepository.saveAndFlush(companyDTO).getId();

        var createJobDTO =
                CreateJobDTO.builder()
                        .benefits("BENEFITS_TEST")
                        .description("DESCRIPTION")
                        .level("LEVEL_TEST")
                        .build();

        var result =
                mvc.perform(
                                MockMvcRequestBuilders.post("/company/job")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(TestUtils.objectToJSON(createJobDTO))
                                        .header(
                                                "Authorization",
                                                TestUtils.generateToken(companyId, SECRET_KEY)))
                        .andExpect(MockMvcResultMatchers.status().isOk());

        System.out.println(result);
    }

    @Test()
    public void should_not_create_job_if_company_not_found() throws Exception {
        var createJobDTO =
                CreateJobDTO.builder()
                        .benefits("BENEFITS_TEST")
                        .description("DESCRIPTION")
                        .level("LEVEL_TEST")
                        .build();

        mvc.perform(
                        MockMvcRequestBuilders.post("/company/job")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.objectToJSON(createJobDTO))
                                .header(
                                        "Authorization",
                                        TestUtils.generateToken(UUID.randomUUID(), SECRET_KEY)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
