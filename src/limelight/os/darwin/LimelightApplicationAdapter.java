//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.os.darwin;

import com.apple.eawt.*;
import limelight.AppMain;
import limelight.Context;
import limelight.Log;

import java.io.File;
import java.util.List;

public class LimelightApplicationAdapter
{
  private boolean registered;
  public static int startupsReceived;
  public Application application;

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

  public boolean isRegistered()
  {
    return registered;
  }

  public void register()
  {
    application = Application.getApplication();
    application.setAboutHandler(new LimelightAboutHandler());
    application.setOpenFileHandler(new LimelightOpenFileHandler());
    application.setPreferencesHandler(new LimelightPreferencesHandler());
    application.setPrintFileHandler(new LimelightPrintFileHandler());
    application.setQuitHandler(new LimelightQuitHandler());
    application.setOpenURIHandler(new LimelightOpenURIHandler());
    registered = true;
  }

  private class LimelightAboutHandler implements AboutHandler
  {
    public void handleAbout(AppEvent.AboutEvent aboutEvent)
    {
      Log.info("Darwin Limelight Application: handleAbout");
    }
  }

  private class LimelightOpenFileHandler implements OpenFilesHandler
  {
    public void openFiles(AppEvent.OpenFilesEvent openFilesEvent)
    {
      final List<File> files = openFilesEvent.getFiles();
      for(File file : files)
      {
        final String productionPath = file.getAbsolutePath();
        Log.info("Darwin Limelight Application: openFileHandler: " + productionPath);
        openProduction(productionPath);
      }
    }
  }

  private class LimelightPreferencesHandler implements PreferencesHandler
  {
    public void handlePreferences(AppEvent.PreferencesEvent preferencesEvent)
    {
      Log.info("Darwin Limelight Application: handlePreferences");
    }
  }

  private class LimelightPrintFileHandler implements PrintFilesHandler
  {
    public void printFiles(AppEvent.PrintFilesEvent printFilesEvent)
    {
      final List<File> files = printFilesEvent.getFiles();
      for(File file : files)
      {
        Log.info("Darwin Limelight Application: handlePrintFile: " + file.getAbsolutePath());
      }
    }
  }

  private class LimelightQuitHandler implements QuitHandler
  {
    public void handleQuitRequestWith(AppEvent.QuitEvent quitEvent, QuitResponse quitResponse)
    {
      Log.info("Darwin Limelight Application: handleQuit");
      Context.instance().attemptShutdown();
      quitResponse.performQuit();
    }
  }

  private class LimelightOpenURIHandler implements OpenURIHandler
  {
    public void openURI(AppEvent.OpenURIEvent openURIEvent)
    {
      final String productionPath = openURIEvent.getURI().toString();
      Log.info("Darwin Limelight Application: openURIHandler: " + productionPath);
      openProduction(productionPath);
    }
  }
}
