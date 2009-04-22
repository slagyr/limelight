package limelight.ui.model.inputs;

import junit.framework.TestCase;
import limelight.ui.model.AlertFrameManager;
import limelight.ui.model.MockStageFrame;
import limelight.ui.model.StageFrame;
import limelight.ui.api.MockStage;
import limelight.ui.api.MockTheater;
import limelight.Context;

import java.awt.event.WindowEvent;

public class AlertFrameManagerTest extends TestCase
{
  private AlertFrameManager manager;

  public void setUp() throws Exception
  {
    manager = new AlertFrameManager();
    Context.instance().frameManager = manager;
  }

  public void testCloseAllFrames() throws Exception
  {
    MockStageFrame frame = new MockStageFrame();
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
}
