package victor.testing.spring.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import victor.testing.spring.domain.Product;
import victor.testing.spring.repo.ProductRepo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductMvcTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ProductRepo productRepo;

    @Test
    public void testSearch() throws Exception {
        productRepo.save(new Product("Tree"));

        mockMvc.perform(post("/product/search")
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(header().string("Custom-Header", "true"))
//                .andExpect(content().string(contains("Tree")))
                .andExpect(jsonPath("$[0].name").value("Tree"));
    }


}
