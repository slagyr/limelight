//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import junit.framework.TestCase;
import limelight.ui.api.MockStage;
import limelight.Context;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class FrameManagerTest extends TestCase
{
  private FrameManager manager;
  private Frame frame;

  public void setUp() throws Exception
  {
    manager = new AlertFrameManager();
    Context.instance().frameManager = manager;
    MockStage stage = new MockStage();
    frame = new Frame(stage);
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
    for (WindowFocusListener listener : frame.getWindowFocusListeners())
      listener.windowGainedFocus(event);

    assertSame(frame, manager.getActiveFrame());
  }

}
