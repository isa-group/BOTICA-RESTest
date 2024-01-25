package com.restestica.main;

import java.util.List;

import com.botica.runners.ShutdownLoader;
import com.botica.utils.shutdown.ShutdownUtils;

public class LaunchShutdown {
    private static String shutdownPropertiesFilePath = "src/main/resources/BOTICAConfig/shutdown.properties";

    public static void main(String[] args) {

        if (args.length == 1) {
            shutdownPropertiesFilePath = args[0];
        }

        ShutdownLoader shutdownLoader = new ShutdownLoader(shutdownPropertiesFilePath, true);

        List<String> botBotsToShutdown = shutdownLoader.getBotsToShutdown();
        String host = shutdownLoader.getHost();
        String shutdownCommandType = shutdownLoader.getShutdownCommandType();
        Integer timeToWait = shutdownLoader.getTimeToWait();
        
        ShutdownUtils.closing();
    }
}
