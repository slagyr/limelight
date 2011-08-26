//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

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

  @Test
  public void removingTheLastAction() throws Exception
  {
    handler.add(SimpleEvent.class, action);
    handler.remove(SimpleEvent.class, action);

    assertEquals(0, handler.getActions(SimpleEvent.class).size());
  }
  
  @Test
  public void dispatchingEmptyEvent() throws Exception
  {
    handler.dispatch(new SimpleEvent());

    handler.add(SimpleEvent.class, action);
    handler.remove(SimpleEvent.class, action);

    handler.dispatch(new SimpleEvent());
    assertEquals(false, action.invoked);
  }

}
