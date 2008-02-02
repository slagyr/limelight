package limelight.ui;

import limelight.LimelightException;
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

  public void paint(Graphics2D graphics)
  {
    new PanelLayout(this).doLayout();

    for (Painter painter : painters)
      painter.paint(graphics);
    
    for(Panel panel : children)
    {     
      Graphics2D childGraphics = (Graphics2D)graphics.create(panel.getX(), panel.getY(), panel.getWidth(), panel.getHeight());
      panel.paint(childGraphics);
    }
  }

  protected boolean shouldBuildBuffer()
  {
    return buffer == null || checksum != checksum();
  }

  public limelight.Rectangle getRectangle()
  {
    return new limelight.Rectangle(0, 0, getWidth(), getHeight());
  }

  public void snapToSize()
  {
    Rectangle r = getParent().getRectangleInsidePadding();
    width = translateDimension(block.getStyle().getWidth(), r.width);
    height = translateDimension(block.getStyle().getHeight(), r.height);
  }

  public limelight.Rectangle getRectangleInsideMargins()
  {
    limelight.Rectangle r = getRectangle();
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

}

class SterilePanelException extends LimelightException
{
  SterilePanelException(String name)
  {
    super("The panel for block named '" + name + "' has been sterilized and child components may not be added.");
  }
}
