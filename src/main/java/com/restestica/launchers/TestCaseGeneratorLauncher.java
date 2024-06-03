package com.restestica.launchers;

import static es.us.isa.restest.util.FileManager.createDir;

import com.restestica.utils.PropertyReader;
import es.us.isa.botica.configuration.MainConfiguration;
import es.us.isa.botica.launchers.AbstractLauncher;
import es.us.isa.restest.generators.*;
import es.us.isa.restest.runners.RESTestLoader;
import es.us.isa.restest.testcases.TestCase;
import es.us.isa.restest.util.RESTestException;
import es.us.isa.restest.writers.restassured.RESTAssuredWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import org.json.JSONObject;

/** This class is a launcher for generating test cases. */
public class TestCaseGeneratorLauncher extends AbstractLauncher {

  private final String propertyFilePath;

  private RESTestLoader loader;
  private AbstractTestCaseGenerator testCaseGenerator;
  private String generatorType;
  private String testCasesPath;

  public TestCaseGeneratorLauncher(MainConfiguration configuration) {
    super(configuration);
    this.propertyFilePath = System.getenv("USER_CONFIG_PATH");
  }

  /** Generates test cases based on the specified generator type. */
  @Override
  protected void botAction() {
    try {
      this.loader = new RESTestLoader(this.propertyFilePath);
      this.generatorType = PropertyReader.readProperty(this.propertyFilePath, "generator");
      this.testCaseGenerator = getGenerator(this.loader, this.generatorType);

      auxBotAction(this.loader, this.testCaseGenerator);
    } catch (RESTestException e) {
      logger.error("Error launching test generator: {}", this.botInstanceConfiguration.getId(), e);
    }
  }

  @Override
  protected JSONObject createMessage() {
    JSONObject message = new JSONObject();
    message.put("order", this.botTypeConfiguration.getPublishConfiguration().getOrder());
    message.put("botId", this.botInstanceConfiguration.getId());
    message.put("generatorType", this.generatorType);
    message.put("faultyRatio", this.testCaseGenerator.getFaultyRatio());
    message.put("nTotalFaulty", this.testCaseGenerator.getnFaulty());
    message.put("nTotalNominal", this.testCaseGenerator.getnNominal());
    message.put("maxTriesPerTestCase", this.testCaseGenerator.getMaxTriesPerTestCase());
    message.put("targetDirJava", this.loader.getTargetDirJava());
    message.put("allureReportsPath", this.loader.getAllureReportsPath());
    message.put("experimentName", this.loader.getExperimentName());
    message.put("propertyFilePath", this.propertyFilePath);
    message.put("testCasesPath", this.testCasesPath);

    return message;
  }

  private void auxBotAction(RESTestLoader loader, AbstractTestCaseGenerator generator) {
    Collection<TestCase> testCases = null;
    try {
      testCases = generator.generate();
    } catch (RESTestException e) {
      logger.error("Error generating test cases: {}", e.getMessage());
    }

    String targetDir = loader.getTargetDirJava();
    try {
      Files.createDirectories(Paths.get(targetDir));
    } catch (Exception e) {
      logger.error("Error creating directory: {}", e.getMessage());
    }
    this.testCasesPath = targetDir + "/t.tmp";
    try (FileOutputStream fos = new FileOutputStream(this.testCasesPath);
        ObjectOutputStream oos = new ObjectOutputStream(fos)) {
      oos.writeObject(testCases);
    } catch (IOException e) {
      logger.error("Error writing test cases to file: {}", e.getMessage());
    }

    // Create target directory for test cases if it does not exist
    createDir(loader.getTargetDirJava());

    // Write (RestAssured) test cases
    RESTAssuredWriter writer = (RESTAssuredWriter) loader.createWriter();
    writer.write(testCases);
  }

  private static AbstractTestCaseGenerator getGenerator(RESTestLoader loader, String generatorType)
      throws RESTestException {
    switch (generatorType) {
      case "FT":
      case "RT":
      case "CBT":
      case "ART":
        break;
      default:
        throw new RESTestException(
            "Property 'generator' must be one of 'FT', 'RT', 'CBT' or 'ART'");
    }
    return loader.createGenerator();
  }

  @Override
  protected boolean shutdownCondition() {
    return true;
  }
}
