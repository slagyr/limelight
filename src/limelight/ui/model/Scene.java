//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model;

import limelight.model.Production;
import limelight.model.Stage;
import limelight.model.StylesSource;
import limelight.model.api.PlayerRecruiter;
import limelight.model.api.PropProxy;
import limelight.styles.RichStyle;
import limelight.ui.ButtonGroupCache;
import limelight.ui.Panel;
import limelight.util.Opts;

import java.awt.*;
import java.util.Collection;
import java.util.Map;

public interface Scene extends Panel, ParentPanel, StylesSource
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

  void removeFromCaches(PropPanel prop);

  Production getProduction();

  void setProduction(Production production);

  boolean shouldAllowClose();

  boolean isVisible();

  PropProxy getProxy();

  PlayerRecruiter getPlayerRecruiter();

  String getAbsoluteName();

  String getPath();

  Opts getBackstage(Prop prop);

  ButtonGroupCache getButtonGroups();
}


