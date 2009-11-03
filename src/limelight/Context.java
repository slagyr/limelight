//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight;

import limelight.io.TempDirectory;
import limelight.ui.Panel;
import limelight.ui.model.FrameManager;
import limelight.ui.model.PropFrame;
import limelight.caching.Cache;
import limelight.styles.abstrstyling.StyleAttributeCompilerFactory;
import limelight.background.AnimationLoop;
import limelight.background.IdleThreadLoop;
import limelight.background.CacheCleanerLoop;
import limelight.os.OS;

import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;

public class Context
{
  protected static Context instance;

  public final String limelightHome;

  public String environment = "production";
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
  public RuntimeFactory runtimeFactory;
  public boolean isShutdown;
  private boolean isShuttingDown;
  

  protected Context()
  {
    limelightHome = System.getProperty("limelight.home");
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

  public static PropFrame getActiveFrame()
  {
    if(instance().frameManager != null)
      return instance().frameManager.getFocusedFrame();
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
    if(studio != null)
      studio.shutdown();

    if(frameManager != null)
      frameManager.closeAllFrames();

    if(panelPanter != null)
      panelPanter.stop();

    if(animationLoop != null)
      animationLoop.stop();

    if(cacheCleaner != null)
      cacheCleaner.stop();

    if(os != null && os.isInKioskMode())
      os.exitKioskMode();

    if(!"test".equals(environment))
      System.exit(0);

    isShuttingDown = false;
    isShutdown = true;
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
}
