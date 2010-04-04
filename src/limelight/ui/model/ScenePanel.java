//- Copyright � 2008-2009 8th Light, Inc. All Rights Reserved.
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
import java.util.*;

public class ScenePanel extends PropPanel implements RootPanel
{
  private Container contentPane;
  private EventListener listener;
  private boolean alive;
  private final ArrayList<Panel> panelsNeedingLayout = new ArrayList<Panel>(50);
  private final ArrayList<Rectangle> dirtyRegions = new ArrayList<Rectangle>(50);
  private ImageCache imageCache;
  private PropFrame frame;
  private Map<String, Style> styles;

  public ScenePanel(Prop prop)
  {
    super(prop);
    styles = Collections.synchronizedMap(new HashMap<String, Style>());
  }

  public void setFrame(PropFrame frame)
  {
    this.frame = frame;
    contentPane = frame.getContentPane();

    listener = new EventListener(this);
    contentPane.addMouseListener(listener);
    contentPane.addMouseMotionListener(listener);
    contentPane.addMouseWheelListener(listener);
    contentPane.addKeyListener(listener);
    alive = true;
    addPanelNeedingLayout(this);
  }

  public Container getContentPane()
  {
    return contentPane;
  }

  public Box getChildConsumableArea()
  {
    Box box = new Box(getX(), getY(), getWidth(), getHeight());
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

  public void destroy()
  {
    removeKeyboardFocus();                                                                                                     
    contentPane.removeMouseListener(listener);
    contentPane.removeMouseMotionListener(listener);
    contentPane.removeMouseWheelListener(listener);
    contentPane.removeKeyListener(listener);
    removeAll();
    listener = null;
    alive = false;
  }

  private void removeKeyboardFocus()
  {
    Panel focuedPanel = Context.instance().keyboardFocusManager.getFocusedPanel();
    if(focuedPanel != null && focuedPanel.getRoot() == this)
      Context.instance().keyboardFocusManager.unfocusCurrentlyFocusedComponent();
  }

  public ScenePanel getRoot()
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

  // TODO MDM - Get rid of me.
  public Panel getPanel()
  {
    return this;
  }

  public boolean isAlive()
  {
    return alive;
  }

  public EventListener getListener()
  {
    return listener;
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

  public PropFrame getFrame()
  {
    return frame;
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

  public Map<String, Style> getStyles()
  {
    return styles;
  }
}
