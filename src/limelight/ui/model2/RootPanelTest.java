package limelight.ui.model2;

import junit.framework.TestCase;

import java.awt.*;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.Iterator;

import limelight.ui.model2.updates.PaintUpdate;
import limelight.ui.model2.updates.Updates;
import limelight.ui.model2.updates.MockUpdate;

public class RootPanelTest extends TestCase
{
  private RootPanel root;
  private MockPropablePanel child;
  private Container contentPane;

  public void setUp() throws Exception
  {
    Frame frame = new MockFrame();
    root = new RootPanel(frame);
    child = new MockPropablePanel();
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

  public void testChangedPanelsEmpytByDefault() throws Exception
  {
    assertEquals(0, root.getChangedPanelCount());

    root.addChangedPanel(child);

    assertEquals(1, root.getChangedPanelCount());
  }
  
  public void testCantAddSameChangedPanelMultipleTimes() throws Exception
  {
    root.addChangedPanel(child);
    root.addChangedPanel(child);
    assertEquals(1, root.getChangedPanelCount());
  }
  
  public void testChangesPanelsIsClearedWhenDestroyed() throws Exception
  {
    root.setPanel(child);
    root.addChangedPanel(child);

    root.destroy();

    assertEquals(0, root.getChangedPanelCount());
  }

  public void testRepaintChangedPanels() throws Exception
  {
    root.setPanel(child);
    root.addChangedPanel(child);
    MockUpdate mockUpdate = new MockUpdate();
    child.setNeededUpdate(mockUpdate);

    root.repaintChangedPanels();

    assertEquals(0, root.getChangedPanelCount());
    assertEquals(true, mockUpdate.updatePerformed);
    assertEquals(child, mockUpdate.updatedPanel);
    assertEquals(true, child.changeMarkerWasReset);
  }
}
