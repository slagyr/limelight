//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model;

import limelight.model.Production;
import limelight.model.Stage;
import limelight.model.api.CastingDirector;
import limelight.model.api.PropProxy;
import limelight.styles.RichStyle;
import limelight.ui.Panel;
import limelight.util.ResourceLoader;

import java.awt.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public interface Scene extends Panel, ParentPanel
{
  void setStage(Stage frame);

  boolean hasPanelsNeedingLayout();

  boolean hasDirtyRegions();

  void getAndClearPanelsNeedingLayout(Collection<Panel> panelBuffer);

  void getAndClearDirtyRegions(Collection<Rectangle> regionBuffer);

  void addDirtyRegion(Rectangle bounds);

  public Map<String, RichStyle> getStyles();

  void setStyles(Map<String,RichStyle> styles);

  void addPanelNeedingLayout(Panel panel);

  Stage getStage();

  void setCursor(Cursor cursor);

  Cursor getCursor();

  ImageCache getImageCache();

  void addToIndex(PropPanel prop);

  void removeFromIndex(PropPanel prop);

  Production getProduction();

  void setProduction(Production production);

  boolean shouldAllowClose();

  boolean isVisible();

  PropProxy getProxy();
  
  CastingDirector getCastingDirector();

  ResourceLoader getResourceLoader();

  String getAbsoluteName();
}


