package limelight.ui.model.inputs;

import limelight.ui.EventAction;
import limelight.ui.Panel;
import limelight.ui.events.ButtonPushedEvent;
import limelight.ui.events.CharTypedEvent;
import limelight.ui.events.Event;
import limelight.ui.events.MouseClickedEvent;
import limelight.ui.model.PropPanel;

public abstract class AbstractButtonPanel extends InputPanel
{
  protected AbstractButtonPanel()
  {
    getEventHandler().add(MouseClickedEvent.class, PushAction.instance);
    getEventHandler().add(CharTypedEvent.class, PushAction.instance);
    getEventHandler().add(ButtonPushedEvent.class, PropogatePushAction.instance);
  }

  private static boolean isPushEvent(Event event)
  {
    return event instanceof MouseClickedEvent || ((event instanceof CharTypedEvent) && ((CharTypedEvent)event).getChar() == ' ');
  }

  private static class PushAction implements EventAction
  {

    public static PushAction instance = new PushAction();

    public void invoke(Event event)
    {
      if(isPushEvent(event))
      {
        final Panel panel = event.getPanel();
        panel.getEventHandler().dispatch(new ButtonPushedEvent(panel));
      }
    }
  }

  private static class PropogatePushAction implements EventAction
  {
    public static PropogatePushAction instance = new PropogatePushAction();

    public void invoke(Event event)
    {
      final Panel parent = event.getPanel().getParent();
      if(parent instanceof PropPanel)
        parent.getEventHandler().dispatch(new ButtonPushedEvent(parent));
    }
  }
}
