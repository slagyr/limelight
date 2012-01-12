package limelight.ui.ninepatch;

import limelight.ui.MockGraphics;
import limelight.util.Box;
import limelight.util.TestUtil;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

import static junit.framework.Assert.assertEquals;

public class NinePatchTest
{
  public static BufferedImage image;

  @BeforeClass
  public static void setup() throws Exception
  {
    String imagePath = TestUtil.dataDirPath("ninepatchtest.9.png");
    image = ImageIO.read(TestUtil.fs.inputStream(imagePath));
  }

  @Test
  public void itIsInitializedWithAnImage() throws Exception
  {
    NinePatch.Info info = NinePatch.Info.inspect(image);

    assertEquals(new Box(1, 1, 3, 11), info.topLeftBounds);
    assertEquals(new Box(4, 1, 5, 11), info.topMiddleBounds);
    assertEquals(new Box(9, 1, 7, 11), info.topRightBounds);
    assertEquals(new Box(1, 12, 3, 13), info.middleLeftBounds);
    assertEquals(new Box(4, 12, 5, 13), info.middleMiddleBounds);
    assertEquals(new Box(9, 12, 7, 13), info.middleRightBounds);
    assertEquals(new Box(1, 25, 3, 17), info.bottomLeftBounds);
    assertEquals(new Box(4, 25, 5, 17), info.bottomMiddleBounds);
    assertEquals(new Box(9, 25, 7, 17), info.bottomRightBounds);
  }

  @Test
  public void itDrawsAnImageOfTheSameSize() throws Exception
  {
    NinePatch patch = NinePatch.load(image);
    MockGraphics graphics = new MockGraphics();
    patch.draw(graphics, 0, 0, 15, 41);
    assertEquals(9, graphics.drawnImages.size());

    checkDrawnImage(graphics.drawnImages.get(0), new Rectangle(1, 1, 3, 11), new Rectangle(0, 0, 3, 11));   // top left
    checkDrawnImage(graphics.drawnImages.get(1), new Rectangle(4, 1, 5, 11), new Rectangle(3, 0, 5, 11));   // top middle
    checkDrawnImage(graphics.drawnImages.get(2), new Rectangle(9, 1, 7, 11), new Rectangle(8, 0, 7, 11));   // top right
    checkDrawnImage(graphics.drawnImages.get(3), new Rectangle(1, 12, 3, 13), new Rectangle(0, 11, 3, 13)); // middle left
    checkDrawnImage(graphics.drawnImages.get(4), new Rectangle(4, 12, 5, 13), new Rectangle(3, 11, 5, 13)); // middle middle
    checkDrawnImage(graphics.drawnImages.get(5), new Rectangle(9, 12, 7, 13), new Rectangle(8, 11, 7, 13)); // middle right
    checkDrawnImage(graphics.drawnImages.get(6), new Rectangle(1, 25, 3, 17), new Rectangle(0, 24, 3, 17)); // bottom left
    checkDrawnImage(graphics.drawnImages.get(7), new Rectangle(4, 25, 5, 17), new Rectangle(3, 24, 5, 17)); // bottom middle
    checkDrawnImage(graphics.drawnImages.get(8), new Rectangle(9, 25, 7, 17), new Rectangle(8, 24, 7, 17)); // bottom right
  }

  @Test
  public void itDrawsAnImageMinimalSize() throws Exception
  {
    NinePatch patch = NinePatch.load(image);
    MockGraphics graphics = new MockGraphics();
    patch.draw(graphics, 0, 0, 10, 28);
    assertEquals(4, graphics.drawnImages.size());

    checkDrawnImage(graphics.drawnImages.get(0), new Rectangle(1, 1, 3, 11), new Rectangle(0, 0, 3, 11));   // top left
    checkDrawnImage(graphics.drawnImages.get(1), new Rectangle(9, 1, 7, 11), new Rectangle(3, 0, 7, 11));   // top right
    checkDrawnImage(graphics.drawnImages.get(2), new Rectangle(1, 25, 3, 17), new Rectangle(0, 11, 3, 17)); // bottom left
    checkDrawnImage(graphics.drawnImages.get(3), new Rectangle(9, 25, 7, 17), new Rectangle(3, 11, 7, 17)); // bottom right
  }

  @Test
  public void itDrawsAnImageStretchedInBothDimensions() throws Exception
  {
    NinePatch patch = NinePatch.load(image);
    MockGraphics graphics = new MockGraphics();
    patch.draw(graphics, 0, 0, 100, 100);
    assertEquals(9, graphics.drawnImages.size());

    checkDrawnImage(graphics.drawnImages.get(0), new Rectangle(1, 1, 3, 11), new Rectangle(0, 0, 3, 11));   // top left
    checkDrawnImage(graphics.drawnImages.get(1), new Rectangle(4, 1, 5, 11), new Rectangle(3, 0, 90, 11));   // top middle
    checkDrawnImage(graphics.drawnImages.get(2), new Rectangle(9, 1, 7, 11), new Rectangle(93, 0, 7, 11));   // top right
    checkDrawnImage(graphics.drawnImages.get(3), new Rectangle(1, 12, 3, 13), new Rectangle(0, 11, 3, 72)); // middle left
    checkDrawnImage(graphics.drawnImages.get(4), new Rectangle(4, 12, 5, 13), new Rectangle(3, 11, 90, 72)); // middle middle
    checkDrawnImage(graphics.drawnImages.get(5), new Rectangle(9, 12, 7, 13), new Rectangle(93, 11, 7, 72)); // middle right
    checkDrawnImage(graphics.drawnImages.get(6), new Rectangle(1, 25, 3, 17), new Rectangle(0, 83, 3, 17)); // bottom left
    checkDrawnImage(graphics.drawnImages.get(7), new Rectangle(4, 25, 5, 17), new Rectangle(3, 83, 90, 17)); // bottom middle
    checkDrawnImage(graphics.drawnImages.get(8), new Rectangle(9, 25, 7, 17), new Rectangle(93, 83, 7, 17)); // bottom right
  }

  private void checkDrawnImage(MockGraphics.DrawnImage drawnImage, Rectangle expectedSource, Rectangle expectedDestination)
  {
    assertEquals(image, drawnImage.image);
    assertEquals(expectedSource, drawnImage.source);
    assertEquals(expectedDestination, drawnImage.destination);
  }
}
