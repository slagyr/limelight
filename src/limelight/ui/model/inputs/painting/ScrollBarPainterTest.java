package limelight.ui.model.inputs.painting;

import limelight.ui.MockGraphics;
import limelight.ui.model.inputs.ScrollBar2Panel;
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
    assertEquals(-11, fillerGraphics.drawnImages.get(0).x);
    assertEquals(6, fillerGraphics.drawnImages.get(1).x);
    assertEquals(23, fillerGraphics.drawnImages.get(2).x);
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
    assertEquals(-11, fillerGraphics.drawnImages.get(0).y);
    assertEquals(6, fillerGraphics.drawnImages.get(1).y);
    assertEquals(23, fillerGraphics.drawnImages.get(2).y);
  }
}
