//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.LimelightException;
import limelight.model.FakeProduction;
import limelight.styles.RichStyle;
import limelight.ui.Panel;
import limelight.model.api.MockPropProxy;
import limelight.Context;
import limelight.util.Util;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.awt.*;
import java.util.Map;

public class ScenePanelTest extends Assert
{ 
  private ScenePanel root;
  private MockProp child;
  private MockStage frame;

  @Before
  public void setUp() throws Exception
  {
    frame = new MockStage();
    root = new ScenePanel(new MockPropProxy());
    child = new MockProp("child");
    Context.instance().keyboardFocusManager = new limelight.ui.KeyboardFocusManager().installed();
  }
  
  @Test
  public void hasButtonGroups() throws Exception
  {
    assertNotNull(root.getButtonGroups());
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
    MockProp grandChild = new MockProp();
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
    MockProp grandChild = new MockProp("grandChild");
    MockProp greatGrandChild = new MockProp("greatGrandChild");
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
    MockProp grandChild = new MockProp();
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
    MockProp grandChild = new MockProp();
    MockProp greatGrandChild = new MockProp("greatGrandChild");
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
    FakeProduction production = new FakeProduction();
    root.setProduction(production);
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
    PropPanel panel = new PropPanel(new MockPropProxy(), Util.toMap("id", "some id"));

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
    PropPanel panel = new PropPanel(new MockPropProxy(), Util.toMap("id", "some id"));
    root.add(panel);

    assertSame(panel, root.find("some id"));
  }

  @Test
  public void duplicateIdsCausesAnError() throws Exception
  {
    setupIlluminatedScene();
    PropPanel prop1 = new PropPanel(new MockPropProxy(), Util.toMap("id", "some id"));
    PropPanel prop2 = new PropPanel(new MockPropProxy(), Util.toMap("id", "some id"));

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
    PropPanel prop = new PropPanel(new MockPropProxy(), Util.toMap("id", "some id"));
    root.add(prop);

    root.removeFromIndex(prop);

    assertEquals(null, root.find("some id"));
  }

  @Test
  public void unindexingPropWithoutIdDoesntCrash() throws Exception
  {
    setupIlluminatedScene();
    PropPanel prop = new PropPanel(new MockPropProxy());
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
    PropPanel parent = new PropPanel(new MockPropProxy(), Util.toMap("id", "parent"));
    PropPanel child = new PropPanel(new MockPropProxy(), Util.toMap("id", "child"));
    parent.add(child);
    
    root.add(parent);
    assertSame(parent, root.find("parent"));
    assertSame(child, root.find("child"));

    root.remove(parent);
    assertEquals(null, root.find("parent"));
    assertEquals(null, root.find("child"));
  }
  
  @Test
  public void sceneGetLoaderFromOptions() throws Exception
  {
    assertEquals("", root.getResourceLoader().getRoot());

    root = new ScenePanel(new MockPropProxy());
    root.addOptions(Util.toMap("path", "/some/path"));
    assertEquals("/some/path", root.getResourceLoader().getRoot());
  }
}
