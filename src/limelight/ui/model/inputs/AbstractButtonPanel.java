package limelight.ui.model.inputs;

import limelight.ui.EventAction;
import limelight.ui.Panel;
import limelight.ui.events.ButtonPushedEvent;
import limelight.ui.events.CharTypedEvent;
import limelight.ui.events.Event;
import limelight.ui.events.MouseClickedEvent;

public abstract class AbstractButtonPanel extends InputPanel
{
  protected AbstractButtonPanel()
  {
    getEventHandler().add(MouseClickedEvent.class, PushAction.instance);
    getEventHandler().add(CharTypedEvent.class, PushAction.instance);
    getEventHandler().add(ButtonPushedEvent.class, PropogateToParentAction.instance);
  }

  private static boolean isPushEvent(Event event)
  {
    return event instanceof MouseClickedEvent || ((event instanceof CharTypedEvent) && ((CharTypedEvent) event).getChar() == ' ');
  }

  private static class PushAction implements EventAction
  {
    public static PushAction instance = new PushAction();

    public void invoke(Event event)
    {
      if(!event.isConsumed() && isPushEvent(event))
      {
        final Panel panel = event.getRecipient();
        new ButtonPushedEvent(panel).dispatch(panel);
      }
    }
  }

}
