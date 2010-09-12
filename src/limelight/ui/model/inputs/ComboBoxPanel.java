//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import com.android.ninepatch.NinePatch;
import limelight.styles.HorizontalAlignment;
import limelight.styles.Style;
import limelight.styles.VerticalAlignment;
import limelight.styles.values.SimpleHorizontalAlignmentValue;
import limelight.styles.values.SimpleVerticalAlignmentValue;
import limelight.ui.*;
import limelight.ui.events.*;
import limelight.ui.events.Event;
import limelight.ui.images.Images;
import limelight.ui.model.PropPanel;
import limelight.ui.model.TextPanel;
import limelight.ui.painting.BackgroundPainter;

import java.awt.*;
import java.awt.font.TextLayout;
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
    style.setDefault(Style.LEFT_PADDING, 8);
    style.setDefault(Style.HORIZONTAL_ALIGNMENT, "left");
    style.setDefault(Style.VERTICAL_ALIGNMENT, "center");
    style.setDefault(Style.FONT_FACE, "Arial");
    style.setDefault(Style.FONT_SIZE, 12);
    style.setDefault(Style.FONT_STYLE, "bold");
    style.setDefault(Style.TEXT_COLOR, "black");
  }

  @Override
  protected Painter getPropPainter(PropPanel propPanel)
  {
    return ComboBoxPropPainter.instance;
  }

  public void setText(String text)
  {
    if(text == null)
      return;

    for(Object option : options)
    {
      if(text.equals(option.toString()))
      {
        setSelectedOption(option);
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
    if(options.size() == 1)
      setSelectedOption(option);
  }

  public Object getSelectedOption()
  {
    return selectedOption;
  }

  public void setOptions(Object... options)
  {
    clear();
    for(Object option : options)
      addOption(option);
  }

  public void setSelectedOption(Object option)
  {
    if(option != null && option.equals(selectedOption))
      return;

    if(options.contains(option))
    {
      selectedOption = option;
      valueChanged();
    }
  }

  public List<Object> getOptions()
  {
    return new LinkedList<Object>(options);
  }

  public void clear()
  {
    this.options.clear();
    selectedOption = null;
    markAsDirty();
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


  public static class ComboBoxPropPainter implements Painter
  {
    public static Painter instance = new ComboBoxPropPainter();

    public void paint(Graphics2D graphics, PaintablePanel panel)
    {
      BackgroundPainter.instance.paint(graphics, panel);
      ComboBoxBorderPainter.instance.paint(graphics, panel);
    }
  }

  private static class ComboBoxBorderPainter implements Painter
  {
    public static ComboBoxBorderPainter instance = new ComboBoxBorderPainter();

    private static NinePatch normalPatch;
    private static NinePatch focusPatch;
    static
    {
      normalPatch = NinePatch.load(Images.load("combo_box.9.png"), true, true);
      focusPatch = NinePatch.load(Images.load("combo_box_focus.9.png"), true, true);
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
        System.err.println("ComboBox: NinePatch choked again");
      }
    }
  }
}
