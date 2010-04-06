//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.os.darwin;

import com.apple.eawt.ApplicationAdapter;
import com.apple.eawt.ApplicationEvent;
import com.apple.eawt.Application;
import limelight.Context;
import limelight.Main;
import limelight.util.Debug;

public class LimelightApplicationAdapter extends ApplicationAdapter
{
  private boolean registered;
  public static int startupsReceived;

  public void register()
  {
    Application.getApplication().addApplicationListener(this);
    registered = true;
  }

  public void handleAbout(ApplicationEvent applicationEvent)
  {
    super.handleAbout(applicationEvent);
  }

  public void handleOpenApplication(ApplicationEvent applicationEvent)
  {
    super.handleOpenApplication(applicationEvent);
  }

  public void handleOpenFile(ApplicationEvent applicationEvent)
  {
    String productionPath = applicationEvent.getFilename();
    openProduction(productionPath);
  }

  public void openProduction(String productionPath)
  {
    startupsReceived++;
    try
    {
      if(Context.instance().studio != null)
        Context.instance().studio.open(productionPath);
      else
        Main.setStartupPath(productionPath);
    }
    catch(Throwable e)
    {
      Main.handleError(e);
    }
  }

  public void handlePreferences(ApplicationEvent applicationEvent)
  {
    super.handlePreferences(applicationEvent);
  }

  public void handlePrintFile(ApplicationEvent applicationEvent)
  {
    super.handlePrintFile(applicationEvent);
  }

  public void handleQuit(ApplicationEvent applicationEvent)
  {
    Context.instance().attemptShutdown();
  }

  public void handleReOpenApplication(ApplicationEvent applicationEvent)
  {
    super.handleReOpenApplication(applicationEvent);
  }

  public boolean isRegistered()
  {
    return registered;
  }
}
