//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model;

import limelight.model.Production;
import limelight.model.Stage;
import limelight.model.api.FakePlayerRecruiter;
import limelight.model.api.PlayerRecruiter;
import limelight.styles.RichStyle;
import limelight.ui.ButtonGroupCache;
import limelight.ui.Panel;
import limelight.util.Opts;

import java.awt.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

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
  private ButtonGroupCache buttonGroups = new ButtonGroupCache();

  @Override
  public Scene getRoot()
  {
    return this;
  }

  public void setStage(Stage stage)
  {
    this.stage = stage;
  }

  public boolean hasPanelsNeedingLayout()
  {
    return false;
  }

  public boolean hasDirtyRegions()
  {
    return false;
  }

  public void addPanelNeedingLayout(Panel panel)
  {
  }

  public Stage getStage()
  {
    return stage;
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
    return null;
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

  public void getAndClearPanelsNeedingLayout(Collection<Panel> panelBuffer)
  {
  }

  public void getAndClearDirtyRegions(Collection<Rectangle> regionBuffer)
  {
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
