package limelight.ui.model.inputs;

import limelight.styles.Style;
import limelight.ui.MockGraphics;
import limelight.ui.api.MockProp;
import limelight.ui.events.*;
import limelight.ui.model.PropPanel;
import limelight.ui.model.PropablePanel;
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
    @Override
    protected void setDefaultStyles(Style style)
    {
    }

    public void setText(String text)
    {
    }

    public String getText()
    {
      return null;
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

    new MouseClickedEvent(panel, 0, null, 0).dispatch(panel);

    assertEquals(true, action.invoked);
  }
                                      
  @Test
  public void buttonPressedEventNotInvokedWhenClickIsConsumed() throws Exception
  {
    panel.getEventHandler().add(ButtonPushedEvent.class, action);

    new MouseClickedEvent(panel, 0, null, 0).consumed().dispatch(panel);

    assertEquals(false, action.invoked);
  }
  
  @Test
  public void aButtonParentWillAlsoGetThePushEvent() throws Exception
  {
    PropPanel parent = new PropPanel(new MockProp());
    parent.add(panel);
    parent.getEventHandler().add(ButtonPushedEvent.class, action);

    new MouseClickedEvent(panel, 0, null, 0).dispatch(panel);

    assertEquals(true, action.invoked);
    assertEquals(parent, action.recipient);
  }

  @Test
  public void buttonPressedEventInvokedWhenSpaceTyped() throws Exception
  {
    panel.getEventHandler().add(ButtonPushedEvent.class, action);

    new CharTypedEvent(panel, 0, ' ').dispatch(panel);

    assertEquals(true, action.invoked);
  }

  @Test
  public void buttonPressedEventNotInvokedWhenSpaceTypedEventIsConsumed() throws Exception
  {
    panel.getEventHandler().add(ButtonPushedEvent.class, action);

    new CharTypedEvent(panel, 0, ' ').consumed().dispatch(panel);

    assertEquals(false, action.invoked);
  }
                                     
  @Test
  public void buttonPressedNotEventInvokedWhenOtherCharsTypes() throws Exception
  {
    panel.getEventHandler().add(ButtonPushedEvent.class, action);

    new CharTypedEvent(panel, 0, 'a').dispatch(panel);

    assertEquals(false, action.invoked);
  }
}
