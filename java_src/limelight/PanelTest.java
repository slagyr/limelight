package limelight;

import junit.framework.TestCase;
import java.awt.*;

public class PanelTest extends TestCase
{
	private Block block;
	private Panel panel;
  private Style style;
  private MockPanel parent;

  public void setUp() throws Exception
	{
		block = new MockBlock();
		panel = block.getPanel();
    style = block.getStyle();
    parent = new MockPanel();
    parent.add(panel);
  }

	public void tearDown() throws Exception
	{
	}

	public void testGetDefaultFont() throws Exception
	{
		Font font = panel.createFont();
		assertEquals("ArialMT", font.getFontName());
	}

	public void testCreateFontWithBoldHelvetica() throws Exception
	{
		block.getStyle().setFontFace("Helvetica");
		block.getStyle().setFontSize("13");
		block.getStyle().setFontStyle("bold");

		Font font = panel.createFont();

		assertEquals("Helvetica-Bold", font.getFontName());
		assertEquals(13, font.getSize());
		assertEquals(Font.BOLD, font.getStyle());
	}

	public void testCreateFontWithCourierItalics() throws Exception
	{
		style.setFontFace("Courier");
		style.setFontSize("6");
		style.setFontStyle("italic");

		Font font = panel.createFont();

		assertEquals("Courier-Oblique", font.getFontName());
		assertEquals(6, font.getSize());
		assertEquals(Font.ITALIC, font.getStyle());
	}

	public void testCreateFontWithTimesBoldItalic() throws Exception
	{
		style.setFontFace("Times");
		style.setFontSize("9");
		style.setFontStyle("bold italic");

		Font font = panel.createFont();

		assertEquals("Times-BoldItalic", font.getFontName());
		assertEquals(9, font.getSize());
		assertEquals(Font.BOLD | Font.ITALIC, font.getStyle());
	}
  
  public void testSnapToSize() throws Exception
  {
    style.setWidth("100");
    style.setHeight("200");

    panel.snapToDesiredSize();

    assertEquals(100, panel.getWidth());
    assertEquals(200, panel.getHeight());
  }

  public void testSnapOffsets() throws Exception
  {
    style.setYOffset("40");
    style.setXOffset("30");

    panel.snapOffsets();

    assertEquals(30, panel.getXOffset());
    assertEquals(40, panel.getYOffset());
  }
}
