package limelight.ui;

import limelight.LimelightException;

import java.util.LinkedList;
import java.awt.*;

public abstract class ParentPanel extends Panel
{
  protected LinkedList<Panel> children;
  private boolean sterilized;

  public ParentPanel()
  {
    super();
    children = new LinkedList<Panel>();
  }

  public void add(Panel panel) throws SterilePanelException
  {
    if (sterilized)
      throw new SterilePanelException(getBlock().getClassName());
    children.add(panel);
    panel.setParent(this);
  }

  public boolean hasChildren()
  {
    return children.size() > 0;
  }

  public LinkedList<Panel> getChildren()
  {
    return children;
  }

  public void sterilize()
  {
    sterilized = true;
  }

  public boolean isSterilized()
  {
    return sterilized;
  }

  public abstract Rectangle getChildConsumableArea();

  public void repaint()
  {
    getParent().repaint();
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

  public void replace(Panel existing, Panel replacement)
  {
    int index = children.indexOf(existing);
    if(index > 0)
    {
      children.remove(index);
      children.add(index, replacement);
    }
  }

  public void clearChildren()
  {
    children.clear();
    sterilized = false;
  }
}

class SterilePanelException extends LimelightException
{
  SterilePanelException(String name)
  {
    super("The panel for block named '" + name + "' has been sterilized. Child components may not be added.");
  }
}
