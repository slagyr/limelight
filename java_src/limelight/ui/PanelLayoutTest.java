package limelight.ui;

import junit.framework.TestCase;

import java.awt.*;

public class PanelLayoutTest extends TestCase
{
  private MockBlockPanel parent;
  private PanelLayout layout;

  public void setUp() throws Exception
  {
    parent = new MockBlockPanel();
    parent.getBlock().getStyle().setWidth("100");
    parent.getBlock().getStyle().setHeight("100");
//    parent.setSize(100, 100);
    layout = new PanelLayout(parent);
  }

  public void testLayoutWithOneFullSizedChild() throws Exception
  {
    MockPanel child = createAndAddChildWithSize(100, 100);

    layout.doLayout();

    assertTrue(child.wasLaidOut);
    assertEquals(new Point(0, 0), child.getLocation());
  }

  public void testLayoutWithTwoChildrenSameRow() throws Exception
  {
    MockPanel child1 = createAndAddChildWithSize(50, 50);
    MockPanel child2 = createAndAddChildWithSize(50, 50);

    layout.doLayout();

    assertTrue(child1.wasLaidOut);
    assertTrue(child2.wasLaidOut);
    assertEquals(new Point(0, 0), child1.getLocation());
    assertEquals(new Point(50, 0), child2.getLocation());
  }
  
  public void testLayoutWrappingToNewRow() throws Exception
  {
    MockPanel child1 = createAndAddChildWithSize(50, 50);
    MockPanel child2 = createAndAddChildWithSize(50, 50);
    MockPanel child3 = createAndAddChildWithSize(50, 50);

    layout.doLayout();

    assertEquals(new Point(0, 0), child1.getLocation());
    assertEquals(new Point(50, 0), child2.getLocation());
    assertEquals(new Point(0, 50), child3.getLocation());
  }

  public void testLayoutWithOneChildAlignedBottomRight() throws Exception
  {
    parent.getBlock().getStyle().setVerticalAlignment("bottom");
    parent.getBlock().getStyle().setHorizontalAlignment("right");
    MockPanel child = createAndAddChildWithSize(50, 50);

    layout.doLayout();

    assertTrue(child.wasLaidOut);
    assertEquals(new Point(50, 50), child.getLocation());
  }

  public void testLayoutWithTwoChildrenAlignedCenterCenter() throws Exception
  {
    parent.getBlock().getStyle().setVerticalAlignment("center");
    parent.getBlock().getStyle().setHorizontalAlignment("center");
    MockPanel child1 = createAndAddChildWithSize(25, 50);
    MockPanel child2 = createAndAddChildWithSize(25, 50);

    layout.doLayout();

    assertEquals(new Point(25, 25), child1.getLocation());
    assertEquals(new Point(50, 25), child2.getLocation());
  }

  public void testChildrenAreLaidout() throws Exception
  {
    MockBlockPanel child = new MockBlockPanel();
    child.getBlock().getStyle().setWidth("50");
    child.getBlock().getStyle().setHeight("50");
    child.getBlock().getStyle().setHorizontalAlignment("center");
    child.getBlock().getStyle().setVerticalAlignment("center");
    parent.add(child);

    MockPanel grandChild = new MockPanel();
    grandChild.prepForSnap(10, 10);
    child.add(grandChild);

    layout.doLayout();

    assertEquals(new Point(0, 0), child.getLocation());
    assertEquals(new Point(20, 20), grandChild.getLocation());
  }

  public void testSnapToSizeWithAbsoluteValues() throws Exception
  {
    parent.setSize(1000, 1000);
    MockBlock block = new MockBlock();
    BlockPanel panel = new BlockPanel(block);
    parent.add(panel);

    block.style.setWidth("100");
    block.style.setHeight("200");
    layout.snapPanelToSize(panel);
    assertEquals(100, panel.getWidth());
    assertEquals(200, panel.getHeight());
  }

  public void testSnapToSizeWithPercentages() throws Exception
  {
    parent.setSize(1000, 1000);
    parent.getBlock().getStyle().setMargin("100");
    parent.getBlock().getStyle().setPadding("200");
    MockBlock block = new MockBlock();
    BlockPanel panel = new BlockPanel(block);
    parent.add(panel);

    block.style.setWidth("100%");
    block.style.setHeight("50%");
    layout.snapPanelToSize(panel);
    assertEquals(400, panel.getWidth());
    assertEquals(200, panel.getHeight());
  }

  private MockPanel createAndAddChildWithSize(int width, int height) throws SterilePanelException
  {
    MockPanel child = new MockPanel();
    child.prepForSnap(width, height);
    parent.add(child);
    return child;
  }
}
