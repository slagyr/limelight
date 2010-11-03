//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.*;
import limelight.model.Production;
import limelight.model.Stage;
import limelight.model.api.CastingDirector;
import limelight.styles.RichStyle;
import limelight.styles.Style;
import limelight.ui.ButtonGroupCache;
import limelight.ui.Panel;
import limelight.model.api.PropProxy;
import limelight.util.ResourceLoader;

import java.awt.*;
import java.util.*;

public class ScenePanel extends PropPanel implements Scene
{
  private final AbstractList<Panel> panelsNeedingLayout = new ArrayList<Panel>(50);
  private final AbstractList<Rectangle> dirtyRegions = new ArrayList<Rectangle>(50);
  private ImageCache imageCache;
  private Stage stage;
  private final Map<String, RichStyle> styles;
  private HashMap<String, PropPanel> index = new HashMap<String, PropPanel>();
  private Production production;
  private boolean shouldAllowClose = true;
  private ButtonGroupCache buttonGroups = new ButtonGroupCache();
  private ResourceLoader resourceLoader;
  private CastingDirector castingDirector;

  public ScenePanel(PropProxy propProxy)
  {
    super(propProxy);
    styles = Collections.synchronizedMap(new HashMap<String, RichStyle>());
    getStyle().setDefault(Style.WIDTH, "100%");
    getStyle().setDefault(Style.HEIGHT, "100%");
  }

  @Override
  public Layout getDefaultLayout()
  {
    return SceneLayout.instance;
  }

  @Override
  public Scene getRoot()
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
      ResourceLoader loader = getProduction().getResourceLoader();
      imageCache = new ImageCache(loader);
    }
    return imageCache;
  }

  public void setStage(Stage newFrame)
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
  public Stage getStage()
  {
    return stage;
  }

  public Map<String, RichStyle> getStylesStore()
  {
    return styles;
  }

  public void addToIndex(PropPanel prop)
  {
    PropPanel value = index.get(prop.getId());
    if(value != null && value != prop)
      throw new LimelightException("Duplicate id: " + prop.getId());
    index.put(prop.getId(), prop);
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

  public boolean isVisible()
  {
    return stage != null && stage.isVisible();
  }

  public ButtonGroupCache getButtonGroups()
  {
    return buttonGroups;
  }

  public ResourceLoader getResourceLoader()
  {
    if(resourceLoader == null)
      resourceLoader = ResourceLoader.forRoot("");
    return resourceLoader;
  }

  @Override
  public void addOptions(Map<String, Object> newOptions)
  {
    if(newOptions.containsKey("path"))
      resourceLoader = ResourceLoader.forRoot("" + newOptions.remove("path"));
    super.addOptions(newOptions);
  }

  public void setCastingDirector(CastingDirector castingDirector)
  {
    this.castingDirector = castingDirector;
  }

  public CastingDirector getCastingDirector()
  {
    return castingDirector;
  }

  private static class SceneLayout implements Layout
  {
    public static Layout instance = new SceneLayout();

    public void doLayout(Panel panel)
    {
      ScenePanel scene = (ScenePanel)panel;
      Style style = scene.getStyle();
      final Stage stage = scene.stage;
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
