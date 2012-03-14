//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model.inputs;

import limelight.events.Event;
import limelight.events.EventAction;
import limelight.styles.Style;
import limelight.ui.PaintablePanel;
import limelight.ui.Painter;
import limelight.ui.events.panel.*;
import limelight.ui.images.Images;
import limelight.ui.model.PropPanel;
import limelight.ui.ninepatch.NinePatch;
import limelight.ui.painting.BackgroundPainter;

import java.awt.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class DropDownPanel extends AbstractButtonPanel
{
  private Object selectedChoice;
  private List<Object> choices;
  private DropDownPopup popup;

  public DropDownPanel()
  {
    choices = new LinkedList<Object>();
    getEventHandler().add(ButtonPushedEvent.class, LoadPopupListAction.instance);
    getEventHandler().add(KeyPressedEvent.class, PopupKeyControlAction.instance);
    getEventHandler().add(FocusLostEvent.class, ClosePopupOnFocusLostAction.instance);
  }

  @Override
  protected void setDefaultStyles(Style style)
  {
    style.setDefault(Style.WIDTH, 128);
    style.setDefault(Style.HEIGHT, 27);
    style.setDefault(Style.LEFT_PADDING, 8);
    style.setDefault(Style.HORIZONTAL_ALIGNMENT, "left");
    style.setDefault(Style.VERTICAL_ALIGNMENT, "center");
    style.setDefault(Style.FONT_FACE, "Arial");
    style.setDefault(Style.FONT_SIZE, 12);
    style.setDefault(Style.FONT_STYLE, "bold");
    style.setDefault(Style.TEXT_COLOR, "black");
  }

  @Override
  protected Painter getPropPainter(PropPanel prop)
  {
    return DropDownPropPainter.instance;
  }

  public void setText(String text)
  {
    if(text == null)
      return;

    for(Object choice : choices)
    {
      if(text.equals(choice.toString()))
      {
        setSelectedChoice(choice);
        return;
      }
    }
  }

  public String getText()
  {
    if(selectedChoice == null)
      return "";
    else
      return selectedChoice.toString();
  }

  public void addChoice(Object choice)
  {
    if(choice == null)
      return;

    this.choices.add(choice);
    if(this.choices.size() == 1)
      setSelectedChoice(choice);
  }

  public Object getSelectedChoice()
  {
    return selectedChoice;
  }

  public void setChoicesVargs(Object... choices)
  {
    setChoices(Arrays.asList(choices));
  }

  public void setChoices(Collection<?> choices)
  {
    clear();
    for(Object choice : choices)
      addChoice(choice);
  }

  public void setSelectedChoice(Object choice)
  {
    if(choice != null && choice.equals(selectedChoice))
      return;

    if(choices.contains(choice))
    {
      selectedChoice = choice;
      markAsDirty();
      valueChanged();
    }
  }

  public List<Object> getChoices()
  {
    return new LinkedList<Object>(choices);
  }

  public void clear()
  {
    this.choices.clear();
    selectedChoice = null;
    markAsDirty();
  }

  public void setPopup(DropDownPopup dropDownPopup)
  {
    popup = dropDownPopup;
  }

  public DropDownPopup getPopup()
  {
    return popup;
  }

  private static class LoadPopupListAction implements EventAction
  {
    private static LoadPopupListAction instance = new LoadPopupListAction();

    public void invoke(Event e)
    {
      PanelEvent event = (PanelEvent)e;
      if(event.isConsumed())
        return;

      final DropDownPanel dropDown = (DropDownPanel) event.getRecipient();
      if(!dropDown.hasFocus() && dropDown.getStage() != null)
      {
        dropDown.getStage().getKeyListener().focusOn(dropDown);
      }
      if(dropDown.getPopup() == null)
      {
        final DropDownPopup popup = new DropDownPopup(dropDown);
        popup.open();
      }
    }
  }

  private static class PopupKeyControlAction implements EventAction
  {
    private static PopupKeyControlAction instance = new PopupKeyControlAction();

    public void invoke(Event e)
    {
      PanelEvent event = (PanelEvent)e;
      if(event.isConsumed())
        return;

      DropDownPanel dropDownPanel = (DropDownPanel) event.getRecipient();
      final DropDownPopup popup = dropDownPanel.getPopup();
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
    public void invoke(Event e)
    {
      PanelEvent event = (PanelEvent)e;
      if(event.isConsumed())
        return;

      final DropDownPanel dropDown = (DropDownPanel) event.getRecipient();
      if(dropDown.getPopup() != null)
        dropDown.getPopup().close();
    }
  }

  public static class DropDownPropPainter implements Painter
  {
    public static Painter instance = new DropDownPropPainter();

    public void paint(Graphics2D graphics, PaintablePanel panel)
    {
      BackgroundPainter.instance.paint(graphics, panel);
      DropDownBorderPainter.instance.paint(graphics, panel);
    }
  }

  private static class DropDownBorderPainter implements Painter
  {
    public static DropDownBorderPainter instance = new DropDownBorderPainter();

    private static NinePatch normalPatch;
    private static NinePatch focusPatch;
    static
    {
      normalPatch = NinePatch.load(Images.load("drop_down.9.png"));
      focusPatch = NinePatch.load(Images.load("drop_down_focus.9.png"));
    }

    public void paint(Graphics2D graphics, PaintablePanel panel)
    {
      try
      {
        if(panel.hasFocus())
          focusPatch.draw(graphics, 0, 0, panel.getWidth(), panel.getHeight());
        normalPatch.draw(graphics, 0, 0, panel.getWidth(), panel.getHeight());
      }
      catch(IndexOutOfBoundsException e)
      {
        System.err.println("DropDown: NinePatch choked again");
      }
    }
  }
}
