//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.model.api.FakePropProxy;
import limelight.styles.Style;
import limelight.ui.events.panel.ButtonPushedEvent;
import limelight.ui.events.panel.CharTypedEvent;
import limelight.ui.events.panel.MouseClickedEvent;
import limelight.ui.model.PropPanel;
import org.junit.Before;
import org.junit.Test;

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

    new MouseClickedEvent(0, null, 0).dispatch(panel);

    assertEquals(true, action.invoked);
  }
                                      
  @Test
  public void buttonPressedEventNotInvokedWhenClickIsConsumed() throws Exception
  {
    panel.getEventHandler().add(ButtonPushedEvent.class, action);

    new MouseClickedEvent(0, null, 0).consumed().dispatch(panel);

    assertEquals(false, action.invoked);
  }
  
  @Test
  public void aButtonParentWillAlsoGetThePushEvent() throws Exception
  {
    PropPanel parent = new PropPanel(new FakePropProxy());
    parent.add(panel);
    parent.getEventHandler().add(ButtonPushedEvent.class, action);

    new MouseClickedEvent(0, null, 0).dispatch(panel);

    assertEquals(true, action.invoked);
    assertEquals(parent, action.recipient);
  }

  @Test
  public void buttonPressedEventInvokedWhenSpaceTyped() throws Exception
  {
    panel.getEventHandler().add(ButtonPushedEvent.class, action);

    new CharTypedEvent(0, ' ').dispatch(panel);

    assertEquals(true, action.invoked);
  }

  @Test
  public void buttonPressedEventNotInvokedWhenSpaceTypedEventIsConsumed() throws Exception
  {
    panel.getEventHandler().add(ButtonPushedEvent.class, action);

    new CharTypedEvent(0, ' ').consumed().dispatch(panel);

    assertEquals(false, action.invoked);
  }
                                     
  @Test
  public void buttonPressedNotEventInvokedWhenOtherCharsTypes() throws Exception
  {
    panel.getEventHandler().add(ButtonPushedEvent.class, action);

    new CharTypedEvent(0, 'a').dispatch(panel);

    assertEquals(false, action.invoked);
  }
}
