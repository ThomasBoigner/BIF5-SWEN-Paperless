package at.fhtw.ocrworker.archunit;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.library.Architectures.onionArchitecture;

@AnalyzeClasses(
        packages = "at.fhtw.ocrworker",
        importOptions = {
            ImportOption.DoNotIncludeTests.class,
            ImportOption.DoNotIncludeJars.class
        })
public class ArchitectureTest {

    private static final String PKG_DOMAIN_MODEL = "..domain.model..";
    private static final String PKG_DOMAIN_SERVICES = "..domain.service..";
    private static final String PKG_APPLICATION_SERVICES = "..application..";
    private static final String PKG_ADAPTER_CONFIGURATION = "..infrastructure.configuration..";
    private static final String PKG_ADAPTER_FILESTORAGE = "..infrastructure.filestorage..";
    private static final String PKG_ADAPTER_MESSAGING = "..infrastructure.messaging..";
    private static final String PKG_ADAPTER_OCR = "..infrastructure.ocr..";

    @ArchTest
    static final ArchRule ONION_ARCHITECTURE = onionArchitecture()
            .domainModels(PKG_DOMAIN_MODEL)
            .domainServices(PKG_DOMAIN_SERVICES)
            .applicationServices(PKG_APPLICATION_SERVICES)
            .adapter("configuration", PKG_ADAPTER_CONFIGURATION)
            .adapter("filestorage", PKG_ADAPTER_FILESTORAGE)
            .adapter("messaging", PKG_ADAPTER_MESSAGING)
            .adapter("ocr", PKG_ADAPTER_OCR)
            .allowEmptyShould(true);

}
