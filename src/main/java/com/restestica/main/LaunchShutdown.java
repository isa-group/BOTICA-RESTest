package com.restestica.main;

import es.us.isa.botica.runners.ShutdownLoader;
import es.us.isa.botica.utils.shutdown.ShutdownUtils;
import java.io.File;

public class LaunchShutdown {
  private static final String DEFAULT_CONFIG_PATH = "config.yml";

  public static void main(String[] args) {
    String configPath = args.length == 1 ? args[0] : DEFAULT_CONFIG_PATH;
    ShutdownLoader shutdownLoader = new ShutdownLoader(new File(configPath));
    ShutdownUtils.shutdown(shutdownLoader);
  }
}
