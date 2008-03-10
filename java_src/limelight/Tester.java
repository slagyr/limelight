package limelight;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.LinkedList;
import java.util.Iterator;

public class Tester
{

  public static void main(String[] args)
  {
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setSize(600, 600);
    frame.setBackground(Color.gray);
    frame.setLayout(new LayoutManager() {

      public void addLayoutComponent(String name, Component comp)
      {
      }

      public void removeLayoutComponent(Component comp)
      {
      }

      public Dimension preferredLayoutSize(Container parent)
      {
        return null;
      }

      public Dimension minimumLayoutSize(Container parent)
      {
        return null;
      }

      public void layoutContainer(Container parent)
      {
        parent.getComponents()[0].setLocation(50, 50);
      }
    });

    JPanel panel = new JPanel() {
      protected void paintComponent(Graphics g)
      {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        g2.setStroke(new BasicStroke(10));

        int x = 100;
        int y = 100;
        int width = 200;
        int height = 200;
        int tlr = 10;
        int trr = 20;
        int brr = 30;
        int blr = 40;

        MyShape border = new MyShape(x, y, width, height, tlr, trr, brr, blr);

        g.setColor(Color.red);

        g2.draw(border.tl);
        g2.draw(border.tr);
        g2.draw(border.br);
        g2.draw(border.bl);
        g2.draw(border.topl);
        g2.draw(border.rightl);
        g2.draw(border.bottoml);
        g2.draw(border.leftl);

                g.setColor(Color.blue);
        g2.fill(border);
      }
    };
    panel.setBackground(Color.white);
    panel.setSize(500, 500);

    frame.add(panel);
    frame.setVisible(true);
  }

  static class MyShape implements Shape
  {
    private int x;
    private int y;
    private int width;
    private int height;
    private int tlr;
    private int trr;
    private int brr;
    private int blr;
    private int top;
    private int right;
    private int bottom;
    private int left;
    public Shape topl;
    public Shape rightl;
    public Shape bottoml;
    public Shape leftl;
    public Shape tl;
    public Shape tr;
    public Shape br;
    public Shape bl;
    public Rectangle bounds;


    public MyShape(int x, int y, int width, int height, int tlr, int trr, int brr, int blr)
    {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      this.tlr = tlr;
      this.trr = trr;
      this.brr = brr;
      this.blr = blr;
      top = y;
      right = x + width;
      bottom = y + height;
      left = x;
      topl = new Line2D.Float(left + tlr, top, right - trr, top);
      rightl = new Line2D.Float(right, top + trr, right, bottom - brr);
      bottoml = new Line2D.Float(right - brr, bottom, left + blr, bottom);
      leftl = new Line2D.Float(left, bottom - blr, left, top + tlr);
      tl = new Arc2D.Double(left, top, tlr*2, tlr*2, 180, -90, Arc2D.OPEN);
      tr = new Arc2D.Double(right - trr*2, top, trr*2, trr*2, 90, -90, Arc2D.OPEN);
      br = new Arc2D.Double(right - brr*2, bottom - brr*2, brr*2, brr*2, 0, -90, Arc2D.OPEN);
      bl = new Arc2D.Double(left, bottom - blr*2, blr*2, blr*2, 270, -90, Arc2D.OPEN);
      bounds = new Rectangle(x, y, width, height);
    }

    public Rectangle getBounds()
    {
      return bounds;
    }

    public Rectangle2D getBounds2D()
    {
      return bounds;
    }

    public boolean contains(double x, double y)
    {
      return bounds.contains(x, y);
    }

    public boolean contains(Point2D p)
    {
      return bounds.contains(p);
    }

    public boolean intersects(double x, double y, double w, double h)
    {
      return bounds.intersects(x, y, w, h);
    }

    public boolean intersects(Rectangle2D r)
    {
      return bounds.intersects(r);
    }

    public boolean contains(double x, double y, double w, double h)
    {
      return bounds.contains(x, y, w, h);
    }

    public boolean contains(Rectangle2D r)
    {
      return bounds.contains(r);
    }

