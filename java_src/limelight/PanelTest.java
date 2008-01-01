package limelight;

import junit.framework.TestCase;
import java.awt.event.*;

class TestablePanel extends Panel
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

public class PanelTest extends TestCase
{
	private MockBlock block;
	private TestablePanel panel;
  private FlatStyle style;
  private MockPanel parent;

  public void setUp() throws Exception
	{
		block = new MockBlock();
		panel = new TestablePanel(block);
    style = block.getStyle();
    parent = new MockPanel();
    parent.add(panel);
  }

	public void tearDown() throws Exception
	{
	}
  
  public void testPreferredSize() throws Exception
  {
    style.setWidth("100");
    style.setHeight("200");

    panel.setSize(panel.getPreferredSize());

    assertEquals(100, panel.getWidth());
    assertEquals(200, panel.getHeight());
  }

  public void testOffsets() throws Exception
  {
    style.setYOffset("40");
    style.setXOffset("30");

    assertEquals(30, panel.getXOffset());
    assertEquals(40, panel.getYOffset());
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
    panel.setSize(panel.getPreferredSize());
  }

  public void testPainters() throws Exception
  {
    assertEquals(2, panel.getPainters().size());
    assertEquals(BackgroundPainter.class, panel.getPainters().get(0).getClass());
    assertEquals(BorderPainter.class, panel.getPainters().get(1).getClass());
  }

  public void testPaintComponent() throws Exception
  {
    makePaintable();
    MockPainter painter1 = new MockPainter();
    MockPainter painter2 = new MockPainter();
    panel.getPainters().clear();
    panel.getPainters().add(painter1);
    panel.getPainters().add(painter2);

    panel._buildBuffer();

    assertTrue(painter1.painted);
    assertTrue(painter2.painted);
  }

  public void testSterilization() throws Exception
  {
    block.name = "Blah";
    panel.sterilize();

    try
    {
      panel.add(new Panel(new MockBlock()));
      fail("Should have thrown an exception");
    }
    catch(Error e)
    {
      assertEquals(SterilePanelException.class, e.getClass());
      assertEquals("The panel for block named 'Blah' has been sterilized and child components may not be added.", e.getMessage());
    }
    
    assertEquals(0, panel.getComponents().length);
    assertTrue(panel.isSterilized());
  }

  public void testKeyActions() throws Exception
  {
    assertEquals(1, panel.getKeyListeners().length);
    KeyListener listener = panel.getKeyListeners()[0];

    KeyEvent e = new KeyEvent(panel, 1, 2, 3, 4, '5');

    listener.keyPressed(e);
    assertEquals(e, block.pressedKey);

    listener.keyReleased(e);
    assertEquals(e, block.releasedKey);

    listener.keyTyped(e);
    assertEquals(e, block.typedKey);
  }

  public void testMouseActions() throws Exception
  {
    assertEquals(1, panel.getMouseListeners().length);
    MouseListener listener = panel.getMouseListeners()[0];

    MouseEvent e = new MouseEvent(panel, 1, 2, 3, 4, 5, 6, false);

    listener.mouseClicked(e);
    assertEquals(e, block.clickedMouse);

    listener.mouseEntered(e);
    assertEquals(e, block.enteredMouse);
    assertTrue(block.hooverOn);

    listener.mouseExited(e);
    assertEquals(e, block.exitedMouse);
    assertFalse(block.hooverOn);

    listener.mousePressed(e);
    assertEquals(e, block.pressedMouse);

    listener.mouseReleased(e);
    assertEquals(e, block.releasedMouse);
  }

  public void testMouseMotionActions() throws Exception
  {
    assertEquals(1, panel.getMouseMotionListeners().length);
    MouseMotionListener listener = panel.getMouseMotionListeners()[0];

    MouseEvent e = new MouseEvent(panel, 1, 2, 3, 4, 5, 6, false);

    listener.mouseDragged(e);
    assertEquals(e, block.draggedMouse);

    listener.mouseMoved(e);
    assertEquals(e, block.movedMouse);
  }

  public void testClearingEventListeners() throws Exception
  {
    panel.clearEventListeners();
    assertEquals(0, panel.getMouseListeners().length);
    assertEquals(0, panel.getMouseMotionListeners().length);
    assertEquals(0, panel.getKeyListeners().length);
  }

  public void testSetLocation() throws Exception
  {
    panel.setLocation(100, 100);
    assertEquals(100, panel.getX());
    assertEquals(100, panel.getY());

    style.setXOffset("12");
    style.setYOffset("34");
    panel.setLocation(100, 100);

    assertEquals(112, panel.getX());
    assertEquals(134, panel.getY());
  }

  public void testTextAccessor() throws Exception
  {
    assertEquals(TextPaneTextAccessor.class, panel.getTextAccessor().getClass());
  }
}
