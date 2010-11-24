//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight;

import limelight.audio.RealAudioPlayer;
import limelight.background.AnimationLoop;
import limelight.background.CacheCleanerLoop;
import limelight.background.PanelPainterLoop;
import limelight.caching.TimedCache;
import limelight.io.TempDirectory;
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
  private static boolean booted;

  public static void reset()
  {
    booted = false;
  }

  public static void boot()
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
    context().frameManager = new AlertFrameManager();

    installCommonConfigComponents();

    context().panelPanter = new PanelPainterLoop().started();
    context().animationLoop = new AnimationLoop().started();
    context().cacheCleaner = new CacheCleanerLoop().started();
  }

  private static void installCommonConfigComponents()
  {
    context().keyboardFocusManager = new KeyboardFocusManager().installed();
    initializeTempDirectory();
    context().audioPlayer = new RealAudioPlayer();
    context().bufferedImageCache = new TimedCache<Panel, BufferedImage>(1);
    context().bufferedImagePool = new BufferedImagePool(1);
    context().studio = new Studio();
  }

  public static void initializeTempDirectory()
  {
    context().tempDirectory = new TempDirectory();
  }
}
