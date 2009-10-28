//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import junit.framework.TestCase;
import limelight.ui.api.MockStage;
import limelight.Context;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;

public class FrameManagerTest extends TestCase
{
  private FrameManager manager;
  private StageFrame frame;

  public void setUp() throws Exception
  {
    manager = new AlertFrameManager();
    Context.instance().frameManager = manager;
    MockStage stage = new MockStage();
    frame = new StageFrame(stage);
  }

  public void testAddingFrames() throws Exception
  {
    manager.watch(frame);

    assertEquals(1, frame.getWindowFocusListeners().length);
    assertEquals(manager, frame.getWindowFocusListeners()[0]);
  }

  public void testFocusingOnAWindow() throws Exception
  {
    manager.watch(frame);

    WindowEvent event = new WindowEvent(frame, 123);
    for (WindowListener listener : frame.getWindowListeners())
      listener.windowActivated(event);

    assertSame(frame, manager.getFocusedFrame());
  }
}
