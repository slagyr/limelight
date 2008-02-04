package limelight.ui;

import junit.framework.TestCase;

public class FrameTest extends TestCase
{
  private Frame frame;
  private MockBlock block;

  public void setUp() throws Exception
  {
    block = new MockBlock();
    frame = new Frame(block);
  }

  public void testCanSetSize() throws Exception
  {
    frame.setSize(100, 200);
    assertEquals(100, frame.getWidth());
    assertEquals(200, frame.getHeight());
  }

  public void testHasRootPanel() throws Exception
  {
    BlockPanel panel = frame.getPanel();

    assertNotNull(panel);
    assertEquals(0, panel.getX());
    assertEquals(0, panel.getY());
  }

  public void testPaintWillPaintPanel() throws Exception
  {
    MockPanel mockPanel = new MockPanel();
    frame.setPanel(mockPanel);

    MockGraphics graphics = new MockGraphics();
    frame.paint(graphics);

    assertEquals(graphics.getClipBounds(), mockPanel.paintedClip);
  }

  public void testListeners() throws Exception
  {
    checkListeners(frame.getMouseListeners(), "MouseListeners");
    checkListeners(frame.getMouseMotionListeners(), "MouseMotionListeners");
    checkListeners(frame.getMouseWheelListeners(), "MouseWheelListeners");
    checkListeners(frame.getKeyListeners(), "KeyListeners");
  }

  private void checkListeners(Object[] listeners, String listenerType)
  {
    boolean found = false;
    for (Object listener : listeners)
    {
      if(listener.getClass() == FrameListener.class)
        found = true;
    }
    assertTrue("FrameListener was not included in " + listenerType, found);
  }
}
