package limelight.ui;

import junit.framework.TestCase;
import java.awt.Rectangle;

class TestablePanel extends BlockPanel
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

public class BlockPanelTest extends TestCase
{
  private TestablePanel panel;
  private MockBlock block;

  public void setUp() throws Exception
  {
    block = new MockBlock();
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
    BlockPanel panel1 = new BlockPanel(new MockBlock());
    BlockPanel panel2 = new BlockPanel(new MockBlock());

    panel.add(panel1);
    panel.add(panel2);

    assertEquals(panel1, panel.getChildren().get(0));
    assertEquals(panel2, panel.getChildren().get(1));
  }

  public void testPaintingWillPaintAllTheChildPanels() throws Exception
  {
    MockPanel panel1 = new MockPanel();
    panel1.getBlock().getStyle().setWidth("100");
    panel1.getBlock().getStyle().setHeight("101");
    MockPanel panel2 = new MockPanel();
    panel2.getBlock().getStyle().setWidth("200");
    panel2.getBlock().getStyle().setHeight("202");
//    panel2.setLocation(20, 22);
//    panel2.setSize(200, 202);

    panel.add(panel1);
    panel.add(panel2);

    MockGraphics graphics = new MockGraphics();
    graphics.setClip(0, 0, 500, 500);
    panel.paint(graphics);

    Rectangle bounds1 = (Rectangle)panel1.paintedGraphics.getClip();
    assertEquals(0, bounds1.getX(), 0.1);
    assertEquals(0, bounds1.getY(), 0.1);
    assertEquals(100, bounds1.getWidth(), 0.1);
    assertEquals(101, bounds1.getHeight(), 0.1);

    Rectangle bounds2 = (Rectangle)panel2.paintedGraphics.getClip();
    assertEquals(0, bounds2.getX(), 0.1);
    assertEquals(0, bounds2.getY(), 0.1);
    assertEquals(200, bounds2.getWidth(), 0.1);
    assertEquals(202, bounds2.getHeight(), 0.1);
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
      assertEquals("The panel for block named 'Blah' has been sterilized and child components may not be added.", e.getMessage());
    }

    assertEquals(0, panel.getChildren().size());
    assertTrue(panel.isSterilized());
  }

  public void testSnapToSizeWithAbsoluteValues() throws Exception
  {
    BlockPanel parent = new BlockPanel(new MockBlock());
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
    BlockPanel parent = new BlockPanel(new MockBlock());
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
}
