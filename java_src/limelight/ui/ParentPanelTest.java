package limelight.ui;

import junit.framework.TestCase;

import java.awt.*;

public class ParentPanelTest extends TestCase
{
  private MockParentPanel panel;

  public void setUp() throws Exception
  {
    panel = new MockParentPanel();
  }

  public void testCanAddPanels() throws Exception
  {
    ParentPanel panel1 = new BlockPanel(new MockBlock());
    ParentPanel panel2 = new BlockPanel(new MockBlock());

    panel.add(panel1);
    panel.add(panel2);

    assertEquals(panel1, panel.getChildren().get(0));
    assertEquals(panel2, panel.getChildren().get(1));
  }

  public void testGetOwnerOfPoint() throws Exception
  {
    Panel panel1 = new MockRootBlockPanel();
    Panel panel2 = new MockRootBlockPanel();

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

  public void testGetOwnerOfPointWithNestedPanels() throws Exception
  {
    MockRootBlockPanel panel1 = new MockRootBlockPanel();
    Panel panel2 = new MockRootBlockPanel();

    panel1.setLocation(50, 50);
    panel1.setSize(100, 100);
    panel2.setLocation(0, 0);
    panel2.setSize(10, 10);

    panel.add(panel1);
    panel1.add(panel2);

    assertSame(panel2, panel.getOwnerOfPoint(new Point(55, 55)));
  }

  public void testSterilization() throws Exception
  {
    MockBlock block = new MockBlock();
    block.name = "Blah";
    panel.block = block;
    panel.sterilize();

    try
    {
      panel.add(new BlockPanel(new MockBlock()));
      fail("Should have thrown an exception");
    }
    catch(Exception e)
    {
      assertEquals(SterilePanelException.class, e.getClass());
      assertEquals("The panel for block named 'Blah' has been sterilized. Child components may not be added.", e.getMessage());
    }

    assertEquals(0, panel.getChildren().size());
    assertTrue(panel.isSterilized());
  }

  public void testReplacePanel() throws Exception
  {
    MockPanel panel1 = new MockPanel();
    MockPanel panel2 = new MockPanel();
    MockPanel panel3 = new MockPanel();
    MockPanel panel4 = new MockPanel();

    panel.add(panel1);
    panel.add(panel2);
    panel.add(panel3);

    panel.replace(panel2, panel4);

    assertEquals(3, panel.getChildren().size());
    assertSame(panel4, panel.getChildren().get(1));
  }
}
