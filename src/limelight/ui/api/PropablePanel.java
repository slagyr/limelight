package limelight.ui.api;

import limelight.ui.painting.PaintAction;
import limelight.util.Box;
import limelight.styles.Style;

import java.awt.*;

public interface PropablePanel extends limelight.ui.Panel
{
  void add(limelight.ui.Panel child);
  boolean remove(limelight.ui.Panel child);
  void removeAll();
  void doLayout();
  void repaint();
  void paintImmediately(int x, int y, int width, int height);
  void setAfterPaintAction(PaintAction action);
  void setText(String text);
  String getText();
  void setCursor(Cursor cursor);
  Box getBoundingBox();
  Box getBoxInsideBorders();
  Graphics2D getGraphics();
  Style getStyle();
  Prop getProp();

  //Used by layout
  void snapToSize();
}
