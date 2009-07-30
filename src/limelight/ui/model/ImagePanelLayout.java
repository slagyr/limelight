package limelight.ui.model;

import limelight.ui.Panel;
import limelight.styles.Style;
import limelight.util.Box;

class ImagePanelLayout implements Layout
{
  public static ImagePanelLayout instance = new ImagePanelLayout();

  public void doLayout(Panel thePanel)
  {
    ImagePanel panel = (ImagePanel)thePanel;
    panel.resetLayout();
    Style style = panel.getStyle();
    Box consumableArea = panel.getParent().getChildConsumableArea();
    if(panel.getImage() == null)
      return;

    int width = consumableArea.width;
    int height = consumableArea.height;

    if(panel.getImage() != null)
    {
      if(panel.getRotation() != 0.0)
      {
        double cos = Math.abs(Math.cos(Math.toRadians(panel.getRotation())));
        double sin = Math.abs(Math.sin(Math.toRadians(panel.getRotation())));
        panel.setRotatedWidth(panel.getImageWidth() * cos + panel.getImageHeight() * sin);
        panel.setRotatedHeight(panel.getImageWidth() * sin + panel.getImageHeight() * cos);
      }
      else
      {
        panel.setRotatedWidth(panel.getImageWidth());
        panel.setRotatedHeight(panel.getImageHeight());
      }

      if(style.getCompiledHeight().isAuto())
        height = (int)(panel.getRotatedHeight() + 0.5);

      if(style.getCompiledWidth().isAuto())
        width = (int)(panel.getRotatedWidth() + 0.5);
    }
    panel.setSize(width, height);
  }

  public boolean overides(Layout other)
  {
    return true;
  }
}
