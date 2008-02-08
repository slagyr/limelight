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

    job.applyAlphaCompositeFor(panel);
    assertEquals(1.0, ((AlphaComposite)job.getGraphics().getComposite()).getAlpha(), 0.001);

    block.style.setTransparency("");
    job.applyAlphaCompositeFor(panel);
    assertEquals(1.0, ((AlphaComposite)job.getGraphics().getComposite()).getAlpha(), 0.001);

    block.style.setTransparency("0");
    job.applyAlphaCompositeFor(panel);
    assertEquals(1.0, ((AlphaComposite)job.getGraphics().getComposite()).getAlpha(), 0.001);

    block.style.setTransparency("50");
    job.applyAlphaCompositeFor(panel);
    assertEquals(0.5, ((AlphaComposite)job.getGraphics().getComposite()).getAlpha(), 0.001);

    job.restoreComposite();
    assertEquals(1.0, ((AlphaComposite)job.getGraphics().getComposite()).getAlpha(), 0.001);
  }

  public void testPasteClipWhenPanelIsFullyContained() throws Exception
  {
    MockGraphics graphics = new MockGraphics();
    job.substituteGraphics(graphics);

    MockPanel panel = new MockPanel();
    panel.shouldUseBuffer = true;
    panel.setLocation(200, 300);
    panel.setSize(100, 100);

    job.paintClipFor(panel);

    Rectangle destination = graphics.drawnImageDestination;
    assertEquals(new Rectangle(100, 100, 100, 100), destination);
    Rectangle source = graphics.drawnImageSource;
    assertEquals(new Rectangle(0, 0, 100, 100), source);
  }

  public void testPasteClipWhenPanelPariallyOverlapping() throws Exception
  {
    MockGraphics graphics = new MockGraphics();
    job.substituteGraphics(graphics);

    MockPanel panel = new MockPanel();
    panel.shouldUseBuffer = true;
    panel.setLocation(300, 100);
    panel.setSize(200, 200);

    job.paintClipFor(panel);

    Rectangle destination = graphics.drawnImageDestination;
    assertEquals(new Rectangle(200, 0, 100, 100), destination);
    Rectangle source = graphics.drawnImageSource;
    assertEquals(new Rectangle(0, 100, 100, 100), source);
  }

  public void testHandlingOfNonBufferedPanel() throws Exception
  {
    MockGraphics graphics = new MockGraphics();
    job.substituteGraphics(graphics);

    MockPanel panel = new MockPanel();
    panel.shouldUseBuffer = false;
    panel.setLocation(200, 300);
    panel.setSize(100, 100);

    job.paintClipFor(panel);

    Graphics2D subGraphics = panel.paintedOnGraphics;
    assertNotNull(subGraphics);
    assertEquals(new Rectangle(0, 0, 100, 100), subGraphics.getClipBounds());
  }

  public void testApplyToGraphics() throws Exception
  {
    MockGraphics graphics = new MockGraphics();

    job.applyTo(graphics);

    assertSame(job.getBuffer(), graphics.drawnImage);
    assertEquals(100, graphics.drawnImageX);
    assertEquals(200, graphics.drawnImageY);
  }
}
