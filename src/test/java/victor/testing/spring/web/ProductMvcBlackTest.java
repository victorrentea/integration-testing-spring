package victor.testing.spring.web;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.infra.SafetyServiceClient;
import victor.testing.spring.repo.SupplierRepo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductMvcBlackTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SafetyServiceClient safetyClient;
    @Autowired
    private SupplierRepo supplierRepo;

    @Test
    public void testSearch() throws Exception {
        // given
        Long supplierId = supplierRepo.save(new Supplier().setActive(true)).getId();
        when(safetyClient.isSafe("UPC")).thenReturn(true);

        // when create
        // language=json
        String createJson = String.format("{\"name\": \"Tree\", \"supplierId\": \"%d\", \"upc\": \"UPC\"}", supplierId);
        mockMvc.perform(post("/product/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
            .andExpect(status().isOk());

        // then search
        mockMvc.perform(post("/product/search")
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(header().string("Custom-Header", "true"))
                .andExpect(jsonPath("$[0].name").value("Tree"));
    }


}
