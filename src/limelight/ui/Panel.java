//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui;

import limelight.model.Stage;
import limelight.styles.ScreenableStyle;
import limelight.ui.events.panel.PanelEventHandler;
import limelight.ui.model.Layout;
import limelight.ui.model.ParentPanelBase;
import limelight.ui.model.Scene;
import limelight.util.Box;

import java.awt.*;

public interface Panel extends Iterable<Panel>
{
  void setLocation(int x, int y);
  Point getLocation();
  void setSize(int width, int height);
  int getX();
  int getY();
  int getWidth();
  int getHeight();

  Point getAbsoluteLocation();
  Box getAbsoluteBounds();
  Panel getOwnerOfPoint(Point point);
  void clearCache();

  ParentPanelBase getParent();
  void setParent(ParentPanelBase panel);
  Scene getRoot();
  Stage getStage();
  boolean isDescendantOf(Panel ancestor);
  Panel getClosestCommonAncestor(Panel panel);

  Graphics2D getGraphics();
  void paintOn(Graphics2D graphics);
  boolean canBeBuffered();

  Layout getDefaultLayout();

  ScreenableStyle getStyle();

  boolean isFloater();

  void consumableAreaChanged();
  void markAsNeedingLayout();
  void markAsNeedingLayout(Layout layout);
  boolean needsLayout();
  Layout resetNeededLayout();
  void markAsDirty();

  boolean isIlluminated();
  void illuminate();
  void delluminate();

  boolean hasFocus();

  PanelEventHandler getEventHandler();
}
