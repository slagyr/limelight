package limelight.builtin.players;

import limelight.ui.events.panel.PanelEvent;
import limelight.ui.model.ImagePanel;
import limelight.ui.model.PropPanel;

public class Image
{
  private PropPanel propPanel;
  private ImagePanel imagePanel;

  public void install(PanelEvent event)
  {
    imagePanel = new ImagePanel();
    propPanel = (PropPanel) event.getRecipient();
    propPanel.add(imagePanel);
    propPanel.getStagehands().put("image", this);
  }

  public PropPanel getPropPanel()
  {
    return propPanel;
  }

  public ImagePanel getImagePanel()
  {
    return imagePanel;
  }

  public void setFilename(String path)
  {
    imagePanel.setFilename(path);
  }

  public String getFilename()
  {
    return imagePanel.getFilename();
  }

  public void setRotation(double angle)
  {
    imagePanel.setRotation(angle);
  }

  public double getRotation()
  {
    return imagePanel.getRotation();
  }

  public boolean isScaled()
  {
    return imagePanel.isScaled();
  }

  public void setScaled(boolean value)
  {
    imagePanel.setScaled(value);
  }
}
