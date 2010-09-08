//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.styles.Style;
import limelight.ui.*;
import limelight.ui.events.*;
import limelight.ui.events.Event;
import limelight.ui.model.*;
import limelight.ui.model.inputs.painting.ComboBoxPainter;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ComboBoxPanel extends AbstractButtonPanel
{
  private Object selectedOption;
  private java.util.List<Object> options;
  private ComboBoxPopup popup;

  public ComboBoxPanel()
  {
    options = new LinkedList<Object>();
    getEventHandler().add(ButtonPushedEvent.class, LoadPopupListAction.instance);
    getEventHandler().add(KeyPressedEvent.class, PopupKeyControlAction.instance);
    getEventHandler().add(FocusLostEvent.class, ClosePopupOnFocusLostAction.instance);
  }

  @Override
  protected void setDefaultStyles(Style style)
  {    
    style.setDefault(Style.WIDTH, 128);
    style.setDefault(Style.HEIGHT, 27);
  }

  public void paintOn(Graphics2D graphics)
  {
    ComboBoxPainter.paintOn(graphics, this);
  }

  public void setText(PropablePanel panel, String text)
  {
    setText(text);
  }

  public void setText(String text)
  {
    if(text == null)
      return;

    for(Object option : options)
    {
      if(text.equals(option.toString()))
      {
        selectedOption = option;
        return;
      }
    }
  }

  public String getText()
  {
    if(selectedOption == null)
      return "";
    else
      return selectedOption.toString();
  }

  public void addOption(Object option)
  {
    if(option == null)
      return;

    options.add(option);
    if(selectedOption == null)
      selectedOption = option;
  }

  public Object getSelectedOption()
  {
    return selectedOption;
  }

  public void setOptions(Object... options)
  {
    clear();
    selectedOption = null;
    for(Object option : options)
    {
      addOption(option);
    }
  }

  public void setSelectedOption(Object option)
  {
    if(options.contains(option))
      selectedOption = option;
  }

  public List<Object> getOptions()
  {
    return new LinkedList<Object>(options);
  }

  public void clear()
  {
    this.options.clear();
  }

  public void setPopup(ComboBoxPopup comboBoxPopup)
  {
    popup = comboBoxPopup;
  }

  public ComboBoxPopup getPopup()
  {
    return popup;
  }

  private static class LoadPopupListAction implements EventAction
  {
    private static LoadPopupListAction instance = new LoadPopupListAction();

    public void invoke(limelight.ui.events.Event event)
    {
      if(event.isConsumed())
        return;

      final ComboBoxPanel comboBox = (ComboBoxPanel) event.getRecipient();
      if(!comboBox.hasFocus() && comboBox.getRoot() != null)
      {
        comboBox.getRoot().getKeyListener().focusOn(comboBox);
      }
      if(comboBox.getPopup() == null)
      {
        final ComboBoxPopup popup = new ComboBoxPopup(comboBox);
        popup.open();
      }
    }
  }

  private static class PopupKeyControlAction implements EventAction
  {
    private static PopupKeyControlAction instance = new PopupKeyControlAction();

    public void invoke(Event event)
    {
      if(event.isConsumed())
        return;

      ComboBoxPanel comboBox = (ComboBoxPanel) event.getRecipient();
      final ComboBoxPopup popup = comboBox.getPopup();
      if(popup == null)
        return;

      KeyPressedEvent keyEvent = (KeyPressedEvent) event;
      switch(keyEvent.getKeyCode())
      {
        case KeyEvent.KEY_ESCAPE:
          popup.close();
          break;
        case KeyEvent.KEY_DOWN:
          popup.selectNext();
          break;
        case KeyEvent.KEY_UP:
          popup.selectPrevious();
          break;
        case KeyEvent.KEY_ENTER:
          popup.choose(popup.getSelectedItem());
      }
    }
  }

  private static class ClosePopupOnFocusLostAction implements EventAction
  {
    private static ClosePopupOnFocusLostAction instance = new ClosePopupOnFocusLostAction();
    public void invoke(Event event)
    {
      if(event.isConsumed())
        return;

      final ComboBoxPanel comboBox = (ComboBoxPanel) event.getRecipient();
      if(comboBox.getPopup() != null)
        comboBox.getPopup().close();
    }
  }
}
