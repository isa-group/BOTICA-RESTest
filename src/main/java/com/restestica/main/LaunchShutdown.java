package com.restestica.main;

import java.util.List;

import es.us.isa.botica.runners.ShutdownLoader;
import es.us.isa.botica.utils.shutdown.ShutdownUtils;

public class LaunchShutdown {
    private static String propertiesFilePath = "src/main/resources/BOTICAConfig/shutdown.properties";

    public static void main(String[] args) {
        ShutdownLoader shutdownLoader = new ShutdownLoader(propertiesFilePath, true);

        List<String> botBotsToShutDown = shutdownLoader.getBotsToShutDown();
        String host = shutdownLoader.getHost();
        String shutdownCommandType = shutdownLoader.getShutdownCommandType();
        Integer timeToWait = shutdownLoader.getTimeToWait();
        String shutdownQueue = shutdownLoader.getShutdownQueue();

        ShutdownUtils.shutdown(botBotsToShutDown, shutdownCommandType, timeToWait, shutdownQueue, host);
    }
}
