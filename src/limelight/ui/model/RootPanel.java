//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.styles.RichStyle;
import limelight.ui.Panel;
import java.awt.*;
import java.util.Collection;
import java.util.Map;

public interface RootPanel extends Panel, ParentPanel
{

  void setFrame(PropFrame frame);

  boolean hasPanelsNeedingLayout();

  boolean hasDirtyRegions();

  void getAndClearPanelsNeedingLayout(Collection<Panel> panelBuffer);

  void getAndClearDirtyRegions(Collection<Rectangle> regionBuffer);

  void addDirtyRegion(Rectangle bounds);

  public Map<String, RichStyle> getStylesStore();

  void addPanelNeedingLayout(Panel panel);

  PropFrame getFrame();

  void setCursor(Cursor cursor);

  Cursor getCursor();

  ImageCache getImageCache();

  RootKeyListener getKeyListener();
}


