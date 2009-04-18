package limelight.ui.model.inputs;

import junit.framework.TestCase;
import limelight.ui.model.AlertFrameManager;
import limelight.ui.model.MockFrame;

public class AlertFrameManagerTest extends TestCase
{
  private AlertFrameManager manager;

  public void setUp() throws Exception
  {
    manager = new AlertFrameManager();
  }

  public void testCloseAllFrames() throws Exception
  {
    MockFrame frame = new MockFrame();
    manager.watch(frame);

    manager.closeAllFrames();

    assertEquals(true, frame.closed);
  }
}
