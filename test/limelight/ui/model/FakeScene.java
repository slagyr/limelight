//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model;

import limelight.model.Production;
import limelight.model.Stage;
import limelight.model.api.FakePlayerRecruiter;
import limelight.model.api.PlayerRecruiter;
import limelight.styles.RichStyle;
import limelight.ui.ButtonGroupCache;
import limelight.ui.MockGraphics;
import limelight.ui.Panel;
import limelight.util.NullLock;
import limelight.util.Opts;

import java.awt.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.locks.Lock;

public class FakeScene extends MockProp implements Scene
{
  public LinkedList<Rectangle> dirtyRegions = new LinkedList<Rectangle>();
  public Map<String, RichStyle> styleStore;
  public Production production;
  public boolean shouldAllowClose;
  public Stage stage;
  public boolean visible;
  public PlayerRecruiter playerRecruiter = new FakePlayerRecruiter();
  public Map<Prop, Opts> backstage = new HashMap<Prop, Opts>();
  public ButtonGroupCache buttonGroups = new ButtonGroupCache();
  public HashMap<Panel, Layout> panelsNeedingLayout = new HashMap<Panel, Layout>();
  public ImageCache imageCache;


  @Override
  public Scene getRoot()
  {
    return this;
  }

  public void setStage(Stage stage)
  {
    this.stage = stage;
  }

  public Stage getStage()
  {
    return stage;
  }

  public boolean hasPanelsNeedingLayout()
  {
    return panelsNeedingLayout.size() > 0;
  }

  public boolean hasDirtyRegions()
  {
    return dirtyRegions.size() > 0;
  }

  public void addPanelNeedingLayout(Panel panel, Layout layout)
  {
    panelsNeedingLayout.put(panel, layout);
  }

  public boolean hasPanelNeedingLayout(Panel panel)
  {
    return panelsNeedingLayout.containsKey(panel);
  }

  public Lock getLock()
  {
    return NullLock.instance;
  }

  public void layoutRequired()
  {
  }

  public boolean isLayoutRequired()
  {
    return false;
  }

  public void resetLayoutRequired()
  {
  }

  @Override
  public Graphics2D getGraphics()
  {
    return new MockGraphics();
  }

  public void setCursor(Cursor cursor)
  {
  }

  public Cursor getCursor()
  {
    return Cursor.getDefaultCursor();
  }

  public ImageCache getImageCache()
  {
    if(imageCache == null)
      imageCache = new ImageCache("/test/path");
    return imageCache;
  }

  public void addToIndex(PropPanel prop)
  {
  }

  public void removeFromCaches(PropPanel prop)
  {
  }

  public Production getProduction()
  {
    return production;
  }

  public void setProduction(Production production)
  {
    this.production = production;
  }

  public boolean shouldAllowClose()
  {
    return shouldAllowClose;
  }

  public boolean isVisible()
  {
    return stage != null && stage.isVisible();
  }

  public PlayerRecruiter getPlayerRecruiter()
  {
    return playerRecruiter;
  }

  public String getAbsoluteName()
  {
    return "FakeScene.absoluteName";
  }

  public String getPath()
  {
    return "fake_scene/path";
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

  public ButtonGroupCache getButtonGroups()
  {
    return buttonGroups;
  }

  public void getAndClearPanelsNeedingLayout(Map<Panel, Layout> layoutBuffer)
  {
    layoutBuffer.putAll(panelsNeedingLayout);
    panelsNeedingLayout.clear();
  }

  public void getAndClearDirtyRegions(Collection<Rectangle> regionBuffer)
  {
    regionBuffer.addAll(dirtyRegions);
    dirtyRegions.clear();
  }

  public void addDirtyRegion(Rectangle bounds)
  {
    dirtyRegions.add(bounds);
  }

  public Map<String, RichStyle> getStyles()
  {
    return styleStore;
  }

  public void setStyles(Map<String, RichStyle> styles)
  {
    styleStore = styles;
  }
}
