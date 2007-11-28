//package limelight;
//
//import junit.framework.TestCase;
//import java.awt.*;
//import java.awt.geom.Line2D;
//
//public class BlockTest extends TestCase
//{
//	private Block block;
//
//	public void setUp() throws Exception
//	{
//		block = new Block();
//	}
//
//	public void tearDown() throws Exception
//	{
//	}
//
//	public void testConstruction() throws Exception
//	{
//		assertEquals(Panel.class, block.getPanel().getClass());
//		assertFalse(block.getPanel().isOpaque());
//	}
//
//	public void testDefaultStyles() throws Exception
//	{
//		assertEquals("repeat", block.getStyle().getBackgroundImageFillStrategy());
//	}
//
//	public void testSetBorderColor() throws Exception
//	{
//		Color blue = Colors.resolve("blue");
//
//		block.getStyle().setBorderColor("blue");
//
//		assertEquals(blue, block.getStyle().getTopBorderColor());
//		assertEquals(blue, block.getStyle().getRightBorderColor());
//		assertEquals(blue, block.getStyle().getBottomBorderColor());
//		assertEquals(blue, block.getStyle().getLeftBorderColor());
//	}
//
//	public void testSettingEachBorderColor() throws Exception
//	{
//		Color red = Colors.resolve("red");
//
//		block.getStyle().setTopBorderColor("red");
//		block.getStyle().setRightBorderColor("red");
//		block.getStyle().setBottomBorderColor("red");
//		block.getStyle().setLeftBorderColor("red");
//
//
//		assertEquals(red, block.getStyle().getTopBorderColor());
//		assertEquals(red, block.getStyle().getRightBorderColor());
//		assertEquals(red, block.getStyle().getBottomBorderColor());
//		assertEquals(red, block.getStyle().getLeftBorderColor());
//	}
//
//	public void testSetBorderWidth() throws Exception
//	{
//		block.getStyle().setBorderWidth("6");
//
//		assertEquals("6", block.getStyle().getTopBorderWidth());
//		assertEquals("6", block.getStyle().getRightBorderWidth());
//		assertEquals("6", block.getStyle().getBottomBorderWidth());
//		assertEquals("6", block.getStyle().getLeftBorderWidth());
//	}
//
//	public void testSettingEachBorderWidth() throws Exception
//	{
//		block.getStyle().setTopBorderWidth("6");
//		block.getStyle().setRightBorderWidth("6");
//		block.getStyle().setBottomBorderWidth("6");
//		block.getStyle().setLeftBorderWidth("6");
//
//		assertEquals("6", block.getStyle().getTopBorderWidth());
//		assertEquals("6", block.getStyle().getRightBorderWidth());
//		assertEquals("6", block.getStyle().getBottomBorderWidth());
//		assertEquals("6", block.getStyle().getLeftBorderWidth());
//	}
//
//	public void testSetMargin() throws Exception
//	{
//		block.getStyle().setMargin("6");
//
//		assertEquals("6", block.getStyle().getTopMargin());
//		assertEquals("6", block.getStyle().getRightMargin());
//		assertEquals("6", block.getStyle().getBottomMargin());
//		assertEquals("6", block.getStyle().getLeftMargin());
//	}
//
//	public void testSettingEachMargin() throws Exception
//	{
//		block.getStyle().setTopMargin("6");
//		block.getStyle().setRightMargin("6");
//		block.getStyle().setBottomMargin("6");
//		block.getStyle().setLeftMargin("6");
//
//		assertEquals("6", block.getStyle().getTopMargin());
//		assertEquals("6", block.getStyle().getRightMargin());
//		assertEquals("6", block.getStyle().getBottomMargin());
//		assertEquals("6", block.getStyle().getLeftMargin());
//	}
//
//	public void testSetPadding() throws Exception
//	{
//		block.getStyle().setPadding("6");
//
//		assertEquals("6", block.getStyle().getTopPadding());
//		assertEquals("6", block.getStyle().getRightPadding());
//		assertEquals("6", block.getStyle().getBottomPadding());
//		assertEquals("6", block.getStyle().getLeftPadding());
//	}
//
//	public void testSettingEachPadding() throws Exception
//	{
//		block.getStyle().setTopPadding("6");
//		block.getStyle().setRightPadding("6");
//		block.getStyle().setBottomPadding("6");
//		block.getStyle().setLeftPadding("6");
//
//		assertEquals("6", block.getStyle().getTopPadding());
//		assertEquals("6", block.getStyle().getRightPadding());
//		assertEquals("6", block.getStyle().getBottomPadding());
//		assertEquals("6", block.getStyle().getLeftPadding());
//	}
//
//	public void testBackgroundImage() throws Exception
//	{
//		block.getStyle().setBackgroundImage("etc/star.gif");
//
//		assertEquals("etc/star.gif", block.getStyle().getBackgroundImage());
//	}
//
//	public void testHorizontalAlignment() throws Exception
//	{
//		block.getStyle().setHorizontalAlignment("center");
//		assertEquals("center", block.getStyle().getHorizontalAlignment());
//	}
//
//	public void testVerticalAlignment() throws Exception
//	{
//		block.getStyle().setVerticalAlignment("center");
//		assertEquals("center", block.getStyle().getVerticalAlignment());
//	}
//
//	public void testColor() throws Exception
//	{
//		block.getStyle().setTextColor("blue");
//		assertEquals(Color.blue, block.getStyle().getTextColor());
//	}
//
//	public void testFontFace() throws Exception
//	{
//		block.getStyle().setFontFace("Helvetica");
//		assertEquals("Helvetica", block.getStyle().getFontFace());
//	}
//
//	public void testFontSize() throws Exception
//	{
//		block.getStyle().setFontSize("12");
//		assertEquals("12", block.getStyle().getFontSize());
//	}
//
//	public void testFontStyle() throws Exception
//	{
//		block.getStyle().setFontStyle("bold italic");
//		assertEquals("bold italic", block.getStyle().getFontStyle());
//	}
//
//	public void testPaintBorder() throws Exception
//	{
//		block.setX(0);
//		block.setY(1);
//		block.getStyle().setWidth("123");
//		block.getStyle().setHeight("321");
//		block.getStyle().setTopBorderColor("red");
//		block.getStyle().setTopBorderWidth("1");
//		block.getStyle().setRightBorderColor("orange");
//		block.getStyle().setRightBorderWidth("2");
//		block.getStyle().setBottomBorderColor("yellow");
//		block.getStyle().setBottomBorderWidth("3");
//		block.getStyle().setLeftBorderColor("green");
//		block.getStyle().setLeftBorderWidth("4");
//
//		MockPanel panel = new MockPanel();
//		panel.add(block.getPanel());
//	  block.getPanel().snapToDesiredSize();
//
//		MockGraphics graphics = new MockGraphics();
//		block.getPanel().paintBorder(graphics);
//
//		assertEquals(4, graphics.drawnShapes.size());
//		checkLine(graphics.drawnShape(0), 2, 0, 121, 0, Color.red, 1);
//		checkLine(graphics.drawnShape(1), 121, 0, 121, 319, Color.orange, 2);
//		checkLine(graphics.drawnShape(2), 121, 319, 2, 319, Color.yellow, 3);
//		checkLine(graphics.drawnShape(3), 2, 319, 2, 0, Color.green, 4);
//	}
//
//	public void testPaintBorderWhenThereIsNoBordewr() throws Exception
//	{
//		block.getStyle().setBorderWidth("0");
//
//		MockGraphics graphics = new MockGraphics();
//		block.getPanel().paintBorder(graphics);
//
//		assertEquals(0, graphics.drawnShapes.size());
//	}
//
//	public void testPaintBorderWithSomeMargin() throws Exception
//	{
//		block.setX(0);
//		block.setY(1);
//		block.getStyle().setWidth("123");
//		block.getStyle().setHeight("321");
//		block.getStyle().setTopBorderColor("red");
//		block.getStyle().setTopBorderWidth("1");
//		block.getStyle().setTopMargin("10");
//		block.getStyle().setRightBorderColor("orange");
//		block.getStyle().setRightBorderWidth("2");
//		block.getStyle().setRightMargin("20");
//		block.getStyle().setBottomBorderColor("yellow");
//		block.getStyle().setBottomBorderWidth("3");
//		block.getStyle().setBottomMargin("30");
//		block.getStyle().setLeftBorderColor("green");
//		block.getStyle().setLeftBorderWidth("4");
//		block.getStyle().setLeftMargin("40");
//
//		MockPanel panel = new MockPanel();
//		panel.add(block.getPanel());
//	  block.getPanel().snapToDesiredSize();
//
//		MockGraphics graphics = new MockGraphics();
//		block.getPanel().paintBorder(graphics);
//
//		assertEquals(4, graphics.drawnShapes.size());
//		checkLine(graphics.drawnShape(0), 42, 10, 101, 10, Color.red, 1);
//		checkLine(graphics.drawnShape(1), 101, 10, 101, 289, Color.orange, 2);
//		checkLine(graphics.drawnShape(2), 101, 289, 42, 289, Color.yellow, 3);
//		checkLine(graphics.drawnShape(3), 42, 289, 42, 10, Color.green, 4);
//	}
//
//	private void checkLine(MockGraphics.DrawnShape shape, int x1, int y1, int x2, int y2, Color color, int width)
//	{
//		Line2D.Double line = (Line2D.Double)shape.shape;
////System.err.println("x1: " + line.getX1());
////System.err.println("y1: " + line.getY1());
////System.err.println("x2: " + line.getX2());
////System.err.println("y2: " + line.getY2());
//		assertEquals(x1, line.getX1(), 0.1);
//		assertEquals(y1, line.getY1(), 0.1);
//		assertEquals(x2, line.getX2(), 0.1);
//		assertEquals(y2, line.getY2(), 0.1);
//		assertEquals(color, shape.color);
//		assertEquals(width, shape.stroke.getLineWidth(), 0.1);
//	}
//
//
//}
