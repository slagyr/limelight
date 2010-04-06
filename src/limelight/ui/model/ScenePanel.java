//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.*;
import limelight.styles.RichStyle;
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
  private final AbstractList<Panel> panelsNeedingLayout = new ArrayList<Panel>(50);
  private final AbstractList<Rectangle> dirtyRegions = new ArrayList<Rectangle>(50);
  private ImageCache imageCache;
  private PropFrame frame;
  private final Map<String, RichStyle> styles;

  public ScenePanel(Prop prop)
  {
    super(prop);
    styles = Collections.synchronizedMap(new HashMap<String, RichStyle>());
  }

  public void setFrame(PropFrame newFrame)
  {
    if(frame != null && newFrame != frame)
      delluminate();
    
    frame = newFrame;

    if(frame != null)
    {
      illuminate();
      addPanelNeedingLayout(this);
    }
  }

  @Override
  public void illuminate()
  {
    contentPane = frame.getContentPane();
    listener = new EventListener(this);
    contentPane.addMouseListener(listener);
    contentPane.addMouseMotionListener(listener);
    contentPane.addMouseWheelListener(listener);
    contentPane.addKeyListener(listener);
    super.illuminate();
  }

  @Override
  public void delluminate()
  {
    removeKeyboardFocus();
    contentPane.removeMouseListener(listener);
    contentPane.removeMouseMotionListener(listener);
    contentPane.removeMouseWheelListener(listener);
    contentPane.removeKeyListener(listener);
    listener = null;
    super.delluminate();
  }

  public Container getContentPane()
  {
    return contentPane;
  }

  @Override
  public Box getChildConsumableArea()
  {
    return new Box(getX(), getY(), getWidth(), getHeight());
  }

  @Override
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

  private void removeKeyboardFocus()
  {
    limelight.KeyboardFocusManager focusManager = Context.instance().keyboardFocusManager;
    if(focusManager == null)
      return;
    Panel focusedPanel = focusManager.getFocusedPanel();
    if(focusedPanel != null && focusedPanel.getRoot() == this)
      focusManager.unfocusCurrentlyFocusedComponent();
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

  public void getAndClearPanelsNeedingLayout(Collection<Panel> buffer)
  {
    synchronized(panelsNeedingLayout)
    {
      buffer.addAll(panelsNeedingLayout);
      panelsNeedingLayout.clear();
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

  public void getAndClearDirtyRegions(Collection<Rectangle> buffer)
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

  public Map<String, RichStyle> getStylesStore()
  {
    return styles;
  }
}
