//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.events;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class EventTest
{
    private Event event;

    private static class TestableEvent extends Event
    {
    }

    @Before
    public void setUp() throws Exception
    {
      event = new TestableEvent();
    }

    @Test
    public void isConsumable() throws Exception
    {
      assertEquals(false, event.isConsumed());

      event.consume();

      assertEquals(true, event.isConsumed());
    }
//
//    @Test
//    public void dispatching() throws Exception
//    {
//      MockPanel recipient = new MockPanel();
//
//      event.dispatch(recipient);
//
//      assertEquals(event, recipient.mockEventHandler.events.get(0));
//      assertEquals(source, event.getSource());
//      assertEquals(source, event.getRecipient());
//    }
  }

