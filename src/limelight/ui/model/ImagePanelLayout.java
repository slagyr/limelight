//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.ui.Panel;
import limelight.styles.Style;
import limelight.util.Box;

import java.awt.*;

class ImagePanelLayout implements Layout
{
  public static ImagePanelLayout instance = new ImagePanelLayout();

  public void doLayout(Panel thePanel)
  {
    ImagePanel panel = (ImagePanel)thePanel;
    panel.resetLayout();
    Box consumableArea = panel.getParent().getChildConsumableBounds();
    if(panel.getImage() == null)
      return;


    Dimension size;
    if(panel.isScaled())
      size = new Dimension(consumableArea.width, consumableArea.height);
    else
      size = new Dimension((int)(panel.getImageWidth() + 0.5), (int)(panel.getImageHeight() + 0.5));

    if(panel.getImage() != null)
    {
      handleRotation(panel);
      handleAutoDimensions(panel, size);
    }
  
    panel.setSize(size.width, size.height);
  }

  private void handleRotation(ImagePanel panel)
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
  }

  private void handleAutoDimensions(ImagePanel panel, Dimension size)
  {
    Style style = panel.getStyle();
    boolean autoWidth = style.getCompiledWidth().isAuto();
    boolean autoHeight = style.getCompiledHeight().isAuto();
    if(autoWidth && autoHeight)
    {
      size.width = (int)(panel.getRotatedWidth() + 0.5);
      size.height = (int)(panel.getRotatedHeight() + 0.5);
    }
    else if(autoWidth)
    {
      double ratio = size.height / panel.getRotatedHeight();
      size.width = (int)(panel.getRotatedWidth() * ratio + 0.5);
    }
    else if(autoHeight)
    {
      double ratio = size.width / panel.getRotatedWidth();
      size.height = (int)(panel.getRotatedHeight() * ratio + 0.5);
    }
  }

  public boolean overides(Layout other)
  {
    return true;
  }

  public void doLayout(Panel panel, boolean topLevel)
  {
    doLayout(panel);
  }
}
