package limelight.ui.api;

import limelight.ui.painting.PaintAction;
import limelight.util.Box;
import java.awt.*;

public interface PropablePanel
{
  void add(limelight.ui.Panel child);
  void remove(limelight.ui.Panel child);
  void removeAll();
  void doLayout();
  void repaint();
  void paintImmediately(int x, int y, int width, int height);
  void setAfterPaintAction(PaintAction action);
  void setText(String text);
  String getText();
  void setCursor(Cursor cursor);
  Box getBox();
  Box getBoxInsideBorders();
  Graphics2D getGraphics();
}
