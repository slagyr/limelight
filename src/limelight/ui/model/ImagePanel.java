//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model;

import limelight.LimelightException;
import limelight.styles.ScreenableStyle;

import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.ByteArrayInputStream;

public class ImagePanel extends PanelBase
{
  private double rotation;
  private String filename;
  private Image image;
  private AffineTransform transform;
  private double rotatedWidth;
  private double rotatedHeight;
  private double imageWidth;
  private double imageHeight;
  private boolean scaled = true;

  public ScreenableStyle getStyle()
  {
    return getParent().getStyle();
  }

  @Override
  public void setParent(ParentPanelBase panel)
  {
    super.setParent(panel);
    panel.sterilize();
  }

  public double getImageHeight()
  {
    return imageHeight;
  }

  public double getImageWidth()
  {
    return imageWidth;
  }

  public double getRotatedHeight()
  {
    return rotatedHeight;
  }

  public double getRotatedWidth()
  {
    return rotatedWidth;
  }

  public void setRotatedHeight(double height)
  {
    rotatedHeight = height;
  }

  public void setRotatedWidth(double width)
  {
    rotatedWidth = width;
  }

  public void doLayout()
  {
    ImagePanelLayout.instance.doLayout(this);
  }

  public Layout getDefaultLayout()
  {
    return ImagePanelLayout.instance;
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
    if(image == null && filename != null && filename.trim().length() > 0)
    {
      try
      {
        Scene rootPanel = getRoot();
        if(rootPanel != null && filename != null)
        {
          ImageCache imageCache = rootPanel.getImageCache();
          setImage(imageCache.getImage(filename));
        }
      }
      catch(Exception e)
      {
        throw new LimelightException("Could not load image: " + filename + " (" + e.toString() + ")");
      }
    }
    return image;
  }

  private void setImage(Image theImage)
  {
    image = theImage;
    imageWidth = image.getWidth(null);
    imageHeight = image.getHeight(null);
  }

  public void setRotation(double angle)
  {
    rotation = angle;
    markAsDirty();
    getParent().markAsDirty();
    markAsNeedingLayout();
    doPropagateSizeChangeUp(getParent());
  }

  public double getRotation()
  {
    return rotation;
  }

  public void setFilename(String filePath)
  {
    filename = filePath;
  }

  public String getFilename()
  {
    return filename;
  }

  public boolean isScaled()
  {
    return scaled;
  }

  public void setScaled(boolean b)
  {
    scaled = b;
    markAsNeedingLayout();
    getParent().markAsNeedingLayout();
  }

  public void setData(byte[] bytes) throws Exception
  {
    ImageInputStream imageInput = new MemoryCacheImageInputStream(new ByteArrayInputStream(bytes));
    setImage(ImageIO.read(imageInput));
    filename = "[DATA]";

    markAsNeedingLayout();
    getParent().markAsNeedingLayout();
  }
}
