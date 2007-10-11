package limelight;

import java.awt.*;

import junit.framework.TestCase;

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

	public void testResolveFullHexColors() throws Exception
	{
		checkColor(Colors.resolve("#000000"), 0, 0, 0);
		checkColor(Colors.resolve("#ffffff"), 0xFF, 0xFF, 0xFF);
		checkColor(Colors.resolve("#FFFFFF"), 0xFF, 0xFF, 0xFF);
		checkColor(Colors.resolve("#FffFFf"), 0xFF, 0xFF, 0xFF);
		checkColor(Colors.resolve("#123456"), 0x12, 0x34, 0x56);
		checkColor(Colors.resolve("#ABCDEF"), 0xAB, 0xCD, 0xEF);
	}

	public void testResolveShortHexColors() throws Exception
	{
		checkColor(Colors.resolve("#123"), 0x11, 0x22, 0x33);
		checkColor(Colors.resolve("#AAA"), 0xAA, 0xAA, 0xAA);
		checkColor(Colors.resolve("#aaa"), 0xAA, 0xAA, 0xAA);
		checkColor(Colors.resolve("#aAa"), 0xAA, 0xAA, 0xAA);
		checkColor(Colors.resolve("#B2B"), 0xBB, 0x22, 0xBB);
	}

	public void checkColor(Color color, int red, int green, int blue)
	{
		assertEquals(red, color.getRed());
		assertEquals(green, color.getGreen());
		assertEquals(blue, color.getBlue());
	}
}
