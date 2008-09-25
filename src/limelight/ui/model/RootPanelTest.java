//- Copyright 2008 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import junit.framework.TestCase;
import limelight.ui.model.inputs.TextBoxPanel;
import limelight.ui.Panel;
import limelight.Context;
import java.util.Arrays;
import java.util.ArrayList;
import java.awt.*;

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
    Context.instance().keyboardFocusManager = new limelight.KeyboardFocusManager().installed();
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
  
  public void testKeyboardFocusIfLostWhenDestroyed() throws Exception
  {
    TextBoxPanel inputPanel = new TextBoxPanel();
    child.add(inputPanel);
    root.setPanel(child);

    Context.instance().keyboardFocusManager.focusPanel(inputPanel);
    root.destroy();

    assertNotSame(inputPanel, Context.instance().keyboardFocusManager.getFocusedPanel());
  }

  public void testAddPanelNeedingLayout() throws Exception
  {
    root.addPanelNeedingLayout(child);

    ArrayList<Panel> panels = new ArrayList<Panel>();
    root.getAndClearPanelsNeedingLayout(panels);

    assertEquals(1, panels.size());
    assertEquals(child, panels.get(0));

    panels.clear();
    root.getAndClearPanelsNeedingLayout(panels);

    assertEquals(0, panels.size());
  }

  public void testAddDirtyRegion() throws Exception
  {
    Rectangle rectangle = new Rectangle(1, 2, 3, 4);
    root.addDirtyRegion(rectangle);

    ArrayList<Rectangle> regions = new ArrayList<Rectangle>();
    root.getAndClearDirtyRegions(regions);

    assertEquals(1, regions.size());
    assertEquals(rectangle, regions.get(0));

    regions.clear();
    root.getAndClearDirtyRegions(regions);

    assertEquals(0, regions.size());
  }

  public void testWontAddDirtyRegionIfAlreadyCovered() throws Exception
  {
    Rectangle big = new Rectangle(0, 0, 100, 100);
    Rectangle small = new Rectangle(1, 2, 3, 4);
    root.addDirtyRegion(big);
    root.addDirtyRegion(small);

    ArrayList<Rectangle> regions = new ArrayList<Rectangle>();
    root.getAndClearDirtyRegions(regions);

    assertEquals(1, regions.size());
    assertEquals(big, regions.get(0));  
  }

  public void testWillRemoveSmallerRegionsWhenCoveredByLarger() throws Exception
  {
    Rectangle big = new Rectangle(0, 0, 100, 100);
    Rectangle small = new Rectangle(1, 2, 3, 4);
    root.addDirtyRegion(small);
    root.addDirtyRegion(big);

    ArrayList<Rectangle> regions = new ArrayList<Rectangle>();
    root.getAndClearDirtyRegions(regions);

    assertEquals(1, regions.size());
    assertEquals(big, regions.get(0));
  }
}
