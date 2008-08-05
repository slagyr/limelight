//- Copyright 2008 8th Light, Inc.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight;

import junit.framework.TestCase;
import limelight.io.Downloader;
import limelight.task.TaskEngine;
import limelight.task.Task;
import limelight.task.RecurringTask;
import limelight.caching.Cache;
import limelight.caching.TimedCache;
import limelight.ui.Panel;
import limelight.audio.RealAudioPlayer;

import java.awt.image.BufferedImage;

public class MainTest extends TestCase
{
  private Main main;

  public void setUp() throws Exception
  {
    main = new Main();
    Context.removeInstance();
  }
  
  public void testTempFileIsAddedToContext() throws Exception
  {
    main.configureContext();

    assertNotNull(Context.instance().tempDirectory);
  }

  public void testDownloaderIsAddedToContext() throws Exception
  {
    main.configureContext();

    Downloader downloader = Context.instance().downloader;
    assertSame(Context.instance().tempDirectory, downloader.getTempDirectory());
  }

  public void testTaskEngineIsAddedToContextAndStarted() throws Exception
  {
    main.configureContext();

    TaskEngine engine = Context.instance().taskEngine;
    assertEquals(true, engine.isRunning());
  }
  
  public void testBufferedImageCacheIsAddedToContext() throws Exception
  {
    main.configureContext();

    Cache<Panel, BufferedImage> cache = Context.instance().bufferedImageCache;
    assertEquals(TimedCache.class, cache.getClass());
    assertEquals(1, ((TimedCache)cache).getTimeoutSeconds(), 0.01);
  }

  public void testRecurringTaskAddedForCleaningTheBufferedImageCache() throws Exception
  {
    main.configureContext();

    Task task = findTaskEngineTaskNamed("Buffered Image Cache Cleaner");
    assertEquals(true, task instanceof RecurringTask);
    assertEquals(1, ((RecurringTask)task).getPerformancesPerSecond(), 0.01);
  }

  private Task findTaskEngineTaskNamed(String name)
  {
    TaskEngine engine = Context.instance().taskEngine;
    for(Task task : engine.getTasks())
    {
      if(name.equals(task.getName()))
        return task;
    }
    return null;
  }
  
  public void testFrameManagerIsAddedToContext() throws Exception
  {
    main.configureContext();

    assertNotNull(Context.instance().frameManager);
  }

  public void testPainterTaskIsAddedToEngine() throws Exception
  {
    main.configureContext();

    Task task = findTaskEngineTaskNamed("Panel Painter");
    assertEquals(true, task instanceof RecurringTask);
    assertEquals(80, ((RecurringTask)task).getPerformancesPerSecond(), 0.01);
    assertEquals(false, ((RecurringTask)task).isStrict());
  }

  public void testAudioPlayerIsAdded() throws Exception
  {
    main.configureContext();

    assertEquals(RealAudioPlayer.class, Context.instance().audioPlayer.getClass());
  }
  
  public void testKeyboardFocusListenerIsInstalled() throws Exception
  {
    main.configureContext();

    assertSame(Context.instance().keyboardFocusManager, java.awt.KeyboardFocusManager.getCurrentKeyboardFocusManager());
  }
}
