package victor.testing.spring.facade;


import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.lanwen.wiremock.ext.WiremockResolver;
import ru.lanwen.wiremock.ext.WiremockResolver.Wiremock;
import ru.lanwen.wiremock.ext.WiremockUriResolver;
import ru.lanwen.wiremock.ext.WiremockUriResolver.WiremockUri;
import victor.testing.spring.domain.Product;
import victor.testing.spring.domain.Supplier;
import victor.testing.spring.facade.ProductFacade;
import victor.testing.spring.facade.WireMockExtension;
import victor.testing.spring.repo.ProductRepo;
import victor.testing.spring.web.ProductDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@Transactional
@ActiveProfiles("db-mem")
@SpringBootTest(properties = "safety.service.url.base=http://localhost:8089")
public class ProductFacadeWireMockTest {
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private ProductFacade productFacade;

    @MockBean
    private Clock clock;

    private LocalDateTime currentTime = LocalDateTime.now();

    @BeforeEach
    public void setupTime() {
        when(clock.instant()).thenAnswer(call -> currentTime.toInstant(ZoneId.systemDefault().getRules().getOffset(currentTime)));
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
    }


    @RegisterExtension
    public WireMockExtension extension = new WireMockExtension(8089);

    @Test
    public void throwsWhenNotSafe() {
        Assertions.assertThrows(IllegalStateException.class, () -> {
            Product product = new Product().setExternalRef("UNSAFE").setSupplier(new Supplier().setActive(true));

            productRepo.save(product);

            productFacade.getProduct(product.getId());
        });
    }

    @Test
    public void success() {
        Product product = new Product()
            .setName("Prod")
            .setExternalRef("SAFE")
            .setSupplier(new Supplier().setActive(true)); // long-live CascadeType.PERSIST
        productRepo.save(product);
        currentTime = LocalDateTime.parse("2020-01-01T20:00:00");

        WireMock.stubFor(get(urlEqualTo("/product/SAFE/safety"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("[{\"category\":\"DETERMINED\", \"safeToSell\":true}]")));
        //                                          ^ BUG in client!
        ProductDto dto = productFacade.getProduct(product.getId());

        assertThat(dto.productName).isEqualTo("Prod");
        System.out.println(dto.sampleDate);
        assertThat(dto.sampleDate).isEqualTo("2020-01-01T19:59:55");
    }
}
