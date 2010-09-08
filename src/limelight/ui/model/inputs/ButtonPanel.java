//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import com.android.ninepatch.NinePatch;
import limelight.styles.Style;
import limelight.styles.values.SimpleHorizontalAlignmentValue;
import limelight.ui.EventAction;
import limelight.ui.events.*;
import limelight.ui.images.Images;
import limelight.ui.model.*;
import limelight.styles.HorizontalAlignment;
import limelight.styles.VerticalAlignment;
import limelight.styles.values.SimpleVerticalAlignmentValue;

import java.awt.*;
import java.awt.font.TextLayout;

public class ButtonPanel extends AbstractButtonPanel
{
  private static Drawable normalPatch;
  private static Drawable selectedPatch;
  private static Drawable focusPatch;

  static
  {
    normalPatch = NinePatch.load(Images.load("button.9.png"), true, true);
    selectedPatch = NinePatch.load(Images.load("button_selected.9.png"), true, true);
    focusPatch = NinePatch.load(Images.load("button_focus.9.png"), true, true);
  }

  private static Font font = new Font("Arial", Font.BOLD, 12); // TODO MDM should pull font from style
  private static SimpleHorizontalAlignmentValue horizontalTextAlignment = new SimpleHorizontalAlignmentValue(HorizontalAlignment.CENTER);
  private static SimpleVerticalAlignmentValue verticalTextAlignment = new SimpleVerticalAlignmentValue(VerticalAlignment.CENTER);

  private Drawable activePatch;
  private String text;
  private Rectangle textBounds;
  private Dimension textDimensions;
  private TextLayout textLayout;

  public ButtonPanel()
  {
    activePatch = normalPatch;
    getEventHandler().add(MousePressedEvent.class, MousePressedAction.instance);
    getEventHandler().add(MouseReleasedEvent.class, MouseReleasedAction.instance);
  }

  @Override
  protected void setDefaultStyles(Style style)
  {
    style.setDefault(Style.WIDTH, 128);
    style.setDefault(Style.HEIGHT, 27);
  }

  public void paintOn(Graphics2D graphics)
  {
    if(hasFocus())
      focusPatch.draw(graphics, 0, 0, width, height);
    activePatch.draw(graphics, 0, 0, width, height);

    graphics.setColor(Color.BLACK);
    calculateTextDimentions();
    int textX = horizontalTextAlignment.getX(textDimensions.width, getBoundingBox());
    float textY = verticalTextAlignment.getY(textDimensions.height, getBoundingBox()) + textLayout.getAscent();
    textLayout.draw(graphics, textX, textY + 1);
  }

  private void calculateTextDimentions()
  {
    if(textBounds == null)
    {
      textLayout = new TextLayout(text, font, TextPanel.staticFontRenderingContext);
      int height = (int) ((textLayout.getAscent() + textLayout.getDescent() + textLayout.getLeading()) + 0.5);
      int width = (int) (textLayout.getBounds().getWidth() + textLayout.getBounds().getX() + 0.5);
      textDimensions = new Dimension(width, height);
    }
  }

  public void setText(PropablePanel panel, String text)
  {
    this.text = text;
    textBounds = null;
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
      panel.activePatch = selectedPatch;
      panel.markAsDirty();
    }
  }

  private static class MouseReleasedAction implements EventAction
  {
    public static MouseReleasedAction instance = new MouseReleasedAction();

    public void invoke(limelight.ui.events.Event event)
    {
      ButtonPanel panel = (ButtonPanel) event.getRecipient();
      panel.activePatch = normalPatch;
      panel.markAsDirty();
    }
  }

  // TODO MDM - Change color when space bar is pressed
}
