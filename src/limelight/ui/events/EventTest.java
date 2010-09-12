package limelight.ui.events;

import limelight.ui.EventAction;
import limelight.ui.MockPanel;
import limelight.ui.Panel;
import limelight.ui.model.TestablePanelBase;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;

public class EventTest
{
  private Event event;
  private MockPanel source;

  private static class TestableEvent extends Event
  {
    public TestableEvent(Panel source)
    {
      super(source);
    }
  }

  @Before
  public void setUp() throws Exception
  {
    source = new MockPanel();
    event = new TestableEvent(source);
  }
  
  @Test
  public void rememberThePanel() throws Exception
  {
    assertSame(source, event.getSource());
  }
  
  @Test
  public void isConsumable() throws Exception
  {
    assertEquals(false, event.isConsumed());

    event.consume();

    assertEquals(true, event.isConsumed());
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
  public void dispatching() throws Exception
  {
    MockPanel recipient = new MockPanel();

    event.dispatch(recipient);

    assertEquals(event, recipient.mockEventHandler.events.get(0));
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
        dispatchedRecipient = event.getRecipient();
      }
    });

    event.dispatch(recipient);

    assertEquals(recipient, dispatchedRecipient);
    assertEquals(source, event.getRecipient());

  }
}
