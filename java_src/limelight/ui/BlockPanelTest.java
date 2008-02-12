package limelight.ui;

import junit.framework.TestCase;

import java.awt.*;

public class BlockPanelTest extends TestCase
{
  private TestablePanel panel;
  private MockBlock block;

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
    panel = new TestablePanel(block);
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

    block.style.setTransparency("50");
    assertFalse(panel._shouldBuildBuffer());
  }

  private void makePaintable()
  {
    block.style.setWidth("100");
    block.style.setHeight("100");
    block.style.setTextColor("blue");
  }

  public void testPainters() throws Exception
  {
    assertEquals(2, panel.getPainters().size());
    assertEquals(BackgroundPainter.class, panel.getPainters().get(0).getClass());
    assertEquals(BorderPainter.class, panel.getPainters().get(1).getClass());
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

  public void testRepaintDelegatesToParentWhenSizeChanges() throws Exception
  {
    MockParentPanel parent = new MockParentPanel();
    panel.setParent(parent);

    panel.getBlock().getStyle().setWidth("123");
    panel.getBlock().getStyle().setHeight("321");

    panel.repaint();

    assertTrue(parent.repainted);
  }
}
