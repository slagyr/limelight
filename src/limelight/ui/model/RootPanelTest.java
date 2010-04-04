//- Copyright � 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.ui.api.MockProp;
import limelight.ui.model.inputs.TextBoxPanel;
import limelight.ui.Panel;
import limelight.ui.api.MockScene;
import limelight.Context;
import limelight.MockResourceLoader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.ArrayList;
import java.awt.*;

public class RootPanelTest extends Assert
{ 
  private RootPanel root;
  private MockPropablePanel child;
  private Container contentPane;
  private PropFrame stageFrame;

  @Before
  public void setUp() throws Exception
  {
    stageFrame = new MockPropFrame();
    root = new RootPanel(new MockProp());
    child = new MockPropablePanel("child");
    contentPane = stageFrame.getContentPane();
    Context.instance().keyboardFocusManager = new limelight.KeyboardFocusManager().installed();
  }

  @Test
  public void shouldSetPanelSetsParentOnePanel() throws Exception
  {
    root.add(child);
    assertSame(root, child.getParent());
  }

  @Test
  public void shouldDestroyRemovesChildsParent() throws Exception
  {
    root.setFrame(stageFrame);
    root.add(child);
    root.destroy();
    assertEquals(null, child.getParent());
  }

  @Test
  public void shouldAddListenersUponSettingTheFrame() throws Exception
  {
    assertEquals(null, root.getListener());

    root.setFrame(stageFrame);
    EventListener listener = root.getListener();
    assertNotNull(listener);

    assertEquals(true, Arrays.asList(contentPane.getMouseListeners()).contains(listener));
    assertEquals(true, Arrays.asList(contentPane.getMouseMotionListeners()).contains(listener));
    assertEquals(true, Arrays.asList(contentPane.getMouseWheelListeners()).contains(listener));
    assertEquals(true, Arrays.asList(contentPane.getKeyListeners()).contains(listener));
  }

  @Test
  public void shouldDestroyRemovesListeners() throws Exception
  {
    root.setFrame(stageFrame);
    EventListener listener = root.getListener();
    root.destroy();

    assertEquals(false, Arrays.asList(contentPane.getMouseListeners()).contains(listener));
    assertEquals(false, Arrays.asList(contentPane.getMouseMotionListeners()).contains(listener));
    assertEquals(false, Arrays.asList(contentPane.getMouseWheelListeners()).contains(listener));
    assertEquals(false, Arrays.asList(contentPane.getKeyListeners()).contains(listener));
    assertNull(root.getListener());
  }

  @Test
  public void shouldBsAliveAfterSettingFrame() throws Exception
  {
    assertEquals(false, root.isAlive());

    root.setFrame(stageFrame);
    assertEquals(true, root.isAlive());

    root.destroy();
    assertEquals(false, root.isAlive());
  }

  @Test
  public void shouldKeyboardFocusDoesNotRemainOnChildWhenDestroyed() throws Exception
  {
    TextBoxPanel inputPanel = new TextBoxPanel();
    root.setFrame(stageFrame);
    child.add(inputPanel);
    root.add(child);

    Context.instance().keyboardFocusManager.focusPanel(inputPanel);
    root.destroy();

    assertNotSame(inputPanel, Context.instance().keyboardFocusManager.getFocusedPanel());
  }

  @Test
  public void shouldAddPanelNeedingLayout() throws Exception
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

  @Test
  public void shouldAddPanelNeedingLayoutDoesntAllowDuplicates() throws Exception
  {
    root.addPanelNeedingLayout(child);
    root.addPanelNeedingLayout(child);

    ArrayList<Panel> panels = new ArrayList<Panel>();
    root.getAndClearPanelsNeedingLayout(panels);

    assertEquals(1, panels.size());
    assertEquals(child, panels.get(0));

    panels.clear();
    root.getAndClearPanelsNeedingLayout(panels);
  }

