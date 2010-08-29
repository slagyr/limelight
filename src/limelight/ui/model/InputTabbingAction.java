package limelight.ui.model;

import limelight.ui.EventAction;
import limelight.ui.events.KeyEvent;

public class InputTabbingAction implements EventAction
{
  public static InputTabbingAction instance = new InputTabbingAction();

  public void invoke(limelight.ui.events.Event event)
  {
    KeyEvent typeEvent = (KeyEvent)event;
System.err.println("typeEvent = " + typeEvent);    
    if(typeEvent.getKeyCode() != KeyEvent.KEY_TAB)
      return;
    
    final RootKeyListener keyListener = event.getPanel().getRoot().getKeyListener();
    if(keyListener == null)
      return;

    if(typeEvent.isShiftDown())
      keyListener.focusOnPreviousInput();
    else
      keyListener.focusOnNextInput();

    event.consume();
  }
}
