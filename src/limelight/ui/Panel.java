//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui;

import limelight.styles.ScreenableStyle;
import limelight.ui.model.EventHandler;
import limelight.ui.model.RootPanel;
import limelight.util.Box;
import limelight.ui.model.Layout;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

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
  Box getBoxInsidePadding();
  Panel getOwnerOfPoint(Point point);
  void clearCache();

  Panel getParent();
  void setParent(Panel panel);
  RootPanel getRoot();
  boolean isDescendantOf(Panel ancestor);
  Panel getClosestCommonAncestor(Panel panel);
  Box getChildConsumableArea();

  void add(Panel child);
  List<Panel> getChildren();
  boolean remove(Panel child);
  void removeAll();
  void sterilize();
  boolean isSterilized();
  boolean hasChildren();

  void repaint();     
  Graphics2D getGraphics();
  void paintOn(Graphics2D graphics);
  boolean canBeBuffered();

  void doLayout();
  Layout getDefaultLayout();

  ScreenableStyle getStyle();
  
  boolean isFloater();
  void doFloatLayout();

  void consumableAreaChanged();
  boolean needsLayout();
  void markAsNeedingLayout();
  void markAsDirty();

  boolean isIlluminated();
  void illuminate();
  void delluminate();

  boolean hasFocus();

  EventHandler getEventHandler();
}
