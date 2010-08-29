//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import junit.framework.TestCase;
import limelight.ui.api.MockStudio;
import limelight.Context;
import limelight.MockContext;
import limelight.KeyboardFocusManager;
import org.junit.Before;
import org.junit.Test;

import java.awt.event.WindowEvent;
import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

public class AlertFrameManagerTest
{
  private AlertFrameManager manager;
  private MockPropFrame frame;
  private MockStudio studio;

  @Before
  public void setUp() throws Exception
  {
    manager = new AlertFrameManager();
    studio = new MockStudio();
    Context.instance().frameManager = manager;
    Context.instance().studio = studio;
    Context.instance().environment = "test";
    frame = new MockPropFrame();
  }

  @Test
  public void closeAllFrames() throws Exception
  {
    manager.watch(frame);

    manager.closeAllFrames();

    assertEquals(true, frame.closed);
  }

  @Test
  public void theaterIsNotifiedOfActivatedStage() throws Exception
  {
    manager.windowActivated(new WindowEvent(frame.getWindow(), 1));

    assertEquals(true, frame.activated);
  }

  @Test
  public void shouldAskStageFrameIfItCanClose() throws Exception
  {
    frame.shouldAllowClose = false;
    manager.windowClosing(new WindowEvent(frame.getWindow(), 1));
    assertEquals(false, frame.closed);

    frame.shouldAllowClose = true;
    manager.windowClosing(new WindowEvent(frame.getWindow(), 1));
    assertEquals(true, frame.closed);
  }

  @Test
  public void checkingWithStudioBeforeShuttingDown() throws Exception
  {
    MockContext context = MockContext.stub();
    manager.watch(frame);
    manager.windowClosed(new WindowEvent(frame.getWindow(), 1));
    assertEquals(true, context.shutdownAttempted);
  }

  @Test
  public void shouldNotInvokeShutdownForNonVitalFrames() throws Exception
  {
    MockContext context = MockContext.stub();
    frame.setVital(false);
    manager.watch(frame);
    manager.windowClosed(new WindowEvent(frame.getWindow(), 1));
    assertEquals(false, context.shutdownAttempted);
  }
  
  @Test
  public void shouldInvokeShutdownWhenOnlyNonVitalFramesRemain() throws Exception
  {
    MockContext context = MockContext.stub();
    frame.setVital(false);
    MockPropFrame frame2 = new MockPropFrame();
    manager.watch(frame);
    manager.watch(frame2);

    manager.windowClosed(new WindowEvent(frame2.getWindow(), 1));
    assertEquals(true, context.shutdownAttempted);
  }

  @Test
  public void getActiveFrameWhenNoneHasGainedFocus() throws Exception
  {
    manager.watch(frame);
    assertEquals(null, manager.getFocusedFrame());

    frame.visible = true;
    assertEquals(frame, manager.getFocusedFrame());
  }

  @Test
  public void getVisibleFrames() throws Exception
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

  @Test
  public void frameIsNotifiedOfClose() throws Exception
  {
    MockContext.stub();
    frame.setVital(false);
    manager.watch(frame);
    manager.windowClosed(new WindowEvent(frame.getWindow(), 1));

    assertEquals(true, frame.wasClosed);
  }

//  @Test
//  public void tellsKeyboradFocusManagerToReleaseStageUponClosing() throws Exception
//  {
//    KeyboardFocusManager keyboard = new KeyboardFocusManager();
//    Context.instance().keyboardFocusManager = keyboard;
//    keyboard.focusFrame(frame.getWindow());
//
//    manager.windowClosed(new WindowEvent(frame.getWindow(), 1));
//
//    assertEquals(null, keyboard.getFocusedWindow());
//    assertEquals(null, keyboard.getFocusedFrame());
//  }

  @Test
  public void stageNotifiedWhenActivationLost() throws Exception
  {
    frame.activated = true;
    manager.windowDeactivated(new WindowEvent(frame.getWindow(), 1));
    assertEquals(false, frame.activated);
  }
  
  @Test
  public void activatingFrame() throws Exception
  {
    frame.activated = false;
    manager.windowActivated(new WindowEvent(frame.getWindow(), 1));

    assertEquals(true, frame.activated);
  }

  @Test
  public void frameNotifiedWhenIconifiedAndDeiconified() throws Exception
  {
    manager.windowIconified(new WindowEvent(frame.getWindow(), 1));
    assertEquals(true, frame.iconified);                
    manager.windowDeiconified(new WindowEvent(frame.getWindow(), 1));
    assertEquals(false, frame.iconified);
  }

  @Test
  public void frameNotifiedWhenActivatedAndDeactivated() throws Exception
  {
    manager.windowActivated(new WindowEvent(frame.getWindow(), 1));
    assertEquals(true, frame.activated);                
    manager.windowDeactivated(new WindowEvent(frame.getWindow(), 1));
    assertEquals(false, frame.activated);
  }
}
