package com.restestica.launchers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.json.JSONObject;

import es.us.isa.botica.launchers.AbstractLauncher;
import com.restestica.utils.PropertyReader;

import es.us.isa.restest.coverage.CoverageGatherer;
import es.us.isa.restest.coverage.CoverageMeter;
import es.us.isa.restest.reporting.AllureReportManager;
import es.us.isa.restest.reporting.StatsReportManager;
import es.us.isa.restest.runners.RESTestLoader;
import es.us.isa.restest.specification.OpenAPISpecification;
import es.us.isa.restest.testcases.TestCase;
import es.us.isa.restest.util.AllureAuthManager;
import es.us.isa.restest.util.RESTestException;

/**
 * This class is a launcher for generating test reports.
 */
public class TestReporterLauncher extends AbstractLauncher {
    private static final String EXPERIMENT_NAME_PROPERTY = "experiment.name";
    private static final String PROPERTY_FILE_PATH_JSON_KEY = "propertyFilePath";
    private static final String TEST_CASES_PATH = "testCasesPath";

    public TestReporterLauncher(String keyToPublish, String orderToPublish, Properties botProperties) {
        super(keyToPublish, orderToPublish, botProperties);
    }

    /**
     * Generates test reports.
     */
    @Override
    protected void botAction() {
        Collection<TestCase> testCases = new ArrayList<>();

        String propertyFilePath = messageData.getString(PROPERTY_FILE_PATH_JSON_KEY);
        String testCasesPath = messageData.getString(TEST_CASES_PATH);

        try (FileInputStream fis = new FileInputStream(testCasesPath);
                ObjectInputStream ois = new ObjectInputStream(fis)) {
            readTestCasesFromObjectStream(ois, testCases);
        } catch (IOException e) {
            logger.error("Error writing test cases to file: {}", e.getMessage());
        }

        RESTestLoader loader = new RESTestLoader(propertyFilePath);
        try{
            loader.createGenerator(); //TODO: FIX (It is necessary to assign value to spec property in the Loader class)
        }catch(RESTestException e){
            logger.error("Error creating generator: {}", e.getMessage());
        }

        String experimentName = PropertyReader.readProperty(propertyFilePath, EXPERIMENT_NAME_PROPERTY);

        AllureReportManager allureReportManager = createAllureReportManager(propertyFilePath);
        StatsReportManager statsReportManager = createStatsReportManager(propertyFilePath);
        
        allureReportManager.generateReport();
        statsReportManager.setTestCases(testCases);
        statsReportManager.generateReport(experimentName, true);
    }

    @Override
    protected JSONObject createMessage() {
        JSONObject message = new JSONObject();
        message.put("order", this.orderToPublish);

        return message;
    }

    private Collection<TestCase> readTestCasesFromObjectStream(ObjectInputStream ois, Collection<TestCase> testCases) throws IOException {
        try {
            Object obj = ois.readObject();
            if (obj instanceof Collection<?>) {
                Collection<?> aux = (Collection<?>) obj;
                // TODO: Check how to improve the performance of this loop
                for (Object o : aux) {
                    if (o instanceof TestCase) {
                        testCases.add((TestCase) o);
                    }
                }
            }
            return testCases;
        } catch (ClassNotFoundException e) {
            logger.error("Error reading test cases from file: {}", e.getMessage());
            return testCases;
        }
    }

    //TODO: Change (Own definition of createStatsReportManager)
    private static StatsReportManager createStatsReportManager(String propertyFilePath) {

        String experimentName = PropertyReader.readProperty(propertyFilePath, EXPERIMENT_NAME_PROPERTY);
        String testDataDir = PropertyReader.readProperty(propertyFilePath, "data.tests.dir") + "/" + experimentName;
        String coverageDataDir = PropertyReader.readProperty(propertyFilePath, "data.coverage.dir") + "/" + experimentName;
        boolean enableCSVStats = Boolean.parseBoolean(PropertyReader.readProperty(propertyFilePath, "stats.csv"));
        boolean enableInputCoverage = Boolean.parseBoolean(PropertyReader.readProperty(propertyFilePath, "coverage.input"));
        boolean enableOutputCoverage = Boolean.parseBoolean(PropertyReader.readProperty(propertyFilePath, "coverage.output"));
        String OAISpecPath = PropertyReader.readProperty(propertyFilePath, "oas.path");

        OpenAPISpecification spec = new OpenAPISpecification(OAISpecPath);

		CoverageMeter coverageMeter = new CoverageMeter(new CoverageGatherer(spec));

		return new StatsReportManager(testDataDir, coverageDataDir, enableCSVStats, enableInputCoverage,
					enableOutputCoverage, coverageMeter);
	}

    //TODO: Change (Own definition of createAllureReportManager)
    private static AllureReportManager createAllureReportManager(String propertyFilePath) {
		AllureReportManager arm = null;
        String experimentName = PropertyReader.readProperty(propertyFilePath, EXPERIMENT_NAME_PROPERTY);
        String allureResultsDir = PropertyReader.readProperty(propertyFilePath, "allure.results.dir") + "/" + experimentName;
        String allureReportDir = PropertyReader.readProperty(propertyFilePath, "allure.report.dir") + "/" + experimentName;
        String confPath = PropertyReader.readProperty(propertyFilePath, "conf.path");
        String OAISpecPath = PropertyReader.readProperty(propertyFilePath, "oas.path");
		
        //Find auth property names (if any)
        List<String> authProperties = AllureAuthManager.findAuthProperties(new OpenAPISpecification(OAISpecPath), confPath);

        arm = new AllureReportManager(allureResultsDir, allureReportDir, authProperties);
        arm.setEnvironmentProperties(propertyFilePath);
        arm.setHistoryTrend(true);
		
		return arm;
	}

    @Override
    protected boolean shutdownCondition() {
        return true;
    }
}
