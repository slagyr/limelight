//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight;

import junit.framework.TestCase;
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

  public void testBufferedImageCacheIsAddedToContext() throws Exception
  {
    main.configureContext();

    Cache<Panel, BufferedImage> cache = Context.instance().bufferedImageCache;
    assertEquals(TimedCache.class, cache.getClass());
    assertEquals(1, ((TimedCache)cache).getTimeoutSeconds(), 0.01);
  }
  
  public void testFrameManagerIsAddedToContext() throws Exception
  {
    main.configureContext();

    assertNotNull(Context.instance().frameManager);
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

  public void testBufferedImagePoolIsInstalled() throws Exception
  {
    main.configureContext();

    assertNotNull(Context.instance().bufferedImagePool);
  }
}
