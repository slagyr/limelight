package limelight.ui.model2;

import limelight.styles.Style;
import limelight.util.Box;
import limelight.ui.Panel;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.Composite;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Color;

public class PaintJob
{
  private Box clip;
  private BufferedImage buffer;
  private Graphics2D graphics;
  private Composite composite;

  public PaintJob(Box clip)
  {
    this.clip = clip;
    buffer = new BufferedImage(clip.width, clip.height, BufferedImage.TYPE_4BYTE_ABGR);
    graphics = (Graphics2D)buffer.getGraphics();
    graphics.setBackground(Color.white);
    graphics.clearRect(0, 0, clip.width, clip.height);
    composite = graphics.getComposite();
  }

  public void paint(Panel panel)
  {
    Box panelBounds = panel.getAbsoluteBounds();
    int x = panelBounds.x - clip.x;
    int y = panelBounds.y - clip.y;
    Graphics2D graphics = (Graphics2D)this.graphics.create(x, y, panel.getWidth(), panel.getHeight());

    paint(panel, graphics);
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
    return graphics;
  }

  public boolean panelIsInClip(Panel panel)
  {
    Box panelClip = panel.getAbsoluteBounds();
    return clip.intersects(panelClip);
  }

  public void paintClipFor(Panel panel, Graphics2D graphics)
  {
//    if(panel.usesBuffer())
//      graphics.drawImage(panel.getBuffer(), 0, 0, null);
//    else
      panel.paintOn(graphics);
  }

  public void applyAlphaCompositeFor(Panel panel, Graphics2D graphics)
  {
    if(panel instanceof RootPanel)
      return;

    Style style = panel.getProp().getStyle();
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
      limelight.util.Box innards = panel.getChildConsumableArea();
      graphics.clipRect(innards.x, innards.y, innards.width, innards.height);
      for (Panel child : panel.getChildren())
      {
        if(panelIsInClip(child))
        {
          Graphics2D childGraphics = (Graphics2D)graphics.create(child.getX(), child.getY(), child.getWidth(), child.getHeight());
          paint(child, childGraphics);
        }
      }
    }
  }

  public void applyTo(Graphics graphics)
  {
System.err.println("applyTo " + graphics.getClip());
    graphics.drawImage(buffer, clip.x, clip.y, null);

  }

    // MDM - Purely for debuggin graphics
  private void showClip(final Box clip, final BufferedImage buffer, String title)
  {
    JFrame jframe = new JFrame(title);
    jframe.setSize(clip.width,  clip.height + 30);
    jframe.add(new JPanel() {
      public void paint(Graphics g)
      {
        g.drawImage(buffer, clip.x, clip.y, clip.x + clip.width, clip.y + clip.height, clip.x, clip.y, clip.x + clip.width, clip.y + clip.height, null);
      }
    });
    jframe.setVisible(true);
  }

  public void substituteGraphics(Graphics2D graphics)
  {
    this.graphics = graphics;
  }
}

