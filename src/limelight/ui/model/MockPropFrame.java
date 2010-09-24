//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.events.EventHandler;
import limelight.ui.MockGraphics;

import java.awt.*;

public class MockPropFrame implements PropFrame
{
  private RootPanel root;
  public boolean visible;
  public boolean closed;
  public boolean shouldAllowClose;
  public boolean wasClosed;
  public boolean vital = true;
  private Cursor cursor = Cursor.getDefaultCursor();
  private Dimension size = new Dimension(0, 0);
  private EventHandler eventHandler = new EventHandler();
  private RootKeyListener keyListener = new RootKeyListener(this);

  public RootPanel getRoot()
  {
    return root;
  }

  public void close()
  {
    closed = true;
  }
  
  public boolean isVisible()
  {
    return visible;
  }

  public boolean isVital()
  {
    return vital;
  }

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

  public void setRoot(RootPanel root)
  {
    this.root = root;
  }

  public void setVital(boolean vital)
  {
    this.vital = vital;
  }

  public void setSize(int width, int height)
  {
    size = new Dimension(width, height);
  }

  public EventHandler getEventHandler()
  {
    return eventHandler;
  }

  public RootKeyListener getKeyListener()
  {
    return keyListener;
  }

  public RootMouseListener getMouseListener()
  {
    return null;
  }
}
