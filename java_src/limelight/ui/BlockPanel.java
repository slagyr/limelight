package limelight.ui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class BlockPanel extends ParentPanel
{
  private Block block;
  private BufferedImage buffer;
  private int checksum;
  private LinkedList<Painter> painters;
  private TextAccessor textAccessor;

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

  public BufferedImage getBuffer()
  {
    if (shouldBuildBuffer())
      buildBuffer();
    return buffer;
  }

  public void repaint()
  {
    PaintJob job = new PaintJob(getAbsoluteBounds());
    job.paint(getFrame().getPanel());
    job.applyTo(getFrame().getGraphics());
  }

  public void snapToSize()
  {
    Rectangle r = getParent().getChildConsumableArea();
    width = translateDimension(block.getStyle().getWidth(), r.width);
    height = translateDimension(block.getStyle().getHeight(), r.height);
  }

  public Rectangle getRectangleInsideMargins()
  {
    Rectangle r = getInternalRectangle();
    Style style = block.getStyle();
    r.shave(style.asInt(style.getTopMargin()), style.asInt(style.getRightMargin()), style.asInt(style.getBottomMargin()), style.asInt(style.getLeftMargin()));
    return r;
  }

  public Rectangle getRectangleInsideBorders()
  {
    Rectangle r = getRectangleInsideMargins();
    Style style = block.getStyle();
    r.shave(style.asInt(style.getTopBorderWidth()), style.asInt(style.getRightBorderWidth()), style.asInt(style.getBottomBorderWidth()), style.asInt(style.getLeftBorderWidth()));
    return r;
  }

  public Rectangle getRectangleInsidePadding()
  {
    Rectangle r = getRectangleInsideBorders();
    Style style = block.getStyle();
    r.shave(style.asInt(style.getTopPadding()), style.asInt(style.getRightPadding()), style.asInt(style.getBottomPadding()), style.asInt(style.getLeftPadding()));
    return r;
  }

  public Rectangle getChildConsumableArea()
  {
    return getRectangleInsidePadding();
  }

  public Panel getOwnerOfPoint(Point point)
  {
    point = new Point(point.x - getX(), point.y - getY());
    for (Panel panel : children)
    {
      if(panel.containsRelativePoint(point))
        return panel.getOwnerOfPoint(point);
    }
    return this;
  }

  public boolean usesBuffer()
  {
    return true;
  }

  protected boolean shouldBuildBuffer()
  {
    return buffer == null || checksum != checksum();
  }

  protected void buildBuffer()
  {
    buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
    Graphics2D bufferGraphics = (Graphics2D) buffer.getGraphics();

    paintOn(bufferGraphics);

    checksum = checksum();
  }

  public void paintOn(Graphics2D bufferGraphics)
  {
    for (Painter painter : painters)
      painter.paint(bufferGraphics);
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


