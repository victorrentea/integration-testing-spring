package victor.testing.spring.tools;

import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.TimeUnit;

@Slf4j
public class WaitForSpringActuator implements BeforeAllCallback {

    private final String baseUrl;

    public WaitForSpringActuator(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public void beforeAll(ExtensionContext context) {
        log.info("Waiting for {} to come UP...", baseUrl);
        Awaitility.await().pollInterval(1,TimeUnit.SECONDS)
            .atMost(1, TimeUnit.MINUTES)
            .until(() -> isApplicationUp(baseUrl + "/actuator"));
        log.info("{} is UP", baseUrl);
    }

    public static boolean isApplicationUp(String urlString) {
        try {
            log.debug("Ping {}", urlString);

            var response = HttpClient.newHttpClient()
                .send(HttpRequest.newBuilder().GET().uri(URI.create(urlString)).build(), BodyHandlers.ofString());

            return response.statusCode() == 200;
        } catch (Exception ex) {
            // ignore
            return false;
        }
    }
}
