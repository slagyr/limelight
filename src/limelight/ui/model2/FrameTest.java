package limelight.ui.model2;

import junit.framework.TestCase;
import limelight.ui.api.MockStage;
import limelight.ui.MockPanel;
import limelight.Context;

import javax.swing.*;

public class FrameTest extends TestCase
{
  private MockStage stage;
  private Frame frame;
  private FrameManager frameManager;

  public void setUp() throws Exception
  {
    frameManager = new FrameManager();
    Context.instance().frameManager = frameManager;

    stage = new MockStage();
    frame = new Frame(stage);
  }

  public void testIcon() throws Exception
  {
    assertNotNull(frame.getIconImage());
  }
  
  public void testStage() throws Exception
  {
    assertSame(stage, frame.getStage());
  }

  public void testLoad() throws Exception
  {
    MockPanel panel = new MockPanel();
    frame.load(panel);

    RootPanel root = frame.getRoot();

    assertSame(panel, root.getPanel());
  }

  public void testLoadWillDestroyPreviousRoots() throws Exception
  {
    MockPanel panel = new MockPanel();
    frame.load(panel);

    RootPanel firstRoot = frame.getRoot();
    assertEquals(true, firstRoot.isAlive());

    MockPanel panel2 = new MockPanel();
    frame.load(panel2);

    assertEquals(false, firstRoot.isAlive());
  }
  
  public void testAddsSelfToFrameManager() throws Exception
  {
    assertEquals(1, frameManager.getFrameCount());
    assertEquals(true, frameManager.isWatching(frame));
  }
  
  public void testDefaultCloseOperations() throws Exception
  {
    assertEquals(WindowConstants.DISPOSE_ON_CLOSE, frame.getDefaultCloseOperation());
  }
}
