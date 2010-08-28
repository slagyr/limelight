//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import com.android.ninepatch.NinePatch;
import limelight.styles.ScreenableStyle;
import limelight.styles.values.SimpleHorizontalAlignmentValue;
import limelight.styles.values.SimpleVerticalAlignmentValue;
import limelight.ui.model.*;
import limelight.util.Box;
import limelight.styles.HorizontalAlignment;
import limelight.styles.VerticalAlignment;

import javax.imageio.ImageIO;
import java.awt.font.TextLayout;
import java.awt.*;
import java.io.IOException;

public class ComboBoxPanel extends InputPanel implements TextAccessor
{
  private static NinePatch normalPatch;
  private static NinePatch focusPatch;
  private static final int BUTTON_WIDTH = 25;
  private static final int LEFT_PADDING = 8; //TODO MDM - Use style padding

  static
  {
    try
    {
      ClassLoader classLoader = ComboBoxPanel.class.getClassLoader();

      normalPatch = NinePatch.load(ImageIO.read(classLoader.getResource("limelight/ui/images/combo_box.9.png")), true, true);
      focusPatch = NinePatch.load(ImageIO.read(classLoader.getResource("limelight/ui/images/combo_box_focus.9.png")), true, true);
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

  private static Font font = new Font("Arial", Font.BOLD, 12);

  private static SimpleHorizontalAlignmentValue horizontalTextAlignment = new SimpleHorizontalAlignmentValue(HorizontalAlignment.LEFT);
  private static SimpleVerticalAlignmentValue verticalTextAlignment = new SimpleVerticalAlignmentValue(VerticalAlignment.CENTER);
  private String text;
  private Dimension textDimensions;
  private TextLayout textLayout;
  public ComboBoxPanel()
  {
    setSize(128, 27);
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

  public Box getBoxInsidePadding()
  {
    return getBoundingBox();
  }

  public Box getChildConsumableArea()
  {
    return getBoundingBox();
  }

  public void paintOn(Graphics2D graphics)
  {
    if(hasFocus())
      focusPatch.draw(graphics, 0, 0, width, height);
    normalPatch.draw(graphics, 0, 0, width, height);

    if(text != null)
    {
      graphics.setColor(Color.BLACK);
      calculateTextDimentions();
      int textX = horizontalTextAlignment.getX(textDimensions.width, getBoundingBox()) + LEFT_PADDING;
      float textY = verticalTextAlignment.getY(textDimensions.height, getBoundingBox()) + textLayout.getAscent();
      textLayout.draw(graphics, textX, textY + 1);
    }
  }

  private void calculateTextDimentions()
  {
    if(text != null)
    {
      textLayout = new TextLayout(text, font, TextPanel.staticFontRenderingContext);
      int height = (int) ((textLayout.getAscent() + textLayout.getDescent() + textLayout.getLeading()) + 0.5);
      int width = (int) (textLayout.getBounds().getWidth() + textLayout.getBounds().getX() + 0.5);
      textDimensions = new Dimension(width, height);
    }
  }

  public ScreenableStyle getStyle()
  {
    return getParent().getStyle();
  }

  public void setText(PropablePanel panel, String text)
  {
    this.text = text;
  }

  public String getText()
  {
    return text;
  }

  public void addItem(String item)
  {
    // TODO
  }

  public String getSelectedItem()
  {
    // TODO
    return null;
  }
}
