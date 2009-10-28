//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.ui.api.MockStage;
import limelight.ui.MockGraphics;

import java.awt.*;
import java.awt.event.WindowEvent;

public class MockStageFrame extends StageFrame
{
  private final Container contentPanel;
  public boolean wasRefreshed;
  public boolean closed;
  public boolean shouldAllowClose;
  public boolean visible;
  public boolean wasClosed;
  public boolean iconified;
  public boolean activated;

  public MockStageFrame()
  {
    setStage(new MockStage());
    contentPanel = new Container(){
      public Graphics getGraphics()
      {
        return new MockGraphics();
      }
    };
  }

  public Container getContentPane()
  {
    return contentPanel;
  }

  public void setRoot(RootPanel root)
  {
    this.root = root;
  }

  public void refresh()
  {
    wasRefreshed = true;
  }

  public void close(WindowEvent e)
  {
    closed = true;
  }

  public boolean shouldAllowClose()
  {
    return shouldAllowClose;
  }

  public boolean isVisible()
  {
    return visible;
  }

  public void closed(WindowEvent e)
  {
    wasClosed = true;
  }
  
  public void iconified(WindowEvent e)
  {
    iconified = true;
  }

  public void deiconified(WindowEvent e)
  {
    iconified = false;
  }

  public void activated(WindowEvent e)
  {
    activated = true;
  }

  public void deactivated(WindowEvent e)
  {
    activated = false;
  }
}
