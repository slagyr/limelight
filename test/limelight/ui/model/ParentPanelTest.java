//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model;

import limelight.model.api.FakePropProxy;
import limelight.ui.MockPanel;
import limelight.ui.Panel;
import limelight.util.Box;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.*;

import static org.junit.Assert.*;

public class ParentPanelTest
{
  private TestableParentPanel panel;
  private MockParentPanel parent;
  private MockParentPanel child;
  private MockParentPanel grandChild;
  private MockPanel sibling;
  private FakeScene root;

  @Before
  public void setUp() throws Exception
  {
    root = new FakeScene();
    root.setStage(new MockStage());
    panel = new TestableParentPanel();
    root.add(panel);
  }

  private void createFamilyTree()
  {
    root = new FakeScene();
    parent = new MockParentPanel();
    root.add(parent);
    child = new MockParentPanel();
    parent.add(child);
    grandChild = new MockParentPanel();
    child.add(grandChild);
    sibling = new MockPanel();
    parent.add(sibling);

    root.setStage(new MockStage());
  }

  @Test
  public void shouldCanAddPanels() throws Exception
  {
    Panel panel1 = new MockPanel();
    Panel panel2 = new MockPanel();

    panel.add(panel1);
    panel.add(panel2);

    assertEquals(panel1, panel.getChildren().get(0));
    assertEquals(panel2, panel.getChildren().get(1));
  }

  @Test
  public void shouldGetOwnerOfPoint() throws Exception
  {
    Panel panel1 = new MockPanel();
    Panel panel2 = new MockPanel();

    panel1.setLocation(0, 0);
    panel1.setSize(100, 100);
    panel2.setLocation(100, 100);
    panel2.setSize(100, 100);

    panel.add(panel1);
    panel.add(panel2);

    assertSame(panel1, panel.getOwnerOfPoint(new Point(0, 0)));
    assertSame(panel2, panel.getOwnerOfPoint(new Point(100, 100)));
    assertSame(panel1, panel.getOwnerOfPoint(new Point(50, 50)));
    assertSame(panel2, panel.getOwnerOfPoint(new Point(150, 150)));
    assertSame(panel, panel.getOwnerOfPoint(new Point(150, 50)));
    assertSame(panel, panel.getOwnerOfPoint(new Point(50, 150)));
  }

  @Test
  public void shouldGetOwnerOfPointWithNestedPanels() throws Exception
  {
    MockParentPanel panel1 = new MockParentPanel();
    Panel panel2 = new MockPanel();

    panel1.setLocation(50, 50);
    panel1.setSize(100, 100);
    panel2.setLocation(0, 0);
    panel2.setSize(10, 10);

    panel.add(panel1);
    panel1.add(panel2);

    assertSame(panel2, panel.getOwnerOfPoint(new Point(55, 55)));
  }

  @Test
  public void shouldGetOwnerOfPointWithAFloater() throws Exception
  {
    MockPanel child1 = new MockPanel();
    child1.setLocation(0, 0);
    child1.setSize(100, 100);
    MockPanel floater = new MockPanel();
    floater.floater = true;
    floater.setLocation(25, 25);
    floater.setSize(50, 50);

    panel.add(child1);
    panel.add(floater);

    assertSame(child1, panel.getOwnerOfPoint(new Point(0, 0)));
    assertSame(floater, panel.getOwnerOfPoint(new Point(50, 50)));
  }

  @Test
  public void shouldGetOwnerOfPointWithOverlappingFloaters() throws Exception
  {
    MockPanel child1 = new MockPanel();
    child1.setLocation(0, 0);
    child1.setSize(100, 100);
    MockPanel floater1 = new MockPanel();
    floater1.floater = true;
    floater1.setLocation(10, 10);
    floater1.setSize(50, 50);
    MockPanel floater2 = new MockPanel();
    floater2.floater = true;
    floater2.setLocation(40, 40);
    floater2.setSize(50, 50);

    panel.add(child1);
    panel.add(floater1);
    panel.add(floater2);

    assertSame(child1, panel.getOwnerOfPoint(new Point(0, 0)));
    assertSame(floater2, panel.getOwnerOfPoint(new Point(50, 50)));
    assertSame(floater1, panel.getOwnerOfPoint(new Point(20, 20)));
    assertSame(floater2, panel.getOwnerOfPoint(new Point(80, 80)));
  }

