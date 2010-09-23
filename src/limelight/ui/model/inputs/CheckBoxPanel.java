//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.events.Event;
import limelight.styles.Style;
import limelight.events.EventAction;
import limelight.ui.events.panel.ButtonPushedEvent;
import limelight.ui.events.panel.PanelEvent;
import limelight.ui.images.Images;

import java.awt.*;
import java.awt.image.BufferedImage;

public class CheckBoxPanel extends AbstractButtonPanel
{
  private boolean imagesLoaded;
  private BufferedImage normalImage;
  private BufferedImage selectedImage;
  private BufferedImage focusImage;
  private boolean selected;

  public CheckBoxPanel()
  {
    getEventHandler().add(ButtonPushedEvent.class, SelectAction.instance);
  }

  @Override
  protected void setDefaultStyles(Style style)
  {
    style.setDefault(Style.WIDTH, 20);
    style.setDefault(Style.HEIGHT, 20);
  }

  public void loadImages()
  {
    if(imagesLoaded)
      return;
    
    normalImage = Images.load("checkbox.png");
    selectedImage = Images.load("checkbox_selected.png");
    focusImage = Images.load("checkbox_focus.png");
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

  public void setSelected(boolean value)
  {
    if(value == selected)
      return;

    this.selected = value;

    markAsDirty();
    valueChanged();
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
      PanelEvent event = (PanelEvent)e;
      if(event.isConsumed())
        return;

      final CheckBoxPanel panel = (CheckBoxPanel) event.getRecipient();
      panel.setSelected(!panel.isSelected());
    }
  }
}
