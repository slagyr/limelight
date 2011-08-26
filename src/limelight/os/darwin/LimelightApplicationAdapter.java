//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.os.darwin;

import com.apple.eawt.ApplicationAdapter;
import com.apple.eawt.ApplicationEvent;
import com.apple.eawt.Application;
import limelight.AppMain;
import limelight.Context;

// DEPRACATED in Java 6
//
// replaced by AboutHandler, PreferencesHandler, AppReOpenedListener, OpenFilesHandler, PrintFilesHandler, QuitHandler, QuitResponse.

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
        AppMain.setStartupPath(productionPath);
    }
    catch(Throwable e)
    {
      AppMain.handleError(e);
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
