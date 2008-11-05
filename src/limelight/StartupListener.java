package limelight;

class StartupListener implements com.install4j.api.launcher.StartupNotification.Listener
{
  public static StartupListener instance;

  public static void register()
  {
    instance = new StartupListener();
    com.install4j.api.launcher.StartupNotification.registerStartupListener(instance);
  }

  public void startupPerformed(String productionPath)
  {
    Main.startupProvided = true;
    try
    {
      while(Context.instance().studio == null)
       Thread.sleep(100);
      Context.instance().studio.open(productionPath);
    }
    catch(Throwable e)
    {
      Main.handleError(e);
    }
  }
}
