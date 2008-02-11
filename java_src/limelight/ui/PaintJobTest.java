package limelight.ui;

import junit.framework.TestCase;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PaintJobTest extends TestCase
{
  private Rectangle clip;
  private PaintJob job;

  public void setUp() throws Exception
  {
    clip = new Rectangle(100, 200, 300, 400);
    job = new PaintJob(clip);
  }

  public void testCreation() throws Exception
  {
    Rectangle clip = new Rectangle(1, 2, 3, 4);
    PaintJob job = new PaintJob(clip);

    assertSame(clip, job.getClip());

    BufferedImage buffer = job.getBuffer();
    assertEquals(3, buffer.getWidth());
    assertEquals(4, buffer.getHeight());

    Graphics2D graphics = job.getGraphics();
    assertNotNull(graphics);
    assertEquals(Color.white, graphics.getBackground());
  }

  public void testPaintRoot() throws Exception
  {
    MockBlockPanel panel = new MockBlockPanel();
    panel.setLocation(1, 2);
    panel.setSize(1000, 1000);

    MockGraphics graphics = new MockGraphics();
    job.substituteGraphics(graphics);

    job.paint(panel);

    assertEquals(new Rectangle(-99, -198, 1000, 1000), graphics.createdGraphicsRectangle);
  }

  public void testPanelIsInClip() throws Exception
  {
    MockPanel panel = new MockPanel();
    panel.setLocation(0, 0);
    panel.setSize(100, 100);
    assertFalse(job.panelIsInClip(panel));

    panel.setLocation(1, 101);
    assertTrue(job.panelIsInClip(panel));

    panel.setSize(99, 99);
    assertFalse(job.panelIsInClip(panel));

    panel.setLocation(200, 300);
    assertTrue(job.panelIsInClip(panel));
  }

  public void testApplyComposite() throws Exception
  {
    MockBlock block = new MockBlock();
    BlockPanel panel = new BlockPanel(block);
    Graphics2D graphics = job.getGraphics();
    
    job.applyAlphaCompositeFor(panel, graphics);
    assertEquals(1.0, ((AlphaComposite) graphics.getComposite()).getAlpha(), 0.001);

    block.style.setTransparency("");
    job.applyAlphaCompositeFor(panel, graphics);
    assertEquals(1.0, ((AlphaComposite) graphics.getComposite()).getAlpha(), 0.001);

    block.style.setTransparency("0");
    job.applyAlphaCompositeFor(panel, graphics);
    assertEquals(1.0, ((AlphaComposite) graphics.getComposite()).getAlpha(), 0.001);

    block.style.setTransparency("50");
    job.applyAlphaCompositeFor(panel, graphics);
    assertEquals(0.5, ((AlphaComposite) graphics.getComposite()).getAlpha(), 0.001);

    job.restoreComposite(graphics);
    assertEquals(1.0, ((AlphaComposite) graphics.getComposite()).getAlpha(), 0.001);
  }

  public void testPasteClipWhenPanelBuffered() throws Exception
  {
    MockGraphics graphics = new MockGraphics();
    MockPanel panel = new MockPanel();
    panel.shouldUseBuffer = true;
    panel.buffer = new MockBufferedImage();

    job.paintClipFor(panel, graphics);

    assertEquals(0, graphics.drawnImageX);
    assertEquals(0, graphics.drawnImageY);
    assertEquals(panel.buffer, graphics.drawnImage);
  }

  public void testHandlingOfNonBufferedPanel() throws Exception
  {
    MockGraphics graphics = new MockGraphics();
    MockPanel panel = new MockPanel();
    panel.shouldUseBuffer = false;
    panel.setLocation(200, 300);
    panel.setSize(100, 100);

    job.paintClipFor(panel, graphics);

    Graphics2D subGraphics = panel.paintedOnGraphics;
    assertNotNull(subGraphics);
    assertSame(graphics, panel.paintedOnGraphics);
  }

  public void testApplyToGraphics() throws Exception
  {
    MockGraphics graphics = new MockGraphics();

    job.applyTo(graphics);

    assertSame(job.getBuffer(), graphics.drawnImage);
    assertEquals(100, graphics.drawnImageX);
    assertEquals(200, graphics.drawnImageY);
  }

  public void testPaintingChildren() throws Exception
  {
    MockBlockPanel parent = new MockBlockPanel();
    MockPanel child = new MockPanel();
    parent.add(child);
    parent.childConsumableRectangle = new Rectangle(12, 34, 56, 78);
    child.setLocation(123, 456);
    child.setSize(100, 200);
    MockGraphics graphics = new MockGraphics();

    job.paintChildren(parent, graphics);

    assertEquals(new Rectangle(12, 34, 56, 78), graphics.clippedRectangle);
    assertEquals(new Rectangle(123, 456, 100, 200), graphics.createdGraphicsRectangle);
  }
}
