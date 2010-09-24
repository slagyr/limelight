//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.LimelightException;
import limelight.styles.RichStyle;
import limelight.ui.Panel;
import limelight.ui.api.MockPropProxy;
import limelight.ui.api.MockSceneProxy;
import limelight.Context;
import limelight.util.MockResourceLoader;
import limelight.util.Util;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.awt.*;
import java.util.Map;

public class ScenePanelTest extends Assert
{ 
  private Scene root;
  private MockPropablePanel child;
  private MockStage frame;

  @Before
  public void setUp() throws Exception
  {
    frame = new MockStage();
    root = new Scene(new MockPropProxy());
    child = new MockPropablePanel("child");
    Context.instance().keyboardFocusManager = new limelight.ui.KeyboardFocusManager().installed();
  }

  @Test
  public void shouldSetPanelSetsParentOnePanel() throws Exception
  {
    root.add(child);
    assertSame(root, child.getParent());
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
    MockSceneProxy scene = new MockSceneProxy();
    child.prop.sceneProxy = scene;
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
    root.setStage(frame);
    assertEquals(true, root.isIlluminated());
  }

  @Test
  public void shouldDelluminateWhenSettingFrameToNull() throws Exception
  {
    root.setStage(frame);
    root.setStage(null);
    
    assertEquals(false, root.isIlluminated());
  }

  @Test
  public void propWithIdIsIndexWhenAddedToScene() throws Exception
  {
    setupIlluminatedScene();
    Prop panel = new Prop(new MockPropProxy(), Util.toMap("id", "some id"));

    root.add(panel);

    assertSame(panel, root.find("some id"));
  }

  private void setupIlluminatedScene()
  {
    root.setStage(frame);
    root.illuminate();
  }

  @Test
  public void propConnectedToSceneIsIndexedWhenIdIsSet() throws Exception
  {
    setupIlluminatedScene();
    Prop panel = new Prop(new MockPropProxy(), Util.toMap("id", "some id"));
    root.add(panel);

    assertSame(panel, root.find("some id"));
  }

  @Test
  public void duplicateIdsCausesAnError() throws Exception
  {
    setupIlluminatedScene();
    Prop prop1 = new Prop(new MockPropProxy(), Util.toMap("id", "some id"));
    Prop prop2 = new Prop(new MockPropProxy(), Util.toMap("id", "some id"));

    root.add(prop1);
    
    try
    {
      root.add(prop2);
      fail("Should have raised error");
    }
    catch(LimelightException e)
    {
      assertEquals("Duplicate id: some id", e.getMessage());
    }
  }

  @Test
  public void unindexingAProp() throws Exception
  {
    setupIlluminatedScene();
    Prop prop = new Prop(new MockPropProxy(), Util.toMap("id", "some id"));
    root.add(prop);

    root.removeFromIndex(prop);

    assertEquals(null, root.find("some id"));
  }

  @Test
  public void unindexingPropWithoutIdDoesntCrash() throws Exception
  {
    setupIlluminatedScene();
    Prop prop = new Prop(new MockPropProxy());
    root.add(prop);

    try
    {
      root.removeFromIndex(prop);
    }
    catch(Exception e)
    {
      fail("Should not throw error: " + e.toString());
    }
  }

  @Test
  public void propTreesAreIndexedWhenAddedAndUnindexedWhenRemoved() throws Exception
  {
    setupIlluminatedScene();
    Prop parent = new Prop(new MockPropProxy(), Util.toMap("id", "parent"));
    Prop child = new Prop(new MockPropProxy(), Util.toMap("id", "child"));
    parent.add(child);
    
    root.add(parent);
    assertSame(parent, root.find("parent"));
    assertSame(child, root.find("child"));

    root.remove(parent);
    assertEquals(null, root.find("parent"));
    assertEquals(null, root.find("child"));
  }
}
