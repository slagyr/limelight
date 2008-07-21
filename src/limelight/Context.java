//- Copyright 2008 8th Light, Inc.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight;

import limelight.io.TempDirectory;
import limelight.io.Downloader;
import limelight.task.TaskEngine;
import limelight.ui.Panel;
import limelight.ui.model2.FrameManager;
import limelight.caching.Cache;
import javax.sound.sampled.Clip;
import java.awt.image.BufferedImage;

public class Context
{
  private static Context instance;
  
  public TempDirectory tempDirectory;
  public Downloader downloader;
  public TaskEngine taskEngine;
  public Cache<Panel, BufferedImage> bufferedImageCache;
  public FrameManager frameManager;
  public AudioPlayer audioPlayer;

  protected Context()
  {
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
}
