//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model;

import limelight.model.Stage;
import limelight.ui.Panel;
import limelight.ui.events.panel.IlluminatedEvent;
import limelight.ui.events.panel.PanelEventHandler;
import limelight.util.Box;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Iterator;

public abstract class PanelBase implements Panel
{
  protected int height;
  protected int width;
  private int x;
  private int y;
  private ParentPanelBase parent;
  protected Point absoluteLocation;
  private Box absoluteBounds;
  private Box boundingBox;
  protected boolean laidOut;  // TODO MDM This doesn't seem useful.  Delete me!
  private boolean illuminated;
  protected PanelEventHandler eventHandler;

  protected PanelBase()
  {
    width = 50;
    height = 50;
    eventHandler = new PanelEventHandler(this);
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
      int x = getX();
      int y = getY();

      if(parent != null)
      {
        Point absoluteParentLocation = parent.getAbsoluteLocation();
        x += absoluteParentLocation.x;
        y += absoluteParentLocation.y;
      }

      absoluteLocation = new Point(x, y);
    }
    return absoluteLocation;
  }

  public Box getBounds()
  {
    if(boundingBox == null)
      boundingBox = new Box(getX(), getY(), getWidth(), getHeight());
    return boundingBox;
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

  public ParentPanelBase getParent()
  {
    return parent;
  }

  public void setParent(ParentPanelBase newParent)
  {
    if(newParent != parent && isIlluminated())
      delluminate();

    parent = newParent;

    if(parent != null && parent.isIlluminated())
      illuminate();
  }

  public Scene getRoot()
  {
    if(parent == null)
      return null;
    return parent.getRoot();
  }

  public Stage getStage()
  {
    Scene root = getRoot();
    if(root == null)
      return null;
    return root.getStage();
  }

  public boolean isDescendantOf(Panel panel)
  {
    return parent != null && (parent == panel || parent.isDescendantOf(panel));
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


  public Layout getDefaultLayout()
  {
    return BasePanelLayout.instance;
  }

  public PanelEventHandler getEventHandler()
  {
    return eventHandler;
  }

  public Panel getOwnerOfPoint(Point point)
  {
    return this;
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
    markAsNeedingLayout();
  }

  public boolean canBeBuffered()
  {
//    return true;
    return false;  // Seems to be twice as fast without buffering.
  }

  public synchronized void markAsNeedingLayout(Layout layout)
  {
    final Scene root = getRoot();
    if(root != null)
      root.addPanelNeedingLayout(this, layout);
  }

  public void markAsNeedingLayout()
  {
    markAsNeedingLayout(getDefaultLayout());
  }

  //TODO This is a little inefficient.  Reconsider what gets passed to props.
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

  public static boolean needsLayout(Panel panel)
  {
    final Scene root = panel.getRoot();
    return root != null && root.hasPanelNeedingLayout(panel);
  }

  protected void doPropagateSizeChangeUp(Panel panel)
  {
    if(panel != null && !needsLayout(panel) && panel instanceof PanelBase)
    {
      panel.markAsNeedingLayout();
      // TODO MDM Do we need to propagate this size change down?
      doPropagateSizeChangeUp(panel.getParent());
    }
  }

  public void markAsDirty()
  {
    Scene rootPanel = getRoot();
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

  public boolean isIlluminated()
  {
    return illuminated;
  }

  public void illuminate()
  {
    illuminated = true;
    markAsNeedingLayout();
    new IlluminatedEvent(this).dispatch(this);
  }

  public void delluminate()
  {
    illuminated = false;
  }

  public boolean hasFocus()
  {
    return false;
  }
}
