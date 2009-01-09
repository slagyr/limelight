//- Copyright 2008 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight;

import limelight.io.TempDirectory;
import limelight.io.Downloader;
import limelight.ui.Panel;
import limelight.ui.api.Studio;
import limelight.ui.model.FrameManager;
import limelight.ui.model.Frame;
import limelight.caching.Cache;
import limelight.styles.abstrstyling.StyleAttributeCompilerFactory;
import limelight.background.AnimationLoop;
import limelight.background.IdleThreadLoop;
import limelight.background.CacheCleanerLoop;

import java.awt.image.BufferedImage;

public class Context
{
  private static Context instance;

  public final String limelightHome;
  public final String os;
  public final boolean runningAsApp;

  public TempDirectory tempDirectory;
  public Downloader downloader;

  public IdleThreadLoop panelPanter;                                                    
  public AnimationLoop animationLoop;
  public CacheCleanerLoop cacheCleaner;

  public Cache<Panel, BufferedImage> bufferedImageCache;
  public FrameManager frameManager;
  public AudioPlayer audioPlayer;
  public KeyboardFocusManager keyboardFocusManager;
  public BufferedImagePool bufferedImagePool;
  public Studio studio;
  public StyleAttributeCompilerFactory styleAttributeCompilerFactory;

  private Context()
  {
    limelightHome = System.getProperty("limelight.home");
    runningAsApp = "true".equals(System.getProperty("limelight.as.app"));
    if(System.getProperty("mrj.version") == null)
      os = "windows";
    else
      os = "osx";
  }

  public static Context instance()
  {
    if(instance == null)
      instance = new Context();
    return instance;
  }

  public static void removeInstance()
  {
    instance = null;
  }

  public static Frame getActiveFrame()
  {
    if(instance().frameManager != null)
      return instance().frameManager.getActiveFrame();
    else
      return null;
  }

  public boolean isWindows()
  {
    return "windows".equals(os);
  }

  public boolean isOsx()
  {
    return "osx".equals(os);
  }

  public static void kickPainter()
  {
    if(instance().panelPanter != null)
      instance().panelPanter.go();
  }
}
