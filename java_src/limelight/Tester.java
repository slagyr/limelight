package limelight;

import javax.swing.*;
import java.awt.*;

public class Tester
{

  public static void main(String[] args)
  {
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setSize(500, 500);
    frame.setBackground(Color.red);
    BorderLayout layout = new BorderLayout();
    frame.setLayout(layout);

    JPanel view = new javax.swing.JPanel();
    view.setBackground(Color.green);
    view.setSize(190, 600);
    view.setLayout(new GridLayout(3, 1));
    view.add(new Button("1"));
    view.add(new Button("2"));
    view.add(new Button("3"));

    
    JScrollPane scrollPane = new JScrollPane(view);
    scrollPane.setSize(200, 200);
    scrollPane.setBackground(Color.blue);
    frame.add(scrollPane, BorderLayout.CENTER);

    frame.setVisible(true);
  }

//  public static void main(String[] args)
//  {
//    JFrame frame = new JFrame();
//    frame.setSize(200, 50);
//    JTextField input = new JTextField();
//    frame.getContentPane().add(input);
//    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//    frame.setVisible(true);
//  }
}


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
//		Prop[] props = new Prop[num];
//		Hashtable[] tables = new Hashtable[num];
//		FlatStyle[] styles = new FlatStyle[num];
//
//		for(int i = 0; i < num; i++)
//		{
//			props[i] = new Prop();
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
//		for(int i = 0; i < props.length; i++)
//		{
//			Prop prop = props[i];
//			for(int j = 0; j < reps; j++)
//				loadProp(prop);
//		}
//		now = System.currentTimeMillis();
//		duration = now - then;
//		System.out.println("Loading props: " + duration);
//
//		then = System.currentTimeMillis();
//		for(int i = 0; i < props.length; i++)
//		{
//			Prop prop = props[i];
//			for(int j = 0; j < reps; j++)
//				unloadProp(prop);
//		}
//		now = System.currentTimeMillis();
//		duration = now - then;
//		System.out.println("Unloading props: " + duration);
//
//
//	}
//
//	public static void unloadProp(Prop prop)
//	{
//		prop.getStyle().getWidth();
//		prop.getStyle().getHeight();
//		prop.getStyle().getTopBorderColor();
//		prop.getStyle().getRightBorderColor();
//		prop.getStyle().getBottomBorderColor();
//		prop.getStyle().getLeftBorderColor();
//		prop.getStyle().getTopBorderWidth();
//		prop.getStyle().getRightBorderWidth();
//		prop.getStyle().getBottomBorderWidth();
//		prop.getStyle().getLeftBorderWidth();
//		prop.getStyle().getTopMargin();
//		prop.getStyle().getRightMargin();
//		prop.getStyle().getBottomMargin();
//		prop.getStyle().getLeftMargin();
//		prop.getStyle().getTopPadding();
//		prop.getStyle().getRightPadding();
//		prop.getStyle().getBottomPadding();
//		prop.getStyle().getLeftPadding();
//		prop.getStyle().getBackgroundColor();
//		prop.getStyle().getBackgroundImage();
//		prop.getStyle().getBackgroundImageFillStrategy();
//		prop.getStyle().getHorizontalAlignment();
//		prop.getStyle().getVerticalAlignment();
//		prop.getStyle().getTextColor();
//		prop.getStyle().getFontFace();
//		prop.getStyle().getFontSize();
//		prop.getStyle().getFontStyle();
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
//	public static void loadProp(Prop prop)
//	{
//		prop.getStyle().setWidth("123");
//		prop.getStyle().setHeight("123");
//		prop.getStyle().setTopBorderColor("red");
//		prop.getStyle().setRightBorderColor("red");
//		prop.getStyle().setBottomBorderColor("red");
//		prop.getStyle().setLeftBorderColor("red");
//		prop.getStyle().setTopBorderWidth("123");
//		prop.getStyle().setRightBorderWidth("123");
//		prop.getStyle().setBottomBorderWidth("123");
//		prop.getStyle().setLeftBorderWidth("123");
//		prop.getStyle().setTopMargin("123");
//		prop.getStyle().setRightMargin("123");
//		prop.getStyle().setBottomMargin("123");
//		prop.getStyle().setLeftMargin("123");
//		prop.getStyle().setTopPadding("123");
//		prop.getStyle().setRightPadding("123");
//		prop.getStyle().setBottomPadding("123");
//		prop.getStyle().setLeftPadding("123");
//		prop.getStyle().setBackgroundColor("red");
//		prop.getStyle().setBackgroundImage("blah.png");
//		prop.getStyle().setBackgroundImageFillStrategy("static");
//		prop.getStyle().setHorizontalAlignment("left");
//		prop.getStyle().setVerticalAlignment("left");
//		prop.getStyle().setTextColor("red");
//		prop.getStyle().setFontFace("anything");
//		prop.getStyle().setFontSize("123");
//		prop.getStyle().setFontStyle("bold");
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
