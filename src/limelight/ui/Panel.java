package limelight.ui;

import limelight.ui.api.Prop;
import limelight.util.Box;

import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

public interface Panel
{
  Panel getRoot();

  boolean containsRelativePoint(Point point);

  Panel getOwnerOfPoint(Point point);

  void setParent(Panel Panel);

  int getX();

  int getY();

  Panel getParentPanel();

  boolean isAncestor(Panel ancestor);

  Graphics2D getGraphics2D();

  Box getChildConsumableArea();

  void mouseWheelMoved(MouseWheelEvent e);

  void setCursor(Cursor cursor);

  void repaint();

  Box getAbsoluteBounds();

  int getWidth();

  int getHeight();

  void paintOn(Graphics2D graphics);

  Prop getProp();

  boolean hasChildren();

  LinkedList<Panel> getChildren();

  void doLayout();

  void setLocation(int x, int y);

  void mousePressed(MouseEvent e);

  void mouseReleased(MouseEvent e);

  void mouseClicked(MouseEvent e);

  void mouseDragged(MouseEvent e);

  void mouseEntered(MouseEvent e);

  Panel getClosestCommonAncestor(Panel panel);

  void mouseExited(MouseEvent e);

  void mouseMoved(MouseEvent e);
}
