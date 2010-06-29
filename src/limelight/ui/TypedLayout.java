//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui;

import limelight.util.Box;

import java.awt.*;
import java.awt.font.TextHitInfo;
import java.awt.geom.Rectangle2D;

public interface TypedLayout
{

  public abstract void draw(Graphics2D graphics, float x, float y);
  public abstract boolean hasDrawn();
  public abstract String toString();
  public abstract String getText();
  public abstract float getAscent();
  public abstract float getDescent();
  public abstract float getLeading();
  int getWidth();
  int getHeight();

  int getWidthOf(String text);

  public TextHitInfo hitTestChar(float x, float y);
  int getIndexAt(int x);

  Box getCaretShape(int caretIndex);
}
