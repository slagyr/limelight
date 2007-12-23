//package limelight;
//
//import javax.swing.*;
//import javax.swing.text.JTextComponent;
//import javax.swing.border.Border;
//import java.awt.*;
//import java.util.Hashtable;
//
//public class Tester
//{
//	public static void main(String[] args)
//	{
//		int num = 1000;
//		int reps = 1000;
//
//		Block[] blocks = new Block[num];
//		Hashtable[] tables = new Hashtable[num];
//		FlatStyle[] styles = new FlatStyle[num];
//
//		for(int i = 0; i < num; i++)
//		{
//			blocks[i] = new Block();
//			tables[i] = new Hashtable();
//			styles[i] = new FlatStyle();
//		}
//
//		double then, now, duration;
//
//		// TABLE
//		then = System.currentTimeMillis();
//		for(int i = 0; i < tables.length; i++)
//		{
//			Hashtable table = tables[i];
//			for(int j = 0; j < reps; j++)
//				loadTable(table);
//		}
//		now = System.currentTimeMillis();
//		duration = now - then;
//		System.out.println("Loading tables: " + duration);
//
//		then = System.currentTimeMillis();
//		for(int i = 0; i < tables.length; i++)
//		{
//			Hashtable table = tables[i];
//			for(int j = 0; j < reps; j++)
//				unloadTable(table);
//		}
//		now = System.currentTimeMillis();
//		duration = now - then;
//		System.out.println("Unloading tables: " + duration);
//
//
//		//STYLE
//		then = System.currentTimeMillis();
//		for(int i = 0; i < styles.length; i++)
//		{
//			FlatStyle style = styles[i];
//			for(int j = 0; j < reps; j++)
//				loadStyle(style);
//		}
//		now = System.currentTimeMillis();
//		duration = now - then;
//		System.out.println("Loading tables: " + duration);
//
//		then = System.currentTimeMillis();
//		for(int i = 0; i < styles.length; i++)
//		{
//			FlatStyle style = styles[i];
//			for(int j = 0; j < reps; j++)
//				unloadStyle(style);
//		}
//		now = System.currentTimeMillis();
//		duration = now - then;
//		System.out.println("Unloading tables: " + duration);
//
//
//		//BLOCK
//		then = System.currentTimeMillis();
//		for(int i = 0; i < blocks.length; i++)
//		{
//			Block block = blocks[i];
//			for(int j = 0; j < reps; j++)
//				loadBlock(block);
//		}
//		now = System.currentTimeMillis();
//		duration = now - then;
//		System.out.println("Loading blocks: " + duration);
//
//		then = System.currentTimeMillis();
//		for(int i = 0; i < blocks.length; i++)
//		{
//			Block block = blocks[i];
//			for(int j = 0; j < reps; j++)
//				unloadBlock(block);
//		}
//		now = System.currentTimeMillis();
//		duration = now - then;
//		System.out.println("Unloading blocks: " + duration);
//
//
//	}
//
//	public static void unloadBlock(Block block)
//	{
//		block.getStyle().getWidth();
//		block.getStyle().getHeight();
//		block.getStyle().getTopBorderColor();
//		block.getStyle().getRightBorderColor();
//		block.getStyle().getBottomBorderColor();
//		block.getStyle().getLeftBorderColor();
//		block.getStyle().getTopBorderWidth();
//		block.getStyle().getRightBorderWidth();
//		block.getStyle().getBottomBorderWidth();
//		block.getStyle().getLeftBorderWidth();
//		block.getStyle().getTopMargin();
//		block.getStyle().getRightMargin();
//		block.getStyle().getBottomMargin();
//		block.getStyle().getLeftMargin();
//		block.getStyle().getTopPadding();
//		block.getStyle().getRightPadding();
//		block.getStyle().getBottomPadding();
//		block.getStyle().getLeftPadding();
//		block.getStyle().getBackgroundColor();
//		block.getStyle().getBackgroundImage();
//		block.getStyle().getBackgroundImageFillStrategy();
//		block.getStyle().getHorizontalAlignment();
//		block.getStyle().getVerticalAlignment();
//		block.getStyle().getTextColor();
//		block.getStyle().getFontFace();
//		block.getStyle().getFontSize();
//		block.getStyle().getFontStyle();
//	}
//
//	public static void unloadTable(Hashtable table)
//	{
//		table.get("Width");
//		table.get("Height");
//		table.get("TopBorderColor");
//		table.get("RightBorderColor");
//		table.get("BottomBorderColor");
//		table.get("LeftBorderColor");
//		table.get("TopBorderWidth");
//		table.get("RightBorderWidth");
//		table.get("BottomBorderWidth");
//		table.get("LeftBorderWidth");
//		table.get("TopMargin");
//		table.get("RightMargin");
//		table.get("BottomMargin");
//		table.get("LeftMargin");
//		table.get("TopPadding");
//		table.get("RightPadding");
//		table.get("BottomPadding");
//		table.get("LeftPadding");
//		table.get("BackgroundColor");
//		table.get("BackgroundImage");
//		table.get("BackgroundImageFillStrategy");
//		table.get("HorizontalAlignment");
//		table.get("VerticalAlignment");
//		table.get("TextColor");
//		table.get("FontFace");
//		table.get("FontSize");
//		table.get("FontStyle");
//	}
//
//	public static void loadBlock(Block block)
//	{
//		block.getStyle().setWidth("123");
//		block.getStyle().setHeight("123");
//		block.getStyle().setTopBorderColor("red");
//		block.getStyle().setRightBorderColor("red");
//		block.getStyle().setBottomBorderColor("red");
//		block.getStyle().setLeftBorderColor("red");
//		block.getStyle().setTopBorderWidth("123");
//		block.getStyle().setRightBorderWidth("123");
//		block.getStyle().setBottomBorderWidth("123");
//		block.getStyle().setLeftBorderWidth("123");
//		block.getStyle().setTopMargin("123");
//		block.getStyle().setRightMargin("123");
//		block.getStyle().setBottomMargin("123");
//		block.getStyle().setLeftMargin("123");
//		block.getStyle().setTopPadding("123");
//		block.getStyle().setRightPadding("123");
//		block.getStyle().setBottomPadding("123");
//		block.getStyle().setLeftPadding("123");
//		block.getStyle().setBackgroundColor("red");
//		block.getStyle().setBackgroundImage("blah.png");
//		block.getStyle().setBackgroundImageFillStrategy("static");
//		block.getStyle().setHorizontalAlignment("left");
//		block.getStyle().setVerticalAlignment("left");
//		block.getStyle().setTextColor("red");
//		block.getStyle().setFontFace("anything");
//		block.getStyle().setFontSize("123");
//		block.getStyle().setFontStyle("bold");
//
//	}
//
//	public static void loadTable(Hashtable table)
//	{
//		table.put("Width", "123");
//		table.put("Height", "123");
//		table.put("TopBorderColor", "red");
//		table.put("RightBorderColor", "red");
//		table.put("BottomBorderColor", "red");
//		table.put("LeftBorderColor", "red");
//		table.put("TopBorderWidth", 123);
//		table.put("RightBorderWidth", 123);
//		table.put("BottomBorderWidth", 123);
//		table.put("LeftBorderWidth", 123);
//		table.put("TopMargin", 123);
//		table.put("RightMargin", 123);
//		table.put("BottomMargin", 123);
//		table.put("LeftMargin", 123);
//		table.put("TopPadding", 123);
//		table.put("RightPadding", 123);
//		table.put("BottomPadding", 123);
//		table.put("LeftPadding", 123);
//		table.put("BackgroundColor", Color.red);
//		table.put("BackgroundImage", "blah.png");
//		table.put("BackgroundImageFillStrategy", "static");
//		table.put("HorizontalAlignment", "left");
//		table.put("VerticalAlignment", "left");
//		table.put("TextColor", "red");
//		table.put("FontFace", "anything");
//		table.put("FontSize", 123);
//		table.put("FontStyle", "bold");
//	}
//
//	public static void loadStyle(FlatStyle style)
//	{
//		style.setWidth("123");
//		style.setHeight("123");
//		style.setTopBorderColor("red");
//		style.setRightBorderColor("red");
//		style.setBottomBorderColor("red");
//		style.setLeftBorderColor("red");
//		style.setTopBorderWidth("123");
//		style.setRightBorderWidth("123");
//		style.setBottomBorderWidth("123");
//		style.setLeftBorderWidth("123");
//		style.setTopMargin("123");
//		style.setRightMargin("123");
//		style.setBottomMargin("123");
//		style.setLeftMargin("123");
//		style.setTopPadding("123");
//		style.setRightPadding("123");
//		style.setBottomPadding("123");
//		style.setLeftPadding("123");
//		style.setBackgroundColor("red");
//		style.setBackgroundImage("blah.png");
//		style.setBackgroundImageFillStrategy("static");
//		style.setHorizontalAlignment("left");
//		style.setVerticalAlignment("left");
//		style.setTextColor("red");
//		style.setFontFace("anything");
//		style.setFontSize("123");
//		style.setFontStyle("bold");
//	}
//
//public static void unloadStyle(FlatStyle style)
//	{
//		style.getWidth();
//		style.getHeight();
//		style.getTopBorderColor();
//		style.getRightBorderColor();
//		style.getBottomBorderColor();
//		style.getLeftBorderColor();
//		style.getTopBorderWidth();
//		style.getRightBorderWidth();
//		style.getBottomBorderWidth();
//		style.getLeftBorderWidth();
//		style.getTopMargin();
//		style.getRightMargin();
//		style.getBottomMargin();
//		style.getLeftMargin();
//		style.getTopPadding();
//		style.getRightPadding();
//		style.getBottomPadding();
//		style.getLeftPadding();
//		style.getBackgroundColor();
//		style.getBackgroundImage();
//		style.getBackgroundImageFillStrategy();
//		style.getHorizontalAlignment();
//		style.getVerticalAlignment();
//		style.getTextColor();
//		style.getFontFace();
//		style.getFontSize();
//		style.getFontStyle();
//	}
//}
//
//
