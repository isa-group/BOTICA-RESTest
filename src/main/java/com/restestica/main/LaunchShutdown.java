package com.restestica.main;

import java.util.List;

import com.botica.runners.CollectorLoader;
import com.botica.runners.ShutdownLoader;
import com.botica.utils.shutdown.ShutdownUtils;

public class LaunchShutdown {
    private static String shutdownPropertiesFilePath = "src/main/resources/BOTICAConfig/shutdown.properties";
    private static String configurationPropertiesFilePath = "src/main/resources/BOTICAConfig/collector.properties";

    public static void main(String[] args) {

        if (args.length > 0) {
            shutdownPropertiesFilePath = args[0];
        }

        if (args.length > 1) {
            configurationPropertiesFilePath = args[1];
        }

        ShutdownLoader shutdownLoader = new ShutdownLoader(shutdownPropertiesFilePath, true);
        CollectorLoader collectorLoader = new CollectorLoader(configurationPropertiesFilePath, true);

        List<String> botBotsToShutDown = shutdownLoader.getBotsToShutDown();
        String host = shutdownLoader.getHost();
        String shutdownCommandType = shutdownLoader.getShutdownCommandType();
        Integer timeToWait = shutdownLoader.getTimeToWait();
        String shutdownQueue = shutdownLoader.getShutdownQueue();

        ShutdownUtils.shutdown(botBotsToShutDown, shutdownCommandType, timeToWait, shutdownQueue, host,
                collectorLoader);
    }
}
