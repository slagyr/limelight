package limelight.ui.ninepatch;

import limelight.util.Box;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public class NinePatch
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
    NinePatch patch = new NinePatch(image, info);
    return patch;
  }

  public Image getImage()
  {
    return image;
  }

  public void draw(Graphics2D graphics, int x, int y, int width, int height)
  {
    drawBox(graphics, info.topLeftBounds,   x, y);
    drawBox(graphics, info.topMiddleBounds, x+info.topLeftBounds.width, y);
    drawBox(graphics, info.topRightBounds,  x+info.topLeftBounds.width+info.topMiddleBounds.width, y);
    drawBox(graphics, info.middleLeftBounds, x, y+info.topLeftBounds.height);
    drawBox(graphics, info.middleMiddleBounds, x+info.topLeftBounds.width, y+info.topRightBounds.height);
    drawBox(graphics, info.middleRightBounds, x+info.topLeftBounds.width+info.topMiddleBounds.width, y+info.topRightBounds.height);
    int row3y = y + info.topLeftBounds.height + info.middleLeftBounds.height;
    drawBox(graphics, info.bottomLeftBounds, x, row3y);
    drawBox(graphics, info.bottomMiddleBounds, x+info.bottomLeftBounds.width, row3y);
    drawBox(graphics, info.bottomRightBounds, x+info.bottomLeftBounds.width+info.bottomMiddleBounds.width, row3y);
  }

  private void drawBox(Graphics2D graphics, Box source, int x, int y)
  {
    graphics.drawImage(image, x, y, x+source.width, y+source.height,
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
      for (int x = image.getWidth() - 1; x > 0; --x)
      {
        raster.getPixel(x, 0, pixel);
        if (isBlack(pixel))
          return x;
      }
      throw new RuntimeException("The 9 Patch image does not have a left horizontal stripe.");
    }

    public int horizontalStretchStart(BufferedImage image, Raster raster)
    {
      int[] pixel = new int[4];
      for (int x = 0; x < image.getWidth(); ++x)
      {
        raster.getPixel(x, 0, pixel);
        if (isBlack(pixel))
          return x;
      }
      throw new RuntimeException("The 9 Patch image does not have a left horizontal stripe.");
    }

    private int verticalStretchEnd(BufferedImage image, Raster raster)
    {
      int[] pixel = new int[4];
      for (int y = image.getHeight() - 1; y >= 0; --y)
      {
        raster.getPixel(0, y, pixel);
        if (isBlack(pixel))
          return y;
      }
      throw new RuntimeException("The 9 Patch image does not have a left vertical stripe.");
    }

    public int verticalStretchStart(BufferedImage image, Raster raster)
    {
      int[] pixel = new int[4];
      for (int y = 0; y < image.getHeight(); ++y)
      {
        raster.getPixel(0, y, pixel);
        if (isBlack(pixel))
          return y;
      }
      throw new RuntimeException("The 9 Patch image does not have a left vertical stripe.");
    }

    private boolean isBlack(int[] pixel)
    {
      return pixel[0] == 0 && pixel[1] == 0 && pixel[2] == 0 && pixel[3] == 255;
    }
  }
}
