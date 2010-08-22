//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.styles.ScreenableStyle;
import limelight.ui.model.*;
import limelight.ui.RadioButtonGroupMember;
import limelight.ui.RadioButtonGroup;
import limelight.util.Box;
import limelight.Context;

import javax.imageio.ImageIO;
import java.awt.event.MouseEvent;
import java.awt.event.FocusEvent;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.IOException;

public class RadioButtonPanel extends BasePanel implements TextAccessor, InputPanel, RadioButtonGroupMember
{
  private boolean focused;
  private boolean imagesLoaded;
  private BufferedImage normalImage;
  private BufferedImage selectedImage;
  private BufferedImage focusImage;
  private boolean selected;
  private RadioButtonGroup radioButtonGroup;

  public RadioButtonPanel()
  {
    setSize(21, 21);
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

  public void loadImages()
  {
    if(imagesLoaded)
      return;
    try
    {
      normalImage = ImageIO.read(getClass().getClassLoader().getResource("limelight/ui/images/radio_button.png"));
      selectedImage = ImageIO.read(getClass().getClassLoader().getResource("limelight/ui/images/radio_button_selected.png"));
      focusImage = ImageIO.read(getClass().getClassLoader().getResource("limelight/ui/images/radio_button_focus.png"));
      imagesLoaded = true;
    }
    catch(IOException e)
    {
      throw new RuntimeException(e);
    }
  }

  public void paintOn(Graphics2D graphics)
  {
    loadImages();
    if(focused)
      graphics.drawImage(focusImage, 0, 0, null);
    if(selected)
      graphics.drawImage(selectedImage, 0, 0, null);
    else
      graphics.drawImage(normalImage, 0, 0, null);
  }

  public ScreenableStyle getStyle()
  {
    return getParent().getStyle();
  }

  public void setText(PropablePanel panel, String text)
  {
    if("on".equals(text))
      selected = true;
    else
      selected = false;
  }

  public String getText()
  {
    return selected ? "on" : "off";
  }

  public boolean isSelected()
  {
    return selected;
  }

  public void setGroup(RadioButtonGroup radioButtonGroup)
  {
    this.radioButtonGroup = radioButtonGroup;
  }

  public void setSelected(boolean value)
  {
    if(value == selected)
      return;
    this.selected = value;
    if(selected)
      radioButtonGroup.buttonSelected(this);
    repaint();
  }

  public boolean getSelected()
  {
    return selected;
  }

  public void mousePressed(MouseEvent e)
  {
    super.mousePressed(e);
  }

  public void mouseReleased(MouseEvent e)
  {
    super.mouseReleased(e);
    setSelected(true);
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
