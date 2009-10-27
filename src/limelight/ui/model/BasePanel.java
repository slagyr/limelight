//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.styles.Style;
import limelight.ui.Panel;
import limelight.ui.model.inputs.ScrollBarPanel;
import limelight.util.Box;
import limelight.util.Debug;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public abstract class BasePanel implements Panel
{
  protected int height;
  protected int width;
  private int x;
  private int y;
  private Panel parent;
  protected Point absoluteLocation;
  private Box absoluteBounds;
  protected final LinkedList<Panel> children;
  private boolean sterilized;
  private Box boundingBox;
  private List<Panel> readonlyChildren;
  private Layout neededLayout = getDefaultLayout();
  protected boolean laidOut;

  protected BasePanel()
  {
    width = 50;
    height = 50;
    children = new LinkedList<Panel>();
  }

  public int getHeight()
  {
    return height;
  }

  public int getWidth()
  {
    return width;
  }

  public int getX()
  {
    return x;
  }

  public int getY()
  {
    return y;
  }

  public void setSize(int w, int h)
  {
    if(w != width || h != height)
    {
//      //TODO Test Me!
//      markAsDirty();

      clearCache();
      width = w;
      height = h;
    }
  }

  public void clearCache()
  {
    absoluteLocation = null;
    boundingBox = null;
    absoluteBounds = null;

    for(Panel child : getChildren())
      child.clearCache();
  }

  public void setLocation(int x, int y)
  {
    if(x != this.x || y != this.y)
      clearCache();
    this.x = x;
    this.y = y;
  }

  public Point getLocation()
  {
    return new Point(x, y);
  }

  public Point getAbsoluteLocation()
  {
    if(absoluteLocation == null)
    {
      int x = this.x;
      int y = this.y;

      Panel p = parent;
      while(p != null)
      {
        x += p.getX();
        y += p.getY();
        p = p.getParent();
      }
      absoluteLocation = new Point(x, y);
    }
    return absoluteLocation;
  }

  public Box getAbsoluteBounds()
  {
    if(absoluteBounds == null)
    {
      Point absoluteLocation = getAbsoluteLocation();
      absoluteBounds = new Box(absoluteLocation.x, absoluteLocation.y, getWidth(), getHeight());
    }
    return absoluteBounds;
  }

  public Panel getParent()
  {
    return parent;
  }

  public void setParent(Panel panel)
  {
    parent = panel;
  }

  public boolean containsRelativePoint(Point point)
  {
    return point.x >= x &&
        point.x < x + width &&
        point.y >= y &&
        point.y < y + height;
  }

  public boolean containsAbsolutePoint(Point point)
  {
    Point absoluteLocation = getAbsoluteLocation();
    return point.x >= absoluteLocation.x &&
        point.x < absoluteLocation.x + width &&
        point.y >= absoluteLocation.y &&
        point.y < absoluteLocation.y + height;
  }

  //TODO  MDM Change my return type to RootPanel
  public RootPanel getRoot()
  {
    if(parent == null)
      return null;
    return parent.getRoot();
  }

  public boolean isDescendantOf(Panel panel)
  {
    if(parent == null)
      return false;
    else if(parent == panel)
      return true;
    else
      return parent.isDescendantOf(panel);
  }

  public Panel getClosestCommonAncestor(Panel panel)
  {
    Panel ancestor = getParent();
    while(ancestor != null && !panel.isDescendantOf(ancestor))
      ancestor = ancestor.getParent();

    return ancestor;
  }

  public Graphics2D getGraphics()
  {
    Box bounds = getAbsoluteBounds();
    return (Graphics2D) getRoot().getGraphics().create(bounds.x, bounds.y, bounds.width, bounds.height);
  }

  public void doLayout()
  {
    Layout layout = neededLayout;
    if(layout != null)
    {
//      resetLayout();
      layout.doLayout(this);
    }
    else
      getDefaultLayout().doLayout(this);
  }

  // TODO MDM Calling this from the layout is error prone.  A Layout might forget to call.  Can easily solve by resetting when ever the panel is laid out (prior to layout)
  public synchronized void resetLayout()
  {
    neededLayout = null;
  }

  public Layout getDefaultLayout()
  {
    return BasePanelLayout.instance;
  }

  public void mousePressed(MouseEvent e)
  {
    parent.mousePressed(e);
  }

  public void mouseReleased(MouseEvent e)
  {
    parent.mouseReleased(e);
  }

  public void mouseClicked(MouseEvent e)
  {
    parent.mouseClicked(e);
  }

  public void mouseDragged(MouseEvent e)
  {
    parent.mouseDragged(e);
  }

  public void mouseEntered(MouseEvent e)
  {
  }

  public void mouseExited(MouseEvent e)
  {
  }

  public void mouseMoved(MouseEvent e)
  {
    parent.mouseMoved(e);
  }

  public void mouseWheelMoved(MouseWheelEvent e)
  {
    parent.mouseWheelMoved(e);
  }

  public void focusLost(FocusEvent e)
  {
    parent.focusLost(e);
  }

  public void focusGained(FocusEvent e)
  {
    parent.focusGained(e);
  }

  public void keyTyped(KeyEvent e)
  {
    parent.keyTyped(e);
  }

  public void keyPressed(KeyEvent e)
  {
    parent.keyPressed(e);
  }

  public void keyReleased(KeyEvent e)
  {
    parent.keyReleased(e);
  }

  public void buttonPressed(ActionEvent e)
  {
    parent.buttonPressed(e);
  }

  public void valueChanged(Object e)
  {
    parent.valueChanged(e);
  }

  public void add(Panel panel)
  {
    add(-1, panel);
  }

  public void add(int index, Panel child)
  {
    if(sterilized && !(child instanceof ScrollBarPanel))
      throw new SterilePanelException("Unknown name");

    synchronized(children)
    {
      if(index == -1)
        children.add(child);
      else
        children.add(index, child);
    }
    readonlyChildren = null;

    child.setParent(this);
    propagateSizeChangeUp(this);
    markAsNeedingLayout();
  }

  public boolean hasChildren()
  {
    return children.size() > 0;
  }

  public List<Panel> getChildren()
  {
    if(readonlyChildren == null)
    {
      synchronized(children)
      {
        readonlyChildren = Collections.unmodifiableList(new ArrayList<Panel>(children));
      }
    }
    return readonlyChildren;
  }

  public void sterilize()
  {
    sterilized = true;
  }

  public boolean isSterilized()
  {
    return sterilized;
  }

  public void repaint()
  {
    getParent().repaint();
  }

  public Panel getOwnerOfPoint(Point point)
  {
    point = new Point(point.x - getX(), point.y - getY());
    if(children.size() > 0)
    {
      synchronized(children)
      {
        for(ListIterator<Panel> iterator = children.listIterator(children.size()); iterator.hasPrevious();)
        {
          Panel panel = iterator.previous();
          if(panel.isFloater() && panel.containsRelativePoint(point))
            return panel.getOwnerOfPoint(point);
        }
        for(Panel panel : children)
        {
          if(!panel.isFloater() && panel.containsRelativePoint(point))
            return panel.getOwnerOfPoint(point);
        }
      }
    }
    return this;
  }

  public boolean isChild(Panel child)
  {
    return children.contains(child);
  }

  public boolean remove(Panel child)
  {
    boolean removed = false;
    synchronized(children)
    {
      removed = children.remove(child);
    }
    if(removed)
    {
      child.setParent(null);
      readonlyChildren = null;
      propagateSizeChangeUp(this);
      markAsNeedingLayout();
      return true;
    }
    return false;
  }

  public void removeAll()
  {
    if(children.size() > 0)
    {
      synchronized(children)
      {
        for(Iterator<Panel> iterator = children.iterator(); iterator.hasNext();)
        {
          Panel child = iterator.next();
          if(canRemove(child))
          {
            child.setParent(null);
            iterator.remove();
          }
        }
      }
      readonlyChildren = null;
      sterilized = false;
      propagateSizeChangeUp(this);
      markAsNeedingLayout();
    }
  }

  protected boolean canRemove(Panel child)
  {
    return true;
  }

  public Box getBoundingBox()
  {
    if(boundingBox == null)
      boundingBox = new Box(0, 0, getWidth(), getHeight());
    return boundingBox;
  }

  public boolean isFloater()
  {
    return false;
  }

  public void doFloatLayout()
  {
    // Panels are not floaters by default.
  }

  public void consumableAreaChanged()
  {
    Style style = getStyle();    
    if(!needsLayout() && style != null && style.hasDynamicDimension())
    {
      markAsNeedingLayout();
      synchronized(children)
      {
        for(Panel child : children)
          child.consumableAreaChanged();
      }
    }
  }

  public boolean canBeBuffered()
  {
//    return true;
    return false;  // Seems to be twice as fast without buffering.
  }

  public synchronized void markAsNeedingLayout(Layout layout)
  {
    if(getRoot() != null)
    {
      if(neededLayout == null)
      {
        neededLayout = layout; // Set first... race conditions otherwise.
        getRoot().addPanelNeedingLayout(this);
      }
      else if(layout.overides(neededLayout))
        neededLayout = layout;
    }
  }

  public void markAsNeedingLayout()
  {
    markAsNeedingLayout(getDefaultLayout());
  }

  public boolean needsLayout()
  {
    return neededLayout != null;
  }

  //TODO This is a little inefficient.  Reconsider what get's passed to props.
  protected MouseEvent translatedEvent(MouseEvent e)
  {
    e = new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiers(), e.getX(), e.getY(), e.getClickCount(), false);
    Point absoluteLocation = getAbsoluteLocation();
    e.translatePoint(absoluteLocation.x * -1, absoluteLocation.y * -1);
    return e;
  }

  public Iterator<Panel> iterator()
  {
    return new PanelIterator(this);
  }

  protected void propagateSizeChangeUp(Panel panel)
  {
    if(panel != null && !panel.needsLayout() && panel instanceof BasePanel)
    {
//      Style style = panel.getStyle();
//      if(style != null && style.hasAutoDimension())
//      {     
      panel.markAsNeedingLayout();
      propagateSizeChangeUp(panel.getParent());
//      }
    }
  }

  public void markAsDirty()
  {
    RootPanel rootPanel = getRoot();
    if(rootPanel != null)
      rootPanel.addDirtyRegion(getAbsoluteBounds());
  }

  public boolean isLaidOut()
  {
    return laidOut;
  }

  public void wasLaidOut()
  {
    laidOut = true;
  }

  protected void propagateSizeChangeDown()
  {
    synchronized(children)
    {
      for(Panel child : children)
        child.consumableAreaChanged();
    }
  }
}
