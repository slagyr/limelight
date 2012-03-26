//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model;

import limelight.Context;
import limelight.LimelightException;
import limelight.Log;
import limelight.model.Production;
import limelight.model.Stage;
import limelight.model.api.PlayerRecruiter;
import limelight.model.api.PropProxy;
import limelight.styles.RichStyle;
import limelight.styles.Style;
import limelight.ui.ButtonGroupCache;
import limelight.ui.Panel;
import limelight.util.Opts;
import limelight.util.Util;

import java.awt.*;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ScenePanel extends PropPanel implements Scene
{
  private static final Map<String, RichStyle> EMPTY_STYLES = Collections.unmodifiableMap(new HashMap<String, RichStyle>());

  private final Map<Panel, Layout> panelsNeedingLayout = new HashMap<Panel, Layout>(47);
  private final AbstractList<Rectangle> dirtyRegions = new ArrayList<Rectangle>(50);
  private ImageCache imageCache;
  private Stage stage;
  private Map<String, RichStyle> styles = EMPTY_STYLES;
  private HashMap<String, PropPanel> index = new HashMap<String, PropPanel>();
  private Production production;
  private boolean shouldAllowClose = true;
  private ButtonGroupCache buttonGroups = new ButtonGroupCache();
  private String pathRelativeToProduction;
  private PlayerRecruiter playerRecruiter;
  private Map<Prop, Opts> backstage = new HashMap<Prop, Opts>();
  private Lock lock = new ReentrantLock();
  private boolean layoutRequired;


  public ScenePanel(PropProxy propProxy, PlayerRecruiter playerRecruiter)
  {
    super(propProxy);
    this.playerRecruiter = playerRecruiter;
    getStyle().setDefault(Style.WIDTH, "100%");
    getStyle().setDefault(Style.HEIGHT, "100%");
  }

  public ScenePanel(PropProxy propProxy, PlayerRecruiter playerRecruiter, Map<String, Object> options)
  {
    this(propProxy, playerRecruiter);
    addOptions(options);
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
  public synchronized Graphics2D getGraphics()
  {
    if(stage == null)
      return null;
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

  public void addPanelNeedingLayout(Panel child, Layout layout)
  {
    synchronized(panelsNeedingLayout)
    {
//Log.debug("adding panel needing layout", new Exception());
      Log.debug("adding panel needing layout: " + child + " " + layout);
      if(!panelsNeedingLayout.containsKey(child) || layout.overides(panelsNeedingLayout.get(child)))
        panelsNeedingLayout.put(child, layout);
//      boolean shouldAdd = true;
//      for(Iterator<Panel> iterator = panelsNeedingLayout.keySet().iterator(); iterator.hasNext(); )
//      {
//        Panel panel = iterator.next();
//        if(child == panel)
//        {
//          shouldAdd = layout.overides(panelsNeedingLayout.get(child));
//          break;
//        }
//        else if(child.isDescendantOf(panel) && panelsNeedingLayout.containsKey(child.getParent()))
//        {
//          shouldAdd = false;
//          break;
//        }
//        else if(panel.isDescendantOf(child) && panelsNeedingLayout.containsKey(panel))
//        {
//          // TODO MDM This is questionable.  It forces layout on all descendants all the time.  If we know which children need layout, we can be strategic, do less work.
//          iterator.remove();
//        }
//      }
//      if(shouldAdd)
//      {
//        panelsNeedingLayout.put(child, layout);
//      }
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

  public boolean hasPanelNeedingLayout(Panel panel)
  {
    synchronized(panelsNeedingLayout)
    {
      return panelsNeedingLayout.containsKey(panel);
    }
  }

  public Lock getLock()
  {
    return lock;
  }

  public void layoutRequired()
  {
    layoutRequired = true;
Log.debug("setting layout required " + this);
    Context.kickPainter();
  }

  public boolean isLayoutRequired()
  {
    return layoutRequired;
  }

  public void resetLayoutRequired()
  {
    layoutRequired = false;
  }

  public void getAndClearPanelsNeedingLayout(Map<Panel, Layout> buffer)
  {
    synchronized(panelsNeedingLayout)
    {
      buffer.putAll(panelsNeedingLayout);
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
        for(Iterator<Rectangle> iterator = dirtyRegions.iterator(); iterator.hasNext(); )
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
      imageCache = new ImageCache(production.getPath());
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
      markAsNeedingLayout();
//      addPanelNeedingLayout(this, getDefaultLayout());
    }
  }

  @Override
  public Stage getStage()
  {
    return stage;
  }

  public Map<String, RichStyle> getStyles()
  {
    return styles;
  }

  public void setStyles(Map<String, RichStyle> styles)
  {
    this.styles = styles;
  }

  public void addToIndex(PropPanel prop)
  {
    PropPanel value = index.get(prop.getId());
    if(value != null && value != prop)
      throw new LimelightException("Duplicate id: " + prop.getId());
    index.put(prop.getId(), prop);
  }

  public void removeFromCaches(PropPanel prop)
  {
    index.remove(prop.getId());
    backstage.remove(prop);
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

  public String getAbsoluteName()
  {
    return pathRelativeToProduction;
  }

  public String getPath()
  {
    String productionPath = production == null ? null : production.getPath();
    return Context.fs().pathTo(productionPath, pathRelativeToProduction);
  }

  @Override
  public void addOptions(Map<String, Object> newOptions)
  {
    if(newOptions.containsKey("path"))
      pathRelativeToProduction = Util.toString(newOptions.remove("path"));

    illuminateId(newOptions.remove("id"));
    illuminateName(newOptions.remove("name"));
    if(playerRecruiter != null)
      illuminatePlayers(newOptions.remove("players"));

    super.addOptions(newOptions);
  }

  @Override
  public void illuminate()
  {
    final Lock lock = getLock();
    try
    {
      lock.lock();
      super.illuminate();
    }
    finally {
      lock.unlock();
    }
  }

  public PlayerRecruiter getPlayerRecruiter()
  {
    return playerRecruiter;
  }

  public Opts getBackstage(Prop child)
  {
    Opts result = backstage.get(child);
    if(result == null)
    {
      result = new Opts();
      backstage.put(child, result);
    }
    return result;
  }

  public Map<Prop, Opts> backstage_PRIVATE()
  {
    return backstage;
  }

  private static class SceneLayout extends PropPanelLayout
  {
    public static Layout instance = new SceneLayout();

    public void doExpansion(Panel panel)
    {
      ScenePanel scene = (ScenePanel) panel;
      Style style = scene.getStyle();
      final Stage stage = scene.stage;

      if(stage == null)
      {
        Log.warn("ScenePanel - doLayout called on un-staged scene.");
        return;
      }

      Insets insets = stage.getInsets();

      panel.setLocation(insets.left, insets.top);

      final int consumableWidth = stage.getWidth() - insets.left - insets.right;
      final int consumableHeight = stage.getHeight() - insets.top - insets.bottom;
      final int width = style.getCompiledWidth().calculateDimension(consumableWidth, style.getCompiledMinWidth(), style.getCompiledMaxWidth(), 0);
      final int height = style.getCompiledHeight().calculateDimension(consumableHeight, style.getCompiledMinHeight(), style.getCompiledMaxHeight(), 0);
      scene.setSize(width, height);

      PropPanelLayout.instance.doExpansion(scene);
    }

    public boolean overides(Layout other)
    {
      return true;
    }
  }
}
