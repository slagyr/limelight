package limelight;

import junit.framework.TestCase;
import java.awt.*;

public class PanelTest extends TestCase
{
	private Block block;
	private Panel panel;

	public void setUp() throws Exception
	{
		block = new Block();
		panel = block.getPanel();
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
		block.getStyle().setFontFace("Courier");
		block.getStyle().setFontSize("6");
		block.getStyle().setFontStyle("italic");

		Font font = panel.createFont();

		assertEquals("Courier-Oblique", font.getFontName());
		assertEquals(6, font.getSize());
		assertEquals(Font.ITALIC, font.getStyle());
	}

	public void testCreateFontWithTimesBoldItalic() throws Exception
	{
		block.getStyle().setFontFace("Times");
		block.getStyle().setFontSize("9");
		block.getStyle().setFontStyle("bold italic");

		Font font = panel.createFont();

		assertEquals("Times-BoldItalic", font.getFontName());
		assertEquals(9, font.getSize());
		assertEquals(Font.BOLD | Font.ITALIC, font.getStyle());
	}
}
