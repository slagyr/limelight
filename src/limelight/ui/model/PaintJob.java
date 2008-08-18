package limelight.ui.model;

import limelight.Context;
import limelight.caching.Cache;
import limelight.styles.Style;
import limelight.ui.Panel;
import limelight.util.Box;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PaintJob
{
  private Box clip;
  private BufferedImage buffer;
  private Graphics2D rootGraphics;
  private Composite composite;
  private Cache<Panel, BufferedImage> bufferCache;

  public PaintJob(Box clip)
  {
    bufferCache = Context.instance().bufferedImageCache;
    this.clip = clip;
    buffer = new BufferedImage(clip.width, clip.height, BufferedImage.TYPE_4BYTE_ABGR);
    rootGraphics = (Graphics2D) buffer.getGraphics();
    rootGraphics.setBackground(Color.white);
    rootGraphics.clearRect(0, 0, clip.width, clip.height);
    composite = rootGraphics.getComposite();
  }

  public void paint(Panel panel)
  {
    Box panelBounds = panel.getAbsoluteBounds();
    int x = panelBounds.x - clip.x;
    int y = panelBounds.y - clip.y;

    Graphics2D graphics = (Graphics2D) rootGraphics.create(x, y, panel.getWidth(), panel.getHeight());
    paint(panel, graphics);
    graphics.dispose();
    rootGraphics.dispose();
  }

  public void paint(Panel panel, Graphics2D graphics)
  {
    applyAlphaCompositeFor(panel, graphics);
    paintClipFor(panel, graphics);
    restoreComposite(graphics);
    paintChildren(panel, graphics);
  }

  public Box getClip()
  {
    return clip;
  }

  public BufferedImage getBuffer()
  {
    return buffer;
  }

  public Graphics2D getGraphics()
  {
    return rootGraphics;
  }

  public boolean panelIsInClip(Panel panel)
  {
    Box panelClip = panel.getAbsoluteBounds();
    return clip.intersects(panelClip);
  }

  public void paintClipFor(Panel panel, Graphics2D graphics)
  {
    if(panel.canBeBuffered())
    {
      BufferedImage panelBuffer = bufferCache.retrieve(panel);
      if(shouldBuildBufferFor(panel, panelBuffer))
        panelBuffer = buildBufferFor(panel);
      graphics.drawImage(panelBuffer, 0, 0, null);
    }
    else
      panel.paintOn(graphics);
  }

  public void applyAlphaCompositeFor(Panel panel, Graphics2D graphics)
  {
    Style style = panel.getStyle();
    int alphaPercentage = style.asInt(style.getTransparency());
    if(alphaPercentage > 0)
    {
      float alpha = 1.0f - (alphaPercentage / 100.0f);
      Composite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
      graphics.setComposite(alphaComposite);
    }
  }

  public void restoreComposite(Graphics2D graphics)
  {
    graphics.setComposite(composite);
  }

  public void paintChildren(Panel panel, Graphics2D graphics)
  {
    if(panel.hasChildren())
    {
      Box innards = panel.getBoxInsidePadding();
      graphics.clipRect(innards.x, innards.y, innards.width, innards.height);
      for(Panel child : panel.getChildren())
        if(!child.isFloater())
          paintChild(graphics, child);
      for(Panel child : panel.getChildren())
        if(child.isFloater())
          paintChild(graphics, child);
    }
  }

  private void paintChild(Graphics2D graphics, Panel child)
  {
    if(panelIsInClip(child))
    {
      Graphics2D childGraphics = (Graphics2D) graphics.create(child.getX(), child.getY(), child.getWidth(), child.getHeight());
      paint(child, childGraphics);
      childGraphics.dispose();
    }
  }

  public void applyTo(Graphics graphics)
  {
    if(graphics != null)
    {
      graphics.drawImage(buffer, clip.x, clip.y, null);
      Toolkit.getDefaultToolkit().sync(); // required to sync display on some systems according "Killer Game Programming"
    }
  }

//    // MDM - Purely for debuggin graphics
//  private void showClip(final Box clip, final BufferedImage buffer, String title)
//  {
//    JFrame jframe = new JFrame(title);
//    jframe.setSize(clip.width,  clip.height + 30);
//    jframe.add(new JPanel() {
//      public void paint(Graphics g)
//      {
//        g.drawImage(buffer, clip.x, clip.y, clip.x + clip.width, clip.y + clip.height, clip.x, clip.y, clip.x + clip.width, clip.y + clip.height, null);
//      }
//    });
//    jframe.setVisible(true);
//  }

  public void substituteGraphics(Graphics2D graphics)
  {
    this.rootGraphics = graphics;
  }

  public boolean shouldBuildBufferFor(Panel panel, BufferedImage buffer)
  {
    if(buffer == null)
      return true;
    Style style = panel.getStyle();
    if(panel.getWidth() != buffer.getWidth() || panel.getHeight() != buffer.getHeight())
      return true;
    else if(style.changed() && !(style.getChangedCount() == 1 && style.changed(Style.TRANSPARENCY)))
      return true;
    else
      return false;
  }

  public BufferedImage buildBufferFor(Panel panel)
  {
    BufferedImage buffer = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
    Graphics2D graphics = buffer.createGraphics();
    panel.paintOn(graphics);
    graphics.dispose();
    panel.getStyle().flushChanges(); //TODO Maybe called redundantly because Panel.paintOn() should also flushChanges. 
    bufferCache.cache(panel, buffer);
    return buffer;
  }
}

