package limelight.events;

import limelight.ui.model.inputs.MockEventAction;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class EventHandlerTest
{
  private static class SimpleEvent extends Event
  {
  }

  private EventHandler handler;
  private MockEventAction action;

  @Before
  public void setUp() throws Exception
  {
    handler = new EventHandler();
    action = new MockEventAction();
  }

  @Test
  public void unhandledInheritableEventsArePassedToParent() throws Exception
  {
    handler.add(SimpleEvent.class, action);

    handler.dispatch(new SimpleEvent());

    assertEquals(true, action.invoked);
  }
}
