package limelight.ui;

import limelight.LimelightException;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class BlockPanel extends Panel
{
  private LinkedList<Panel> children;
  private Block block;
  private BufferedImage buffer;
  private int checksum;
  private LinkedList<Painter> painters;
  private TextAccessor textAccessor;
  private boolean sterilized;

  public BlockPanel(Block block)
  {
    super();
    this.block = block;
    children = new LinkedList<Panel>();
    buildPainters();
    textAccessor = new TextPaneTextAccessor(this);
  }

  public Block getBlock()
  {
    return block;
  }

  public void add(Panel panel) throws SterilePanelException
  {
    if (sterilized)
      throw new SterilePanelException(block.getClassName());
    children.add(panel);
    panel.setParent(this);
  }

  public LinkedList<Panel> getChildren()
  {
    return children;
  }

  public TextAccessor getTextAccessor()
  {
    return textAccessor;
  }

  public void setTextAccessor(TextAccessor textAccessor)
  {
    this.textAccessor = textAccessor;
  }

  public LinkedList<Painter> getPainters()
  {
    return painters;
  }

  public void sterilize()
  {
    sterilized = true;
  }

  public boolean isSterilized()
  {
    return sterilized;
  }

  public void paint(Rectangle clip)
  {
    new PanelLayout(this).doLayout();
 
    if (shouldBuildBuffer())
      buildBuffer();

    Point absoluteLocation = getAbsoluteLocation();
    Graphics2D graphics = (Graphics2D)getFrame().getGraphics().create(absoluteLocation.x + clip.x, absoluteLocation.y + clip.y, clip.width, clip.height);


    Composite originalComposite = graphics.getComposite();
    applyAlphaComposite(graphics);
    graphics.drawImage(buffer, 0, 0, clip.width, clip.height, clip.x, clip.y, clip.x + clip.width, clip.y + clip.height, null);
    graphics.setComposite(originalComposite);


    for(Panel panel : children)
    {
      Rectangle panelBounds = panel.getExternalRectangle();
      if(clip.intersects(panelBounds))
      {
        Rectangle intersection = clip.intersection(panelBounds);
        intersection.translate(panel.getX() * -1, panel.getY() * -1);
        panel.paint(intersection);
      }
    }
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

  public void repaint()
  {
    Point point = getAbsoluteLocation();
    getFrame().getPanel().paint(new Rectangle(point.x, point.y, width, height));
  }


  public void snapToSize()
  {
    Rectangle r = getParent().getRectangleInsidePadding();
    width = translateDimension(block.getStyle().getWidth(), r.width);
    height = translateDimension(block.getStyle().getHeight(), r.height);
  }

  public limelight.Rectangle getRectangleInsideMargins()
  {
    limelight.Rectangle r = getInternalRectangle();
    Style style = block.getStyle();
    r.shave(style.asInt(style.getTopMargin()), style.asInt(style.getRightMargin()), style.asInt(style.getBottomMargin()), style.asInt(style.getLeftMargin()));
    return r;
  }

  public limelight.Rectangle getRectangleInsideBorders()
  {
    limelight.Rectangle r = getRectangleInsideMargins();
    Style style = block.getStyle();
    r.shave(style.asInt(style.getTopBorderWidth()), style.asInt(style.getRightBorderWidth()), style.asInt(style.getBottomBorderWidth()), style.asInt(style.getLeftBorderWidth()));
    return r;
  }

  public limelight.Rectangle getRectangleInsidePadding()
  {
    limelight.Rectangle r = getRectangleInsideBorders();
    Style style = block.getStyle();
    r.shave(style.asInt(style.getTopPadding()), style.asInt(style.getRightPadding()), style.asInt(style.getBottomPadding()), style.asInt(style.getLeftPadding()));
    return r;
  }

  public Panel getOwnerOfPoint(Point point)
  {
    point = new Point(point.x - getX(), point.y - getY());
    for (Panel panel : children)
    {
      if(panel.containsPoint(point))
        return panel.getOwnerOfPoint(point);
    }
    return this;
  }

  protected boolean shouldBuildBuffer()
  {
    return buffer == null || checksum != checksum();
  }

  protected void buildBuffer()
  {
    buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
    Graphics2D bufferGraphics = (Graphics2D) buffer.getGraphics();

    for (Painter painter : painters)
      painter.paint(bufferGraphics);

    checksum = checksum();
  }

  public int checksum()
  {
    int checksum = block.getStyle().checksum();
    if (block.getText() != null)
      checksum ^= block.getText().hashCode();
    return checksum;
  }

  private void buildPainters()
  {
    painters = new LinkedList<Painter>();
    painters.add(new BackgroundPainter(this));
    painters.add(new BorderPainter(this));
  }

  private int translateDimension(String sizeString, int maxSize)
  {
    if (sizeString == null)
      return 0;
    else if (sizeString.endsWith("%"))
    {
      double percentage = Double.parseDouble(sizeString.substring(0, sizeString.length() - 1));
      return (int) ((percentage * 0.01) * (double) maxSize);
    }
    else
    {
      return Integer.parseInt(sizeString);
    }
  }


  private void applyAlphaComposite(Graphics graphics)
  {
    if (block.getStyle().getTransparency() != null)
    {
      float transparency = 1f - (Integer.parseInt(block.getStyle().getTransparency()) / 100.0f);
      Composite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency);
      ((Graphics2D) graphics).setComposite(alphaComposite);
    }
  }

}

class SterilePanelException extends LimelightException
{
  SterilePanelException(String name)
  {
    super("The panel for block named '" + name + "' has been sterilized and child components may not be added.");
  }
}
