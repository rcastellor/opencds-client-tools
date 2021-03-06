package org.cdsframework.cds.testcase.importers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.datatype.DatatypeConfigurationException;
import org.apache.poi.POIXMLProperties;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.cdsframework.cds.testcase.TestCasePropertyType;
import org.cdsframework.cds.testcase.TestCaseWrapper;
import org.cdsframework.cds.exceptions.CdsException;
import org.cdsframework.cds.util.LogUtils;

/**
 *
 * @author HLN Consulting, LLC
 */
public class XlsxImporterUtils {

    private final static LogUtils logger = LogUtils.getLogger(XlsxImporterUtils.class);

    public static void importFromWorkBook(XSSFWorkbook wb, TestImportCallback callback)
            throws CdsException, FileNotFoundException, IOException, DatatypeConfigurationException {
        Map<String, String> vaccineGroupMap = new HashMap<String, String>();
        vaccineGroupMap.put("DTaP", "200");
        vaccineGroupMap.put("Tdap", "200");
        vaccineGroupMap.put("Td", "200");
        vaccineGroupMap.put("Td", "200");
        POIXMLProperties properties = wb.getProperties();
        POIXMLProperties.CoreProperties coreProperties = properties.getCoreProperties();
        String category = coreProperties.getCategory();
        logger.info("category: " + category);
        XSSFSheet sheet = wb.getSheet("VTP All Cases");
        if (sheet == null) {
            throw new CdsException("Bad format - missing VTP All Cases sheet");
        }
        String testSheetName = sheet.getSheetName();
        int lastRowNum = sheet.getLastRowNum();
        logger.info("LastRowNum: " + lastRowNum);
        int i = 1;
        int testCount = 0;
        XSSFRow row = sheet.getRow(i);
        Iterator<Cell> cellIterator = row.cellIterator();
        int cellNumber = 0;
        while (cellIterator.hasNext()) {
            Cell nextCell = cellIterator.next();
            logger.info("Cell number: ", cellNumber, " - ", nextCell.getStringCellValue());
            cellNumber++;
        }
        while (i < lastRowNum) {
            i++;
            logger.info("parsing row: ", i);
            row = sheet.getRow(i);

            // testId
            String testId = null;
            try {
                XSSFCell testIdCell = row.getCell(0);
                double testIdCellValue = testIdCell.getNumericCellValue();
                testId = String.valueOf((int) testIdCellValue);
                logger.info("Got testId: ", testId);
            } catch (Exception e) {
                logger.error("Error getting testId!");
            }

            // testName
            String testName = null;
            try {
                XSSFCell testNameCell = row.getCell(1);
                testName = testNameCell.getStringCellValue();
                logger.info("Got testNme: ", testName);
            } catch (Exception e) {
                logger.error("Error getting testName!");
            }

            if (!testName.isEmpty()) {
                testCount++;
                logger.info("Adding test count: ", testCount);
                TestCaseWrapper testCase = TestCaseWrapper.getTestCaseWrapper();
                testCase.setName(testName);

                String location = testSheetName + ", test # " + testId + ", row # " + i;
                testCase.setFileLocation(location);
                logger.info("Test file location: ", location);

                // testDob
                Date testDob = null;
                try {
                    XSSFCell testDobCell = row.getCell(2);
                    testDob = testDobCell.getDateCellValue();
                    logger.info("Got testDob: ", testDob);
                } catch (Exception e) {
                    logger.error("Error getting testDob!");
                }
                testCase.setPatientBirthTime(testDob);


                // testVaccineGroup
                String testVaccineGroup = null;
                try {
                    XSSFCell testVaccineGroupCell = row.getCell(62);
                    testVaccineGroup = testVaccineGroupCell.getStringCellValue();
                    testVaccineGroup = vaccineGroupMap.get(testVaccineGroup);
                    logger.info("Got testVaccineGroup: ", testVaccineGroup);
                } catch (Exception e) {
                    logger.error("Error getting testVaccineGroup!");
                }

                // seriesName
                String seriesName = null;
                try {
                    XSSFCell seriesNameCell = row.getCell(4);
                    seriesName = seriesNameCell.getStringCellValue();
                    logger.info("Got seriesName: ", seriesName);
                } catch (Exception e) {
                    logger.error("Error getting seriesName!");
                }

                // vaccine group series focus
                testCase.addProperty("vaccineGroup", testVaccineGroup, TestCasePropertyType.STRING);
                testCase.addProperty("series", seriesName, TestCasePropertyType.STRING);

                Date dateDose1 = null;
                try {
                    XSSFCell dateDose1Cell = row.getCell(6);
                    dateDose1 = dateDose1Cell.getDateCellValue();
                    logger.info("Got dateDose1: " + dateDose1);
                } catch (Exception e) {
                    logger.error("Error getting dateDose1!");
                }

            }
            throw new UnsupportedOperationException("Blah");
        }
    }

    /**
     * Return a hash from a plain text string.
     *
     * @param string
     * @return
     */
    public static String getShaHashFromString(String string) {
        String hash = "";
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(string.getBytes());
            byte[] byteData = md.digest();

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            hash = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            logger.error(e);
        }
        return hash;
    }
}
