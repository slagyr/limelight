package limelight;

import junit.framework.TestCase;
import java.awt.*;
import java.awt.geom.Line2D;

public class BlockTest extends TestCase
{
	private Block block;

	public void setUp() throws Exception
	{
		block = new Block();
	}

	public void tearDown() throws Exception
	{
	}
	         
	public void testConstruction() throws Exception
	{
		assertEquals(Panel.class, block.getPanel().getClass());
		assertFalse(block.getPanel().isOpaque());
	}

	public void testDefaultStyles() throws Exception
	{
		assertEquals("repeat", block.getBackgroundImageFillStrategy());
	}

	public void testSetBorderColor() throws Exception
	{
		Color blue = Colors.resolve("blue");

		block.setBorderColor("blue");

		assertEquals(blue, block.getTopBorderColor());
		assertEquals(blue, block.getRightBorderColor());
		assertEquals(blue, block.getBottomBorderColor());
		assertEquals(blue, block.getLeftBorderColor());
	}

	public void testSettingEachBorderColor() throws Exception
	{
		Color red = Colors.resolve("red");

		block.setTopBorderColor("red");
		block.setRightBorderColor("red");
		block.setBottomBorderColor("red");
		block.setLeftBorderColor("red");


		assertEquals(red, block.getTopBorderColor());
		assertEquals(red, block.getRightBorderColor());
		assertEquals(red, block.getBottomBorderColor());
		assertEquals(red, block.getLeftBorderColor());
	}

	public void testSetBorderWidth() throws Exception
	{
		block.setBorderWidth(6);

		assertEquals(6, block.getTopBorderWidth());
		assertEquals(6, block.getRightBorderWidth());
		assertEquals(6, block.getBottomBorderWidth());
		assertEquals(6, block.getLeftBorderWidth());
	}

	public void testSettingEachBorderWidth() throws Exception
	{
		block.setTopBorderWidth(6);
		block.setRightBorderWidth(6);
		block.setBottomBorderWidth(6);
		block.setLeftBorderWidth(6);

		assertEquals(6, block.getTopBorderWidth());
		assertEquals(6, block.getRightBorderWidth());
		assertEquals(6, block.getBottomBorderWidth());
		assertEquals(6, block.getLeftBorderWidth());
	}

	public void testSetMargin() throws Exception
	{
		block.setMargin(6);

		assertEquals(6, block.getTopMargin());
		assertEquals(6, block.getRightMargin());
		assertEquals(6, block.getBottomMargin());
		assertEquals(6, block.getLeftMargin());
	}

	public void testSettingEachMargin() throws Exception
	{
		block.setTopMargin(6);
		block.setRightMargin(6);
		block.setBottomMargin(6);
		block.setLeftMargin(6);

		assertEquals(6, block.getTopMargin());
		assertEquals(6, block.getRightMargin());
		assertEquals(6, block.getBottomMargin());
		assertEquals(6, block.getLeftMargin());
	}

	public void testSetPadding() throws Exception
	{
		block.setPadding(6);

		assertEquals(6, block.getTopPadding());
		assertEquals(6, block.getRightPadding());
		assertEquals(6, block.getBottomPadding());
		assertEquals(6, block.getLeftPadding());
	}

	public void testSettingEachPadding() throws Exception
	{
		block.setTopPadding(6);
		block.setRightPadding(6);
		block.setBottomPadding(6);
		block.setLeftPadding(6);

		assertEquals(6, block.getTopPadding());
		assertEquals(6, block.getRightPadding());
		assertEquals(6, block.getBottomPadding());
		assertEquals(6, block.getLeftPadding());
	}

	public void testBackgroundImage() throws Exception
	{
		block.setBackgroundImage("etc/star.gif");

		assertEquals("etc/star.gif", block.getBackgroundImage());
	}

	public void testHorizontalAlignment() throws Exception
	{
		block.setHorizontalAlignment("center");
		assertEquals("center", block.getHorizontalAlignment());
	}

