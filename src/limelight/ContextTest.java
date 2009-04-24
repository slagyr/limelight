package limelight;

import junit.framework.TestCase;
import limelight.io.TempDirectory;
import limelight.background.PanelPainterLoop;
import limelight.background.AnimationLoop;
import limelight.background.CacheCleanerLoop;
import limelight.ui.model.MockFrameManager;

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
}