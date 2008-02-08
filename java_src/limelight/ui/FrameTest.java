package limelight.ui;

import junit.framework.TestCase;

import java.awt.*;

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
    ParentPanel panel = frame.getPanel();

    assertNotNull(panel);
    assertEquals(0, panel.getX());
    assertEquals(0, panel.getY());
  }

//TODO - MDM - to do this properly, we'd have to create a factory for PaintJobs.  Is that really neccessary?  
//  public void testPaintWillPaintPanel() throws Exception
//  {
//    MockRootBlockPanel mockPanel = new MockRootBlockPanel();
//    mockPanel.setSize(100, 100);
//    frame.setPanel(mockPanel);
//
//    MockGraphics graphics = new MockGraphics();
//    frame.paint(graphics);
//
//    assertEquals(graphics.getClipBounds(), mockPanel.paintedClip);
//  }

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

  public void testDoLayout() throws Exception
  {
    MockRootBlockPanel panel = new MockRootBlockPanel();
    frame.setPanel(panel);
    MockLayout layout = new MockLayout();
    panel.setLayout(layout);

    frame.doLayout();

    assertEquals(new Point(0, 0), frame.getLocation());
    assertTrue(layout.layoutPerformed);
  }
}
