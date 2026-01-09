package at.fhtw.genaiworker.archunit;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.library.Architectures.onionArchitecture;

@AnalyzeClasses(
        packages = "at.fhtw.genaiworker",
        importOptions = {
            ImportOption.DoNotIncludeTests.class,
            ImportOption.DoNotIncludeJars.class
        })
public class ArchitectureTest {

    private static final String PKG_DOMAIN_MODEL = "..domain.model..";
    private static final String PKG_DOMAIN_SERVICES = "..domain.service..";
    private static final String PKG_APPLICATION_SERVICES = "..application..";
    private static final String PKG_ADAPTER_PERSISTENCE = "..infrastructure.persistence..";
    private static final String PKG_ADAPTER_CONFIGURATION = "..infrastructure.configuration..";
    private static final String PKG_ADAPTER_MESSAGING = "..infrastructure.messaging..";
    private static final String PKG_ADAPTER_GENAI = "..infrastructure.genai..";

    @ArchTest
    static final ArchRule ONION_ARCHITECTURE = onionArchitecture()
            .domainModels(PKG_DOMAIN_MODEL)
            .domainServices(PKG_DOMAIN_SERVICES)
            .applicationServices(PKG_APPLICATION_SERVICES)
            .adapter("persistence", PKG_ADAPTER_PERSISTENCE)
            .adapter("configuration", PKG_ADAPTER_CONFIGURATION)
            .adapter("messaging", PKG_ADAPTER_MESSAGING)
            .adapter("genai", PKG_ADAPTER_GENAI)
            .allowEmptyShould(true);

}
