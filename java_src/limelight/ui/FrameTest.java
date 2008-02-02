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
    Panel panel = frame.getPanel();

    assertNotNull(panel);
    assertEquals(0, panel.getX());
    assertEquals(0, panel.getY());
  }

  public void testSettingSizeSetsSizeOnPanel() throws Exception
  {
    frame.setSize(100, 200);

    assertEquals(100, frame.getPanel().getWidth());
    assertEquals(200, frame.getPanel().getHeight());
  }

  public void testPaintWillPaintPanel() throws Exception
  {
    MockPanel mockPanel = new MockPanel();
    frame.setPanel(mockPanel);

    MockGraphics graphics = new MockGraphics();
    frame.paint(graphics);

    assertSame(graphics, mockPanel.paintedGraphics);
  }
}
