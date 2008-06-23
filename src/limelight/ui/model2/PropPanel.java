package limelight.ui.model2;

import limelight.ui.Painter;
import limelight.ui.PaintablePanel;
import limelight.ui.Panel;
import limelight.ui.api.PropablePanel;
import limelight.ui.api.Prop;
import limelight.ui.painting.BackgroundPainter;
import limelight.ui.painting.BorderPainter;
import limelight.ui.painting.Border;
import limelight.ui.painting.PaintAction;
import limelight.util.Box;
import limelight.LimelightError;
import limelight.styles.Style;
import java.util.LinkedList;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class PropPanel implements PropablePanel, PaintablePanel, Panel
{
  private int width;
  private Prop prop;
  private int height;
  private boolean sterilized;
  private LinkedList<Panel> children;
  private Panel parent;
  private PropPanelLayout layout;
  private int y;
  private int x;
  private Box absoluteBounds;
  private Point absoluteLocation;
  private LinkedList<Painter> painters;
  private Border borderShaper;
  private TextPaneTextAccessor textAccessor;

  public PropPanel(Prop prop)
  {
    this.prop = prop;
    buildPainters();
    children = new LinkedList<Panel>();
    layout = new PropPanelLayout(this);
    textAccessor = new TextPaneTextAccessor(this);
  }

  public Panel getOwnerOfPoint(Point point)
  {
    point = new Point(point.x - getX(), point.y - getY());
    for (Panel panel : children)
    {
      if(panel.containsRelativePoint(point))
        return panel.getOwnerOfPoint(point);
    }
    return this;
  }

  public boolean containsRelativePoint(Point point)
  {
    return point.x >= x &&
           point.x < x + width &&
           point.y >= y &&
           point.y < y + height;
  }

  private void buildPainters()
  {
    painters = new LinkedList<Painter>();
    painters.add(new BackgroundPainter(this));
    painters.add(new BorderPainter(this));
  }

  public void addChild(Panel panel) throws SterilePanelException
  {
    if (sterilized)
      throw new SterilePanelException(getProp().getName());
    children.add(panel);
    panel.setParent(this);
  }

  public void remove(Panel child)
  {
    children.remove(child);
  }

  public void removeAll()
  {
    children.clear();
  }

  public void add(Panel panel)
  {
    addChild(panel);
  }

  public void setParent(Panel panel)
  {
    parent = panel;
  }

  

  public String getText()
  {
    return textAccessor.getText();
  }

  public void setText(String text) throws LimelightError
  {
    textAccessor.setText(text);
  }

  public void snapToSize()
  {
    Box r = getParentPanel().getChildConsumableArea();
    setWidth(translateDimension(getProp().getStyle().getWidth(), r.width));
    setHeight(translateDimension(getProp().getStyle().getHeight(), r.height));
  }

  public void setHeight(int value)
  {
    height = value;
  }

  public void setWidth(int value)
  {
    width = value;
  }

  public int getWidth()
  {
    return width;
  }

  public int getHeight()
  {
    return height;
  }

  public void setLocation(int x, int y)
  {
    this.x = x;
    this.y = y;
  }

  private int translateDimension(String sizeString, int maxSize)
  {
    if (sizeString == null)
      return 0;
    else if ("auto".equals(sizeString))
      return maxSize;
    else if (sizeString.endsWith("%"))
    {
      double percentage = Double.parseDouble(sizeString.substring(0, sizeString.length() - 1));
      int result = (int) ((percentage * 0.01) * (double) maxSize);
      return result;
    }
    else
    {
      return Integer.parseInt(sizeString);
    }
  }

  public Prop getProp()
  {
    return prop;
  }

  public boolean hasChildren()
  {
    return children.size() > 0;
  }

  public Box getBox()
  {
    return getInternalBox();
  }

  public Box getInternalBox()
  {
    return new Box(0, 0, getWidth(), getHeight());
  }

  public Box getBoxInsideMargins()
  {
    Box r = getInternalBox();
    Style style = prop.getStyle();
    r.shave(style.asInt(style.getTopMargin()), style.asInt(style.getRightMargin()), style.asInt(style.getBottomMargin()), style.asInt(style.getLeftMargin()));
    return r;
  }

  public Box getBoxInsideBorders()
  {
    Box r = getBoxInsideMargins();
    Style style = prop.getStyle();
    r.shave(style.asInt(style.getTopBorderWidth()), style.asInt(style.getRightBorderWidth()), style.asInt(style.getBottomBorderWidth()), style.asInt(style.getLeftBorderWidth()));
    return r;
  }

  public Box getBoxInsidePadding()
  {
    Box r = getBoxInsideBorders();
    Style style = prop.getStyle();
    r.shave(style.asInt(style.getTopPadding()), style.asInt(style.getRightPadding()), style.asInt(style.getBottomPadding()), style.asInt(style.getLeftPadding()));
    return r;
  }

  public Box getChildConsumableArea()
  {
    return getBoxInsidePadding();
  }

  public LinkedList<Panel> getChildren()
  {
    return children;
  }

  public void doLayout()
  {
    layout.doLayout();
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

      Panel p = parent;
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

  public void paintOn(Graphics2D bufferGraphics)
  {
    for (Painter painter : painters)
      painter.paint(bufferGraphics);

  }

  public Panel getParentPanel()
  {
    return parent;
  }

  public Style getStyle()
  {
    return prop.getStyle();
  }

  public Border getBorderShaper()
  {
    if (borderShaper == null)
      borderShaper = new Border(getStyle(), getBoxInsideMargins());
    return borderShaper;
  }

  public void sterilize()
  {
    sterilized = true;
  }

  public Panel getRoot()
  {
    return getParentPanel().getRoot();
  }

  public static class SterilePanelException extends LimelightError
  {
    SterilePanelException(String name)
    {
      super("The panel for prop named '" + name + "' has been sterilized and child components may not be added.");
    }
  }

  public boolean isAncestor(Panel panel)
  {
    if(parent == null)
      return false;
    else if(parent == panel)
      return true;
    else
      return parent.isAncestor(panel);
  }

  public Panel getClosestCommonAncestor(Panel panel)
  {
    Panel ancestor = getParentPanel();
    while(ancestor != null && !panel.isAncestor(ancestor))
      ancestor = ancestor.getParentPanel();

    if(ancestor == null)
      throw new LimelightError("No common ancestor found! Do the panels belong to the same tree?");

    return ancestor;
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
    getProp().mouse_entered(e);
    getProp().hover_on();
  }

  public void mouseExited(MouseEvent e)
  {
    getProp().mouse_exited(e);
    getProp().hover_off();
  }

  public void mouseMoved(MouseEvent e)
  {
    getProp().mouse_moved(e);
  }

  public void mouseWheelMoved(MouseWheelEvent e)
  {
    getParentPanel().mouseWheelMoved(e);
  }

  public void setCursor(Cursor cursor)
  {
    getRoot().setCursor(cursor);
  }

  public void repaint()
  {
    if(getParentPanel() != null && (prop.getStyle().changed(Style.WIDTH) || prop.getStyle().changed(Style.WIDTH)))
      getParentPanel().repaint();
    else
    {
      doLayout();
      PaintJob job = new PaintJob(getAbsoluteBounds());
      job.paint(getRoot());
      job.applyTo(getRoot().getGraphics2D());
    }
  }

  public void paintImmediately(int a, int b, int c, int d)
  {
    repaint();
  }

  public Graphics2D getGraphics()
  {
    return getGraphics2D();
  }

  public Graphics2D getGraphics2D()
  {
    return getRoot().getGraphics2D();
  }

  public String toString()
  {
    return "Panel - " + getProp().getName();
  }

  public void setAfterPaintAction(PaintAction action)
  {
  }
}

