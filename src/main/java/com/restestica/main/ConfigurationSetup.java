package com.restestica.main;

import es.us.isa.botica.utils.configuration.CreateConfiguration;
import java.io.File;

public class ConfigurationSetup {

  private static final String DEFAULT_CONFIG_PATH = "config.yml";

  public static void main(String[] args) {
    String configPath = args.length == 1 ? args[0] : DEFAULT_CONFIG_PATH;
    CreateConfiguration.createConfiguration(new File(configPath));
  }
}
