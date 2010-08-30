//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.styles.ScreenableStyle;
import limelight.ui.EventAction;
import limelight.ui.events.ButtonPushedEvent;
import limelight.ui.events.MouseClickedEvent;
import limelight.ui.model.*;
import limelight.ui.RadioButtonGroupMember;
import limelight.ui.RadioButtonGroup;
import limelight.util.Box;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.IOException;

public class RadioButtonPanel extends AbstractButtonPanel implements TextAccessor, RadioButtonGroupMember
{
  private boolean imagesLoaded;
  private BufferedImage normalImage;
  private BufferedImage selectedImage;
  private BufferedImage focusImage;
  private boolean selected;
  private RadioButtonGroup radioButtonGroup;

  public RadioButtonPanel()
  {
    setSize(21, 21);
    getEventHandler().add(ButtonPushedEvent.class, SelectAction.instance);
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
    if(hasFocus())
      graphics.drawImage(focusImage, 0, 0, null);
    if(selected)
      graphics.drawImage(selectedImage, 0, 0, null);
    else
      graphics.drawImage(normalImage, 0, 0, null);
  }

  public void setText(PropablePanel panel, String text)
  {
    selected = "on".equals(text);
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
    markAsDirty();
  }

  public boolean getSelected()
  {
    return selected;
  }

  private static class SelectAction implements EventAction
  {
    private static SelectAction instance = new SelectAction();

    public void invoke(limelight.ui.events.Event event)
    {
      final RadioButtonPanel panel = (RadioButtonPanel) event.getPanel();
      panel.setSelected(!panel.isSelected());        
    }
  }
}
