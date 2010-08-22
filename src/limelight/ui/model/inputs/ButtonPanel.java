//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import com.android.ninepatch.NinePatch;
import limelight.styles.ScreenableStyle;
import limelight.styles.values.SimpleHorizontalAlignmentValue;
import limelight.ui.model.*;
import limelight.util.Box;
import limelight.styles.HorizontalAlignment;
import limelight.styles.VerticalAlignment;
import limelight.styles.values.SimpleVerticalAlignmentValue;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.FocusEvent;
import java.awt.event.ActionEvent;
import java.awt.font.TextLayout;
import java.io.IOException;

public class ButtonPanel extends BasePanel implements TextAccessor, InputPanel
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
  private boolean focused;

  public ButtonPanel()
  {
    activePatch = normalPatch;
    setSize(128, 27);
  }

  public void setParent(limelight.ui.Panel panel)
  {
// MDM - Why was this needed?    
//    if(panel == null)
//      Context.instance().keyboardFocusManager.focusFrame((StageFrame) getRoot().getFrame());
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
    if(focused)
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

  public ScreenableStyle getStyle()
  {
    return getParent().getStyle();
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

  public void mousePressed(MouseEvent e)
  {
    activePatch = selectedPatch;
    repaint();
  }

  public void mouseReleased(MouseEvent e)
  {
    activePatch = normalPatch;
    repaint();
    super.buttonPressed(new ActionEvent(this, 0, "blah"));
  }

  public void focusGained(FocusEvent e)
  {
    focused = true;
    repaint();
    super.focusGained(e);
  }

  public void focusLost(FocusEvent e)
  {
    focused = false;
    repaint();
    super.focusLost(e);
  }
}
