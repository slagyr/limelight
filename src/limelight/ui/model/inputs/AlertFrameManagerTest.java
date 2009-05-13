package limelight.ui.model.inputs;

import junit.framework.TestCase;
import limelight.ui.model.AlertFrameManager;
import limelight.ui.model.MockStageFrame;
import limelight.ui.model.StageFrame;
import limelight.ui.api.MockStage;
import limelight.ui.api.MockTheater;
import limelight.ui.api.MockStudio;
import limelight.Context;

import java.awt.event.WindowEvent;

public class AlertFrameManagerTest extends TestCase
{
  private AlertFrameManager manager;
  private MockStageFrame frame;
  private MockStudio studio;

  public void setUp() throws Exception
  {
    manager = new AlertFrameManager();
    studio = new MockStudio();
    Context.instance().frameManager = manager;
    Context.instance().studio = studio;
    Context.instance().environment = "test";
    frame = new MockStageFrame();
  }

  public void testCloseAllFrames() throws Exception
  {
    manager.watch(frame);

    manager.closeAllFrames();

    assertEquals(true, frame.closed);
  }

  public void testTheaterIsNotifiedOfActivatedStage() throws Exception
  {
    MockStage stage = new MockStage();
    MockTheater theater = stage.theater;
    StageFrame frame = new StageFrame(stage);
    
    manager.windowGainedFocus(new WindowEvent(frame, 1));

    assertEquals(stage, theater.activatedStage);
  }

  public void testShouldAskStageFrameIfItCanClose() throws Exception
  {
    frame.shouldAllowClose = false;
    manager.windowClosing(new WindowEvent(frame, 1));
    assertEquals(false, frame.closed);

    frame.shouldAllowClose = true;
    manager.windowClosing(new WindowEvent(frame, 1));
    assertEquals(true, frame.closed);
  }

  public void testCheckingWithStudioBeforeShuttingDown() throws Exception
  {
    studio.allowShutdown = false;
    manager.watch(frame);
    manager.windowClosed(new WindowEvent(frame, 1));
    assertEquals(false, Context.instance().isShutdown);

    studio.allowShutdown = true;
    manager.watch(frame);
    manager.windowClosed(new WindowEvent(frame, 1));
    assertEquals(true, Context.instance().isShutdown);
  }
}
