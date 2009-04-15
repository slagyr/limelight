//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight;

import limelight.ui.model.inputs.InputPanel;
import limelight.ui.model.*;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;

public class KeyboardFocusManager extends DefaultKeyboardFocusManager
{
  private InputPanel focusedPanel;
  public limelight.ui.model.Frame frame;

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
      focusedPanel.focusGained(null);
    }
  }

  //TODO Hacked in
  public void focusFrame(limelight.ui.model.Frame frame)
  {
    this.frame = frame;
    focusComponent(this.frame);
//    focusedPanel = frame.getRoot();

  }

  private void focusComponent(Component newlyFocused)
  {
    try
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
    catch(SecurityException e)
    {
      // happens in tests
    }
  }

  public void unfocusCurrentlyFocusedComponent()
  {
    Component focused = getGlobalFocusOwner();
    if(focused != null)
    {
      setGlobalFocusOwner(frame);
      if(focusedPanel != null)
      {
        focusedPanel.focusLost(null);
        focusedPanel = null;
      }
      FocusEvent gained = new FocusEvent(focused, FocusEvent.FOCUS_LOST);
      FocusListener[] listeners = focused.getFocusListeners();
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
