//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.*;
import limelight.model.Production;
import limelight.styles.RichStyle;
import limelight.styles.Style;
import limelight.ui.Panel;
import limelight.ui.api.Prop;
import limelight.util.ResourceLoader;

import java.awt.*;
import java.util.*;

public class ScenePanel extends PropPanel implements RootPanel
{
  private final AbstractList<Panel> panelsNeedingLayout = new ArrayList<Panel>(50);
  private final AbstractList<Rectangle> dirtyRegions = new ArrayList<Rectangle>(50);
  private ImageCache imageCache;
  private PropFrame stage;
  private final Map<String, RichStyle> styles;
  private HashMap<String, PropPanel> index = new HashMap<String, PropPanel>();
  private Production production;
  private boolean shouldAllowClose = true;

  public ScenePanel(Prop prop)
  {
    super(prop);
    styles = Collections.synchronizedMap(new HashMap<String, RichStyle>());
  }

  public void setStage(PropFrame newFrame)
  {   
    if(stage != null && newFrame != stage)
      delluminate();

    stage = newFrame;

    if(stage != null)
    {
      illuminate();
      addPanelNeedingLayout(this);
    }
  }

  @Override
  public Layout getDefaultLayout()
  {
    return SceneLayout.instance;
  }

  @Override
  public RootPanel getRoot()
  {
    return this;
  }

  @Override
  public Graphics2D getGraphics()
  {
    return (Graphics2D) stage.getGraphics();
  }

  @Override
  public boolean isDescendantOf(Panel panel)
  {
    return panel == this;
  }

  @Override
  public Panel getClosestCommonAncestor(Panel panel)
  {
    return this;
  }

  @Override
  public void setCursor(Cursor cursor)
  {
    if(stage.getCursor() != cursor)
      stage.setCursor(cursor);
  }

  public Cursor getCursor()
  {
    return stage.getCursor();
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
      Prop prop = getProp();
      ResourceLoader loader = prop.getLoader();
      imageCache = new ImageCache(loader);
    }
    return imageCache;
  }

  @Override
  public PropFrame getStage()
  {
    return stage;
  }

  public Map<String, RichStyle> getStylesStore()
  {
    return styles;
  }

  public void addToIndex(PropPanel propPanel)
  {
    PropPanel value = index.get(propPanel.getId());
    if(value != null && value != propPanel)
      throw new LimelightException("Duplicate id: " + propPanel.getId());
    index.put(propPanel.getId(), propPanel);
  }

  public void removeFromIndex(PropPanel prop)
  {
    index.remove(prop.getId());
  }

  public PropPanel find(String id)
  {
    return index.get(id);
  }

  public void setProduction(Production production)
  {
    this.production = production;
  }

  public Production getProduction()
  {
    return production;
  }

  public void setShouldAllowClose(boolean value)
  {
    shouldAllowClose = value;
  }

  public boolean shouldAllowClose()
  {
    return shouldAllowClose;
  }

  private static class SceneLayout implements Layout
  {
    public static Layout instance = new SceneLayout();

    public void doLayout(Panel panel)
    {
      ScenePanel scene = (ScenePanel)panel;
      Style style = scene.getStyle();
      final PropFrame stage = scene.stage;
      Insets insets = stage.getInsets();

      panel.setLocation(insets.left, insets.top);

      final int consumableWidth = stage.getWidth() - insets.left - insets.right;
      final int consumableHeight = stage.getHeight() - insets.top - insets.bottom;
      final int width = style.getCompiledWidth().calculateDimension(consumableWidth, style.getCompiledMinWidth(), style.getCompiledMaxWidth(), 0);
      final int height = style.getCompiledHeight().calculateDimension(consumableHeight, style.getCompiledMinHeight(), style.getCompiledMaxHeight(), 0);
      scene.setSize(width, height);

      PropPanelLayout.instance.doLayout(scene);
    }

    public boolean overides(Layout other)
    {
      return true;
    }

    public void doLayout(Panel panel, boolean topLevel)
    {
      doLayout(panel);
    }
  }
}
