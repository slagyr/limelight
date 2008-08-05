package limelight;

import limelight.ui.model.inputs.InputPanel;
import limelight.ui.model.RootPanel;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class KeyboardFocusManager extends DefaultKeyboardFocusManager
{
  private InputPanel focusedPanel;

  public void install()
  {
    java.awt.KeyboardFocusManager.setCurrentKeyboardFocusManager(this);
  }

  public void focusPanel(InputPanel inputPanel)
  {
    if(focusedPanel != inputPanel && inputPanel != null)
    {
      focusComponent(inputPanel.getComponent());
      focusedPanel = inputPanel;
    }
  }

  private void focusComponent(Component newlyFocused)
  {
    if(newlyFocused != getGlobalFocusOwner())
    {
      unfocusCurrentlyFocusedComponent();
      FocusEvent gained = new FocusEvent(newlyFocused, FocusEvent.FOCUS_GAINED);
      FocusListener[] listeners = newlyFocused.getFocusListeners();
      for(FocusListener listener : listeners)
        listener.focusGained(gained);

      this.setGlobalFocusOwner(newlyFocused);
    }
  }

  public void unfocusCurrentlyFocusedComponent()
  {
    Component focued = getGlobalFocusOwner();
    if(focued != null)
    {
      setGlobalFocusOwner(null);
      focusedPanel = null;
      FocusEvent gained = new FocusEvent(focued, FocusEvent.FOCUS_LOST);
      FocusListener[] listeners = focued.getFocusListeners();
      for(FocusListener listener : listeners)
        listener.focusLost(gained);
    }
  }

  public void focusNextComponent(Component aComponent)
  {
    if(focusedPanel != null)
      focusPanel(focusedPanel.nextInputPanel());
  }

  public void focusPreviousComponent(Component aComponent)
  {
    if(focusedPanel != null)
      focusPanel(focusedPanel.previousInputPanel());
  }

  public KeyboardFocusManager installed()
  {
    install();
    return this;
  }

  public InputPanel getFocusedPanel()
  {
    return focusedPanel;
  }

  public Component getFocuedComponent()
  {
    return getGlobalFocusOwner();
  }
}
