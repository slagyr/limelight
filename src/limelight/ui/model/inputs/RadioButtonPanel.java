//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model.inputs;

import limelight.events.Event;
import limelight.styles.Style;
import limelight.events.EventAction;
import limelight.ui.events.panel.ButtonPushedEvent;
import limelight.ui.events.panel.PanelEvent;
import limelight.ui.images.Images;
import limelight.ui.RadioButtonGroupMember;
import limelight.ui.RadioButtonGroup;
import limelight.ui.model.Scene;
import limelight.ui.model.ScenePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class RadioButtonPanel extends AbstractButtonPanel implements RadioButtonGroupMember
{
  private boolean imagesLoaded;
  private BufferedImage normalImage;
  private BufferedImage selectedImage;
  private BufferedImage focusImage;
  private boolean selected;
  private RadioButtonGroup radioButtonGroup;
  private String group;

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
    setSelected("on".equals(text.toLowerCase()));
  }

  public String getText()
  {
    return selected ? "on" : "off";
  }

  public boolean isSelected()
  {
    return selected;
  }

  public void setButtonGroup(RadioButtonGroup radioButtonGroup)
  {
    this.radioButtonGroup = radioButtonGroup;
  }

  public void setGroup(String name)
  {
    this.group = name;
    establishButtonGroup(name);
  }

  public String getGroup()
  {
    return group;
  }

  private void establishButtonGroup(String name)
  {
    if(radioButtonGroup != null)
      radioButtonGroup.remove(this);
    final Scene scene = getRoot();
    if(scene != null)
    {
      final RadioButtonGroup group = scene.getButtonGroups().get(name);
      group.add(this);
    }
  }

  public void setSelected(boolean value)
  {
    if(value == selected)
      return;

    selected = value;
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

    public void invoke(Event e)
    {
      if(e.isConsumed())
        return;

      PanelEvent event = (PanelEvent) e;
      final RadioButtonPanel panel = (RadioButtonPanel) event.getRecipient();
      panel.setSelected(!panel.isSelected());
    }
  }
}
