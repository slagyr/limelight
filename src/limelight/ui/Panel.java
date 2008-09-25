//- Copyright 2008 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui;

import limelight.util.Box;
import limelight.styles.Style;
import limelight.ui.model.Update;
import limelight.ui.model.RootPanel;

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
  Box getChildConsumableArea();
  Box getBoxInsidePadding();
  boolean containsAbsolutePoint(Point point);
  boolean containsRelativePoint(Point point);
  Panel getOwnerOfPoint(Point point);
  void clearCache();

  Panel getParent();
  void setParent(Panel panel);
  RootPanel getRoot();
  boolean isAncestor(Panel ancestor);
  Panel getClosestCommonAncestor(Panel panel);
  
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

  Style getStyle();
  
  boolean isFloater();

  void setNeededUpdate(Update update);
  void resetNeededUpdate();
  Update getNeededUpdate();
  Update getAndClearNeededUpdate();
  boolean needsLayout();
  void setNeedsLayout();
  
  void mousePressed(MouseEvent e);
  void mouseReleased(MouseEvent e);
  void mouseClicked(MouseEvent e);
  void mouseDragged(MouseEvent e);
  void mouseEntered(MouseEvent e);
  void mouseExited(MouseEvent e);
  void mouseMoved(MouseEvent e);
  void mouseWheelMoved(MouseWheelEvent e);
  void focusGained(FocusEvent e);
  void focusLost(FocusEvent e);
  void keyTyped(KeyEvent e);
  void keyPressed(KeyEvent e);
  void keyReleased(KeyEvent e);
  void buttonPressed(ActionEvent e);
  void valueChanged(Object e);
}
