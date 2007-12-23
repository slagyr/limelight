//package limelight;
//
//import junit.framework.TestCase;
//import java.awt.*;
//
//public class BlockStylingTest extends TestCase
//{
//	private Block block;
//	private Page page;
//  private FlatStyle style;
//
//  public void setUp() throws Exception
//	{
//		page = new Page();
//		page.getStyle().setWidth("500");
//		page.getStyle().setHeight("500");
//		block = new Block();
//    style = block.getStyle();
//		page.add(block);
//
//		page.getStyle().setBorderColor("magenta");
//		page.getStyle().setBorderWidth("3");
//	}
//
//	public void tearDown() throws Exception
//	{
//		drawIt();
//	}
//
//	private void drawIt() throws InterruptedException
//	{
//		Book book = new Book();
//		book.open(page);
//		Thread.sleep(2000);
//		book.close();
//	}
//
//	public void testSomeDimentions() throws Exception
//	{
//		block.getStyle().setWidth("100");
//		block.getStyle().setHeight("100");
//		block.getStyle().setBorderColor("red");
//		block.getStyle().setBorderWidth("25");
//	}
//
//	public void testSettingPosition() throws Exception
//	{
//		style.setWidth("100");
//		style.setHeight("50");
//		style.setBorderColor("green");
//		style.setBorderWidth("3");
//		style.setXOffset("100");
//		style.setYOffset("123");
//		block.getPanel().setBackground(Color.blue);
//
//		assertEquals(100, block.getPanel().getXOffset());
//		assertEquals(123, block.getPanel().getYOffset());
//	}
//
//	public void testWithSomeMargin() throws Exception
//	{
//		style.setWidth("100");
//		style.setHeight("100");
//		style.setMargin("20");
//		style.setBorderColor("green");
//		style.setBorderWidth("3");
//	}
//
//	public void testWithBackgroundImage() throws Exception
//	{
//		style.setXOffset("10");
//		style.setYOffset("10");
//		style.setWidth("400");
//		style.setHeight("400");
//		style.setBorderWidth("0");
//		style.setBorderColor("black");
//		style.setBackgroundImage("etc/star.gif");
//		style.setBackgroundImageFillStrategy("repeat");
//	}
//
//	public void testSomeText() throws Exception
//	{
//    style.setWidth("100");
//		style.setHeight("100");
//		style.setXOffset("200");
//		style.setYOffset("200");
//		style.setBorderColor("blue");
//		style.setBorderWidth("2");
//		style.setPadding("0");
//    style.setTextColor("black");
//    block.setText("I'm a little teapot. Short and Stout.  This is my handle and this is my spout.");
//		drawIt();
//
//		style.setHorizontalAlignment("right");
//		drawIt();
//
//		style.setHorizontalAlignment("center");
//		drawIt();
//
//		style.setVerticalAlignment("bottom");
//		drawIt();
//
//		style.setVerticalAlignment("center");
//	}
//
//	public void testOneLineOfText() throws Exception
//	{
//		style.setWidth("60");
//		style.setHeight("40");
//		block.setX(200);
//		block.setY(200);
//		style.setBorderColor("blue");
//		style.setTextColor("black");
//		style.setBorderWidth("2");
//		style.setPadding("0");
//		block.setText("Savy");
//		drawIt();
//
//		style.setHorizontalAlignment("right");
//		drawIt();
//
//		style.setHorizontalAlignment("center");
//		drawIt();
//
//		style.setVerticalAlignment("bottom");
//		drawIt();
//
//		style.setVerticalAlignment("center");
//	}
//
//	public void testFonts() throws Exception
//	{
//		style.setWidth("200");
//		style.setHeight("30");
//		style.setBorderWidth("1");
//    style.setBorderColor("brown");
//    style.setVerticalAlignment("center");
//		block.setText("The lazy brown dog jumped over the quick red fox.");
//		style.setFontFace("Courier");
//		style.setFontSize("10");
//    style.setTextColor("black");
//		drawIt();
//
//		style.setFontFace("Helvetica");
//		style.setFontStyle("bold");
//		drawIt();
//
//		style.setFontFace("Monaco");
//		style.setFontSize("20");
//		style.setFontStyle("bold italic");
//	}
//
//	public void testPercentageDimensions() throws Exception
//	{
//		page.getStyle().setHorizontalAlignment("center");
//		page.getStyle().setVerticalAlignment("center");
//		style.setBorderWidth("2");
//		style.setBorderColor("blue");
//		style.setWidth("25%");
//		style.setHeight("25%");
//		drawIt();
//
//		style.setWidth("50%");
//		style.setHeight("50%");
//		drawIt();
//
//		style.setWidth("75%");
//		style.setHeight("75%");
//	}
//}
