//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model;

import limelight.model.Stage;
import limelight.model.api.MockStageProxy;
import limelight.ui.MockGraphics;

import java.awt.*;

public class MockStage extends Stage
{
  public boolean closed;
  public boolean shouldAllowClose;
  public boolean wasClosed;
  public Cursor cursor = Cursor.getDefaultCursor();
  public Dimension size = new Dimension(0, 0);
  public boolean opened;
  public boolean framed;
  public boolean alwaysOnTop;

  public MockStage(String name)
  {
    super(name);
  }

  public MockStage()
  {
    super("MockStage");
  }

  public MockStage(String name, MockStageProxy stageProxy)
  {
    this(name);
    setProxy(stageProxy);
  }

  @Override
  protected void doOpen()
  {
    opened = true;
  }

  @Override
  protected void doClose()
  {
    closed = true;
  }

  @Override
  public boolean isOpen()
  {
    return opened;
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

  @Override
  public void setFramed(boolean framed)
  {
    this.framed = framed;
  }

  @Override
  public boolean isFramed()
  {
    return framed;
  }

  @Override
  public void setAlwaysOnTop(boolean value)
  {
    alwaysOnTop = value;
  }

  @Override
  public boolean isAlwaysOnTop()
  {
    return alwaysOnTop;
  }

  @Override
  protected void doSetVisible(boolean value)
  {
//    visible = value;
  }

  public void setSize(int width, int height)
  {
    size = new Dimension(width, height);
  }
}
