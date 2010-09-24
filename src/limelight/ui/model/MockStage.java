//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.model.Stage;
import limelight.ui.MockGraphics;

import java.awt.*;

public class MockStage extends Stage
{
  public boolean visible;
  public boolean closed;
  public boolean shouldAllowClose;
  public boolean wasClosed;
  private Cursor cursor = Cursor.getDefaultCursor();
  private Dimension size = new Dimension(0, 0);

  public void close()
  {
    closed = true;
  }
  
  public boolean isVisible()
  {
    return visible;
  }

  @Override
  public boolean shouldAllowClose()
  {
    return shouldAllowClose;
  }

  public int getWidth()
  {
    return size.width;
  }

  public int getHeight()
  {
    return size.height;
  }

  public Graphics getGraphics()
  {
    return new MockGraphics();
  }

  public Cursor getCursor()
  {
    return cursor;
  }

  public void setCursor(Cursor cursor)
  {
    this.cursor = cursor;
  }

  public Insets getInsets()
  {
    return new Insets(0, 0, 0, 0);
  }

  public void setSize(int width, int height)
  {
    size = new Dimension(width, height);
  }
}
