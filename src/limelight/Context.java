//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight;

import limelight.io.TempDirectory;
import limelight.ui.Panel;
import limelight.ui.api.Studio;
import limelight.ui.model.FrameManager;
import limelight.ui.model.StageFrame;
import limelight.caching.Cache;
import limelight.styles.abstrstyling.StyleAttributeCompilerFactory;
import limelight.background.AnimationLoop;
import limelight.background.IdleThreadLoop;
import limelight.background.CacheCleanerLoop;
import limelight.os.OS;

import java.awt.image.BufferedImage;

public class Context
{
  private static Context instance;

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

  private Context()
  {
    limelightHome = System.getProperty("limelight.home");
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
    panelPanter.stop();
    animationLoop.stop();
    cacheCleaner.stop();
    frameManager.closeAllFrames();
    if(!"test".equals(environment))
      System.exit(0);
  }
}
