package limelight.ui.model2;

import limelight.util.Box;
import limelight.ui.Panel;
import limelight.ui.api.Prop;
import limelight.ui.painting.Border;
import limelight.styles.Style;
import java.util.LinkedList;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class RootPanel implements limelight.ui.Panel
{
  private Panel panel;
  private Frame frame;
  private Container contentPane;
  private EventListener listener;

  public RootPanel(Frame frame)
  {
    this.frame = frame;
    contentPane = frame.getContentPane();
  }

  public Box getChildConsumableArea()
  {
    return new Box(getX(), getY(), contentPane.getWidth(), contentPane.getHeight());
  }

  public void doLayout()
  {
    panel.doLayout();
  }

  public int getWidth()
  {
    return contentPane.getWidth();
  }

  public int getHeight()
  {
    return contentPane.getHeight();
  }

  public void setWidth(int value)
  {
    contentPane.setSize(value, getHeight());
  }

  public void setHeight(int value)
  {
    contentPane.setSize(value, getHeight());
  }

  public void setLocation(int x, int y)
  {
    //ignore
  }

  public Box getAbsoluteBounds()
  {
    return new Box(getX(), getY(), contentPane.getWidth(), contentPane.getHeight());
  }

  public Point getAbsoluteLocation()
  {
    return contentPane.getLocation();
  }

  public int getX()
  {
    return getAbsoluteLocation().x;
  }

  public int getY()
  {
    return getAbsoluteLocation().y;
  }

  public Panel getParent()
  {
    return null;
  }

  public void paintOn(Graphics2D graphics)
  {
  }

  public Prop getProp()
  {
    return panel.getProp();
  }

  public boolean hasChildren()
  {
    return true;
  }

  public LinkedList<Panel> getChildren()
  {
    LinkedList<Panel> panels = new LinkedList<Panel>();
    panels.add(panel);
    return panels;
  }

  public void setPanel(Panel child)
  {
    this.panel = child;
    child.setParent(this);

    listener = new EventListener(panel);
    contentPane.addMouseListener(listener);
    contentPane.addMouseMotionListener(listener);
    contentPane.addMouseWheelListener(listener);
    contentPane.addKeyListener(listener);
  }

  public void setParent(Panel panel)
  {
  }

  public Panel getParentPanel()
  {
    return null;
  }

  public Style getStyle()
  {
    throw new RuntimeException("RootPanel.getStyle()");
  }

  public Border getBorderShaper()
  {
    throw new RuntimeException("RootPanel.getBorderShaper()");
  }

  public Box getBoxInsideBorders()
  {
    throw new RuntimeException("RootPanel.getBoxInsideBorders()");
  }

  public Panel getRoot()
  {
    return this;
  }

  public Graphics2D getGraphics2D()
  {
    return (Graphics2D)contentPane.getGraphics();
  }

  public void addChild(Panel panel)
  {
    throw new RuntimeException("RootPanel.addChild()");
  }

  public boolean containsRelativePoint(Point point)
  {
    return true;
  }

  public Panel getOwnerOfPoint(Point point)
  {
    point = new Point(point.x - getX(), point.y - getY());
    if(panel.containsRelativePoint(point))
      return panel.getOwnerOfPoint(point);
    return this;
  }

  public void mousePressed(MouseEvent e)
  {
  }

  public void mouseReleased(MouseEvent e)
  {
  }

  public void mouseClicked(MouseEvent e)
  {
  }

  public void mouseDragged(MouseEvent e)
  {
  }

  public void mouseEntered(MouseEvent e)
  {
  }

  public void mouseExited(MouseEvent e)
  {
  }

  public void mouseMoved(MouseEvent e)
  {
  }

  public void mouseWheelMoved(MouseWheelEvent e)
  {
  }

  public boolean isAncestor(Panel panel)
  {
    return panel == this;
  }

  public Panel getClosestCommonAncestor(Panel panel)
  {
    return this;
  }

  public void setCursor(Cursor cursor)
  {
    contentPane.setCursor(cursor);
  }

  public void repaint()
  {
      doLayout();
      PaintJob job = new PaintJob(getAbsoluteBounds());
      job.paint(panel);
      job.applyTo(getGraphics2D());
  }
}