  @Test
  public void shouldSterilization() throws Exception
  {
    panel.sterilize();

    try
    {
      panel.add(new MockPanel());
      fail("Should have thrown an exception");
    }
    catch(SterilePanelException e)
    {
      assertEquals("The panel for prop 'TestableParentPanel' has been sterilized. Child components may not be added.", e.getMessage());
    }

    assertEquals(0, panel.getChildren().size());
    assertEquals(true, panel.isSterilized());
  }

  @Test
  public void shouldRemovePanel() throws Exception
  {
    MockPanel panel1 = new MockPanel();
    MockPanel panel2 = new MockPanel();
    panel.add(panel1);
    panel.add(panel2);

    panel.remove(panel1);

    assertEquals(1, panel.getChildren().size());
    assertSame(panel2, panel.getChildren().get(0));
  }

  @Test
  public void shouldRemoveAll() throws Exception
  {
    MockPanel panel1 = new MockPanel();
    MockPanel panel2 = new MockPanel();
    panel.add(panel1);
    panel.add(panel2);

    panel.removeAll();

    assertEquals(false, panel.hasChildren());
    assertEquals(0, panel.getChildren().size());
  }

  @Test
  public void shouldRactanglesAreCached() throws Exception
  {
    Box rectangle = panel.getBounds();

    assertSame(rectangle, panel.getBounds());

    panel.setSize(123, 456);

    assertNotSame(rectangle, panel.getBounds());
  }

  @Test
  public void shouldAbsoluteLocationGetsChanged() throws Exception
  {
    Point location = panel.getAbsoluteLocation();

    panel.setLocation(123, 456);

    assertNotSame(location, panel.getAbsoluteLocation());
    assertEquals(123, panel.getAbsoluteLocation().x);
    assertEquals(456, panel.getAbsoluteLocation().y);
  }

  @Test
  public void shouldAbsoluteBoundsChangesWhenLocationChanges() throws Exception
  {
    Box bounds = panel.getAbsoluteBounds();

    panel.setLocation(123, 456);

    assertNotSame(bounds, panel.getAbsoluteBounds());
    assertEquals(123, panel.getAbsoluteBounds().x);
    assertEquals(456, panel.getAbsoluteBounds().y);
  }

  @Test
  public void shouldAbsoluteBoundsChangesWhenSizeChanges() throws Exception
  {
    Box bounds = panel.getAbsoluteBounds();

    assertSame(bounds, panel.getAbsoluteBounds());

    panel.setSize(123, 456);

    assertNotSame(bounds, panel.getBounds());
    assertEquals(123, panel.getAbsoluteBounds().width);
    assertEquals(456, panel.getAbsoluteBounds().height);
  }

  void addPropPanel()
  {
    FakePropProxy prop = new FakePropProxy();
    PropPanel propPanel = new PropPanel(prop);
    propPanel.add(panel);
  }

  @Test
  public void shouldClearingCacheIsRecursive() throws Exception
  {
    panel.setLocation(20, 21);
    Box parentBounds = panel.getAbsoluteBounds();
    MockPanel child = new MockPanel();
    panel.add(child);
    child.setLocation(10, 11);
    Box childBounds = child.getAbsoluteBounds();

    panel.setLocation(30, 31);

    assertNotSame(parentBounds, panel.getAbsoluteBounds());
    assertNotSame(childBounds, child.getAbsoluteBounds());
  }

  @Test
  public void shouldIterator() throws Exception
  {
    Iterator<Panel> iterator = panel.iterator();

    assertEquals(PanelIterator.class, iterator.getClass());
  }

  @Test
  public void shouldAddingPanelsRequiresUpdate() throws Exception
  {
    Panel child = new MockPanel();

    panel.add(child);

    assertEquals(true, root.hasPanelNeedingLayout(panel));
  }