	public void testVerticalAlignment() throws Exception
	{
		block.setVerticalAlignment("middle");
		assertEquals("middle", block.getVerticalAlignment());
	}

	public void testColor() throws Exception
	{
		block.setTextColor("blue");
		assertEquals(Color.blue, block.getTextColor());
	}

	public void testFontFace() throws Exception
	{
		block.setFontFace("Helvetica");
		assertEquals("Helvetica", block.getFontFace());
	}

	public void testFontSize() throws Exception
	{
		block.setFontSize(12);
		assertEquals(12, block.getFontSize());
	}

	public void testFontStyle() throws Exception
	{
		block.setFontStyle("bold italic");
		assertEquals("bold italic", block.getFontStyle());
	}

	public void testPaintBorder() throws Exception
	{
		block.setX(0);
		block.setY(1);
		block.setWidth("123");
		block.setHeight("321");
		block.setTopBorderColor("red");
		block.setTopBorderWidth(1);
		block.setRightBorderColor("orange");
		block.setRightBorderWidth(2);
		block.setBottomBorderColor("yellow");
		block.setBottomBorderWidth(3);
		block.setLeftBorderColor("green");
		block.setLeftBorderWidth(4);

		MockGraphics graphics = new MockGraphics();
		block.getPanel().paintBorder(graphics);

		assertEquals(4, graphics.drawnShapes.size());
		checkLine(graphics.drawnShape(0), 2, 0, 121, 0, Color.red, 1);
		checkLine(graphics.drawnShape(1), 121, 0, 121, 319, Color.orange, 2);
		checkLine(graphics.drawnShape(2), 121, 319, 2, 319, Color.yellow, 3);
		checkLine(graphics.drawnShape(3), 2, 319, 2, 0, Color.green, 4);
	}

	public void testPaintBorderWhenThereIsNoBordewr() throws Exception
	{
		block.setBorderWidth(0);

		MockGraphics graphics = new MockGraphics();
		block.getPanel().paintBorder(graphics);

		assertEquals(0, graphics.drawnShapes.size());
	}

	public void testPaintBorderWithSomeMargin() throws Exception
	{
		block.setX(0);
		block.setY(1);
		block.setWidth("123");
		block.setHeight("321");
		block.setTopBorderColor("red");
		block.setTopBorderWidth(1);
		block.setTopMargin(10);
		block.setRightBorderColor("orange");
		block.setRightBorderWidth(2);
		block.setRightMargin(20);
		block.setBottomBorderColor("yellow");
		block.setBottomBorderWidth(3);
		block.setBottomMargin(30);
		block.setLeftBorderColor("green");
		block.setLeftBorderWidth(4);
		block.setLeftMargin(40);

		MockGraphics graphics = new MockGraphics();
		block.getPanel().paintBorder(graphics);

		assertEquals(4, graphics.drawnShapes.size());
		checkLine(graphics.drawnShape(0), 42, 10, 101, 10, Color.red, 1);
		checkLine(graphics.drawnShape(1), 101, 10, 101, 289, Color.orange, 2);
		checkLine(graphics.drawnShape(2), 101, 289, 42, 289, Color.yellow, 3);
		checkLine(graphics.drawnShape(3), 42, 289, 42, 10, Color.green, 4);
	}

	private void checkLine(MockGraphics.DrawnShape shape, int x1, int y1, int x2, int y2, Color color, int width)
	{
		Line2D.Double line = (Line2D.Double)shape.shape;
//System.err.println("x1: " + line.getX1());
//System.err.println("y1: " + line.getY1());
//System.err.println("x2: " + line.getX2());
//System.err.println("y2: " + line.getY2());
		assertEquals(x1, line.getX1(), 0.1);
		assertEquals(y1, line.getY1(), 0.1);
		assertEquals(x2, line.getX2(), 0.1);
		assertEquals(y2, line.getY2(), 0.1);
		assertEquals(color, shape.color);
		assertEquals(width, shape.stroke.getLineWidth(), 0.1);
	}


}
