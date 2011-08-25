//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.util;

import java.awt.*;

import junit.framework.TestCase;
import limelight.util.Colors;

public class ColorsTest extends TestCase
{
	public void testResolveCommonColors() throws Exception
	{
		assertEquals(Color.blue, Colors.resolve("blue"));
		assertEquals(Color.blue, Colors.resolve("BLUE"));
		assertEquals(Color.blue, Colors.resolve("Blue"));

		assertEquals(Color.red, Colors.resolve("red"));
		assertEquals(Color.red, Colors.resolve("RED"));
		assertEquals(Color.red, Colors.resolve("Red"));
	}
  
  public void testResolvedNamedColorsInWrongFormat() throws Exception
  {
    assertEquals(Colors.resolve("yellow"), Colors.resolve("Yellow"));
    assertEquals(Colors.resolve("yellow"), Colors.resolve("YELLOW"));
    assertEquals(Colors.resolve("lime_green"), Colors.resolve("LimeGreen"));
    assertEquals(Colors.resolve("lime_green"), Colors.resolve("Lime Green"));
    assertEquals(Colors.resolve("lime_green"), Colors.resolve("lime green"));
    assertEquals(Colors.resolve("lime_green"), Colors.resolve("LIME_GREEN"));
    assertEquals(Colors.resolve("lime_green"), Colors.resolve("LIME GREEN"));
  }

  public void testResolveFullHexColors() throws Exception
	{
		checkColor(Colors.resolve("#000000"), 0, 0, 0, 0xFF);
		checkColor(Colors.resolve("#ffffff"), 0xFF, 0xFF, 0xFF, 0xFF);
		checkColor(Colors.resolve("#FFFFFF"), 0xFF, 0xFF, 0xFF, 0xFF);
		checkColor(Colors.resolve("#FffFFf"), 0xFF, 0xFF, 0xFF, 0xFF);
		checkColor(Colors.resolve("#123456"), 0x12, 0x34, 0x56, 0xFF);
		checkColor(Colors.resolve("#ABCDEF"), 0xAB, 0xCD, 0xEF, 0xFF);
	}

	public void testResolveFullHexColorsWithAlpha() throws Exception
	{
		checkColor(Colors.resolve("#00000000"), 0, 0, 0, 0);
		checkColor(Colors.resolve("#ffffffff"), 0xFF, 0xFF, 0xFF, 0xFF);
		checkColor(Colors.resolve("#FFFFFFFF"), 0xFF, 0xFF, 0xFF, 0xFF);
		checkColor(Colors.resolve("#FffFFffF"), 0xFF, 0xFF, 0xFF, 0xFF);
		checkColor(Colors.resolve("#12345678"), 0x12, 0x34, 0x56, 0x78);
		checkColor(Colors.resolve("#ABCDEFED"), 0xAB, 0xCD, 0xEF, 0xED);
	}

	public void testResolveShortHexColors() throws Exception
	{
		checkColor(Colors.resolve("#123"), 0x11, 0x22, 0x33, 0xFF);
		checkColor(Colors.resolve("#AAA"), 0xAA, 0xAA, 0xAA, 0xFF);
		checkColor(Colors.resolve("#aaa"), 0xAA, 0xAA, 0xAA, 0xFF);
		checkColor(Colors.resolve("#aAa"), 0xAA, 0xAA, 0xAA, 0xFF);
		checkColor(Colors.resolve("#B2B"), 0xBB, 0x22, 0xBB, 0xFF);
	}

	public void testResolveShortHexColorsWithAlpha() throws Exception
	{
		checkColor(Colors.resolve("#1234"), 0x11, 0x22, 0x33, 0x44);
		checkColor(Colors.resolve("#AAAA"), 0xAA, 0xAA, 0xAA, 0xAA);
		checkColor(Colors.resolve("#aaaa"), 0xAA, 0xAA, 0xAA, 0xAA);
		checkColor(Colors.resolve("#aAaA"), 0xAA, 0xAA, 0xAA, 0xAA);
		checkColor(Colors.resolve("#B2B2"), 0xBB, 0x22, 0xBB, 0x22);
	}
  
  public void testHexColorsWithoutHash() throws Exception
  {
		checkColor(Colors.resolve("123456"), 0x12, 0x34, 0x56, 0xFF);
		checkColor(Colors.resolve("12345678"), 0x12, 0x34, 0x56, 0x78);
		checkColor(Colors.resolve("123"), 0x11, 0x22, 0x33, 0xFF);
		checkColor(Colors.resolve("1234"), 0x11, 0x22, 0x33, 0x44);
  }

  public void checkColor(Color color, int red, int green, int blue, int alpha)
	{
		assertEquals(red, color.getRed());
		assertEquals(green, color.getGreen());
		assertEquals(blue, color.getBlue());
		assertEquals(alpha, color.getAlpha());
	}

  public void testResolvesTransparent() throws Exception
  {
    checkColor(Colors.resolve("transparent"), 0, 0, 0, 0);
  }

  public void testToString() throws Exception
  {
    assertEquals("#ff0000ff", Colors.toString(Color.RED));
    assertEquals("#00ff00ff", Colors.toString(Color.GREEN));
    assertEquals("#0000ffff", Colors.toString(Color.BLUE));
    assertEquals("#abcdefff", Colors.toString(Colors.resolve("#abcdef")));
    assertEquals("#12345678", Colors.toString(Colors.resolve("#12345678"))); 
    assertEquals("#00000000", Colors.toString(Colors.resolve("#0000")));
  }
}