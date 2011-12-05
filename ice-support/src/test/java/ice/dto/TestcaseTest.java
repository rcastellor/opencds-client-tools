package ice.dto;

import ice.util.Utilities;
import java.util.Date;
import org.opencds.Testcase;
import ice.dto.support.CdsObjectAssist;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author HLN Consulting, LLC
 */
public class TestcaseTest {

    private final static Logger logger = Logger.getLogger(TestcaseTest.class);

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

    @Test
    public void testTestcase() throws Exception {
        long start;
        logger.info("Starting testTestcase...");

        // preform first to time get the static initialization of CdsObjectAssist out of the way
        TestcaseWrapper.getTestcaseWrapper();

        start = System.nanoTime();
        TestcaseWrapper testcase = TestcaseWrapper.getTestcaseWrapper();
        Utilities.logDuration("getTestcaseWrapper", start);

        start = System.nanoTime();
        testcase.setAuthor("SDN");
        testcase.setCreatedate(new Date());
        testcase.setDescription("this is a test - blah");
        testcase.setDosefocus(3);
        testcase.setExecutiondate(new Date());
        testcase.setName("blah blah - hey hey");
        testcase.setNotes("blah blah blh blah blah ghlsdf sdfh aof asodiv das vasdvpoi asdv asdvsadvp asdfg");
        testcase.setNumdoses(2);
        testcase.setPatientBirthTime("19700130");
        testcase.setPatientGender("M");
        testcase.setRuletotest("asjdfhla asdf alksd pdf ahf apahwfe qwefh3984 qenka sd87134tu3hpjahfiq asdf");
        testcase.setSeries("Peds");
        testcase.setTestfocus("Evaluation");
        testcase.setVaccinegroup("100");
        testcase.setVersion("1.0.0");
        testcase.addSubstanceAdministrationEvent("100", "08", "20080223", "VALIDITY", "VALID", "");
        testcase.addSubstanceAdministrationEvent("100", "08", "20090223", "VALIDITY", "INVALID", "TOO_RED");
        testcase.addSubstanceAdministrationEvent("200", "43", "20080223", "VALIDITY", "INVALID", "TOO_RED");
        testcase.addSubstanceAdministrationEvent("200", "43", "20090223", "VALIDITY", "VALID", "");
        testcase.addSubstanceAdministrationProposal("200", "43", "20090223", "PROPOSAL", "RECOMMENDED", "DUE_NOW");
        testcase.addSubstanceAdministrationProposal("100", "", "", "PROPOSAL", "NOT_RECOMMENDED", "COMPLETED");
        Utilities.logDuration("testcase init", start);

        start = System.nanoTime();
        logger.info(CdsObjectAssist.cdsObjectToString(testcase.getTestcase(), Testcase.class));
        Utilities.logDuration("cdsObjectToString", start);

        start = System.nanoTime();
        CdsObjectAssist.validateCdsObject(testcase.getTestcase(), "src/main/resources/jaxb/testcase/testcase.xsd");
        Utilities.logDuration("validateCdsObject", start);

        logger.info("Finished testTestcase...");
        assertTrue(true);
    }

}
