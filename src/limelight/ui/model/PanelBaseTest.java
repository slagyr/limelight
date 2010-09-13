//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

//    assertEquals(true, panel.canBeBuffered());
package limelight.ui.model;

import limelight.ui.MockPanel;
import limelight.ui.api.MockProp;
import limelight.util.Box;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

public class PanelBaseTest extends Assert
{
  private TestablePanelBase panel;
  private MockParentPanel parent;
  private MockParentPanel child;
  private MockPanel grandChild;
  private MockPanel sibling;
  private ScenePanel root;

  @Before
  public void setUp() throws Exception
  {
    root = new ScenePanel(new MockProp());
    root.setFrame(new MockPropFrame());
    panel = new TestablePanelBase();
    root.add(panel);
  }

  @Test
  public void shouldPanelHasDefaultSize() throws Exception
  {
    assertEquals(50, panel.getHeight());
    assertEquals(50, panel.getWidth());
  }

  @Test
  public void shouldLocationDefaults() throws Exception
  {
    assertEquals(0, panel.getX());
    assertEquals(0, panel.getY());
  }

  @Test
  public void shouldCanSetSize() throws Exception
  {
    panel.setSize(100, 200);
    assertEquals(100, panel.getWidth());
    assertEquals(200, panel.getHeight());

    panel.setSize(300, 200);
    assertEquals(300, panel.getWidth());

    panel.setSize(300, 400);
    assertEquals(400, panel.getHeight());
  }

  @Test
  public void shouldCanSetLocation() throws Exception
  {
    panel.setLocation(123, 456);
    assertEquals(123, panel.getX());
    assertEquals(456, panel.getY());
  }

  @Test
  public void shouldIsAncestor() throws Exception
  {
    createFamilyTree();

    assertEquals(true, child.isDescendantOf(parent));
    assertEquals(true, sibling.isDescendantOf(parent));
    assertEquals(true, grandChild.isDescendantOf(parent));
    assertEquals(true, grandChild.isDescendantOf(child));

    assertEquals(false, child.isDescendantOf(sibling));
    assertEquals(false, child.isDescendantOf(grandChild));
  }

  private void createFamilyTree()
  {
    root = new ScenePanel(new MockProp());
    parent = new MockParentPanel();
    root.add(parent);
    child = new MockParentPanel();
    parent.add(child);
    grandChild = new MockPanel();
    child.add(grandChild);
    sibling = new MockPanel();
    parent.add(sibling);

    root.setFrame(new MockPropFrame());
  }

  @Test
  public void shouldGetCommonAncestor() throws Exception
  {
    createFamilyTree();

    assertSame(parent, sibling.getClosestCommonAncestor(child));
    assertSame(parent, child.getClosestCommonAncestor(sibling));
    assertSame(parent, child.getClosestCommonAncestor(grandChild));
    assertSame(parent, grandChild.getClosestCommonAncestor(child));
    assertSame(parent, sibling.getClosestCommonAncestor(grandChild));
    assertSame(parent, grandChild.getClosestCommonAncestor(sibling));
    assertSame(child, grandChild.getClosestCommonAncestor(grandChild));
  }

  @Test
  public void shouldGetClosestCommonAncestorExceptionCase() throws Exception
  {
    createFamilyTree();

    assertEquals(null, parent.getClosestCommonAncestor(new MockPanel()));
  }

  @Test
  public void shouldGetAbsoluteLocation() throws Exception
  {
    createFamilyTree();

    parent.setLocation(1, 10);
    child.setLocation(2, 20);
    grandChild.setLocation(5, 50);

    assertEquals(new Point(1, 10), parent.getAbsoluteLocation());
    assertEquals(new Point(3, 30), child.getAbsoluteLocation());
    assertEquals(new Point(8, 80), grandChild.getAbsoluteLocation());
  }

  @Test
  public void shouldGetRoot() throws Exception
  {
    createFamilyTree();

    assertSame(root, parent.getRoot());
    assertSame(root, sibling.getRoot());
    assertSame(root, child.getRoot());
    assertSame(root, grandChild.getRoot());
  }

  @Test
  public void getBounds() throws Exception
  {
    Box bounds = panel.getBounds();
    assertEquals(panel.getX(), bounds.x);
    assertEquals(panel.getY(), bounds.y);
    assertEquals(panel.getWidth(), bounds.width);
    assertEquals(panel.getHeight(), bounds.height);
  }

}

