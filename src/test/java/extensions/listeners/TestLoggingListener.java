package extensions.listeners;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.extension.*;

@Slf4j
public class TestLoggingListener implements BeforeAllCallback,
        AfterAllCallback,
        BeforeEachCallback,
        AfterEachCallback,
        BeforeTestExecutionCallback,
        AfterTestExecutionCallback {

    public static String separator = "*" + StringUtils.repeat("*", 80);

    @Override
    public void afterAll(ExtensionContext context) {
        log.info("\n" + separator + "\n <<<<<<<<< Finished tests \'" + context.getRequiredTestClass() + "\'. >>>>>>>>>>>>\n" + separator);
    }

    @Override
    public void beforeEach(ExtensionContext context) {

    }

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        log.info("\n" + separator + "\n <<<<<<<<< Starting \'" + context.getDisplayName() + "\' test. >>>>>>>>>>>>\n" + separator);
    }

    @Override
    public void afterTestExecution(ExtensionContext context)  {
        log.info("\n" + separator + "\n <<<<<<<<< Finishing \'" + context.getDisplayName() + "\' test.\n" + separator);
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {

    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        log.info("\n" + separator + "\n <<<<<<<<< Starting tests \'" + context.getRequiredTestClass() + "\'. >>>>>>>>>>>>\n" + separator);
    }




}

