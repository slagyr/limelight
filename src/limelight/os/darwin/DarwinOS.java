//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.os.darwin;

import limelight.os.OS;
import limelight.Context;
import limelight.util.StringUtil;
import com.sun.jna.ptr.IntByReference;

import java.io.File;
import java.io.IOException;

public class DarwinOS extends OS
{
  private static LimelightApplicationAdapter applicationAdapter;

  public int originalMode = 0;
  public int originalOptions = 0;

  public DarwinOS()
  {
    if(applicationAdapter == null)
    {
      applicationAdapter = new LimelightApplicationAdapter();
      applicationAdapter.register();
    }
  }

  public String dataRoot()
  {
    return System.getProperty("user.home") + "/Library/Application Support/Limelight";
  }

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

  protected void launch(String URL) throws IOException
  {
    runtime.exec("open", URL);
  }

  public void configureSystemProperties()
  {
    System.setProperty("jruby.shell", "/bin/sh");
    System.setProperty("jruby.script", "jruby");
  }
  
  public void appIsStarting()
  {
//    if(isRunningAsApp())
//        StartupListener.register();
  }

  public void openProduction(String productionPath)
  {
    if(isRunningAsApp())
//      StartupListener.instance.startupPerformed(productionPath);
      applicationAdapter.openProduction(productionPath);
    else
      Context.instance().studio.open(productionPath);
  }

  public boolean isRunningAsApp()
  {
    return "true".equals(System.getProperty("limelight.as.app"));
  }

  public LimelightApplicationAdapter getApplicationAdapter()
  {
    return applicationAdapter;
  }
}

