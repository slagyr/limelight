//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.events.panel;

import limelight.events.Event;
import limelight.events.EventAction;
import limelight.model.api.FakePropProxy;
import limelight.ui.MockPanel;
import limelight.ui.Panel;
import limelight.ui.model.PropPanel;
import limelight.ui.model.TestablePanelBase;
import limelight.ui.model.inputs.MockEventAction;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;

public class PanelEventTest
{
  private PanelEvent event;
  private MockPanel source;

  private static class TestableEvent extends PanelEvent
  {
  }

  @Before
  public void setUp() throws Exception
  {
    source = new MockPanel();
    event = new TestableEvent();
    event.setSource(source);
  }
  
  @Test
  public void subjectIsTheSource() throws Exception
  {
    assertEquals(event.getSubject(), event.getSource());
  }
  
  @Test
  public void rememberThePanel() throws Exception
  {
    assertSame(source, event.getSource());
  }
  
  @Test
  public void isNotInheritableByDefault() throws Exception
  {
    assertEquals(false, event.isInheritable());
  }

  @Test
  public void recipientIsSameAsSourceByDefault() throws Exception
  {
    assertEquals(source, event.getRecipient());
  }
  
  @Test
  public void dispatchingSetsTheSource() throws Exception
  {
    event = new TestableEvent();

    event.dispatch(source);

    assertEquals(source, event.getSource());
    assertEquals(source, event.getRecipient());
  }
  
  @Test
  public void dispatching() throws Exception
  {
    MockPanel recipient = new MockPanel();
    MockEventAction action = new MockEventAction();
    recipient.getEventHandler().add(event.getClass(), action);

    event.dispatch(recipient);

    assertEquals(event, action.event);
    assertEquals(source, event.getSource());
    assertEquals(source, event.getRecipient());
  }
  
  private Panel dispatchedRecipient;
  @Test
  public void theRecipientIsSetOnlyDuringDispatchAndThenRestored() throws Exception
  {
    TestablePanelBase recipient = new TestablePanelBase();
    recipient.getEventHandler().add(TestableEvent.class, new EventAction(){
      public void invoke(Event event)
      {
        dispatchedRecipient = ((PanelEvent)event).getRecipient();
      }
    });

    event.dispatch(recipient);

    assertEquals(recipient, dispatchedRecipient);
    assertEquals(source, event.getRecipient());
  }

  @Test
  public void getProp() throws Exception
  {
    assertEquals(null, event.getProp());

    final FakePropProxy propProxy = new FakePropProxy();
    PropPanel propPanel = new PropPanel(propProxy);
    event.setRecipient(propPanel);

    assertEquals(propProxy, event.getProp());
  }
}
