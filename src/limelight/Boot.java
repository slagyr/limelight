//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight;

import limelight.audio.RealAudioPlayer;
import limelight.background.AnimationLoop;
import limelight.background.CacheCleanerLoop;
import limelight.background.PanelPainterLoop;
import limelight.caching.TimedCache;
import limelight.io.TempDirectory;
import limelight.model.PlayerRecruiter;
import limelight.model.Studio;
import limelight.os.OS;
import limelight.os.UnsupportedOS;
import limelight.ui.BufferedImagePool;
import limelight.ui.KeyboardFocusManager;
import limelight.ui.Panel;
import limelight.ui.model.AlertFrameManager;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;

public class Boot
{
  public static boolean startBackgroundThreads = true;
  private static boolean booted;

  public static void reset()
  {
    Context.removeInstance();
    booted = false;
  }

  public static void boot()
  {
    boot("production");
  }

  public static void boot(String environment)
  {
    if(booted)
      return;
    booted = true;
    try
    {
      configureOS();
      configureContext();
      configureSystemProperties();

      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch(Exception e)
    {
      throw new LimelightException(e);
    }
  }

  private static Context context()
  {
    return Context.instance();
  }

  public static void configureSystemProperties()
  {
    System.setProperty("jruby.interfaces.useProxy", "true");
    context().os.configureSystemProperties();
  }

  static void configureOS() throws Exception
  {
    if(context().os != null)
      return;

    String className = "limelight.os.UnsupportedOS";
    if(System.getProperty("os.name").indexOf("Windows") != -1)
      className = "limelight.os.win32.Win32OS";
    else
      if(System.getProperty("os.name").indexOf("Mac OS X") != -1)
        className = "limelight.os.darwin.DarwinOS";

    try
    {
      Class klass = Thread.currentThread().getContextClassLoader().loadClass(className);
      Constructor constructor = klass.getConstructor();
      context().os = (OS) constructor.newInstance();
    }
    catch(Exception e)
    {
      System.err.println("OS class could not be loaded:" + e);
      context().os = new UnsupportedOS();
    }

    context().os.appIsStarting();
  }

  public static void configureContext() throws Exception
  {
//VerboseRepaintManager.install();
    if(context().frameManager == null)
      context().frameManager = new AlertFrameManager();

    installCommonConfigComponents();

    if(context().panelPanter == null)
      context().panelPanter = new PanelPainterLoop();if(startBackgroundThreads);

    if(context().animationLoop == null)
      context().animationLoop = new AnimationLoop();

    if(context().cacheCleaner == null)
      context().cacheCleaner = new CacheCleanerLoop();

    if(startBackgroundThreads)
    {
      context().panelPanter.start();
      context().animationLoop.start();
      context().cacheCleaner.start();
    }
  }

  private static void installCommonConfigComponents()
  {
    if(context().keyboardFocusManager == null)
      context().keyboardFocusManager = new KeyboardFocusManager().installed();

    if(context().tempDirectory == null)
      context().tempDirectory = new TempDirectory();

    if(context().audioPlayer == null)
      context().audioPlayer = new RealAudioPlayer();

    if(context().bufferedImageCache == null)
      context().bufferedImageCache = new TimedCache<Panel, BufferedImage>(1);

    if(context().bufferedImagePool == null)
      context().bufferedImagePool = new BufferedImagePool(1);

    if(context().studio == null)
      context().studio = new Studio();

    if(context().playerRecruiter == null)
      context().playerRecruiter = new PlayerRecruiter();
  }

}
