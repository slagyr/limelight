//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.model.Production;
import limelight.model.Stage;
import limelight.styles.RichStyle;
import limelight.ui.Panel;
import java.awt.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

public class MockScene extends MockProp implements Scene
{
  public LinkedList<Rectangle> dirtyRegions = new LinkedList<Rectangle>();
  public Map<String, RichStyle> styleStore;
  public Production production;
  public boolean shouldAllowClose;
  private Stage stage;
  public boolean visible;

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

  public void removeFromIndex(PropPanel prop)
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

  public Map<String, RichStyle> getStylesStore()
  {
    return styleStore;
  }
}
