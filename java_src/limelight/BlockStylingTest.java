package limelight;

import junit.framework.TestCase;
import java.awt.*;

public class BlockStylingTest extends TestCase
{
	private Block block;
	private Page page;

	public void setUp() throws Exception
	{
		page = new Page();
		page.getStyle().setWidth("500");
		page.getStyle().setHeight("500");
		block = new Block();
		page.add(block);

		page.getStyle().setBorderColor("magenta");
		page.getStyle().setBorderWidth("3");
	}

	public void tearDown() throws Exception
	{
		drawIt();
	}

	private void drawIt() throws InterruptedException
	{
		Book book = new Book();
		book.open(page);
		Thread.sleep(2000);
		book.close();
	}

	public void testSomeDimentions() throws Exception
	{
		block.getStyle().setWidth("100");
		block.getStyle().setHeight("100");
		block.getStyle().setBorderColor("red");
		block.getStyle().setBorderWidth("25");
	}

	public void testSettingPosition() throws Exception
	{
		block.getStyle().setWidth("100");
		block.getStyle().setHeight("50");
		block.getStyle().setBorderColor("green");
		block.getStyle().setBorderWidth("3");
		block.setX(100);
		block.setY(123);
		block.getPanel().setBackground(Color.blue);

		assertEquals(100, block.getPanel().getX());
		assertEquals(123, block.getPanel().getY());
	}

	public void testWithSomeMargin() throws Exception
	{
		block.getStyle().setWidth("100");
		block.getStyle().setHeight("100");
		block.getStyle().setMargin("20");
		block.getStyle().setBorderColor("green");
		block.getStyle().setBorderWidth("3");
	}

	public void testWithBackgroundImage() throws Exception
	{
		block.setX(10);
		block.setY(10);
		block.getStyle().setWidth("400");
		block.getStyle().setHeight("400");
		block.getStyle().setBorderWidth("0");
		block.getStyle().setBorderColor("black");
		block.getStyle().setBackgroundImage("etc/star.gif");
		block.getStyle().setBackgroundImageFillStrategy("repeat");
	}

	public void testSomeText() throws Exception
	{
		block.getStyle().setWidth("100");
		block.getStyle().setHeight("100");
		block.setX(200);
		block.setY(200);
		block.getStyle().setBorderColor("blue");
		block.getStyle().setBorderWidth("2");
		block.getStyle().setPadding("0");
		block.setText("I'm a little teapot. Short and Stout.  This is my handle and this is my spout.");
		drawIt();

		block.getStyle().setHorizontalAlignment("right");
		drawIt();

		block.getStyle().setHorizontalAlignment("center");
		drawIt();

		block.getStyle().setVerticalAlignment("bottom");
		drawIt();

		block.getStyle().setVerticalAlignment("center");
	}

	public void testOneLineOfText() throws Exception
	{
		block.getStyle().setWidth("60");
		block.getStyle().setHeight("40");
		block.setX(200);
		block.setY(200);
		block.getStyle().setBorderColor("blue");
		block.getStyle().setTextColor("black");
		block.getStyle().setBorderWidth("2");
		block.getStyle().setPadding("0");
		block.setText("Savy");
		drawIt();

		block.getStyle().setHorizontalAlignment("right");
		drawIt();

		block.getStyle().setHorizontalAlignment("center");
		drawIt();

		block.getStyle().setVerticalAlignment("bottom");
		drawIt();

		block.getStyle().setVerticalAlignment("center");
	}

	public void testFonts() throws Exception
	{
		block.getStyle().setWidth("200");
		block.getStyle().setHeight("30");
		block.getStyle().setBorderWidth("1");
		block.getStyle().setVerticalAlignment("center");
		block.setText("The lazy brown dog jumped over the quick red fox.");
		block.getStyle().setFontFace("Courier");
		block.getStyle().setFontSize("10");
		drawIt();

		block.getStyle().setFontFace("Helvetica");
		block.getStyle().setFontStyle("bold");
		drawIt();

		block.getStyle().setFontFace("Monaco");
		block.getStyle().setFontSize("20");
		block.getStyle().setFontStyle("bold italic");
	}

	public void testPercentageDimensions() throws Exception
	{
		page.getStyle().setHorizontalAlignment("center");
		page.getStyle().setVerticalAlignment("center");
		block.getStyle().setBorderWidth("2");
		block.getStyle().setBorderColor("blue");
		block.getStyle().setWidth("25%");
		block.getStyle().setHeight("25%");
		drawIt();

		block.getStyle().setWidth("50%");
		block.getStyle().setHeight("50%");
		drawIt();

		block.getStyle().setWidth("75%");
		block.getStyle().setHeight("75%");
	}
}
