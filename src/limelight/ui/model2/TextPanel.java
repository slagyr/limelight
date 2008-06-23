package limelight.ui.model2;

import limelight.util.Colors;
import limelight.styles.Style;
import limelight.util.Util;
import limelight.util.Box;
import limelight.util.Aligner;
import limelight.ui.api.*;
import limelight.ui.Panel;
import limelight.ui.painting.Border;
import limelight.LimelightError;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedString;
import java.util.LinkedList;

public class TextPanel implements limelight.ui.Panel
{
  public static double widthPadding = 2.0; // The text measuerments aren't always quite right.  This helps.

  private String text;
  private PropPanel panel;
  private double consumedHeight;
  private double consumedWidth;
  private LinkedList<TextLayout> lines;
  private Graphics2D graphics;
  private boolean textChanged;
  private boolean compiled;
  private int width;
  private int height;
  private int x;
  private int y;
  private Point absoluteLocation;
  private Box absoluteBounds;

  //TODO MDM panel is not really needed here.  It's the same as parent.
  public TextPanel(PropPanel panel, String text)
  {
    this.panel = panel;
    this.text = text;
  }

  public String getText()
  {
    return text;
  }

  public void setText(String text)
  {
    if(!textChanged && !Util.equal(text, this.text))
      textChanged = true;
    this.text = text;
  }

  public Panel getPanel()
  {
    return panel;
  }

  public void paintOn(Graphics2D graphics)
  {   
    Aligner aligner = createAligner();
    graphics.setColor(Colors.resolve(getStyle().getTextColor()));
    float y = 0;
    if(lines == null)
      return;
    for (TextLayout textLayout : lines)
    {
      y += textLayout.getAscent();
      textLayout.draw(graphics, aligner.startingX(textLayout.getBounds().getWidth()), y);
      y += textLayout.getDescent() + textLayout.getLeading();
    }
  }

  public void doLayout()
  {
    if(!compiled || textChanged())
    {
      buildLines();
      calculateDimentions();
      compiled = true;
    }
    snapToSize();
  }

  public void snapToSize()
  {
    setWidth((int)(consumedWidth + 0.5));
    setHeight((int)(consumedHeight + 0.5));
  }

  private Aligner createAligner()
  {
    return new Aligner(new Box(0, 0, getWidth(), getHeight()), getStyle().getHorizontalAlignment(), getStyle().getVerticalAlignment());
  }

  public Style getStyle()
  {
    return panel.getStyle();
  }

  private void buildLines()
  {
    lines = new LinkedList<TextLayout>();
    if(text != null && text.length() > 0)
    {
      String[] paragraphs = text.split("\n");
      Font font = limelight.alt_ui.FontFactory.instance.createFont(getStyle());
      for (String paragraph : paragraphs)
      {
        if(paragraph.length() != 0)
        {
          AttributedString aText = new AttributedString(paragraph);
          aText.addAttribute(TextAttribute.FONT, font);
          LineBreakMeasurer lbm = new LineBreakMeasurer(aText.getIterator(), getGraphics2D().getFontRenderContext());
          while (lbm.getPosition() < paragraph.length())
          {
            //TODO MDM - Wow! This is inefficient. The getChildConsumableArea has to be calculated every time!
            float width1 = (float) panel.getChildConsumableArea().width;
            TextLayout layout = lbm.nextLayout(width1);
            lines.add(layout);
          }
        }
        else
          lines.add(new TextLayout(" ", font, graphics.getFontRenderContext()));
      }
    }
  }

  private void calculateDimentions()
  {
    consumedHeight = 0;
    consumedWidth = 0;
    for (TextLayout layout : lines)
    {
      consumedHeight += (layout.getAscent() + layout.getDescent() + layout.getLeading());
      double lineWidth = layout.getBounds().getWidth() + widthPadding;
      if(lineWidth > consumedWidth)
        consumedWidth = lineWidth;
    }
  }

  public void setGraphics(Graphics graphics)
  {
    this.graphics = (Graphics2D)graphics;
  }

  public boolean textChanged()
  {
    return textChanged;
  }

  public void flushChanges()
  {
    textChanged = false;
  }

  // Implemented only to satisfy the interface

  public void setParent(Panel panel)
  {
    this.panel = (PropPanel)panel;
  }

  public Box getChildConsumableArea()
  {
    return null;
  }

  public int getWidth()
  {
    return width;
  }

  public int getHeight()
  {
    return height;
  }

  public void setWidth(int value)
  {
    width = value;
  }

  public void setHeight(int value)
  {
    height = value;
  }

  public void setLocation(int x, int y)
  {
    this.x = x;
    this.y = y;
  }

  public Box getAbsoluteBounds()
  {
//    if(absoluteBounds == null)
//    {
      Point absoluteLocation = getAbsoluteLocation();
      absoluteBounds = new Box(absoluteLocation.x, absoluteLocation.y, getWidth(), getHeight());
//    }
    return absoluteBounds;
  }

  public Point getAbsoluteLocation()
  {
//    if(absoluteLocation == null)
//    {
      int x = this.x;
      int y = this.y;

      Panel p = panel;
      while(p != null)
      {
        x += p.getX();
        y += p.getY();
        p = p.getParentPanel();
      }
      absoluteLocation = new Point(x, y);
//    }
    return absoluteLocation;
  }

  public int getX()
  {
    return x;
  }

  public int getY()
  {
    return y;
  }

  public Panel getParentPanel()
  {
    return panel;
  }

  public Prop getProp()
  {
    return panel.getProp();
  }

  public boolean hasChildren()
  {
    return false;
  }

  public LinkedList<Panel> getChildren()
  {
    return new LinkedList<Panel>();
  }

  public Border getBorderShaper()
  {
    return null;
  }

  public Box getBoxInsideBorders()
  {
    return null;
  }

  public Panel getRoot()
  {
    return getParentPanel().getRoot();
  }

  public void addChild(Panel panel)
  {
    throw new RuntimeException("TextPanel.addChild()");
  }

  public boolean containsRelativePoint(Point point)
  {
    return true;
  }

  public Panel getOwnerOfPoint(Point point)
  {
    return this;
  }

  public void mousePressed(MouseEvent e)
  {
    getProp().mouse_pressed(e);
  }

  public void mouseReleased(MouseEvent e)
  {
    getProp().mouse_released(e);
  }

  public void mouseClicked(MouseEvent e)
  {
    getProp().mouse_clicked(e);
  }

  public void mouseDragged(MouseEvent e)
  {
    getProp().mouse_dragged(e);
  }

  public void mouseEntered(MouseEvent e)
  {
  }

  public void mouseExited(MouseEvent e)
  {
  }

  public void mouseMoved(MouseEvent e)
  {
    getProp().mouse_moved(e);
  }
  public void mouseWheelMoved(MouseWheelEvent e)
  {
  }

  public boolean isAncestor(Panel panel)
  {
    return this.panel.isAncestor(panel);
  }

  public Panel getClosestCommonAncestor(Panel panel)
  {
    Panel ancestor = getParentPanel();
    while(ancestor != null && !panel.isAncestor(ancestor))
    {
      ancestor = ancestor.getParentPanel();
    }

    if(ancestor == null)
      throw new LimelightError("No common ancestor found! Do the panels belong to the same tree?");

    return ancestor;
  }

  public void setCursor(Cursor cursor)
  {
    panel.setCursor(cursor);
  }

  public Graphics2D getGraphics2D()
  {
    return panel.getGraphics2D();
  }

  public void repaint()
  {
  }

  public String toString()
  {
    return "Text: <" + getText() + ">";
  }
}
