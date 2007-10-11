package limelight;

import junit.framework.TestCase;
import java.awt.*;
import limelight.*;

public class BlockStylingTest extends TestCase
{
	private Block block;
	private Page page;

	public void setUp() throws Exception
	{
		page = new Page();
		page.setWidth("500");
		page.setHeight("500");
		block = new Block();
		page.add(block);

		page.setBorderColor("magenta");
		page.setBorderWidth(3);
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
		block.setWidth("100");
		block.setHeight("100");
		block.setBorderColor("red");
		block.setBorderWidth(25);

		assertEquals(100, block.getPanel().getWidth());
		assertEquals(100, block.getPanel().getHeight());
	}

	public void testSettingPosition() throws Exception
	{
		block.setWidth("100");
		block.setHeight("50");
		block.setBorderColor("green");
		block.setBorderWidth(3);
		block.setX(100);
		block.setY(123);
		block.getPanel().setBackground(Color.blue);

		assertEquals(100, block.getPanel().getX());
		assertEquals(123, block.getPanel().getY());
	}

	public void testWithSomeMargin() throws Exception
	{
		block.setWidth("100");
		block.setHeight("100");
		block.setMargin(20);
		block.setBorderColor("green");
		block.setBorderWidth(3);
	}

	public void testWithBackgroundImage() throws Exception
	{
		block.setX(10);
		block.setY(10);
		block.setWidth("400");
		block.setHeight("400");
		block.setBorderWidth(0);
		block.setBorderColor("black");
		block.setBackgroundImage("etc/star.gif");
		block.setBackgroundImageFillStrategy("repeat");
	}

	public void testSomeText() throws Exception
	{
		block.setWidth("100");
		block.setHeight("100");
		block.setX(200);
		block.setY(200);
		block.setBorderColor("blue");
		block.setBorderWidth(2);
		block.setPadding(0);
		block.setText("I'm a little teapot. Short and Stout.  This is my handle and this is my spout.");
		drawIt();

		block.setHorizontalAlignment("right");
		drawIt();

		block.setHorizontalAlignment("center");
		drawIt();

		block.setVerticalAlignment("bottom");
		drawIt();

		block.setVerticalAlignment("middle");
	}

	public void testOneLineOfText() throws Exception
	{
		block.setWidth("60");
		block.setHeight("40");
		block.setX(200);
		block.setY(200);
		block.setBorderColor("blue");
		block.setTextColor("black");
		block.setBorderWidth(2);
		block.setPadding(0);
		block.setText("Savy");
		drawIt();

		block.setHorizontalAlignment("right");
		drawIt();

		block.setHorizontalAlignment("center");
		drawIt();

		block.setVerticalAlignment("bottom");
		drawIt();

		block.setVerticalAlignment("middle");
	}

	public void testFonts() throws Exception
	{
		block.setWidth("200");
		block.setHeight("30");
		block.setBorderWidth(1);
		block.setVerticalAlignment("middle");
		block.setText("The lazy brown dog jumped over the quick red fox.");
		block.setFontFace("Courier");
		block.setFontSize(10);
		drawIt();

		block.setFontFace("Helvetica");
		block.setFontStyle("bold");
		drawIt();

		block.setFontFace("Monaco");
		block.setFontSize(20);
		block.setFontStyle("bold italic");
	}

	public void testPercentageDimensions() throws Exception
	{
		page.setHorizontalAlignment("center");
		page.setVerticalAlignment("middle");
		block.setBorderWidth(2);
		block.setBorderColor("blue");
		block.setWidth("25%");
		block.setHeight("25%");
		drawIt();

		block.setWidth("50%");
		block.setHeight("50%");
		drawIt();

		block.setWidth("75%");
		block.setHeight("75%");
	}
}
