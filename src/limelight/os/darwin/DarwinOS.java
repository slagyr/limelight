package limelight.os.darwin;

import limelight.os.OS;
import limelight.Context;
import com.sun.jna.ptr.IntByReference;

public class DarwinOS extends OS
{
  public int originalMode = 0;
  public int originalOptions = 0;

  protected void turnOnKioskMode()
  {
    IntByReference startMode = new IntByReference(-1);
    IntByReference startOptions = new IntByReference(-1);
    Carbon.INSTANCE.GetSystemUIMode(startMode, startOptions);
    originalMode = startMode.getValue();
    originalOptions = startOptions.getValue();

    Carbon.INSTANCE.SetSystemUIMode(Carbon.kUIModeContentHidden, Carbon.kUIOptionDisableAppleMenu | Carbon.kUIOptionDisableForceQuit);
  }

  protected void turnOffKioskMode()
  {
    Carbon.INSTANCE.SetSystemUIMode(originalMode, originalOptions);
  }

  public void configureSystemProperties()
  {
    System.setProperty("jruby.shell", "/bin/sh");
    System.setProperty("jruby.script", "jruby");
  }

  public void appIsStarting()
  {
    if(isRunningAsApp())
        StartupListener.register();
  }

  public void openProduction(String productionPath)
  {
    if(isRunningAsApp())
      StartupListener.instance.startupPerformed(productionPath);
    else
      Context.instance().studio.open(productionPath);
  }

  public boolean isRunningAsApp()
  {
    return "true".equals(System.getProperty("limelight.as.app"));
  }
}

