//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.styles.ScreenableStyle;
import limelight.model.api.PropProxy;
import limelight.ui.painting.PaintAction;
import limelight.util.Box;

import java.awt.*;


public interface Prop extends ParentPanel
{
  void add(limelight.ui.Panel child);
  boolean remove(limelight.ui.Panel child);
  void removeAll();
  void doLayout();
  void setAfterPaintAction(PaintAction action);
  void setText(String text);
  String getText();
  TextAccessor getTextAccessor();
  void setTextAccessor(TextAccessor accessor);

  Box getBounds();
  Box getBorderedBounds();
  Graphics2D getGraphics();
  ScreenableStyle getStyle();
  PropProxy getProp();
}
