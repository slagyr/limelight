package limelight.ui.events.stage;

import limelight.Context;
import limelight.ui.api.MockStage;
import limelight.ui.model.MockFrameManager;
import limelight.ui.model.StageFrame;
import limelight.ui.model.inputs.MockEventAction;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class StageEventTest
{
  private StageFrame stage;

  private static class TestableStageEvent extends StageEvent
  {
  }

  @Before
  public void setUp() throws Exception
  {
    Context.instance().frameManager = new MockFrameManager();
    stage = new StageFrame(new MockStage());
  }
  
  @Test
  public void hasStageFrame() throws Exception
  {
    StageEvent event = new TestableStageEvent();
    assertEquals(null, event.getStage());

    event.setStage(stage);

    assertEquals(stage, event.getStage());
  }

  @Test
  public void dispatching() throws Exception
  {
    StageEvent event = new TestableStageEvent();
    MockEventAction action = new MockEventAction();
    stage.getEventHandler().add(TestableStageEvent.class, action);

    event.dispatch(stage);

    assertEquals(true, action.invoked);
  }
}
