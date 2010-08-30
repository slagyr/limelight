package limelight.ui.model.inputs;

import limelight.ui.api.MockProp;
import limelight.ui.events.*;
import limelight.ui.model.PropPanel;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static junit.framework.Assert.assertEquals;

public class AbstractButtonPanelTest
{
  private AbstractButtonPanel panel;
  private MockEventAction action;

  private static class TestableAbstractButtonPanel extends AbstractButtonPanel
  {
    public void paintOn(Graphics2D graphics)
    {
    }
  }

  @Before
  public void setUp() throws Exception
  {
    panel = new TestableAbstractButtonPanel();
    action = new MockEventAction();
  }
                                      
  @Test
  public void buttonPressedEventInvokedWhenClicking() throws Exception
  {
    panel.getEventHandler().add(ButtonPushedEvent.class, action);

    panel.getEventHandler().dispatch(new MouseClickedEvent(panel, 0, null, 0));

    assertEquals(true, action.invoked);
  }
  
  @Test
  public void aButtonParentWillAlsoGetThePushEvent() throws Exception
  {
    PropPanel parent = new PropPanel(new MockProp());
    parent.add(panel);
    parent.getEventHandler().add(ButtonPushedEvent.class, action);

    panel.getEventHandler().dispatch(new MouseClickedEvent(panel, 0, null, 0));

    assertEquals(true, action.invoked);
    assertEquals(parent, action.event.getPanel());
  }

  @Test
  public void buttonPressedEventInvokedWhenSpaceTyped() throws Exception
  {
    panel.getEventHandler().add(ButtonPushedEvent.class, action);

    panel.getEventHandler().dispatch(new CharTypedEvent(panel, 0, ' '));

    assertEquals(true, action.invoked);
  }
                                     
  @Test
  public void buttonPressedNotEventInvokedWhenOtherCharsTypes() throws Exception
  {
    panel.getEventHandler().add(ButtonPushedEvent.class, action);

    panel.getEventHandler().dispatch(new CharTypedEvent(panel, 0, 'a'));

    assertEquals(false, action.invoked);
  }
}
