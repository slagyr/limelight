//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model;

import limelight.model.api.PropProxy;
import limelight.styles.ScreenableStyle;
import limelight.ui.painting.PaintAction;
import limelight.util.Box;

import java.awt.*;


public interface Prop extends ParentPanel
{
  void add(limelight.ui.Panel child);
  boolean remove(limelight.ui.Panel child);
  void removeAll();
  void setAfterPaintAction(PaintAction action);
  void setText(String text); // TODO MDM - this ought to take an Object to be more flexible
  String getText();
  TextAccessor getTextAccessor();
  void setTextAccessor(TextAccessor accessor);

  Box getBounds();
  Box getBorderedBounds();
  Graphics2D getGraphics();
  ScreenableStyle getStyle();
  PropProxy getProxy();
}
