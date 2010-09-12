package limelight.ui.model;

import limelight.ui.events.*;
import limelight.ui.model.inputs.MockEventAction;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static junit.framework.Assert.assertEquals;

public class EventHandlerTest
{
  private MockEventAction action;
  private PanelBase panel;
  private ParentPanelBase parent;

  @Before
  public void setUp() throws Exception
  {
    action = new MockEventAction();
    parent = new TestableParentPanel();
    panel = new TestablePanelBase();
    parent.add(panel);
  }

  @Test
  public void unhandledInheritableEventsArePassedToParent() throws Exception
  {
    parent.getEventHandler().add(MousePressedEvent.class, action);

    panel.getEventHandler().dispatch(new MousePressedEvent(panel, 0, new Point(0, 0), 0));

    assertEquals(true, action.invoked);
    assertEquals(parent, action.recipient);
  }

  @Test
  public void unhandledNoninheritableEventsAreNotPassedToParent() throws Exception
  {
    parent.getEventHandler().add(KeyPressedEvent.class, action);

    panel.getEventHandler().dispatch(new KeyPressedEvent(panel, 0, KeyEvent.KEY_ENTER, KeyEvent.LOCATION_LEFT));

    assertEquals(false, action.invoked);
  }
}