    public PathIterator getPathIterator(AffineTransform at)
    {
      LinkedList<PathIterator> iterators = new LinkedList<PathIterator>();
      iterators.add(topl.getPathIterator(at));
      iterators.add(tr.getPathIterator(at));
      iterators.add(rightl.getPathIterator(at));
      iterators.add(br.getPathIterator(at));
      iterators.add(bottoml.getPathIterator(at));
      iterators.add(bl.getPathIterator(at));
      iterators.add(leftl.getPathIterator(at));
      iterators.add(tl.getPathIterator(at));
      return new MyPathIterator(iterators);
    }

    public PathIterator getPathIterator(AffineTransform at, double flatness)
    {
      return getPathIterator(at);
    }

    class MyPathIterator implements PathIterator
    {
      private LinkedList<PathIterator> iterators;
      private Iterator<PathIterator> iterator;
      private PathIterator pathIterator;
      private boolean done;

      public MyPathIterator(LinkedList<PathIterator> iterators)
      {
        this.iterators = iterators;
        iterator = iterators.iterator();
        pathIterator = iterator.next();
      }

      public int getWindingRule()
      {
        return PathIterator.WIND_EVEN_ODD;
      }

      public boolean isDone()
      {
        return done;
//        return pathIterator.isDone() && !iterator.hasNext();
      }
                       
      public void next()
      {
        pathIterator.next();
        if(pathIterator.isDone())
        {
          if(iterator.hasNext())
          {
System.out.println("Next Iterator");
            pathIterator = iterator.next();
            if(pathIterator.currentSegment(new float[6]) == PathIterator.SEG_MOVETO)
              pathIterator.next();
          }
          else
            done = true;
        }

//System.err.println("Next");
//        if(!pathIterator.isDone())
//        {
//System.err.println("Not done");
//          pathIterator.next();
////System.err.println("pathIterator.isDone() = " + pathIterator.isDone());
//        }
//        else
//        {
//System.err.println("new iterator");
//          pathIterator = iterator.next();
////          pathIterator.next();
//        }
      }

      public int currentSegment(float[] coords)
      {
        int i = pathIterator.currentSegment(coords);
        double[] newCoords = new double[6];
        for (int j = 0; j < newCoords.length; j++)
        {
          newCoords[j] = coords[j]; 
        }
        puts(i, newCoords);
        return i;
      }

      public int currentSegment(double[] coords)
      {
        int i = pathIterator.currentSegment(coords);
        puts(i, coords);
        return i;
      }

      private void puts(int i, double[] coords)
      {
        switch(i)
        {
          case PathIterator.SEG_CLOSE:
            System.out.print("SEG_CLOSE");
            break;
          case PathIterator.SEG_CUBICTO:
            System.out.print("SEG_CUBICTO");
            break;
          case PathIterator.SEG_LINETO:
            System.out.print("SEG_LINETO");
            break;
          case PathIterator.SEG_MOVETO:
            System.out.print("SEG_MOVETO");
            break;
          case PathIterator.SEG_QUADTO:
            System.out.print("SEG_QUADTO");
            break;
        }
        for (double coord : coords)
        {
          System.out.print(", " + coord);
        }
        System.out.println("");
      }
    }
  }

//  public static void main(String[] args)
//  {
//    JFrame frame = new JFrame();
//    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//    frame.setSize(500, 500);
//    frame.setBackground(Color.red);
//    BorderLayout layout = new BorderLayout();
//    frame.setLayout(layout);
//
//    JPanel view = new javax.swing.JPanel();
//    view.setBackground(Color.green);
//    view.setSize(190, 600);
//    view.setLayout(new GridLayout(3, 1));
//    view.add(new Button("1"));
//    view.add(new Button("2"));
//    view.add(new Button("3"));
//
//
//    JScrollPane scrollPane = new JScrollPane(view);
//    scrollPane.setSize(200, 200);
//    scrollPane.setBackground(Color.blue);
//    frame.add(scrollPane, BorderLayout.CENTER);
//
//    frame.setVisible(true);
//  }

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
