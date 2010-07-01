//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.text;

import limelight.util.Box;

import java.awt.*;
import java.awt.font.TextHitInfo;
import java.awt.geom.Rectangle2D;

public interface TypedLayout
{

  void draw(Graphics2D graphics, float x, float y);
  String toString();
  String getText();
  float getAscent();
  float getDescent();
  float getLeading();
  int getWidthOf(String text);
  int getWidth();
  int getHeight();
  int getX(int index);
  int getIndexAt(int x);
  Box getCaretShape(int caretIndex);

  public TextHitInfo hitTestChar(float x, float y);
}
