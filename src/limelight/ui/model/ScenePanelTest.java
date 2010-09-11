//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.styles.RichStyle;
import limelight.ui.api.MockProp;
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
import java.util.Map;

public class ScenePanelTest extends Assert
{ 
  private ScenePanel root;
  private MockPropablePanel child;
  private MockPropFrame frame;

  @Before
  public void setUp() throws Exception
  {
    frame = new MockPropFrame();
    root = new ScenePanel(new MockProp());
    child = new MockPropablePanel("child");
    Context.instance().keyboardFocusManager = new limelight.KeyboardFocusManager().installed();
  }

  @Test
  public void shouldSetPanelSetsParentOnePanel() throws Exception
  {
    root.add(child);
    assertSame(root, child.getParent());
  }

  @Test
  public void shouldAddMouseListenersUponSettingTheFrame() throws Exception
  {
    assertEquals(null, root.getMouseListener());

    root.setFrame(frame);
    RootMouseListener listener = root.getMouseListener();
    assertNotNull(listener);

    assertEquals(true, Arrays.asList(frame.getMouseListeners()).contains(listener));
    assertEquals(true, Arrays.asList(frame.getMouseMotionListeners()).contains(listener));
    assertEquals(true, Arrays.asList(frame.getMouseWheelListeners()).contains(listener));
  }

  @Test
  public void addsKeyListener() throws Exception
  {
    assertEquals(null, root.getKeyListener());

    root.setFrame(frame);
    RootKeyListener listener = root.getKeyListener();
    assertNotNull(listener);

    assertEquals(listener, frame.keyListener);
  }

  @Test
  public void shouldDestroyRemovesListeners() throws Exception
  {
    root.setFrame(frame);
    RootMouseListener listener = root.getMouseListener();
    root.setFrame(null);

    assertEquals(false, Arrays.asList(frame.getMouseListeners()).contains(listener));
    assertEquals(false, Arrays.asList(frame.getMouseMotionListeners()).contains(listener));
    assertEquals(false, Arrays.asList(frame.getMouseWheelListeners()).contains(listener));
    assertEquals(false, Arrays.asList(frame.getKeyListeners()).contains(listener));
    assertNull(root.getMouseListener());
  }

    // TODO MDM - make siure this works
//  @Test
//  public void keyboardFocusDoesNotRemainOnChildWhenDestroyed() throws Exception
//  {
//    TextBoxPanel inputPanel = new TextBoxPanel();
//    root.setFrame(frame);
//    child.add(inputPanel);
//    root.add(child);
//
//    Context.instance().keyboardFocusManager.focusPanel(inputPanel);
//    root.setFrame(null);
//
//    assertNotSame(inputPanel, Context.instance().keyboardFocusManager.getFocusedPanel());
//  }

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

  @Test
  public void shouldHaveStylesMap() throws Exception
  {
    Map<String, RichStyle> styleMap = root.getStylesStore();
    
    assertNotNull(styleMap);
    assertEquals(0, styleMap.size());
  }

  @Test
  public void shouldIlluminateWhenSettingFrame() throws Exception
  {
    root.setFrame(frame);
    assertEquals(true, root.isIlluminated());
  }

  @Test
  public void shouldDelluminateWhenSettingFrameToNull() throws Exception
  {
    root.setFrame(frame);
    root.setFrame(null);
    
    assertEquals(false, root.isIlluminated());
  }
}
