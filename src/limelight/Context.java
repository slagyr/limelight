//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight;

import limelight.io.FileSystem;
import limelight.io.TempDirectory;
import limelight.model.PlayerRecruiter;
import limelight.model.Studio;
import limelight.ui.BufferedImagePool;
import limelight.ui.KeyboardFocusManager;
import limelight.ui.Panel;
import limelight.ui.model.FrameManager;
import limelight.caching.Cache;
import limelight.styles.abstrstyling.StyleAttributeCompilerFactory;
import limelight.background.AnimationLoop;
import limelight.background.IdleThreadLoop;
import limelight.background.CacheCleanerLoop;
import limelight.os.OS;
import limelight.ui.model.StageFrame;
import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;

public class Context
{
  protected static Context instance;

  public final String limelightHome;

  public String environment = "production";
  public FileSystem fs = new FileSystem();
  public TempDirectory tempDirectory;
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
  public OS os;
  public PlayerRecruiter playerRecruiter;
  public boolean isShutdown;
  private boolean isShuttingDown;

  protected Context()
  {
    limelightHome = System.getProperty("limelight.home") == null ? System.getProperty("user.dir") : System.getProperty("limelight.home");
    installStyleAttributeCompilerFactory();
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

  public static StageFrame getActiveFrame()
  {
    if(instance().frameManager != null)
      return instance().frameManager.getActiveFrame();
    else
      return null;
  }

  public static void kickPainter()
  {
    if(instance().panelPanter != null)
      instance().panelPanter.go();
  }

  public void shutdown()
  {
    if(isShutdown || isShuttingDown)
      return;

    isShuttingDown = true;
    Log.info("Context - Limelight is closing down");
    if(studio != null)
      studio.shutdown();

    if(frameManager != null)
      frameManager.closeAllFrames();

    killThreads();

    if(os != null && os.isInKioskMode())
      os.exitKioskMode();

    if(!"test".equals(environment))
    {
      Log.info("Context - Goodbye");
      System.exit(0);
    }

    isShuttingDown = false;
    isShutdown = true;
  }

  public void killThreads()
  {
    if(panelPanter != null)
      panelPanter.stop();

    if(animationLoop != null)
      animationLoop.stop();

    if(cacheCleaner != null)
      cacheCleaner.stop();
  }

  public void attemptShutdown()
  {
    if(studio == null || studio.shouldAllowShutdown())
      shutdown();
  }

  // MDM - This is a hoaky hack to avoid a DIP violation.
  private void installStyleAttributeCompilerFactory()
  {
    try
    {
      Class klass = Class.forName("limelight.styles.compiling.RealStyleAttributeCompilerFactory");
      Constructor constructor = klass.getConstructor();
      styleAttributeCompilerFactory = (StyleAttributeCompilerFactory)constructor.newInstance();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  public static FileSystem fs()
  {
    return instance().fs;
  }
}
