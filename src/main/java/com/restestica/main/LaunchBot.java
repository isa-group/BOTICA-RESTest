package com.restestica.main;

import es.us.isa.botica.configuration.MainConfiguration;
import es.us.isa.botica.launchers.AbstractLauncher;
import es.us.isa.botica.runners.BOTICALoader;
import es.us.isa.botica.utils.bot.BotHandler;

public class LaunchBot {

  private static final String LAUNCHERS_PACKAGE =
      "com.restestica.launchers"; // The package where the launchers are located.

  public static void main(String[] args) {
    MainConfiguration configuration = new BOTICALoader().loadConfiguration();
    AbstractLauncher launcher = BotHandler.handleLauncherType(LAUNCHERS_PACKAGE, configuration);

    if (launcher == null) {
      throw new NullPointerException("Bot launcher does not exist");
    }

    launcher.setLauncherPackage(LAUNCHERS_PACKAGE);
    launcher.launchBot();
  }
}