  @Test
  public void shouldRemoveRequiresUpdate() throws Exception
  {
    Panel child = new MockPanel();
    panel.add(child);
    root.panelsNeedingLayout.clear();

    panel.remove(child);

    assertEquals(true, root.hasPanelNeedingLayout(panel));
  }

  @Test
  public void shouldRemoveDoesntRequireLayoutIfNoChildWasRemoved() throws Exception
  {
    Panel child = new MockPanel();
    panel.add(child);
    root.panelsNeedingLayout.clear();

    panel.remove(new MockPanel());

    assertEquals(false, root.hasPanelNeedingLayout(panel));
  }

  @Test
  public void shouldRemoveAllRequiresUpdate() throws Exception
  {
    Panel child = new MockPanel();
    panel.add(child);
    root.panelsNeedingLayout.clear();

    panel.removeAll();

    assertEquals(true, root.hasPanelNeedingLayout(panel));
  }

  @Test
  public void shouldRemoveAllDoesntRequireUpdateIfNoChildWasRemoved() throws Exception
  {
    root.panelsNeedingLayout.clear();

    panel.removeAll();

    assertEquals(false, root.hasPanelNeedingLayout(panel));
  }

  @Test
  public void shouldAddingChildrenAtIndex() throws Exception
  {
    Panel childA = new MockPanel();
    Panel childB = new MockPanel();
    Panel childC = new MockPanel();

    panel.add(0, childA);
    panel.add(0, childB);
    panel.add(1, childC);

    assertSame(childA, panel.getChildren().get(2));
    assertSame(childB, panel.getChildren().get(0));
    assertSame(childC, panel.getChildren().get(1));
    assertSame(panel, childA.getParent());
    assertSame(panel, childB.getParent());
    assertSame(panel, childC.getParent());
  }

  @Test
  public void shouldAddingChildAtIndexWhenSteralizedThrowsException() throws Exception
  {
    panel.sterilize();
    try
    {
      panel.add(0, panel);
      fail("should have thrown exception");
    }
    catch(Error e)
    {
      //should get exception
    }
  }

  @Test
  public void shouldAddingPanelsAtIndexRequiresLayout() throws Exception
  {
    Panel childA = new MockPanel();
    Panel childB = new MockPanel();

    panel.add(childA);
    panel.add(0, childB);

    assertEquals(true, root.hasPanelNeedingLayout(panel));
  }

  @Test
  public void shouldGetChildrenReturnsACopiedList() throws Exception
  {
    Panel child = new MockPanel();
    panel.add(child);

    java.util.List<Panel> children = panel.getChildren();
    panel.remove(child);

    java.util.List<Panel> children2 = panel.getChildren();

    assertNotSame(children, children2);
    assertEquals(1, children.size());
    assertEquals(0, children2.size());
  }

  @Test
  public void shouldGetChildrenProvidesReadonlyList() throws Exception
  {
    Panel child = new MockPanel();
    panel.add(child);

    java.util.List<Panel> children = panel.getChildren();

    try
    {
      children.add(new MockPanel());
      fail("Should have thrown exception");
    }
    catch(UnsupportedOperationException e)
    {
    }
  }

  @Test
  public void needsLayoutByDefault() throws Exception
  {
    assertEquals(true, root.hasPanelNeedingLayout(panel));
  }

  @Test
  public void needsLayout() throws Exception
  {
    root.panelsNeedingLayout.clear();
    panel.markAsNeedingLayout();

    assertEquals(true, root.hasPanelNeedingLayout(panel));
    assertEquals(1, root.panelsNeedingLayout.size());
    assertEquals(true, root.hasPanelNeedingLayout(panel));

    root.panelsNeedingLayout.clear();

    assertEquals(false, root.hasPanelNeedingLayout(panel));
  }

