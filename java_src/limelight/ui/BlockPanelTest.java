package limelight.ui;

import junit.framework.TestCase;

import java.awt.*;

public class BlockPanelTest extends TestCase
{
  private TestablePanel panel;
  private MockBlock block;
  private ParentPanel rootPanel;

  private class TestablePanel extends BlockPanel
  {
    public TestablePanel(Block owner)
    {
      super(owner);
    }

    public boolean _shouldBuildBuffer()
    {
      return shouldBuildBuffer();
    }

    public void _buildBuffer()
    {
      buildBuffer();
    }
  }

  public void setUp() throws Exception
  {
    block = new MockBlock();
    Frame frame = new MockFrame();
    rootPanel = frame.getPanel();
    panel = new TestablePanel(block);
  }

  public void testPanelHasDefaultSize() throws Exception
  {
    assertEquals(50, panel.getHeight());
    assertEquals(50, panel.getWidth());
  }

  public void testLocationDefaults() throws Exception
  {
    assertEquals(0, panel.getX());
    assertEquals(0, panel.getY());
  }

  public void testCanSetSize() throws Exception
  {
    panel.setSize(100, 200);
    assertEquals(100, panel.getWidth());
    assertEquals(200, panel.getHeight());

    panel.setWidth(300);
    assertEquals(300, panel.getWidth());

    panel.setHeight(400);
    assertEquals(400, panel.getHeight());
  }

  public void testCanSetLocation() throws Exception
  {
    panel.setLocation(123, 456);
    assertEquals(123, panel.getX());
    assertEquals(456, panel.getY());

    panel.setX(234);
    assertEquals(234, panel.getX());

    panel.setY(567);
    assertEquals(567, panel.getY());
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
  
  public void testShouldBuildBuffer() throws Exception
  {
    makePaintable();

    assertTrue(panel._shouldBuildBuffer());
    panel._buildBuffer();
    assertFalse(panel._shouldBuildBuffer());

    block.style.setWidth("101");
    assertTrue(panel._shouldBuildBuffer());
    panel._buildBuffer();
    assertFalse(panel._shouldBuildBuffer());

    block.setText("ABC");
    assertTrue(panel._shouldBuildBuffer());
    panel._buildBuffer();
    assertFalse(panel._shouldBuildBuffer());

    block.setText("XYZ");
    assertTrue(panel._shouldBuildBuffer());
    panel._buildBuffer();
    assertFalse(panel._shouldBuildBuffer());
  }

  private void makePaintable()
  {
    block.style.setWidth("100");
    block.style.setHeight("100");
    block.style.setTextColor("blue");
//    panel.setSize(panel.getPreferredSize());
  }

  public void testPainters() throws Exception
  {
    assertEquals(2, panel.getPainters().size());
    assertEquals(BackgroundPainter.class, panel.getPainters().get(0).getClass());
    assertEquals(BorderPainter.class, panel.getPainters().get(1).getClass());
  }

  public void testSterilization() throws Exception
  {
    block.name = "Blah";
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

  public void testSnapToSizeWithAbsoluteValues() throws Exception
  {
    ParentPanel parent = new BlockPanel(new MockBlock());
    parent.setSize(1000, 1000);
    parent.add(panel);

    block.style.setWidth("100");
    block.style.setHeight("200");
    panel.snapToSize();
    assertEquals(100, panel.getWidth());
    assertEquals(200, panel.getHeight());
  }

  public void testSnapToSizeWithPercentages() throws Exception
  {
    ParentPanel parent = new BlockPanel(new MockBlock());
    parent.setSize(1000, 1000);
    parent.getBlock().getStyle().setMargin("100");
    parent.getBlock().getStyle().setPadding("200");
    parent.add(panel);

    block.style.setWidth("100%");
    block.style.setHeight("50%");
    panel.snapToSize();
    assertEquals(400, panel.getWidth());
    assertEquals(200, panel.getHeight());
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

  public void testRepaint() throws Exception
  {
    MockFrame frame = new MockFrame();
    panel.setParent(frame.getPanel());
    MockLayout layout = new MockLayout();
    panel.setLayout(layout);

    panel.repaint();

    assertTrue(layout.layoutPerformed);
  }
}
