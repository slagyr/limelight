//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.ui.Panel;
import limelight.ui.model.inputs.ScrollBarPanel;
import limelight.util.Box;

import java.awt.*;
import java.util.*;
import java.util.List;

public abstract class ParentPanelBase extends PanelBase implements ParentPanel
{
  protected final LinkedList<limelight.ui.Panel> children;
  private List<limelight.ui.Panel> readonlyChildren;
  private boolean sterilized;

  public ParentPanelBase()
  {
    children = new LinkedList<limelight.ui.Panel>();
  }

  @Override
  public void clearCache()
  {
    super.clearCache();
    for(limelight.ui.Panel child : getChildren())
      child.clearCache();
  }

  @Override
  public void consumableAreaChanged()
  {
    markAsNeedingLayout();
    synchronized(children)
    {
      for(Panel child : children)
        child.consumableAreaChanged();
    }
  }

  @Override
  public Panel getOwnerOfPoint(Point point)
  {
    if(children.size() > 0)
    {
      synchronized(children)
      {
        for(ListIterator<limelight.ui.Panel> iterator = children.listIterator(children.size()); iterator.hasPrevious();)
        {
          limelight.ui.Panel panel = iterator.previous();
          if(panel.isFloater() && panel.getAbsoluteBounds().contains(point))
            return panel.getOwnerOfPoint(point);
        }
        for(limelight.ui.Panel panel : children)
        {
          if(!panel.isFloater() && panel.getAbsoluteBounds().contains(point))
            return panel.getOwnerOfPoint(point);
        }
      }
    }
    return this;
  }

  public void add(limelight.ui.Panel panel)
  {
    add(-1, panel);
  }

  public void add(int index, limelight.ui.Panel child)
  {
    if(sterilized && !(child instanceof ScrollBarPanel))
      throw new SterilePanelException("Unknown name");

    synchronized(children)
    {
      if(index == -1)
        children.add(child);
      else
        children.add(index, child);
      readonlyChildren = null;
    }

    child.setParent(this);
    doPropagateSizeChangeUp(this);
    markAsNeedingLayout();
  }

  public boolean hasChildren()
  {
    return children.size() > 0;
  }

  public List<limelight.ui.Panel> getChildren()
  {
    if(readonlyChildren == null)
    {
      synchronized(children)
      {
        readonlyChildren = Collections.unmodifiableList(new ArrayList<limelight.ui.Panel>(children));
        return readonlyChildren;
      }
    }
    else
      return readonlyChildren;
  }

  public boolean hasChild(limelight.ui.Panel child)
  {
    return children.contains(child);
  }

  public boolean remove(limelight.ui.Panel child)
  {
    boolean removed = false;
    synchronized(children)
    {
      removed = children.remove(child);
      readonlyChildren = null;
    }
    if(removed)
    {
      child.setParent(null);
      doPropagateSizeChangeUp(this);
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
        for(Iterator<limelight.ui.Panel> iterator = children.iterator(); iterator.hasNext();)
        {
          limelight.ui.Panel child = iterator.next();
          if(canRemove(child))
          {
            child.setParent(null);
            iterator.remove();
          }
        }
        readonlyChildren = null;
      }
      sterilized = false;
      doPropagateSizeChangeUp(this);
      markAsNeedingLayout();
    }
  }

  protected boolean canRemove(limelight.ui.Panel child)
  {
    return true;
  }

  public void sterilize()
  {
    sterilized = true;
  }


  public boolean isSterilized()
  {
    return sterilized;
  }

  protected void doPropagateSizeChangeDown()
  {
    synchronized(children)
    {
      for(Panel child : children)
        child.consumableAreaChanged();
    }
  }

  public void illuminate()
  {
    super.illuminate();
    for(Panel child : children)
      child.illuminate();
  }

  public void delluminate()
  {
    super.delluminate();
    for(Panel child : children)
      child.delluminate();
  }

  public abstract Box getChildConsumableBounds();
}
