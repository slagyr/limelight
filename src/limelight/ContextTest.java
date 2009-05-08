package limelight;

import junit.framework.TestCase;
import limelight.io.TempDirectory;
import limelight.background.PanelPainterLoop;
import limelight.background.AnimationLoop;
import limelight.background.CacheCleanerLoop;
import limelight.ui.model.MockFrameManager;
import limelight.os.OS;
import limelight.os.UnsupportedOS;
import limelight.os.win32.Win32OS;
import limelight.os.darwin.DarwinOS;

public class ContextTest extends TestCase
{
  public void setUp() throws Exception
  {
    Context.instance().environment = "test"; 
  }

  public void testTempDirectory() throws Exception
  {
    TempDirectory directory = new TempDirectory();
    Context.instance().tempDirectory = directory;
    assertEquals(directory, Context.instance().tempDirectory);
  }

  public void testStopping() throws Exception
  {
    Context context = Context.instance();
    MockFrameManager frameManager = new MockFrameManager();
    context.frameManager = frameManager;
    context.panelPanter = new PanelPainterLoop().started();
    context.animationLoop = new AnimationLoop().started();
    context.cacheCleaner = new CacheCleanerLoop().started();

    context.shutdown();

    assertEquals(false, context.panelPanter.isRunning());
    assertEquals(false, context.animationLoop.isRunning());
    assertEquals(false, context.cacheCleaner.isRunning());
    assertEquals(true, frameManager.allFramesClosed);
  }

  public void testDarwinOS() throws Exception
  {
    System.setProperty("os.name", "Mac OS X");
    Context.removeInstance();

    OS os = Context.instance().os;
    assertEquals(DarwinOS.class, os.getClass());
  }

  public void testWindowsXPOS() throws Exception
  {
    System.setProperty("os.name", "Windows XP");
    Context.removeInstance();
    OS os = Context.instance().os;
    assertEquals(Win32OS.class, os.getClass());
  }

  public void testWindowsVistaOS() throws Exception
  {
    System.setProperty("os.name", "Windows Vista");
    Context.removeInstance();
    OS os = Context.instance().os;
    assertEquals(Win32OS.class, os.getClass());
  }

  public void testUnsupportedOS() throws Exception
  {
    System.setProperty("os.name", "Something Unsupported");
    Context.removeInstance();

    OS os = Context.instance().os;
    assertEquals(UnsupportedOS.class, os.getClass());
  }
}