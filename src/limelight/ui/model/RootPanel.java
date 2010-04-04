package limelight.ui.model;

import limelight.styles.Style;
import limelight.ui.Panel;
import limelight.util.Box;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public interface RootPanel extends Panel
{
  void destroy();

  void setFrame(PropFrame frame);

  boolean hasPanelsNeedingLayout();

  boolean hasDirtyRegions();

  void getAndClearPanelsNeedingLayout(ArrayList<Panel> panelBuffer);

  void getAndClearDirtyRegions(ArrayList<Rectangle> regionBuffer);

  void addDirtyRegion(Rectangle bounds);

  boolean isAlive();

  public Map<String, Style> getStyles();
}
