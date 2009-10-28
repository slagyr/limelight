//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.api;

import java.awt.event.WindowEvent;


public class MockStage implements Stage
{
  public MockTheater theater;
  public boolean shouldAllowClose;
  public boolean wasClosed;
  public boolean iconified;
  public boolean activated;
  public boolean notifiedOfClosing;

  public MockStage()
  {
    theater = new MockTheater();
  }

  public Theater theater()
  {
    return theater;
  }

  public boolean should_allow_close()
  {
    return shouldAllowClose;
  }

  public void closing(WindowEvent e)
  {
    notifiedOfClosing = true;
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
