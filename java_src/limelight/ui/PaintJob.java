package limelight.ui;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.*;

public class PaintJob
{
  private Rectangle clip;
  private BufferedImage buffer;
  private Graphics2D graphics;
  private Composite composite;

  public PaintJob(Rectangle clip)
  {
    this.clip = clip;
    buffer = new BufferedImage(clip.width, clip.height, BufferedImage.TYPE_4BYTE_ABGR);
    graphics = (Graphics2D)buffer.getGraphics();
    composite = graphics.getComposite();
  }

  public void paint(Panel panel)
  {
    if(panelIsInClip(panel))
    {
      applyAlphaCompositeFor(panel);
      paintClipFor(panel);
      restoreComposite();
      paintChildren(panel);
    }
  }

  public Rectangle getClip()
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
    Rectangle panelClip = panel.getAbsoluteBounds(); 
    return clip.intersects(panelClip);
  }

  public void paintClipFor(Panel panel)
  {
    Rectangle panelBounds = panel.getAbsoluteBounds();

    if (panel.usesBuffer())
    {
      java.awt.Rectangle intersection = clip.intersection(panelBounds);
      int destinationX = intersection.x - clip.x;
      int destinationY = intersection.y - clip.y;
      int sourceX = intersection.x - panelBounds.x;
      int sourceY = intersection.y - panelBounds.y;
      BufferedImage panelBuffer = panel.getBuffer();

      graphics.drawImage(panelBuffer, destinationX, destinationY, intersection.width + destinationX, intersection.height + destinationY,
                                      sourceX, sourceY, intersection.width + sourceX, intersection.height + sourceY, null);
    }
    else
    {
      int x = panelBounds.x - clip.x;
      int y = panelBounds.y - clip.y;
      Graphics2D subGraphics = (Graphics2D)graphics.create(x, y, panel.width, panel.height);
      panel.paintOn(subGraphics);
    }
  }

  public void applyAlphaCompositeFor(Panel panel)
  {
    Style style = panel.getBlock().getStyle();
    int alphaPercentage = style.asInt(style.getTransparency());
    if(alphaPercentage > 0)
    {    
      float alpha = 1.0f - (alphaPercentage / 100.0f);
      Composite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
      graphics.setComposite(alphaComposite);
    }
  }

  public void restoreComposite()
  {
    graphics.setComposite(composite);
  }

  public void paintChildren(Panel panel)
  {
    if(panel.hasChildren())
    {
      for (Panel child : panel.getChildren())
        paint(child);
    }
  }

  public void substituteGraphics(Graphics2D graphics)
  {
    this.graphics = graphics;
  }

  public void applyTo(Graphics graphics)
  {
    graphics.drawImage(buffer, clip.x, clip.y, null);
  }

    // MDM - Purely for debuggin graphics
  private void showClip(final Rectangle clip, final BufferedImage buffer, String title)
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
}
