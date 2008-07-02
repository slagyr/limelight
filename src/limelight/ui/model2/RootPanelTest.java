package limelight.ui.model2;

import junit.framework.TestCase;

import limelight.ui.api.MockStage;
import limelight.ui.MockPanel;

import java.awt.*;
import java.util.Arrays;

public class RootPanelTest extends TestCase
{
  private RootPanel root;
  private Frame frame;
  private MockPanel child;
  private Container contentPane;

  public void setUp() throws Exception
  {
    frame = new Frame(new MockStage());
    root = new RootPanel(frame);
    child = new MockPanel();
    contentPane = frame.getContentPane();
  }

  public void testSetPanelSetsParentOnePanel() throws Exception
  {
    root.setPanel(child);
    assertSame(root, child.getParent());
  }
  
  public void testDestroyRemovesChildsParent() throws Exception
  {
    root.setPanel(child);
    root.destroy();
    assertNull(child.getParent());
  }
  
  public void testSetPanelAddListeners() throws Exception
  {
    assertNull(null, root.getListener());

    root.setPanel(child);
    EventListener listener = root.getListener();
    assertNotNull(listener);

    assertEquals(true, Arrays.asList(contentPane.getMouseListeners()).contains(listener));
    assertEquals(true, Arrays.asList(contentPane.getMouseMotionListeners()).contains(listener));
    assertEquals(true, Arrays.asList(contentPane.getMouseWheelListeners()).contains(listener));
    assertEquals(true, Arrays.asList(contentPane.getKeyListeners()).contains(listener));
  }

  public void testDestroyRemovesListeners() throws Exception
  {
    root.setPanel(child);
    EventListener listener = root.getListener();
    root.destroy();

    assertEquals(false, Arrays.asList(contentPane.getMouseListeners()).contains(listener));
    assertEquals(false, Arrays.asList(contentPane.getMouseMotionListeners()).contains(listener));
    assertEquals(false, Arrays.asList(contentPane.getMouseWheelListeners()).contains(listener));
    assertEquals(false, Arrays.asList(contentPane.getKeyListeners()).contains(listener));
    assertNull(root.getListener());
  }

  public void testIsAlive() throws Exception
  {
    assertEquals(false, root.isAlive());

    root.setPanel(child);
    assertEquals(true, root.isAlive());

    root.destroy();
    assertEquals(false, root.isAlive());
  }

}
