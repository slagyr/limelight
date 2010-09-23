//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.ui.api.MockStudio;
import limelight.Context;
import limelight.MockContext;
import limelight.ui.events.stage.*;
import limelight.ui.model.inputs.MockEventAction;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

public class AlertFrameManagerTest
{

  private static Frame hackFrame = new Frame();
  private MockEventAction action;

  private static class HackedWindowEvent extends WindowEvent
  {
    public HackedWindowEvent(PropFrame frame)
    {
      super(hackFrame, 1, 0, 1);
      source = frame;
    }
  }

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
    action = new MockEventAction();
  }

  @Test
  public void closeAllFrames() throws Exception
  {
    manager.watch(frame);

    manager.closeAllFrames();

    assertEquals(true, frame.closed);
  }

  @Test
  public void shouldAskStageFrameIfItCanClose() throws Exception
  {
    frame.shouldAllowClose = false;
    manager.windowClosing(new HackedWindowEvent(frame));
    assertEquals(false, frame.closed);

    frame.shouldAllowClose = true;
    manager.windowClosing(new HackedWindowEvent(frame));
    assertEquals(true, frame.closed);
  }

  @Test
  public void checkingWithStudioBeforeShuttingDown() throws Exception
  {
    MockContext context = MockContext.stub();
    manager.watch(frame);
    manager.windowClosed(new HackedWindowEvent(frame));
    assertEquals(true, context.shutdownAttempted);
  }

  @Test
  public void shouldNotInvokeShutdownForNonVitalFrames() throws Exception
  {
    MockContext context = MockContext.stub();
    frame.setVital(false);
    manager.watch(frame);
    manager.windowClosed(new HackedWindowEvent(frame));
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

    manager.windowClosed(new HackedWindowEvent(frame2));
    assertEquals(true, context.shutdownAttempted);
  }

  @Test
  public void getActiveFrameWhenNoneHasGainedFocus() throws Exception
  {
    manager.watch(frame);
    assertEquals(null, manager.getActiveFrame());

    frame.visible = true;
    assertEquals(frame, manager.getActiveFrame());
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
  public void frameIsNotifiedWhenOpened() throws Exception
  {
    frame.getEventHandler().add(StageOpenedEvent.class, action);

    manager.windowOpened(new HackedWindowEvent(frame));

    assertEquals(true, action.invoked);
  }

  @Test
  public void frameIsClosedWhenClosingEventIsReceived() throws Exception
  {
    frame.shouldAllowClose = true;
    manager.windowClosing(new HackedWindowEvent(frame));

    assertEquals(true, frame.closed);
  }

//  @Test
//  public void tellsKeyboradFocusManagerToReleaseStageUponClosing() throws Exception
//  {
//    KeyboardFocusManager keyboard = new KeyboardFocusManager();
//    Context.instance().keyboardFocusManager = keyboard;
//    keyboard.focusFrame(frame.getWindow());
//
//    manager.windowClosed(new HackedWindowEvent(frame));
//
//    assertEquals(null, keyboard.getFocusedWindow());
//    assertEquals(null, keyboard.getFocusedFrame());
//  }

  @Test
  public void stageNotifiedWhenActivated() throws Exception
  {
    frame.visible = true;
    frame.getEventHandler().add(StageActivatedEvent.class, action);

    manager.windowActivated(new HackedWindowEvent(frame));

    assertEquals(true, action.invoked);
  }

  @Test
  public void cantActivateFramesThatAreNotVisible() throws Exception
  {
    frame.visible = false;
    frame.getEventHandler().add(StageActivatedEvent.class, action);

    manager.windowActivated(new HackedWindowEvent(frame));
    assertEquals(false, action.invoked);
    assertEquals(null, manager.getActiveFrame());

    frame.visible = true;
    manager.windowActivated(new HackedWindowEvent(frame));
    assertEquals(true, action.invoked);
    assertEquals(frame, manager.getActiveFrame());
  }

  @Test
  public void stageNotifiedWhenDeactivated() throws Exception
  {
    frame.visible = true;
    manager.windowActivated(new HackedWindowEvent(frame));
    assertEquals(frame, manager.getActiveFrame());
    frame.getEventHandler().add(StageDeactivatedEvent.class, action);

    manager.windowDeactivated(new HackedWindowEvent(frame));

    assertEquals(true, action.invoked);
  }
  
  @Test
  public void cantDeactivateFramesThatAreNotActive() throws Exception
  {
    assertEquals(null, manager.getActiveFrame());
    frame.getEventHandler().add(StageDeactivatedEvent.class, action);

    manager.windowDeactivated(new HackedWindowEvent(frame));

    assertEquals(false, action.invoked);
  }

  @Test
  public void stageNotifiedWhenIconified() throws Exception
  {
    frame.getEventHandler().add(StageIconifiedEvent.class, action);

    manager.windowIconified(new HackedWindowEvent(frame));

    assertEquals(true, action.invoked);
  }

  @Test
  public void stageNotifiedWhenDeiconified() throws Exception
  {
    frame.getEventHandler().add(StageDeiconifiedEvent.class, action);

    manager.windowDeiconified(new HackedWindowEvent(frame));

    assertEquals(true, action.invoked);
  }

  @Test
  public void stageNotifiedWhenFocusGained() throws Exception
  {
    frame.getEventHandler().add(StageGainedFocusEvent.class, action);

    manager.windowGainedFocus(new HackedWindowEvent(frame));

    assertEquals(true, action.invoked);
  }
  
  @Test
  public void stageNotifiedWhenFocusLost() throws Exception
  {
    frame.getEventHandler().add(StageLostFocusEvent.class, action);

    manager.windowLostFocus(new HackedWindowEvent(frame));

    assertEquals(true, action.invoked);
  }
}
