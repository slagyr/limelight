//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight;

import junit.framework.TestCase;
import limelight.io.TempDirectory;
import limelight.background.PanelPainterLoop;
import limelight.background.AnimationLoop;
import limelight.background.CacheCleanerLoop;
import limelight.ui.model.MockFrameManager;
import limelight.ui.api.MockStudio;
import limelight.os.MockOS;

public class ContextTest extends TestCase
{
  private Context context;
  private MockFrameManager frameManager;
  private MockStudio studio;

  public void setUp() throws Exception
  {
    Context.removeInstance();
    Context.instance().environment = "test";
    frameManager = new MockFrameManager();
    studio = new MockStudio();
    context = Context.instance();
    context.studio = studio;
    context.frameManager = frameManager;
    context.os = new MockOS();
  }

  public void testTempDirectory() throws Exception
  {
    TempDirectory directory = new TempDirectory();
    Context.instance().tempDirectory = directory;
    assertEquals(directory, Context.instance().tempDirectory);
  }

  public void testStopping() throws Exception
  {
    context.panelPanter = new PanelPainterLoop().started();
    context.animationLoop = new AnimationLoop().started();
    context.cacheCleaner = new CacheCleanerLoop().started();

    context.shutdown();

    assertEquals(false, context.panelPanter.isRunning());
    assertEquals(false, context.animationLoop.isRunning());
    assertEquals(false, context.cacheCleaner.isRunning());
    assertEquals(true, frameManager.allFramesClosed);
    assertEquals(true, studio.isShutdown);
  }

  public void testOsKioskModeIsTurnedOffWhenShuttingDown() throws Exception
  {
    context.os.enterKioskMode();

    context.shutdown();

    assertEquals(false, context.os.isInKioskMode());
  }
  
  public void testAttemptShutdown() throws Exception
  {
    studio.allowShutdown = false;
    context.attemptShutdown();
    assertEquals(false, context.isShutdown);

    studio.allowShutdown = true;
    context.attemptShutdown();
    assertEquals(true, context.isShutdown);
  }
}