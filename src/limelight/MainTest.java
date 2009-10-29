//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight;

import junit.framework.TestCase;
import limelight.caching.Cache;
import limelight.caching.TimedCache;
import limelight.ui.Panel;
import limelight.Studio;
import limelight.audio.RealAudioPlayer;
import limelight.os.MockOS;
import limelight.os.OS;
import limelight.os.UnsupportedOS;
import limelight.os.win32.Win32OS;
import limelight.os.darwin.DarwinOS;

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

  public void testRuntimeFactoryIsInstalled() throws Exception
  {
    main.configureContext();

    assertNotNull(Context.instance().runtimeFactory);
  }
  
  public void testStudioIsInstalled() throws Exception
  {
    main.configureContext();

    assertEquals(Studio.class, Context.instance().studio.getClass());
  }

  public void testSettingSystemCofiguration() throws Exception
  {
    System.setProperty("limelight.home", "/limelighthome");
    MockOS os = new MockOS();
    Context.instance().os = os;
    main.setContext(Context.instance());

    main.configureSystemProperties();

    assertEquals(true, os.systemPropertiesConfigured);
    assertEquals("true", System.getProperty("jruby.interfaces.useProxy"));
  }

  public void testDarwinOS() throws Exception
  {
    System.setProperty("os.name", "Mac OS X");
    main.configureOS();

    OS os = Context.instance().os;
    assertEquals(DarwinOS.class, os.getClass());
  }

  public void testWindowsXPOS() throws Exception
  {
    System.setProperty("os.name", "Windows XP");
    main.configureOS();
    OS os = Context.instance().os;
    assertEquals(Win32OS.class, os.getClass());
  }

  public void testWindowsVistaOS() throws Exception
  {
    System.setProperty("os.name", "Windows Vista");
    main.configureOS();
    OS os = Context.instance().os;
    assertEquals(Win32OS.class, os.getClass());
  }

  public void testUnsupportedOS() throws Exception
  {
    System.setProperty("os.name", "Something Unsupported");
    main.configureOS();

    OS os = Context.instance().os;
    assertEquals(UnsupportedOS.class, os.getClass());
  }
}