  @Test
  public void shouldAncestorsWithAutoDimensionsRequireLayoutWhenChildrenAdded() throws Exception
  {
    createFamilyTree();
    root.panelsNeedingLayout.clear();

    PropPanel newPanel = new PropPanel(new FakePropProxy());
    grandChild.add(newPanel);

    assertEquals(true, root.hasPanelNeedingLayout(grandChild));
    assertEquals(true, root.hasPanelNeedingLayout(child));
    assertEquals(true, root.hasPanelNeedingLayout(parent));
  }

  @Test
  public void shouldAncestorsWithAutoDimensionsRequireLayoutWhenChildrenRemoved() throws Exception
  {
    createFamilyTree();

    MockPanel newPanel = new MockPanel();
    grandChild.add(newPanel);
    root.panelsNeedingLayout.clear();
    grandChild.remove(newPanel);

    assertEquals(true, root.hasPanelNeedingLayout(grandChild));
    assertEquals(true, root.hasPanelNeedingLayout(child));
    assertEquals(true, root.hasPanelNeedingLayout(parent));
  }

  @Test
  public void shouldAncestorsWithAutoDimensionsRequireLayoutWhenAllChildrenRemoved() throws Exception
  {
    createFamilyTree();

    MockPanel newPanel = new MockPanel();
    grandChild.add(newPanel);
    root.panelsNeedingLayout.clear();
    grandChild.removeAll();

    assertEquals(true, root.hasPanelNeedingLayout(grandChild));
    assertEquals(true, root.hasPanelNeedingLayout(child));
    assertEquals(true, root.hasPanelNeedingLayout(parent));
  }

  @Test
  public void shouldRemovingChildrenRemovesTheParent() throws Exception
  {
    createFamilyTree();
    child.remove(grandChild);
    assertEquals(null, grandChild.getParent());

    parent.removeAll();
    assertEquals(null, child.getParent());
  }

  @Test
  public void shouldDoLayoutDoesDefaultLayoutIfNoneAreSet() throws Exception
  {
    root.panelsNeedingLayout.clear();
    panel.getDefaultLayout().doLayout(panel);

    assertEquals(panel, BasePanelLayout.instance.lastPanelProcessed);
  }

  @Test
  public void shouldKnowWhenItIsIlluminated() throws Exception
  {
    panel = new TestableParentPanel();
    assertEquals(false, panel.isIlluminated());

    panel.illuminate();

    assertEquals(true, panel.isIlluminated());

    panel.delluminate();

    assertEquals(false, panel.isIlluminated());
  }

  @Test
  public void shouldNotBeIlluinatedWhenAddedToDelluminatedParent() throws Exception
  {
    panel = new TestableParentPanel();
    PanelBase child = new TestablePanelBase();
    panel.add(child);

    assertEquals(false, child.isIlluminated());
  }

  @Test
  public void shouldBeIlluinatedWhenAddedToDelluminatedParent() throws Exception
  {
    panel = new TestableParentPanel();
    PanelBase child = new TestablePanelBase();
    panel.illuminate();
    panel.add(child);

    assertEquals(true, child.isIlluminated());
  }

  @Test
  public void shouldNotBeIlluminatedWhenRemovedFromParent() throws Exception
  {
    panel = new TestableParentPanel();
    PanelBase child = new TestablePanelBase();
    panel.illuminate();
    panel.add(child);
    child.setParent(null);

    assertEquals(false, child.isIlluminated());
  }

  @Test
  public void shouldIlluminateAllOfItsChildren() throws Exception
  {
    createFamilyTree();

    root.delluminate();
    root.illuminate();

    assertEquals(true, root.isIlluminated());
    assertEquals(true, parent.isIlluminated());
    assertEquals(true, child.isIlluminated());
    assertEquals(true, grandChild.isIlluminated());
    assertEquals(true, sibling.isIlluminated());
  }

  @Test
  public void shouldDelluminateAllOfItsChildren() throws Exception
  {
    createFamilyTree();

    root.illuminate();
    root.delluminate();

    assertEquals(false, root.isIlluminated());
    assertEquals(false, parent.isIlluminated());
    assertEquals(false, child.isIlluminated());
    assertEquals(false, grandChild.isIlluminated());
    assertEquals(false, sibling.isIlluminated());
  }
}
