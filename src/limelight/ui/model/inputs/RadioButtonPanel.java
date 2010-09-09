//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.styles.Style;
import limelight.ui.EventAction;
import limelight.ui.events.ButtonPushedEvent;
import limelight.ui.images.Images;
import limelight.ui.RadioButtonGroupMember;
import limelight.ui.RadioButtonGroup;

import java.awt.image.BufferedImage;
import java.awt.*;

public class RadioButtonPanel extends AbstractButtonPanel implements RadioButtonGroupMember
{
  private boolean imagesLoaded;
  private BufferedImage normalImage;
  private BufferedImage selectedImage;
  private BufferedImage focusImage;
  private boolean selected;
  private RadioButtonGroup radioButtonGroup;

  public RadioButtonPanel()
  {
    getEventHandler().add(ButtonPushedEvent.class, SelectAction.instance);
  }

  @Override
  protected void setDefaultStyles(Style style)
  {
    style.setDefault(Style.WIDTH, 21);
    style.setDefault(Style.HEIGHT, 21);
  }

  public void loadImages()
  {
    if(imagesLoaded)
      return;
    
    normalImage = Images.load("radio_button.png");
    selectedImage = Images.load("radio_button_selected.png");
    focusImage = Images.load("radio_button_focus.png");
    imagesLoaded = true;
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

  public void setText(String text)
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
    if(selected && radioButtonGroup != null)
      radioButtonGroup.buttonSelected(this);

    valueChanged();
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
      if(event.isConsumed())
        return;

      final RadioButtonPanel panel = (RadioButtonPanel) event.getRecipient();
      panel.setSelected(!panel.isSelected());
    }
  }
}
