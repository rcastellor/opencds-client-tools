package ice.service;

import ice.dto.CdsInputWrapper;
import ice.dto.support.CdsObjectAssist;
import ice.util.Utilities;
import java.util.Date;
import javax.xml.ws.WebServiceException;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.opencds.CdsInput;
import org.opencds.CdsOutput;

/**
 *
 * @author HLN Consulting, LLC
 */
public class OpenCdsServiceTest {

    private final static Logger logger = Logger.getLogger(OpenCdsServiceTest.class);

    public OpenCdsServiceTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of evaluate method, of class OpenCdsAssist.
     * @throws Exception
     */
    @Test(expected=WebServiceException.class)
    public void testEvaluateConnect() throws Exception {
        long start;
        logger.info("Starting testEvaluateConnect...");

        String endPoint = "http://dev.hln.info:8120/opencds-decision-support-service-1.0.0-SNAPSHOT/evaluate";

        start = System.nanoTime();
        OpenCdsService service = OpenCdsService.getOpenCDS(endPoint, 10, 10);
        Utilities.logDuration("OpenCdsService init", start);
        CdsInputWrapper cdsInputWrapper = CdsInputWrapper.getCdsInputWrapper();
        String businessId = "bounce";
        Date executionDate = new Date();
        start = System.nanoTime();
        CdsOutput output = service.evaluate(cdsInputWrapper.getCdsObject(), businessId, executionDate);
        Utilities.logDuration("evaluate test 1", start);
    }

    /**
     * Test of evaluate method, of class OpenCdsAssist.
     * @throws Exception
     */
    @Test
    @Ignore
    public void testEvaluate() throws Exception {
        long start;
        logger.info("Starting testEvaluate...");

        String endPoint = "http://dev.hln.info:8120/opencds-decision-support-service-1.0.0-SNAPSHOT/evaluate";

        // preform first to time get the static initialization of OpenCdsService out of the way
        OpenCdsService.getOpenCDS(endPoint);

        start = System.nanoTime();
        OpenCdsService service = OpenCdsService.getOpenCDS(endPoint);
        Utilities.logDuration("OpenCdsService init", start);

        // preform first to time get the static initialization of CdsObjectAssist out of the way
        CdsInputWrapper.getCdsInputWrapper();

        CdsInputWrapper input = CdsInputWrapper.getCdsInputWrapper();
        input.setPatientGender("F");
        input.setPatientBirthTime("19830630");
        input.addSubstanceAdministrationEvent("45", "20080223");

        start = System.nanoTime();
        input = CdsInputWrapper.getCdsInputWrapper();
        Utilities.logDuration("getCdsInputWrapper", start);

        start = System.nanoTime();
        input.setPatientGender("F");
        input.setPatientBirthTime("19830630");
        input.addSubstanceAdministrationEvent("45", "20080223");
//        input.addSubstanceAdministrationEvent("10", "20080223");
//        input.addImmunityObservationResult("500", "VALID", "IS_IMMUNE");
//
//        input.addSubstanceAdministrationEvent("43", "20080223");
//
//        input.addSubstanceAdministrationEvent("08", "20090223");
//
//        input.addSubstanceAdministrationEvent("43", "20080223");

//        String inputXml = CdsObjectAssist.cdsObjectToString(input.getCdsObject(), CdsInput.class);

        Utilities.logDuration("input init", start);

        String businessId = "bounce";
        Date executionDate = new Date();

        start = System.nanoTime();
        byte[] cdsObjectToByteArray = CdsObjectAssist.cdsObjectToByteArray(input.getCdsObject(), CdsInput.class);
        Utilities.logDuration("cdsObjectToByteArray", start);

        start = System.nanoTime();
        byte[] evaluation = service.evaluate(cdsObjectToByteArray, businessId, executionDate);
        Utilities.logDuration("evaluate test 1", start);

        start = System.nanoTime();
        evaluation = service.evaluate(cdsObjectToByteArray, businessId, executionDate);
        Utilities.logDuration("evaluate test 2", start);

        start = System.nanoTime();
        evaluation = service.evaluate(cdsObjectToByteArray, businessId, executionDate);
        Utilities.logDuration("evaluate test 3", start);

        start = System.nanoTime();
        evaluation = service.evaluate(cdsObjectToByteArray, businessId, executionDate);
        Utilities.logDuration("evaluate test 4", start);

        start = System.nanoTime();
        evaluation = service.evaluate(cdsObjectToByteArray, businessId, executionDate);
        Utilities.logDuration("evaluate test 5", start);

        start = System.nanoTime();
        CdsOutput result = CdsObjectAssist.cdsObjectFromByteArray(evaluation, CdsOutput.class);
        Utilities.logDuration("cdsObjectFromByteArray", start);

        logger.info("Finished testEvaluate...");
        assertTrue(result instanceof CdsOutput);
    }
}
