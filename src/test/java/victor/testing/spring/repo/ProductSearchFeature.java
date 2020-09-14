package victor.testing.spring.repo;

import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.platform.engine.Cucumber;

@Cucumber
@CucumberOptions(features = "classpath:victor/testing/spring/repo/product-search.feature",
        glue = {"victor.testing.spring.repo","victor.testing.spring.tools"})
public class ProductSearchFeature {

}
