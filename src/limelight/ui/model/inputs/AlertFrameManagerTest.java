//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import junit.framework.TestCase;
import limelight.ui.model.*;
import limelight.ui.api.MockStudio;
import limelight.Context;
import limelight.MockContext;
import limelight.KeyboardFocusManager;

import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class AlertFrameManagerTest extends TestCase
{
  private AlertFrameManager manager;
  private MockPropFrame frame;
  private MockStudio studio;

  public void setUp() throws Exception
  {
    manager = new AlertFrameManager();
    studio = new MockStudio();
    Context.instance().frameManager = manager;
    Context.instance().studio = studio;
    Context.instance().environment = "test";
    frame = new MockPropFrame();
  }

  public void testCloseAllFrames() throws Exception
  {
    manager.watch(frame);

    manager.closeAllFrames();

    assertEquals(true, frame.closed);
  }

  public void testTheaterIsNotifiedOfActivatedStage() throws Exception
  {
    manager.windowActivated(new WindowEvent(frame.getWindow(), 1));

    assertEquals(true, frame.activated);
  }

  public void testShouldAskStageFrameIfItCanClose() throws Exception
  {
    frame.shouldAllowClose = false;
    manager.windowClosing(new WindowEvent(frame.getWindow(), 1));
    assertEquals(false, frame.closed);

    frame.shouldAllowClose = true;
    manager.windowClosing(new WindowEvent(frame.getWindow(), 1));
    assertEquals(true, frame.closed);
  }

  public void testCheckingWithStudioBeforeShuttingDown() throws Exception
  {
    MockContext context = MockContext.stub();
    manager.watch(frame);
    manager.windowClosed(new WindowEvent(frame.getWindow(), 1));
    assertEquals(true, context.shutdownAttempted);
  }

  public void testShouldNotInvokeShutdownForNonVitalFrames() throws Exception
  {
    MockContext context = MockContext.stub();
    frame.setVital(false);
    manager.watch(frame);
    manager.windowClosed(new WindowEvent(frame.getWindow(), 1));
    assertEquals(false, context.shutdownAttempted);
  }
  
  public void testShouldInvokeShutdownWhenOnlyNonVitalFramesRemain() throws Exception
  {
    MockContext context = MockContext.stub();
    frame.setVital(false);
    MockPropFrame frame2 = new MockPropFrame();
    manager.watch(frame);
    manager.watch(frame2);

    manager.windowClosed(new WindowEvent(frame2.getWindow(), 1));
    assertEquals(true, context.shutdownAttempted);
  }

  public void testGetActiveFrameWhenNoneHasGainedFocus() throws Exception
  {
    manager.watch(frame);
    assertEquals(null, manager.getFocusedFrame());

    frame.visible = true;
    assertEquals(frame, manager.getFocusedFrame());
  }

  public void testGetVisibleFrames() throws Exception
  {
    ArrayList<PropFrame> result = new ArrayList<PropFrame>();
    frame.visible = true;
    MockPropFrame frame2 = new MockPropFrame();

    manager.getVisibleFrames(result);
    assertEquals(0, result.size());
    result.clear();

    manager.watch(frame);
    manager.getVisibleFrames(result);
    assertEquals(1, result.size());
    result.clear();

    frame2.visible = false;
    manager.watch(frame2);
    manager.getVisibleFrames(result);
    assertEquals(1, result.size());
    result.clear();

    frame2.visible = true;
    manager.getVisibleFrames(result);
    assertEquals(2, result.size());
    result.clear();
  }

  public void testFrameIsNotifiedOfClose() throws Exception
  {
    MockContext.stub();
    frame.setVital(false);
    manager.watch(frame);
    manager.windowClosed(new WindowEvent(frame.getWindow(), 1));

    assertEquals(true, frame.wasClosed);
  }

  public void testTellsKeyboradFocusManagerToReleaseStageUponClosing() throws Exception
  {
    KeyboardFocusManager keyboard = new KeyboardFocusManager();
    Context.instance().keyboardFocusManager = keyboard;
    keyboard.focusFrame(frame.getWindow());

    manager.windowClosed(new WindowEvent(frame.getWindow(), 1));

    assertEquals(null, keyboard.getFocusedWindow());
    assertEquals(null, keyboard.getFocusedFrame());
  }

  public void testStageNotifiedWhenActivationLost() throws Exception
  {
    frame.activated = true;
    manager.windowDeactivated(new WindowEvent(frame.getWindow(), 1));
    assertEquals(false, frame.activated);
  }

  public void testFrameNotifiedWhenIconifiedAndDeiconified() throws Exception
  {
    manager.windowIconified(new WindowEvent(frame.getWindow(), 1));
    assertEquals(true, frame.iconified);                
    manager.windowDeiconified(new WindowEvent(frame.getWindow(), 1));
    assertEquals(false, frame.iconified);
  }

  public void testFrameNotifiedWhenActivatedAndDeactivated() throws Exception
  {
    manager.windowActivated(new WindowEvent(frame.getWindow(), 1));
    assertEquals(true, frame.activated);                
    manager.windowDeactivated(new WindowEvent(frame.getWindow(), 1));
    assertEquals(false, frame.activated);
  }
}
