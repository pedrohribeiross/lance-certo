package io.github.pedrohribeiross.lancecerto.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest // sobe o contexto inteiro da aplicação, como se fosse rodar de verdade (inclusive o banco)
@AutoConfigureMockMvc // cria e configura o MockMvc com a cadeia de filtros de segurança
@ActiveProfiles("test") // usa o application-test.yml
public abstract class IntegrationTest {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;

    protected static MockHttpServletRequestBuilder withToken(
            MockHttpServletRequestBuilder request, String token
    ) {
        return request.header("Authorization", "Bearer " + token);
    }

    protected String toJson(Object object) {
        try{
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new IllegalStateException("Falha ao serializar objeto de teste", e);
        }
    }
}
