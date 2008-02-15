package limelight.ui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class BlockPanel extends ParentPanel
{
  private Block block;
  private BufferedImage buffer;
  private LinkedList<Painter> painters;
  private TextAccessor textAccessor;
  protected PanelLayout layout;

  public BlockPanel(Block block)
  {
    super();
    this.block = block;
    buildPainters();
    textAccessor = new TextPaneTextAccessor(this);
    layout = new BlockPanelLayout(this);
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
  
  public void setLayout(PanelLayout layout)
  {
    this.layout = layout;
  }

  public BufferedImage getBuffer()
  {
    if (shouldBuildBuffer())
      buildBuffer();
    return buffer;
  }

  public void repaint()
  {
    if(block.getStyle().changed(Style.WIDTH) || block.getStyle().changed(Style.WIDTH))
      getParent().repaint();
    else
    {
      doLayout();
      PaintJob job = new PaintJob(getAbsoluteBounds());
      job.paint(getFrame().getPanel());
      job.applyTo(getFrame().getGraphics());
    }
  }

  public void doLayout()
  {
    layout.doLayout();  
  }

  public void snapToSize()
  {
    Rectangle r = getParent().getChildConsumableArea();
    setWidth(translateDimension(getBlock().getStyle().getWidth(), r.width));
    setHeight(translateDimension(getBlock().getStyle().getHeight(), r.height));
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

  public boolean usesBuffer()
  {
    return true;
  }

  protected boolean shouldBuildBuffer()
  {
    Style style = getBlock().getStyle();
    if(buffer == null)
      return true;
    else if(style.changed())
    {
      if(style.getChangedCount() == 1 && style.changed(Style.TRANSPARENCY))
        return false;
      else
        return true;
    }
    else
      return false;
  }

  protected void buildBuffer()
  {
    buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
    Graphics2D bufferGraphics = (Graphics2D) buffer.getGraphics();

    paintOn(bufferGraphics);

    getBlock().getStyle().flushChanges();
  }

  public void paintOn(Graphics2D bufferGraphics)
  {
    for (Painter painter : painters)
      painter.paint(bufferGraphics);
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
}


