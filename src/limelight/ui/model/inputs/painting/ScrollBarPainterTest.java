package limelight.ui.model.inputs.painting;

import limelight.ui.MockGraphics;
import limelight.ui.model.inputs.ScrollBarPanel;
import limelight.util.Box;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ScrollBarPainterTest
{
  private ScrollBarPanel scrollBar;
  private MockGraphics graphics;
  private ScrollBarPainter.ScrollBarImages images;
  private ScrollBarPainter painter = ScrollBarPainter.instance;


  private void setUpHorizontally()
  {
    scrollBar = new ScrollBarPanel(ScrollBarPanel.HORIZONTAL);
    scrollBar.setSize(100, 15);
    scrollBar.setValue(0);
    scrollBar.configure(1, 100);
    graphics = new MockGraphics();
    images = ScrollBarPainter.horizontalImages;
  }

  private void setUpVertically()
  {
    scrollBar = new ScrollBarPanel(ScrollBarPanel.VERTICAL);
    scrollBar.setSize(15, 100);
    scrollBar.setValue(0);
    scrollBar.configure(1, 100);
    graphics = new MockGraphics();
    images = ScrollBarPainter.verticalImages;
  }

  @Test
  public void shouldDrawAllImagesForHorizontalScrollBar() throws Exception
  {
    setUpHorizontally();

    painter.paintOn(graphics, scrollBar);

    for(int i = 0; i < 10; i++)
      assertEquals(images.background, graphics.drawnImages.get(i).image);
    assertEquals(images.cap, graphics.drawnImages.get(10).image);
    assertEquals(images.buttons, graphics.drawnImages.get(11).image);
    assertEquals(images.sliderCapOutside, graphics.drawnImages.get(12).image);
    assertEquals(images.sliderCapInside, graphics.drawnImages.get(13).image);
  }

  @Test
  public void shouldDrawAllImagesForVerticalScrollBar() throws Exception
  {
    setUpVertically();

    painter.paintOn(graphics, scrollBar);

    for(int i = 0; i < 10; i++)
      assertEquals(images.background, graphics.drawnImages.get(i).image);
    assertEquals(images.cap, graphics.drawnImages.get(10).image);
    assertEquals(images.buttons, graphics.drawnImages.get(11).image);
    assertEquals(images.sliderCapOutside, graphics.drawnImages.get(12).image);
    assertEquals(images.sliderCapInside, graphics.drawnImages.get(13).image);
  }

  @Test
  public void shouldStartHorizontalSliderAtLeftEdgeForMinValue() throws Exception
  {
    setUpHorizontally();
    
    painter.paintOn(graphics, scrollBar);

    MockGraphics.DrawnImage outsideSliderCap = graphics.drawnImages.get(12);
    MockGraphics.DrawnImage insideSliderCap = graphics.drawnImages.get(13);
    assertEquals(5, outsideSliderCap.x);
    assertEquals(13, insideSliderCap.x);
  }

  @Test
  public void shouldStartVerticallySliderAtLeftEdgeForMinValue() throws Exception
  {
    setUpVertically();
    painter.paintOn(graphics, scrollBar);

    MockGraphics.DrawnImage outsideSliderCap = graphics.drawnImages.get(12);
    MockGraphics.DrawnImage insideSliderCap = graphics.drawnImages.get(13);
    assertEquals(5, outsideSliderCap.y);
    assertEquals(13, insideSliderCap.y);
  }

  @Test
  public void shouldPaintWideSliderHorizontally() throws Exception
  {
    setUpHorizontally();
    scrollBar.configure(90, 100);
    
//    System.err.println("scrollBar.getSliderSize() = " + scrollBar.getSliderSize());
//    scrollBar.getSliderSize() = 56

    painter.paintOn(graphics, scrollBar);
    MockGraphics.DrawnImage outsideSliderCap = graphics.drawnImages.get(12);
    MockGraphics.DrawnImage insideSliderCap = graphics.drawnImages.get(13);

    assertEquals(5, outsideSliderCap.x);
    assertEquals(53, insideSliderCap.x);

    MockGraphics fillerGraphics = graphics.subGraphics.get(0);
    assertEquals(13, fillerGraphics.clip.x);
    assertEquals(-5, fillerGraphics.drawnImages.get(0).x);
    assertEquals(12, fillerGraphics.drawnImages.get(1).x);
    assertEquals(29, fillerGraphics.drawnImages.get(2).x);
  }

  @Test
  public void shouldPaintTallSliderVertically() throws Exception
  {
    setUpVertically();
    scrollBar.configure(90, 100);

    painter.paintOn(graphics, scrollBar);
    MockGraphics.DrawnImage outsideSliderCap = graphics.drawnImages.get(12);
    MockGraphics.DrawnImage insideSliderCap = graphics.drawnImages.get(13);

    assertEquals(5, outsideSliderCap.y);
    assertEquals(53, insideSliderCap.y);

    MockGraphics fillerGraphics = graphics.subGraphics.get(0);
    assertEquals(13, fillerGraphics.clip.y);
    assertEquals(-5, fillerGraphics.drawnImages.get(0).y);
    assertEquals(12, fillerGraphics.drawnImages.get(1).y);
    assertEquals(29, fillerGraphics.drawnImages.get(2).y);
  }

  @Test
  public void paintsAtTheCorrectStartingPointVertically() throws Exception
  {
    setUpVertically();
    scrollBar.configure(10, 100);
    scrollBar.setValue(50);

    painter.paintOn(graphics, scrollBar);
    
    MockGraphics.DrawnImage outsideSliderCap = graphics.drawnImages.get(12);
    assertEquals(scrollBar.getSliderPosition(), outsideSliderCap.y);
  }

  @Test
  public void paintsAtTheCorrectStartingPointHorizontally() throws Exception
  {
    setUpHorizontally();
    scrollBar.configure(10, 100);
    scrollBar.setValue(50);

    painter.paintOn(graphics, scrollBar);

    MockGraphics.DrawnImage outsideSliderCap = graphics.drawnImages.get(12);
    assertEquals(scrollBar.getSliderPosition(), outsideSliderCap.x);
  }

  @Test
  public void paintsActivatedIncreasingButtonVertically() throws Exception
  {
    setUpVertically();
    scrollBar.setIncreasingButtonActive(true);

    painter.paintOn(graphics, scrollBar);

    assertEquals(images.buttonsOutsideSelected, graphics.drawnImages.get(11).image);
  }

  @Test
  public void paintsActivatedDecreasingButtonVertically() throws Exception
  {
    setUpVertically();
    scrollBar.setDecreasingButtonActive(true);

    painter.paintOn(graphics, scrollBar);

    assertEquals(images.buttonsInsideSelected, graphics.drawnImages.get(11).image);
  }

  @Test
  public void paintsActivatedIncreasingButtonHorizontally() throws Exception
  {
    setUpHorizontally();
    scrollBar.setIncreasingButtonActive(true);

    painter.paintOn(graphics, scrollBar);

    assertEquals(images.buttonsOutsideSelected, graphics.drawnImages.get(11).image);
  }
  
  @Test
  public void paintsActivatedDecreasingButtonHorizontally() throws Exception
  {
    setUpHorizontally();
    scrollBar.setDecreasingButtonActive(true);

    painter.paintOn(graphics, scrollBar);

    assertEquals(images.buttonsInsideSelected, graphics.drawnImages.get(11).image);
  }

  @Test
  public void shouldBuildIncreaseBoxForHorizontal() throws Exception
  {
    setUpHorizontally();
    scrollBar.setSize(100, 15);

    Box increasing = painter.getIncreasingBox(scrollBar);

    assertEquals(new Box(84, 0, 16, 15), increasing);
  }

  @Test
  public void shouldBuildIncreaseBoxForVertical() throws Exception
  {
    setUpVertically();
    scrollBar.setSize(15, 100);

    Box increasing = painter.getIncreasingBox(scrollBar);

    assertEquals(new Box(0, 84, 15, 16), increasing);
  }

  @Test
  public void shouldBuildDecreaseBoxForHorizontal() throws Exception
  {
    setUpHorizontally();
    scrollBar.setSize(100, 15);

    Box increasing = painter.getDecreasingBox(scrollBar);

    assertEquals(new Box(67, 0, 17, 15), increasing);
  }

  @Test
  public void shouldBuildDecreaseBoxForVertical() throws Exception
  {
    setUpVertically();
    scrollBar.setSize(15, 100);

    Box increasing = painter.getDecreasingBox(scrollBar);

    assertEquals(new Box(0, 67, 15, 16), increasing);
  }

  @Test
  public void buildsTrackBoxVertically() throws Exception
  {
    setUpVertically();
    scrollBar.setSize(15, 100);

    Box track = painter.getTrackBox(scrollBar);

    assertEquals(new Box(0, 5, 15, 62), track);
  }

  @Test
  public void buildsTrackBoxHorizontally() throws Exception
  {
    setUpHorizontally();
    scrollBar.setSize(100, 15);

    Box track = painter.getTrackBox(scrollBar);

    assertEquals(new Box(5, 0, 62, 15), track);
  }

  @Test
  public void onlyTrackIsDrawnWhenGemIsLargerThanTrack() throws Exception
  {
    setUpHorizontally();
    scrollBar.setSize(100, 15);
    scrollBar.configure(100, 50);

    painter.paintOn(graphics, scrollBar);
    
    assertEquals(10, graphics.drawnImages.size());
    for(MockGraphics.DrawnImage drawnImage : graphics.drawnImages)
    {
      assertEquals(images.background, drawnImage.image);
    }
  }
   
  @Test
  public void onlyTrackIsDrawnWhenGemIsSameSizeAsTrack() throws Exception
  {
    setUpVertically();
    scrollBar.setSize(15, 100);
    scrollBar.configure(100, 100);

    painter.paintOn(graphics, scrollBar);

    assertEquals(10, graphics.drawnImages.size());
    for(MockGraphics.DrawnImage drawnImage : graphics.drawnImages)
    {
      assertEquals(images.background, drawnImage.image);
    }
  }

//  TODO MDM Need to paint pressed button even if at max or min

}
