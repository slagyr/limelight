package limelight.ui.ninepatch;

import limelight.ui.model.Drawable;
import limelight.util.Box;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public class NinePatch implements Drawable
{
  private BufferedImage image = null;
  private Info info;

  private NinePatch(BufferedImage image, Info info)
  {
    this.image = image;
    this.info = info;
  }

  public static NinePatch load(BufferedImage image)
  {
    Info info = Info.inspect(image);
    return new NinePatch(image, info);
  }

  public Image getImage()
  {
    return image;
  }

  public void draw(Graphics2D graphics, int x, int y, int width, int height)
  {
    int midWidth = width - (info.topLeftBounds.width + info.topRightBounds.width);
    int midHeight = height - (info.topLeftBounds.height + info.bottomLeftBounds.height);
    int x2 = x + info.topLeftBounds.width;
    int x3 = x2 + midWidth;
    int y2 = y + info.topLeftBounds.height;
    int y3 = y2 + midHeight;

    // Row 1
    drawBox(graphics, info.topLeftBounds, x, y, info.topLeftBounds.width, info.topLeftBounds.height);
    if(midWidth > 0)
      drawBox(graphics, info.topMiddleBounds, x2, y, midWidth, info.topMiddleBounds.height);
    drawBox(graphics, info.topRightBounds, x3, y, info.topRightBounds.width, info.topRightBounds.height);

    // Row 2
    if(midHeight > 0)
    {
      drawBox(graphics, info.middleLeftBounds, x, y2, info.middleLeftBounds.width, midHeight);
      if(midWidth > 0)
        drawBox(graphics, info.middleMiddleBounds, x2, y2, midWidth, midHeight);
      drawBox(graphics, info.middleRightBounds, x3, y2, info.middleRightBounds.width, midHeight);
    }

    // Row 3
    drawBox(graphics, info.bottomLeftBounds, x, y3, info.bottomLeftBounds.width, info.bottomLeftBounds.height);
    if(midWidth > 0)
      drawBox(graphics, info.bottomMiddleBounds, x2, y3, midWidth, info.bottomMiddleBounds.height);
    drawBox(graphics, info.bottomRightBounds, x3, y3, info.bottomRightBounds.width, info.bottomRightBounds.height);
  }

  private void drawBox(Graphics2D graphics, Box source, int x, int y, int width, int height)
  {
    graphics.drawImage(image,
      x, y, x + width, y + height,
      source.left(), source.top(), source.right() + 1, source.bottom() + 1, null);
  }

  public static class Info
  {
    public Box topLeftBounds;
    public Box topMiddleBounds;
    public Box topRightBounds;
    public Box middleLeftBounds;
    public Box middleMiddleBounds;
    public Box middleRightBounds;
    public Box bottomLeftBounds;
    public Box bottomMiddleBounds;
    public Box bottomRightBounds;

    public static Info inspect(BufferedImage image)
    {
      Info info = new Info();
      info.build(image);
      return info;
    }

    private void build(BufferedImage image)
    {
      Raster raster = image.getRaster();
      int verticalStretchStart = verticalStretchStart(image, raster);
      int verticalStretchEnd = verticalStretchEnd(image, raster);
      int horizontalStretchStart = horizontalStretchStart(image, raster);
      int horizontalStretchEnd = horizontalStretchEnd(image, raster);

      int heightForRow1 = verticalStretchStart - 1;
      int heightForRow2 = verticalStretchEnd - heightForRow1;
      int heightForRow3 = image.getHeight() - verticalStretchEnd - 2;
      int widthForCol1 = horizontalStretchStart - 1;
      int widthForCol2 = horizontalStretchEnd - widthForCol1;
      int widthForCol3 = image.getWidth() - widthForCol1 - widthForCol2 - 2;

      topLeftBounds = new Box(1, 1, widthForCol1, heightForRow1);
      topMiddleBounds = new Box(1 + widthForCol1, 1, widthForCol2, heightForRow1);
      topRightBounds = new Box(1 + widthForCol1 + widthForCol2, 1, widthForCol3, heightForRow1);
      middleLeftBounds = new Box(1, 1 + heightForRow1, widthForCol1, heightForRow2);
      middleMiddleBounds = new Box(1 + widthForCol1, 1 + heightForRow1, widthForCol2, heightForRow2);
      middleRightBounds = new Box(1 + widthForCol1 + widthForCol2, 1 + heightForRow1, widthForCol3, heightForRow2);
      bottomLeftBounds = new Box(1, 1 + heightForRow1 + heightForRow2, widthForCol1, heightForRow3);
      bottomMiddleBounds = new Box(1 + widthForCol1, 1 + heightForRow1 + heightForRow2, widthForCol2, heightForRow3);
      bottomRightBounds = new Box(1 + widthForCol1 + widthForCol2, 1 + heightForRow1 + heightForRow2, widthForCol3, heightForRow3);
    }

    private int horizontalStretchEnd(BufferedImage image, Raster raster)
    {
      int[] pixel = new int[4];
      for(int x = image.getWidth() - 1; x > 0; --x)
      {
        raster.getPixel(x, 0, pixel);
        if(isBlack(pixel))
          return x;
      }
      throw new RuntimeException("The 9 Patch image does not have a top horizontal stretch line.");
    }

    public int horizontalStretchStart(BufferedImage image, Raster raster)
    {
      int[] pixel = new int[4];
      for(int x = 0; x < image.getWidth(); ++x)
      {
        raster.getPixel(x, 0, pixel);
        if(isBlack(pixel))
          return x;
      }
      throw new RuntimeException("The 9 Patch image does not have a top horizontal stretch line.");
    }

    private int verticalStretchEnd(BufferedImage image, Raster raster)
    {
      int[] pixel = new int[4];
      for(int y = image.getHeight() - 1; y >= 0; --y)
      {
        raster.getPixel(0, y, pixel);
        if(isBlack(pixel))
          return y;
      }
      throw new RuntimeException("The 9 Patch image does not have a left vertical stretch line.");
    }

    public int verticalStretchStart(BufferedImage image, Raster raster)
    {
      int[] pixel = new int[4];
      for(int y = 0; y < image.getHeight(); ++y)
      {
        raster.getPixel(0, y, pixel);
        if(isBlack(pixel))
          return y;
      }
      throw new RuntimeException("The 9 Patch image does not have a left vertical stretch line.");
    }

    private boolean isBlack(int[] pixel)
    {
      return pixel[0] == 0 && pixel[1] == 0 && pixel[2] == 0 && pixel[3] == 255;
    }
  }
}
