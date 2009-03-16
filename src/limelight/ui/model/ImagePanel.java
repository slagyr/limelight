//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.LimelightError;
import limelight.styles.Style;
import limelight.ui.Panel;
import limelight.util.Box;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.IOException;

public class ImagePanel extends BasePanel
{
  private double rotation;
  private String imageFile;
  private Image image;
  private AffineTransform transform;
  private double rotatedWidth;
  private double rotatedHeight;
  private double imageWidth;
  private double imageHeight;
  private boolean scaled = true;

  public Box getChildConsumableArea()
  {
    return null;
  }

  public Box getBoxInsidePadding()
  {
    return null;
  }

  public Style getStyle()
  {
    return getParent().getStyle();
  }

  public void setParent(Panel panel)
  {
    super.setParent(panel);
    panel.sterilize();
  }

  public void doLayout()
  {
    Style style = getStyle();
    Box consumableArea = getParent().getChildConsumableArea();
    getImage();
    if(image == null)
      return;

    width = consumableArea.width;
    height = consumableArea.height;

    if(image != null)
    {
      if(rotation != 0.0)
      {
        double cos = Math.abs(Math.cos(Math.toRadians(rotation)));
        double sin = Math.abs(Math.sin(Math.toRadians(rotation)));
        rotatedWidth = imageWidth * cos + imageHeight * sin;
        rotatedHeight = imageWidth * sin + imageHeight * cos;
      }
      else
      {
        rotatedWidth = imageWidth;
        rotatedHeight = imageHeight;
      }

      if(style.getCompiledHeight().isAuto())
        height = (int) rotatedHeight;

      if(style.getCompiledWidth().isAuto())
        width = (int) rotatedWidth;
    }
  }

  public void paintOn(Graphics2D graphics)
  {
    graphics.drawImage(getImage(), getTransform(), null);
  }

  public AffineTransform getTransform()
  {
    transform = new AffineTransform();
    getImage();
    if(image == null)
      return transform;

    if(scaled)
      applyScaling();
    applyRotation();

    return transform;
  }

  private void applyRotation()
  {
    if(rotation != 0.0)
    {
      double dx = (rotatedWidth - imageWidth) / 2.0;
      double dy = (rotatedHeight - imageHeight) / 2.0;
      double halfWidth = imageWidth / 2.0;
      double halfHeight = imageHeight / 2.0;
      double theta = Math.toRadians(rotation);
      transform.translate(halfWidth + dx, halfHeight + dy);
      transform.rotate(theta);
      transform.translate(halfWidth * -1, halfHeight * -1);
    }
  }

  private void applyScaling()
  {
    boolean differentWidth = Math.abs(width - rotatedWidth) > 0.5;
    boolean differentHeight = Math.abs(height - rotatedHeight) > 0.5;
    if(differentWidth || differentHeight)
    {
      double dx = width / rotatedWidth;
      double dy = height / rotatedHeight;
      transform.scale(dx, dy);
    }
  }

  public Image getImage()
  {
    if(image == null)
    {
      try
      {
        RootPanel rootPanel = getRoot();
        if(rootPanel != null)
        {
          image = rootPanel.getImageCache().getImage(imageFile);
          imageWidth = image.getWidth(null);
          imageHeight = image.getHeight(null);
        }
      }
      catch(IOException e)
      {
        throw new LimelightError("Could not load image: " + imageFile);
      }
    }
    return image;
  }

  public void setRotation(double angle)
  {
    rotation = angle;
    getParent().setNeedsLayout();   //TODO Neede to propogate size change here.
    if(getParent().getParent() != null)
      getParent().getParent().setNeedsLayout();
  }

  public double getRotation()
  {
    return rotation;
  }

  public void setImageFile(String filePath)
  {
    imageFile = filePath;
  }

  public String getImageFile()
  {
    return imageFile;
  }

  public boolean isScaled()
  {
    return scaled;
  }

  public void setScaled(boolean b)
  {
    scaled = b;
    getParent().setNeedsLayout();
  }
}
