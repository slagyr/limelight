package limelight.model;

import limelight.Context;
import limelight.model.api.MockPropProxy;
import limelight.model.api.MockStageProxy;
import limelight.ui.model.FramedStage;
import limelight.ui.model.InertFrameManager;
import limelight.ui.model.Scene;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class StageTest
{
  private FramedStage stage;

  @Before
  public void setUp() throws Exception
  {
    Context.instance().frameManager = new InertFrameManager();
    Context.instance().keyboardFocusManager = new limelight.ui.KeyboardFocusManager();
    stage = new FramedStage(new MockStageProxy());
  }

  @Test
  public void shouldVitality() throws Exception
  {
    assertEquals(true, stage.isVital());

    stage.setVital(false);

    assertEquals(false, stage.isVital());
  }

  @Test
  public void shouldShouldAllowClose() throws Exception
  {
    assertEquals(true, stage.shouldAllowClose());

    Scene scene = new Scene(new MockPropProxy());
    stage.setRoot(scene);
    scene.setShouldAllowClose(true);
    assertEquals(true, stage.shouldAllowClose());

    scene.setShouldAllowClose(false);
    assertEquals(false, stage.shouldAllowClose());
  }
}
