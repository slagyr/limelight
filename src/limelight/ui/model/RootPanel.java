package limelight.ui.model;

import limelight.styles.RichStyle;
import limelight.ui.Panel;
import java.awt.*;
import java.util.Collection;
import java.util.Map;

public interface RootPanel extends Panel
{

  void setFrame(PropFrame frame);

  boolean hasPanelsNeedingLayout();

  boolean hasDirtyRegions();

  void getAndClearPanelsNeedingLayout(Collection<Panel> panelBuffer);

  void getAndClearDirtyRegions(Collection<Rectangle> regionBuffer);

  void addDirtyRegion(Rectangle bounds);

  public Map<String, RichStyle> getStylesStore();
}
