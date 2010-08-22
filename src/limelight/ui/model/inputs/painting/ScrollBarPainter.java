package limelight.ui.model.inputs.painting;

import limelight.ui.model.inputs.ScrollBarPanel;
import limelight.util.Box;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ScrollBarPainter
{
  private static final int MIN_GEM_SIZE = 16;
  private static final int INCREASING_BOX_LENGTH = 16;
  private static final int DECREASING_BOX_LENGTH = 17;
  private static final int CAP_LENGTH = 5;

  public static ScrollBarImages horizontalImages;
  public static ScrollBarImages verticalImages;
  public static ScrollBarPainter instance = new ScrollBarPainter();

  public static class ScrollBarImages
  {
    public BufferedImage cap;
    public BufferedImage background;
    public BufferedImage buttons;
    public BufferedImage buttonsInsideSelected;
    public BufferedImage buttonsOutsideSelected;
    public BufferedImage sliderCapInside;
    public BufferedImage sliderCapOutside;
    public BufferedImage sliderFiller;
  }

  static
  {
    try
    {
      ClassLoader classLoader = TextPanelBorderPainter.class.getClassLoader();

      horizontalImages = new ScrollBarImages();
      horizontalImages.background = ImageIO.read(classLoader.getResource("limelight/ui/images/scroll_horizontal_background.png"));
      horizontalImages.buttons = ImageIO.read(classLoader.getResource("limelight/ui/images/scroll_horizontal_buttons.png"));
      horizontalImages.buttonsInsideSelected = ImageIO.read(classLoader.getResource("limelight/ui/images/scroll_horizontal_buttons_left_selected.png"));
      horizontalImages.buttonsOutsideSelected = ImageIO.read(classLoader.getResource("limelight/ui/images/scroll_horizontal_buttons_right_selected.png"));
      horizontalImages.cap = ImageIO.read(classLoader.getResource("limelight/ui/images/scroll_horizontal_cap.png"));
      horizontalImages.sliderCapInside = ImageIO.read(classLoader.getResource("limelight/ui/images/scroll_horizontal_gem_cap_right.png"));
      horizontalImages.sliderCapOutside = ImageIO.read(classLoader.getResource("limelight/ui/images/scroll_horizontal_gem_cap_left.png"));
      horizontalImages.sliderFiller = ImageIO.read(classLoader.getResource("limelight/ui/images/scroll_horizontal_gem_filler.png"));

      verticalImages = new ScrollBarImages();
      verticalImages.background = ImageIO.read(classLoader.getResource("limelight/ui/images/scroll_vertical_background.png"));
      verticalImages.buttons = ImageIO.read(classLoader.getResource("limelight/ui/images/scroll_vertical_buttons.png"));
      verticalImages.buttonsInsideSelected = ImageIO.read(classLoader.getResource("limelight/ui/images/scroll_vertical_buttons_up_selected.png"));
      verticalImages.buttonsOutsideSelected = ImageIO.read(classLoader.getResource("limelight/ui/images/scroll_vertical_buttons_down_selected.png"));
      verticalImages.cap = ImageIO.read(classLoader.getResource("limelight/ui/images/scroll_vertical_cap.png"));
      verticalImages.sliderCapInside = ImageIO.read(classLoader.getResource("limelight/ui/images/scroll_vertical_gem_cap_bottom.png"));
      verticalImages.sliderCapOutside = ImageIO.read(classLoader.getResource("limelight/ui/images/scroll_vertical_gem_cap_top.png"));
      verticalImages.sliderFiller = ImageIO.read(classLoader.getResource("limelight/ui/images/scroll_vertical_gem_filler.png"));
    }
    catch(IOException e)
    {
      throw new RuntimeException("Could not load ScrollBar images", e);
    }
    catch(Exception e)
    {
      System.err.println("e = " + e);
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  public int getOutsideCusion()
  {
    return CAP_LENGTH;
  }

  public int getInsideCushion()
  {
    return INCREASING_BOX_LENGTH + DECREASING_BOX_LENGTH;
  }

  public int getMinSliderSize()
  {
    return MIN_GEM_SIZE;
  }

  public void paintOn(Graphics2D graphics, ScrollBarPanel scrollBar)
  {
    Box bounds = scrollBar.getBoundingBox();
    if(scrollBar.isHorizontal())
      drawHorizontally(graphics, bounds, scrollBar);
    else
      drawVertically(graphics, bounds, scrollBar);
  }

  public Box getIncreasingBox(ScrollBarPanel scrollBar)
  {
    Box bounds = scrollBar.getBoundingBox();
    if(scrollBar.isHorizontal())
      return new Box(bounds.x + bounds.width - INCREASING_BOX_LENGTH, bounds.y, INCREASING_BOX_LENGTH, bounds.height);
    else
      return new Box(bounds.x, bounds.y + bounds.height - INCREASING_BOX_LENGTH, bounds.width, INCREASING_BOX_LENGTH);
  }
  
  public Box getDecreasingBox(ScrollBarPanel scrollBar)
  {
    Box bounds = scrollBar.getBoundingBox();
    if(scrollBar.isHorizontal())
      return new Box(bounds.x + bounds.width - INCREASING_BOX_LENGTH - DECREASING_BOX_LENGTH, bounds.y, DECREASING_BOX_LENGTH, bounds.height);
    else
      return new Box(bounds.x, bounds.y + bounds.height - INCREASING_BOX_LENGTH - DECREASING_BOX_LENGTH, bounds.width, INCREASING_BOX_LENGTH);
  }

  public Box getTrackBox(ScrollBarPanel scrollBar)
  {
    Box bounds = scrollBar.getBoundingBox();
    if(scrollBar.isHorizontal())
      return new Box(bounds.x + CAP_LENGTH, bounds.y, bounds.width - CAP_LENGTH - DECREASING_BOX_LENGTH - INCREASING_BOX_LENGTH, bounds.height);
    else
      return new Box(bounds.x, bounds.y + CAP_LENGTH, bounds.width, bounds.height - CAP_LENGTH - DECREASING_BOX_LENGTH - INCREASING_BOX_LENGTH);
  }

  private static void drawVertically(Graphics2D graphics, Box bounds, ScrollBarPanel scrollBar)
  {
    ScrollBarImages images = verticalImages;
    for(int y = 0; y < bounds.height; y += images.background.getHeight())
      graphics.drawImage(images.background, 0, y, null);

    if(scrollBar.getSliderSize() >= scrollBar.getTrackSize())
      return;

    graphics.drawImage(images.cap, 0, 0, null);

    graphics.drawImage(buttonsImageFor(scrollBar, images), 0, bounds.height - images.buttons.getHeight(), null);

    int y = scrollBar.getSliderPosition();
    graphics.drawImage(images.sliderCapOutside, 0, y, null);
    y += images.sliderCapOutside.getHeight();

    y = paintVerticalFiller(graphics, scrollBar, images, y);

    graphics.drawImage(images.sliderCapInside, 0, y, null);
  }

  private static BufferedImage buttonsImageFor(ScrollBarPanel scrollBar, ScrollBarImages images)
  {
    if(scrollBar.isDecreasingButtonActive())
      return images.buttonsInsideSelected;
    else if(scrollBar.isIncreasingButtonActive())
      return images.buttonsOutsideSelected;
    else
      return images.buttons;
  }

  private static void drawHorizontally(Graphics2D graphics, Box bounds, ScrollBarPanel scrollBar)
  {
    ScrollBarImages images = horizontalImages;
    for(int x = 0; x < bounds.width; x += images.background.getWidth())
      graphics.drawImage(images.background, x, 0, null);
    
    if(scrollBar.getSliderSize() >= scrollBar.getTrackSize())
      return;

    graphics.drawImage(images.cap, 0, 0, null);
    graphics.drawImage(buttonsImageFor(scrollBar, images), bounds.width - images.buttons.getWidth(), 0, null);

    int x = scrollBar.getSliderPosition();
    graphics.drawImage(images.sliderCapOutside, x, 0, null);
    x += images.sliderCapOutside.getWidth();

    x = paintHorizontalFiller(graphics, scrollBar, images, x);

    graphics.drawImage(images.sliderCapInside, x, 0, null);
  }

  private static int paintHorizontalFiller(Graphics2D graphics, ScrollBarPanel scrollBar, ScrollBarImages images, int x)
  {
    int fillerSize = scrollBar.getSliderSize() - images.sliderCapOutside.getWidth() - images.sliderCapInside.getWidth();
    if(fillerSize > 0)
    {
      Graphics fillerGraphics = graphics.create(x, 0, fillerSize, images.sliderFiller.getHeight());
      int fillerWidth = images.sliderFiller.getWidth();
      int fillerX = scrollBar.getSliderPosition() % fillerWidth * -1;
      while(fillerX < fillerSize)
      {
        fillerGraphics.drawImage(images.sliderFiller, fillerX, 0, null);
        fillerX += fillerWidth;
      }
      x += fillerSize;
    }
    return x;
  }

  private static int paintVerticalFiller(Graphics2D graphics, ScrollBarPanel scrollBar, ScrollBarImages images, int y)
  {
    int fillerSize = scrollBar.getSliderSize() - images.sliderCapOutside.getHeight() - images.sliderCapInside.getHeight();
    if(fillerSize > 0)
    {
      Graphics fillerGraphics = graphics.create(0, y, images.sliderFiller.getWidth(), fillerSize);
      int fillerHeight = images.sliderFiller.getHeight();
      int fillerY = scrollBar.getSliderPosition() % fillerHeight * -1;
      while(fillerY < fillerSize)
      {
        fillerGraphics.drawImage(images.sliderFiller, 0, fillerY, null);
        fillerY += fillerHeight;
      }
      y += fillerSize;
    }
    return y;
  }
}
