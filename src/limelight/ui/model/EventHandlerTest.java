package limelight.ui.model;

import limelight.ui.EventAction;
import limelight.ui.events.*;
import limelight.ui.events.Event;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static junit.framework.Assert.assertEquals;

public class EventHandlerTest
{
  private MemoryAction action;
  private BasePanel panel;
  private BasePanel parent;
  private EventHandler handler;

  @Before
  public void setUp() throws Exception
  {
    action = new MemoryAction();
    parent = new TestableBasePanel();
    panel = new TestableBasePanel();
    parent.add(panel);

    handler = panel.getEventHandler();
  }

  private static class MemoryAction implements EventAction
  {
    public boolean invoked;
    private Event event;

    public void invoke(Event event)
    {
      invoked = true;
      this.event = event;
    }
  }
  
  @Test
  public void unhandledInheritableEventsArePassedToParent() throws Exception
  {
    parent.getEventHandler().add(MousePressedEvent.class, action);

    panel.getEventHandler().dispatch(new MousePressedEvent(panel, 0, new Point(0, 0), 0));

    assertEquals(true, action.invoked);
    assertEquals(parent, action.event.getPanel());
  }

  @Test
  public void unhandledNoninheritableEventsAreNotPassedToParent() throws Exception
  {
    parent.getEventHandler().add(KeyPressedEvent.class, action);

    panel.getEventHandler().dispatch(new KeyPressedEvent(panel, 0, KeyEvent.KEY_ENTER, KeyEvent.LOCATION_LEFT));

    assertEquals(false, action.invoked);
  }
}
