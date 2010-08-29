//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.*;
import limelight.styles.RichStyle;
import limelight.ui.Panel;
import limelight.ui.api.Prop;
import limelight.ui.events.*;
import limelight.util.Box;

import java.awt.*;
import java.util.*;

public class ScenePanel extends PropPanel implements RootPanel
{
  private Container contentPane;
  private RootMouseListener mouseListener;
  private RootKeyListener keyListener;
  private final AbstractList<Panel> panelsNeedingLayout = new ArrayList<Panel>(50);
  private final AbstractList<Rectangle> dirtyRegions = new ArrayList<Rectangle>(50);
  private ImageCache imageCache;
  private PropFrame frame;
  private final Map<String, RichStyle> styles;

  public ScenePanel(Prop prop)
  {
    super(prop);
    styles = Collections.synchronizedMap(new HashMap<String, RichStyle>());
    getEventHandler().add(KeyPressedEvent.class, InputTabbingAction.instance);
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

    mouseListener = new RootMouseListener(this);
    contentPane.addMouseListener(mouseListener);
    contentPane.addMouseMotionListener(mouseListener);
    contentPane.addMouseWheelListener(mouseListener);

    keyListener = new RootKeyListener(this);

    frame.addKeyListener(keyListener);

    super.illuminate();
  }

  @Override
  public void delluminate()
  {
    removeKeyboardFocus();
    contentPane.removeMouseListener(mouseListener);
    contentPane.removeMouseMotionListener(mouseListener);
    contentPane.removeMouseWheelListener(mouseListener);
    contentPane.removeKeyListener(keyListener);
    mouseListener = null;
    super.delluminate();
  }

  public Container getContentPane()
  {
    return contentPane;
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
    super.setLocation(x, y);
    contentPane.setLocation(x, y);
  }

  public Point getLocation()
  {
    return contentPane.getLocation();
  }

  public void setSize(int width, int height)
  {
    super.setSize(width, height);
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
    if(contentPane.getCursor() != cursor)
      contentPane.setCursor(cursor);
  }

  public Cursor getCursor()
  {
    return contentPane.getCursor();
  }

  // TODO MDM - Get rid of me.
  public Panel getPanel()
  {
    return this;
  }

  public RootMouseListener getMouseListener()
  {
    return mouseListener;
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

  public Map<String, RichStyle> getStylesStore()
  {
    return styles;
  }

  public RootKeyListener getKeyListener()
  {
    return keyListener;
  }

}
