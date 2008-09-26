package limelight;

class StartupListener implements com.install4j.api.launcher.StartupNotification.Listener
{
  public static void register()
  {
    StartupListener listener = new StartupListener();
    com.install4j.api.launcher.StartupNotification.registerStartupListener(listener);
  }

  public void startupPerformed(String productionPath)
  {
    try
    {
      while(Context.instance().studio == null)
       Thread.sleep(100);
      Context.instance().studio.open(productionPath);
    }
    catch(InterruptedException e)
    {
      e.printStackTrace();
    }
  }
}
