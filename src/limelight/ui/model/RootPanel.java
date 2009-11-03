//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.Context;
import limelight.ResourceLoader;
import limelight.styles.Style;
import limelight.ui.Panel;
import limelight.ui.api.PropablePanel;
import limelight.ui.api.Prop;
import limelight.util.Box;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class RootPanel implements Panel
{
  private Panel panel;
  private final Container contentPane;
  private EventListener listener;
  private boolean alive;
  private final PropFrame frame;
  private final ArrayList<Panel> panelsNeedingLayout = new ArrayList<Panel>(50);
  private final ArrayList<Rectangle> dirtyRegions = new ArrayList<Rectangle>(50);
  private ImageCache imageCache;

  public RootPanel(PropFrame frame)
  {
    this.frame = frame;
    contentPane = frame.getContentPane();
  }

  public Container getContentPane()
  {
    return contentPane;
  }

  public Box getChildConsumableArea()
  {
    Box box = new Box(getX(), getY(), contentPane.getWidth(), contentPane.getHeight());    
    return box;
  }

  public Box getBoxInsidePadding()
  {
    return getChildConsumableArea();
  }

  public int getWidth()
  {
    return contentPane.getWidth();
  }

  public int getHeight()
  {
    return contentPane.getHeight();
  }

  public void setLocation(int x, int y)
  {
    contentPane.setLocation(x, y);
  }

  public Point getLocation()
  {
    return contentPane.getLocation();
  }

  public void setSize(int width, int height)
  {
    contentPane.setSize(width, height);  
  }

  public Box getAbsoluteBounds()
  {
    Rectangle bounds = contentPane.getBounds();
    return new Box(0, 0, bounds.width, bounds.height);
  }

  public Point getAbsoluteLocation()
  {
    return new Point(0, 0);
  }

  public int getX()
  {
    return getAbsoluteLocation().x;
  }

  public int getY()
  {
    return getAbsoluteLocation().y;
  }

  public void setPanel(Panel child)
  {
    panel = child;
    panel.setParent(this);

    listener = new EventListener(panel);
    contentPane.addMouseListener(listener);
    contentPane.addMouseMotionListener(listener);
    contentPane.addMouseWheelListener(listener);
    contentPane.addKeyListener(listener);
    alive = true;
    addPanelNeedingLayout(child);
  }

  public void destroy()
  {
    removeKeyboardFocus();                                                                                                     
    contentPane.removeMouseListener(listener);
    contentPane.removeMouseMotionListener(listener);
    contentPane.removeMouseWheelListener(listener);
    contentPane.removeKeyListener(listener);
    listener = null;
    panel.setParent(null);
    alive = false;
  }

  private void removeKeyboardFocus()
  {
    Panel focuedPanel = Context.instance().keyboardFocusManager.getFocusedPanel();
    if(focuedPanel != null && focuedPanel.getRoot() == this)
      Context.instance().keyboardFocusManager.unfocusCurrentlyFocusedComponent();
  }

  public RootPanel getRoot()
  {
    return this;
  }

  public Graphics2D getGraphics()
  {
    return (Graphics2D) contentPane.getGraphics();
  }

  public boolean isDescendantOf(Panel panel)
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

  public Panel getPanel()
  {
    return panel;
  }

  public boolean isAlive()
  {
    return alive;
  }

  public EventListener getListener()
  {
    return listener;
  }

  public Iterator<Panel> iterator()
  {
    return panel.iterator();
  }

  public void addPanelNeedingLayout(Panel child)
  {
    synchronized(panelsNeedingLayout)
    {
      boolean shouldAdd = true;
      for(Iterator<Panel> iterator = panelsNeedingLayout.iterator(); iterator.hasNext();)
      {
        Panel panel = iterator.next();
        if(child == panel)
        {
          shouldAdd = false;
          break;
        }
        else if(child.isDescendantOf(panel) && child.getParent().needsLayout())
        {
          shouldAdd = false;
          break;
        }
        else if(panel.isDescendantOf(child) && panel.getParent().needsLayout())
        {
          iterator.remove();
        }
      }
      if(shouldAdd)
      {
        panelsNeedingLayout.add(child);
      }
    }
    Context.kickPainter();
  }

  public boolean hasPanelsNeedingLayout()
  {
    synchronized(panelsNeedingLayout)
    {
      return panelsNeedingLayout.size() > 0;
    }
  }

  public void getAndClearPanelsNeedingLayout(ArrayList<Panel> buffer)
  {
    synchronized(panelsNeedingLayout)
    {
      buffer.addAll(panelsNeedingLayout);
      panelsNeedingLayout.clear();
    }
  }

  public boolean panelsNeedingUpdateContains(Panel panel)
  {
    synchronized(panelsNeedingLayout)
    {
      return panelsNeedingLayout.contains(panel);
    }
  }

  public void addDirtyRegion(Rectangle region)
  {
    synchronized(dirtyRegions)
    {
      boolean shouldAdd = true;
      if(region.width <= 0 || region.height <= 0)
        shouldAdd = false;
      else
      {
        for(Iterator<Rectangle> iterator = dirtyRegions.iterator(); iterator.hasNext();)
        {
          Rectangle dirtyRegion = iterator.next();
          if(dirtyRegion.contains(region))
          {
            shouldAdd = false;
            break;
          }
          else if(region.intersects(dirtyRegion))
          {
            iterator.remove();
            region = region.union(dirtyRegion);
          }
        }
      }
      if(shouldAdd)
      {
        dirtyRegions.add(region);
      }
    }
    Context.kickPainter();
  }

  public boolean hasDirtyRegions()
  {
    synchronized(dirtyRegions)
    {
      return dirtyRegions.size() > 0;
    }
  }

  public void getAndClearDirtyRegions(ArrayList<Rectangle> buffer)
  {
    synchronized(dirtyRegions)
    {
      buffer.addAll(dirtyRegions);
      dirtyRegions.clear();
    }
  }

  public boolean dirtyRegionsContains(Rectangle region)
  {
    synchronized(dirtyRegions)
    {
      return dirtyRegions.contains(region);
    }
  }

  public ImageCache getImageCache()
  {
    if(imageCache == null)
    {
      Prop prop = ((PropablePanel) getPanel()).getProp();
      ResourceLoader loader = prop.getLoader();
      imageCache = new ImageCache(loader);
    }
    return imageCache;
  }

  public String toString()
  {
    return "RootPanel: " + panel.toString();
  }

  /////////////////////////////////////////////
  /// NOT NEEDED
  /// TODO - Need to remove this from the Panel hierarchy somehow to delete these methods
  /////////////////////////////////////////////

  public void repaint()
  {
    System.err.println("rooPanel.repaint!!! Not expected!");
    doLayout();
    PaintJob job = new PaintJob(getAbsoluteBounds(), getGraphics().getBackground());
    job.paint(panel);
    job.applyTo(getGraphics());
  }

  public void doLayout()
  {
    panel.doLayout();
  }

  public Layout getDefaultLayout()
  {
    throw new RuntimeException("getDefaultLayout() called on RootPanel");
  }

  public void paintOn(Graphics2D graphics)
  {
  }

  public boolean canBeBuffered()
  {
    return false;
  }

  public boolean hasChildren()
  {
    return true;
  }

  public java.util.List<Panel> getChildren()
  {
    LinkedList<Panel> panels = new LinkedList<Panel>();
    panels.add(panel);
    return panels;
  }

  public boolean remove(Panel child)
  {
    return false;
  }

  public void removeAll()
  {
  }

  public boolean containsAbsolutePoint(Point point)
  {
    return false;
  }

  public void setParent(Panel panel)
  {
  }

  public void sterilize()
  {
  }

  public boolean isSterilized()
  {
    return false;
  }

  public Panel getParent()
  {
    return null;
  }

  public Style getStyle()
  {
    throw new RuntimeException("RootPanel.getStyle()");
  }

  public boolean isFloater()
  {
    return false;
  }

  public void doFloatLayout()
  {
  }

  public void consumableAreaChanged()
  {
  }

  public boolean containsRelativePoint(Point point)
  {
    return true;
  }

  public Panel getOwnerOfPoint(Point point)
  {
    System.err.println("RootPanel.getOwnerOfPoint()");
    // never called
    return this;
  }

  public void clearCache()
  {
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

  public void focusGained(FocusEvent e)
  {
  }

  public void focusLost(FocusEvent e)
  {
  }                                                               

  public void keyTyped(KeyEvent e)
  {
  }

  public void keyPressed(KeyEvent e)
  {
  }

  public void keyReleased(KeyEvent e)
  {
  }

  public void buttonPressed(ActionEvent e)
  {
  }

  public void valueChanged(Object e)
  {
  }

  public boolean needsLayout()
  {
    return false;
  }

  public void markAsNeedingLayout()
  {
  }

  public void add(Panel child)
  {
  }

  public PropFrame getStageFrame()
  {
    return frame;
  }
}
