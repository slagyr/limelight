package limelight.ui.model;

import limelight.ui.Panel;
import limelight.ui.events.panel.*;
import limelight.ui.model.inputs.InputPanel;

import java.awt.event.KeyEvent;

public class RootKeyListener implements java.awt.event.KeyListener
{
  private Panel focusedPanel;
  private Stage stage;

  public RootKeyListener(RootPanel rootPanel)
  {
    focusedPanel = rootPanel;
  }

  public RootKeyListener(Stage stage)
  {
    this.stage = stage;
  }

  public void reset(RootPanel root)
  {
    focusedPanel = root;
  }

  public Panel getFocusedPanel()
  {
    if(focusedPanel == null)
      focusedPanel = stage.getRoot();
    return focusedPanel;
  }

  public void keyTyped(KeyEvent e)
  {
    if(getFocusedPanel() == null)
      return;

    new CharTypedEvent(focusedPanel, e.getModifiers(), e.getKeyChar()).dispatch(focusedPanel);
  }

  public void keyPressed(KeyEvent e)
  {
    if(getFocusedPanel() == null)
      return;

    new KeyPressedEvent(focusedPanel, e.getModifiers(), e.getKeyCode(), e.getKeyLocation()).dispatch(focusedPanel);
  }

  public void keyReleased(KeyEvent e)
  {
    if(getFocusedPanel() == null)
      return;

    new KeyReleasedEvent(focusedPanel, e.getModifiers(), e.getKeyCode(), e.getKeyLocation()).dispatch(focusedPanel);
  }

  public void focusOn(Panel panel)
  {
    if(panel == null || panel == focusedPanel)
      return;

    Panel previouslyFocusPanel = focusedPanel;
    focusedPanel = panel;

    if(previouslyFocusPanel != null)
      new FocusLostEvent(previouslyFocusPanel).dispatch(previouslyFocusPanel);

    new FocusGainedEvent(panel).dispatch(panel);
  }

  public void focusOnNextInput()
  {
    if(getFocusedPanel() == null)
      return;

    focusOn(nextInputPanel(focusedPanel));
  }

  public void focusOnPreviousInput()
  {
    if(getFocusedPanel() == null)
      return;
    
    focusOn(previousInputPanel(focusedPanel));
  }

  public static InputPanel nextInputPanel(Panel start)
  {
    InputPanel next = null;
    InputPanel first = null;
    boolean foundMe = false;

    for(Panel panel : start.getRoot())
    {
      if(panel instanceof InputPanel)
      {
        if(foundMe)
        {
          next = (InputPanel) panel;
          break;
        }
        else if(panel == start)
          foundMe = true;
        if(first == null)
          first = (InputPanel) panel;
      }
    }

    if(next != null)
      return next;
    else
      return first;
  }

  public static InputPanel previousInputPanel(Panel start)
  {
    InputPanel previous = null;

    for(Panel panel : start.getRoot())
    {
      if(panel instanceof InputPanel)
      {
        if(panel == start && previous != null)
        {
          break;
        }
        else
        {
          previous = (InputPanel) panel;
        }
      }
    }

    return previous;
  }
}
