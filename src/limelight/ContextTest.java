package limelight;

import junit.framework.TestCase;
import limelight.io.TempDirectory;
import limelight.background.PanelPainterLoop;
import limelight.background.AnimationLoop;
import limelight.background.CacheCleanerLoop;
import limelight.ui.model.MockFrameManager;
import limelight.os.MockOS;

public class ContextTest extends TestCase
{
  private Context context;
  private MockFrameManager frameManager;

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
    prepareForShutdown();

    context.shutdown();

    assertEquals(false, context.panelPanter.isRunning());
    assertEquals(false, context.animationLoop.isRunning());
    assertEquals(false, context.cacheCleaner.isRunning());
    assertEquals(true, frameManager.allFramesClosed);
  }

  private void prepareForShutdown()
  {
    context = Context.instance();
    frameManager = new MockFrameManager();
    context.frameManager = frameManager;
    context.panelPanter = new PanelPainterLoop().started();
    context.animationLoop = new AnimationLoop().started();
    context.cacheCleaner = new CacheCleanerLoop().started();
    context.os = new MockOS();
  }

  public void testOsKioskModeIsTurnedOffWhenShuttingDown() throws Exception
  {
    prepareForShutdown();
    context.os.enterKioskMode();

    context.shutdown();

    assertEquals(false, context.os.isInKioskMode());
  }
}