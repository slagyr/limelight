//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import com.android.ninepatch.NinePatch;
import limelight.styles.Style;
import limelight.ui.EventAction;
import limelight.ui.PaintablePanel;
import limelight.ui.Painter;
import limelight.ui.events.*;
import limelight.ui.images.Images;
import limelight.ui.model.*;
import limelight.util.Box;

import java.awt.*;

public class ButtonPanel extends AbstractButtonPanel
{
  private Drawable activePatch;
  private String text;
  private boolean isBeingPressed;

  public ButtonPanel()
  {
    isBeingPressed = false;
    getEventHandler().add(MousePressedEvent.class, MousePressedAction.instance);
    getEventHandler().add(MouseReleasedEvent.class, MouseReleasedAction.instance);
  }

  @Override
  protected void setDefaultStyles(Style style)
  {
    style.setDefault(Style.WIDTH, 128);
    style.setDefault(Style.HEIGHT, 27);
    style.setDefault(Style.HORIZONTAL_ALIGNMENT, "center");
    style.setDefault(Style.VERTICAL_ALIGNMENT, "center");
    style.setDefault(Style.FONT_FACE, "Arial");
    style.setDefault(Style.FONT_SIZE, 12);
    style.setDefault(Style.FONT_STYLE, "bold");
    style.setDefault(Style.TEXT_COLOR, "black");
  }

  @Override
  protected Painter getPropPainter(PropPanel propPanel)
  {
    return BottonPropPainter.instance;
  }

  public void setText(String text)
  {
    if(text.equals(this.text))
      return;

    this.text = text;

    valueChanged();
  }

  public String getText()
  {
    return text;
  }

  private static class MousePressedAction implements EventAction
  {
    public static MousePressedAction instance = new MousePressedAction();

    public void invoke(limelight.ui.events.Event event)
    {
      ButtonPanel panel = (ButtonPanel) event.getRecipient();
      panel.isBeingPressed = true;
      panel.markAsDirty();
    }
  }

  private static class MouseReleasedAction implements EventAction
  {
    public static MouseReleasedAction instance = new MouseReleasedAction();

    public void invoke(limelight.ui.events.Event event)
    {
      ButtonPanel panel = (ButtonPanel) event.getRecipient();
      panel.isBeingPressed = false;
      panel.markAsDirty();
    }
  }

  // TODO MDM - Change color when space bar is pressed

  public static class BottonPropPainter implements Painter
  {
    public static BottonPropPainter instance = new BottonPropPainter();

    private static Drawable normalPatch;
    private static Drawable selectedPatch;
    private static Drawable focusPatch;

    static
    {
      normalPatch = NinePatch.load(Images.load("button.9.png"), true, true);
      selectedPatch = NinePatch.load(Images.load("button_selected.9.png"), true, true);
      focusPatch = NinePatch.load(Images.load("button_focus.9.png"), true, true);
    }


    public void paint(Graphics2D graphics, PaintablePanel panel)
    {
      try
      {
        final Box bounds = panel.getMarginedBounds();
        if(panel.hasFocus())
          focusPatch.draw(graphics, bounds.x, bounds.y, bounds.width, bounds.height);

        final ButtonPanel button = (ButtonPanel) ((PropPanel) panel).getTextAccessor(); // TODO MDM Yuk!
        if(button.isBeingPressed)
          selectedPatch.draw(graphics, bounds.x, bounds.y, bounds.width, bounds.height);
        else
          normalPatch.draw(graphics, bounds.x, bounds.y, bounds.width, bounds.height);
      }
      catch(IndexOutOfBoundsException e)
      {
        System.err.println("ButtonPanel: NinePatch choked again");
      }
    }
  }
}
