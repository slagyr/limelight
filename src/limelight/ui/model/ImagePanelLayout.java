//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model;

import limelight.styles.Style;
import limelight.styles.values.AutoDimensionValue;
import limelight.ui.Panel;
import limelight.util.Box;

import java.awt.*;
import java.util.Map;

class ImagePanelLayout extends SimpleLayout
{
  public static ImagePanelLayout instance = new ImagePanelLayout();

  @Override
  public void doExpansion(Panel thePanel)
  {
    ImagePanel panel = (ImagePanel) thePanel;
    Box consumableArea = panel.getParent().getChildConsumableBounds();
    if(panel.getImage() == null)
      return;


    Dimension size;
    if(panel.isScaled())
      size = new Dimension(consumableArea.width, consumableArea.height);
    else
      size = new Dimension((int) (panel.getImageWidth() + 0.5), (int) (panel.getImageHeight() + 0.5));

    if(panel.getImage() != null)
    {
      handleRotation(panel);
      handleAutoDimensions(panel, size);
    }

    panel.setSize(size.width, size.height);
  }

  public void doLayout(Panel thePanel, Map<Panel, Layout> panelsToLayout)
  {
//    ImagePanel panel = (ImagePanel) thePanel;
//    Box consumableArea = panel.getParent().getChildConsumableBounds();
//    if(panel.getImage() == null)
//      return;
//
//
//    Dimension size;
//    if(panel.isScaled())
//      size = new Dimension(consumableArea.width, consumableArea.height);
//    else
//      size = new Dimension((int) (panel.getImageWidth() + 0.5), (int) (panel.getImageHeight() + 0.5));
//
//    if(panel.getImage() != null)
//    {
//      handleRotation(panel);
//      handleAutoDimensions(panel, size);
//    }
//
//    panel.setSize(size.width, size.height);
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
    boolean autoWidth = style.getCompiledWidth() instanceof AutoDimensionValue;
    boolean autoHeight = style.getCompiledHeight() instanceof AutoDimensionValue;
    if(autoWidth && autoHeight)
    {
      size.width = (int) (panel.getRotatedWidth() + 0.5);
      size.height = (int) (panel.getRotatedHeight() + 0.5);
    }
    else if(autoWidth)
    {
      double ratio = size.height / panel.getRotatedHeight();
      size.width = (int) (panel.getRotatedWidth() * ratio + 0.5);
    }
    else if(autoHeight)
    {
      double ratio = size.width / panel.getRotatedWidth();
      size.height = (int) (panel.getRotatedHeight() * ratio + 0.5);
    }
  }

  public boolean overides(Layout other)
  {
    return true;
  }

  public void doLayout(Panel thePanel, Map<Panel, Layout> panelsToLayout, boolean topLevel)
  {
    doLayout(thePanel, null);
  }
}
