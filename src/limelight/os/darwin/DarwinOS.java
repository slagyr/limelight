package limelight.os.darwin;

import limelight.os.OS;
import limelight.Context;
import com.sun.jna.ptr.IntByReference;

public class DarwinOS extends OS
{
  public IntByReference originalMode = new IntByReference(-1);
  public IntByReference originalOptions = new IntByReference(-1);
  protected void turnOnKioskMode()
  {
    Carbon.INSTANCE.GetSystemUIMode(originalMode, originalOptions);
    Carbon.INSTANCE.SetSystemUIMode(Carbon.kUIModeContentHidden, Carbon.kUIOptionDisableAppleMenu | Carbon.kUIOptionDisableForceQuit);
  }

  protected void turnOffKioskMode()
  {
    Carbon.INSTANCE.SetSystemUIMode(originalMode.getValue(), originalOptions.getValue());
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

