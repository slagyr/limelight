package limelight.ui.model.inputs.painting;

import limelight.ui.MockGraphics;
import limelight.ui.model.inputs.ScrollBar2Panel;
import limelight.util.Box;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ScrollBarPainterTest
{
  private ScrollBar2Panel scrollBar;
  private MockGraphics graphics;
  private ScrollBarPainter.ScrollBarImages images;
  private ScrollBarPainter painter = ScrollBarPainter.instance;


  private void setUpHorizontally()
  {
    scrollBar = new ScrollBar2Panel(ScrollBar2Panel.HORIZONTAL);
    scrollBar.setSize(100, 15);
    scrollBar.setValue(0);
    scrollBar.configure(1, 100);
    graphics = new MockGraphics();
    images = ScrollBarPainter.horizontalImages;
  }

  private void setUpVertically()
  {
    scrollBar = new ScrollBar2Panel(ScrollBar2Panel.VERTICAL);
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
    assertEquals(images.gemCapOutside, graphics.drawnImages.get(12).image);
    assertEquals(images.gemCapInside, graphics.drawnImages.get(13).image);
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
    assertEquals(images.gemCapOutside, graphics.drawnImages.get(12).image);
    assertEquals(images.gemCapInside, graphics.drawnImages.get(13).image);
  }

  @Test
  public void shouldStartHorizontalGemAtLeftEdgeForMinValue() throws Exception
  {
    setUpHorizontally();
    
    painter.paintOn(graphics, scrollBar);

    MockGraphics.DrawnImage outsideGemCap = graphics.drawnImages.get(12);
    MockGraphics.DrawnImage insideGemCap = graphics.drawnImages.get(13);
    assertEquals(5, outsideGemCap.x);
    assertEquals(13, insideGemCap.x);
  }

  @Test
  public void shouldStartVerticallyGemAtLeftEdgeForMinValue() throws Exception
  {
    setUpVertically();
    painter.paintOn(graphics, scrollBar);

    MockGraphics.DrawnImage outsideGemCap = graphics.drawnImages.get(12);
    MockGraphics.DrawnImage insideGemCap = graphics.drawnImages.get(13);
    assertEquals(5, outsideGemCap.y);
    assertEquals(13, insideGemCap.y);
  }

  @Test
  public void shouldPaintWideGemHorizontally() throws Exception
  {
    setUpHorizontally();
    scrollBar.configure(90, 100);
    
//    System.err.println("scrollBar.getGemSize() = " + scrollBar.getGemSize());
//    scrollBar.getGemSize() = 56

    painter.paintOn(graphics, scrollBar);
    MockGraphics.DrawnImage outsideGemCap = graphics.drawnImages.get(12);
    MockGraphics.DrawnImage insideGemCap = graphics.drawnImages.get(13);

    assertEquals(5, outsideGemCap.x);
    assertEquals(53, insideGemCap.x);

    MockGraphics fillerGraphics = graphics.subGraphics.get(0);
    assertEquals(13, fillerGraphics.clip.x);
    assertEquals(-5, fillerGraphics.drawnImages.get(0).x);
    assertEquals(12, fillerGraphics.drawnImages.get(1).x);
    assertEquals(29, fillerGraphics.drawnImages.get(2).x);
  }

  @Test
  public void shouldPaintTallGemVertically() throws Exception
  {
    setUpVertically();
    scrollBar.configure(90, 100);

    painter.paintOn(graphics, scrollBar);
    MockGraphics.DrawnImage outsideGemCap = graphics.drawnImages.get(12);
    MockGraphics.DrawnImage insideGemCap = graphics.drawnImages.get(13);

    assertEquals(5, outsideGemCap.y);
    assertEquals(53, insideGemCap.y);

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
    
    MockGraphics.DrawnImage outsideGemCap = graphics.drawnImages.get(12);
    assertEquals(scrollBar.getGemLocation(), outsideGemCap.y);
  }

  @Test
  public void paintsAtTheCorrectStartingPointHorizontally() throws Exception
  {
    setUpHorizontally();
    scrollBar.configure(10, 100);
    scrollBar.setValue(50);

    painter.paintOn(graphics, scrollBar);

    MockGraphics.DrawnImage outsideGemCap = graphics.drawnImages.get(12);
    assertEquals(scrollBar.getGemLocation(), outsideGemCap.x);
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

    assertEquals(new Box(82, 0, 18, 15), increasing);
  }

  @Test
  public void shouldBuildIncreaseBoxForVertical() throws Exception
  {
    setUpVertically();
    scrollBar.setSize(15, 100);

    Box increasing = painter.getIncreasingBox(scrollBar);

    assertEquals(new Box(0, 82, 15, 18), increasing);
  }

  @Test
  public void shouldBuildDecreaseBoxForHorizontal() throws Exception
  {
    setUpHorizontally();
    scrollBar.setSize(100, 15);

    Box increasing = painter.getDecreasingBox(scrollBar);

    assertEquals(new Box(64, 0, 18, 15), increasing);
  }

  @Test
  public void shouldBuildDecreaseBoxForVertical() throws Exception
  {
    setUpVertically();
    scrollBar.setSize(15, 100);

    Box increasing = painter.getDecreasingBox(scrollBar);

    assertEquals(new Box(0, 64, 15, 18), increasing);
  }

  @Test
  public void buildsTrackBoxVertically() throws Exception
  {
    setUpVertically();
    scrollBar.setSize(15, 100);

    Box track = painter.getTrackBox(scrollBar);

    assertEquals(new Box(0, 7, 15, 57), track);
  }

  @Test
  public void buildsTrackBoxHorizontally() throws Exception
  {
    setUpHorizontally();
    scrollBar.setSize(100, 15);

    Box track = painter.getTrackBox(scrollBar);

    assertEquals(new Box(7, 0, 57, 15), track);
  }
}
