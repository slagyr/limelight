//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import com.android.ninepatch.NinePatch;
import limelight.styles.values.SimpleHorizontalAlignmentValue;
import limelight.ui.EventAction;
import limelight.ui.events.*;
import limelight.ui.model.*;
import limelight.styles.HorizontalAlignment;
import limelight.styles.VerticalAlignment;
import limelight.styles.values.SimpleVerticalAlignmentValue;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.TextLayout;
import java.io.IOException;

public class ButtonPanel extends AbstractButtonPanel implements TextAccessor
{
  private static Drawable normalPatch;
  private static Drawable selectedPatch;
  private static Drawable focusPatch;

  static
  {
    try
    {
      ClassLoader classLoader = ButtonPanel.class.getClassLoader();

      normalPatch = NinePatch.load(ImageIO.read(classLoader.getResource("limelight/ui/images/button.9.png")), true, true);
      selectedPatch = NinePatch.load(ImageIO.read(classLoader.getResource("limelight/ui/images/button_selected.9.png")), true, true);
      focusPatch = NinePatch.load(ImageIO.read(classLoader.getResource("limelight/ui/images/button_focus.9.png")), true, true);
    }
    catch(IOException e)
    {
      throw new RuntimeException("Could not load ButtonPanel images", e);
    }
    catch(Exception e)
    {
      System.err.println("e = " + e);
      e.printStackTrace();
      throw new RuntimeException(e);
    }
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
    setSize(128, 27);
    getEventHandler().add(MousePressedEvent.class, MousePressedAction.instance);
    getEventHandler().add(MouseReleasedEvent.class, MouseReleasedAction.instance);
  }

  public void setParent(limelight.ui.Panel panel)
  {
    super.setParent(panel);
    if(panel instanceof PropPanel)
    {
      PropPanel propPanel = (PropPanel) panel;
      propPanel.sterilize();
      propPanel.setTextAccessor(this);
    }
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
      ButtonPanel panel = (ButtonPanel)event.getPanel();
      panel.activePatch = selectedPatch;
      panel.markAsDirty();
    }
  }

  private static class MouseReleasedAction implements EventAction
  {
    public static MouseReleasedAction instance = new MouseReleasedAction();

    public void invoke(limelight.ui.events.Event event)
    {
      ButtonPanel panel = (ButtonPanel)event.getPanel();
      panel.activePatch = normalPatch;
      panel.markAsDirty();
    }
  }

  // TODO MDM - Change color when space bar is pressed
}
