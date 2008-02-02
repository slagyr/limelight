package limelight.ui;

import junit.framework.TestCase;

import java.awt.*;

import limelight.ui.FlatStyle;
import limelight.ui.FontFactory;

public class FontFactoryTest extends TestCase
{
  private FontFactory factory;
  private FlatStyle style;

  public void setUp() throws Exception
  {
    factory = FontFactory.instance;
    style = new FlatStyle();
  }
  
  public void testGetDefaultFont() throws Exception
	{
		Font font = factory.createFont(style);
		assertEquals("ArialMT", font.getFontName());
	}

	public void testCreateFontWithBoldHelvetica() throws Exception
	{
		style.setFontFace("Helvetica");
		style.setFontSize("13");
		style.setFontStyle("bold");

		Font font = factory.createFont(style);

		assertEquals("Helvetica-Bold", font.getFontName());
		assertEquals(13, font.getSize());
		assertEquals(Font.BOLD, font.getStyle());
	}

	public void testCreateFontWithCourierItalics() throws Exception
	{
		style.setFontFace("Courier");
		style.setFontSize("6");
		style.setFontStyle("italic");

		Font font = factory.createFont(style);

		assertEquals("Courier-Oblique", font.getFontName());
		assertEquals(6, font.getSize());
		assertEquals(Font.ITALIC, font.getStyle());
	}

	public void testCreateFontWithTimesBoldItalic() throws Exception
	{
		style.setFontFace("Times");
		style.setFontSize("9");
		style.setFontStyle("bold italic");

		Font font = factory.createFont(style);

		assertEquals("Times-BoldItalic", font.getFontName());
		assertEquals(9, font.getSize());
		assertEquals(Font.BOLD | Font.ITALIC, font.getStyle());
	}
}