  @Test
  public void shouldAddPanelNeedingLayoutWontAddWhenAncestorIsAlreadyInTheList() throws Exception
  {
    MockPropablePanel grandChild = new MockPropablePanel();
    child.add(grandChild);

    root.addPanelNeedingLayout(child);
    root.addPanelNeedingLayout(grandChild);

    ArrayList<Panel> panels = new ArrayList<Panel>();
    root.getAndClearPanelsNeedingLayout(panels);

    assertEquals(1, panels.size());
    assertEquals(child, panels.get(0));
  }

  @Test
  public void shouldAddPanelNeedingLayoutWillAddWhenAncestorIsAlreadyInTheListButTheParentDoesntNeedLayout() throws Exception
  {
    MockPropablePanel grandChild = new MockPropablePanel("grandChild");
    MockPropablePanel greatGrandChild = new MockPropablePanel("greatGrandChild");
    child.add(grandChild);
    grandChild.add(greatGrandChild);
    child.doLayout();

    root.addPanelNeedingLayout(child);
    root.addPanelNeedingLayout(greatGrandChild);

    ArrayList<Panel> panels = new ArrayList<Panel>();
    root.getAndClearPanelsNeedingLayout(panels);

    assertEquals(2, panels.size());
    assertEquals(child, panels.get(0));
    assertEquals(greatGrandChild, panels.get(1));
  }

  @Test
  public void shouldAddPanelNeedingLayoutWillRemoveChildWhenAncestorIsAdded() throws Exception
  {
    MockPropablePanel grandChild = new MockPropablePanel();
    child.add(grandChild);

    root.addPanelNeedingLayout(grandChild);
    root.addPanelNeedingLayout(child);

    ArrayList<Panel> panels = new ArrayList<Panel>();
    root.getAndClearPanelsNeedingLayout(panels);

    assertEquals(1, panels.size());
    assertEquals(child, panels.get(0));
  }

  @Test
  public void shouldAddPanelNeedingLayoutWillNotRemoveChildWhenAncestorIsAddedYetChildsParentDoesntNeedLayout() throws Exception
  {
    MockPropablePanel grandChild = new MockPropablePanel();
    MockPropablePanel greatGrandChild = new MockPropablePanel("greatGrandChild");
    child.add(grandChild);
    grandChild.add(greatGrandChild);
    child.doLayout();

    root.addPanelNeedingLayout(greatGrandChild);
    root.addPanelNeedingLayout(child);

    ArrayList<Panel> panels = new ArrayList<Panel>();
    root.getAndClearPanelsNeedingLayout(panels);

    assertEquals(2, panels.size());
    assertEquals(greatGrandChild, panels.get(0));
    assertEquals(child, panels.get(1));
  }

  @Test
  public void shouldAddDirtyRegion() throws Exception
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

  @Test
  public void shouldWontAddDirtyRegionIfAlreadyCovered() throws Exception
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

  @Test
  public void shouldWillRemoveSmallerRegionsWhenCoveredByLarger() throws Exception
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

  @Test
  public void shouldRegionsWithNoOrNegativeDimensionsAreNotAdded() throws Exception
  {
    root.addDirtyRegion(new Rectangle(0, 0, 0, 0));
    root.addDirtyRegion(new Rectangle(10, 10, 0, 0));
    root.addDirtyRegion(new Rectangle(1, 2, -10, -10));
    root.addDirtyRegion(new Rectangle(1, 2, 3, -4));

    ArrayList<Rectangle> regions = new ArrayList<Rectangle>();
    root.getAndClearDirtyRegions(regions);

    assertEquals(0, regions.size());
  }

  @Test
  public void shouldHasAnImageCache() throws Exception
  {
    MockScene scene = new MockScene();
    child.prop.scene = scene;
    scene.loader = new MockResourceLoader();
    root.add(child);
    assertNotNull(root.getImageCache());
  }
}
