//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import junit.framework.TestCase;
import limelight.ui.MockPanel;
import limelight.ui.Panel;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class PanelIteratorTest extends TestCase
{
  private MockParentPanel root;
  private MockParentPanel child1;
  private MockParentPanel child2;
  private MockParentPanel grandChild1;
  private MockParentPanel grandChild2;
  private MockParentPanel grandChild3;

  public void setUp() throws Exception
  {
    root = new MockParentPanel();
    child1 = new MockParentPanel();
    child2 = new MockParentPanel();
    grandChild1 = new MockParentPanel();
    grandChild2 = new MockParentPanel();
    grandChild3 = new MockParentPanel();

    root.add(child1);
    root.add(child2);
    child1.add(grandChild1);
    child1.add(grandChild2);
    child2.add(grandChild3);
  }

  public void testAllPanelsAreIterated() throws Exception
  {
    Set<Panel> iterated = new HashSet<Panel>();
    for(PanelIterator iterator = new PanelIterator(root); iterator.hasNext();)
      iterated.add(iterator.next());

    assertEquals(true, iterated.contains(root));
    assertEquals(true, iterated.contains(child1));
    assertEquals(true, iterated.contains(child2));
    assertEquals(true, iterated.contains(grandChild1));
    assertEquals(true, iterated.contains(grandChild2));
    assertEquals(true, iterated.contains(grandChild3));
  }

  public void testIsDepthFirstTraversal() throws Exception
  {
    List<Panel> iterated = new LinkedList<Panel>();
    for(PanelIterator iterator = new PanelIterator(root); iterator.hasNext();)
      iterated.add(iterator.next());

    assertSame(root, iterated.get(0));
    assertSame(child1, iterated.get(1));
    assertSame(grandChild1, iterated.get(2));
    assertSame(grandChild2, iterated.get(3));
    assertSame(child2, iterated.get(4));
    assertSame(grandChild3, iterated.get(5));
  }
}
