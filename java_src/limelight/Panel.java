package limelight;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

public class Panel extends JPanel
{
	private Block block;
	private BufferedImage buffer;
  private int checksum;
  private int xOffset;
  private int yOffset;
  private List<Painter> painters;
  private boolean sterilized;

  public Panel(Block owner)
	{
		this.block = owner;
		setOpaque(false);
		setDoubleBuffered(false);
		setLayout(new BlockLayout(this));
    BlockEventListener listener = new BlockEventListener(block);
    addKeyListener(listener);
    addMouseListener(listener);
    addMouseMotionListener(listener);
    buildPainters();
  }

  public Component add(Component comp)
  {
    if(sterilized)
      throw new SterilePanelException(block.getClassName());
    return super.add(comp);
  }

  public Block getBlock()
	{
		return block;
	}

  public List<Painter> getPainters()
  {
    return painters;
  }

  public void clearEventListeners()
  {
    for(MouseListener listner : getMouseListeners())
      removeMouseListener(listner);
    for(MouseMotionListener listner : getMouseMotionListeners())
      removeMouseMotionListener(listner);
    for(KeyListener listner : getKeyListeners())
      removeKeyListener(listner);
  }

//  int paints = 0;
//	boolean badName = false;

	public void paint(Graphics graphics)
	{
		if(shouldBuildBuffer())
      buildBuffer();

    Composite	originalComposite = ((Graphics2D)graphics).getComposite();
    applyAlphaComposite(graphics);
    Rectangle clip = new Rectangle(graphics.getClipBounds());
		graphics.drawImage(buffer, clip.x, clip.y, clip.x + clip.width, clip.y + clip.height, clip.x, clip.y, clip.x + clip.width, clip.y + clip.height, null);
		((Graphics2D)graphics).setComposite(originalComposite);
		super.paintChildren(graphics);
	}

  protected boolean shouldBuildBuffer()
  {
    return buffer == null || checksum != checksum();
  }

  protected void buildBuffer()
  {
    buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
    Graphics2D bufferGraphics = (Graphics2D)buffer.getGraphics();

    for (Painter painter : painters)
      painter.paint(bufferGraphics);

    checksum = checksum();
  }

	public Font createFont()
	{
		int style = 0;
		if(block.getStyle().getFontStyle() != null && block.getStyle().getFontStyle().indexOf("bold") != -1)
			style |= Font.BOLD;
		if(block.getStyle().getFontStyle() != null && block.getStyle().getFontStyle().indexOf("italic") != -1)
			style |= Font.ITALIC;

		String face = block.getStyle().getFontFace() == null ? "Arial" : block.getStyle().getFontFace();
		int size = resolveInt(block.getStyle().getFontSize()) <= 0 ? 10 : resolveInt(block.getStyle().getFontSize());

		return new Font(face, style, size);
	}

	public void snapToDesiredSize()
	{
    Rectangle r = ((Panel)getParent()).getRectangleInsidePadding();
    int width = translateDimension(block.getStyle().getWidth(), r.width);
    int height = translateDimension(block.getStyle().getHeight(), r.height);    
    setSize(width, height);
	}

  public void snapOffsets()
  {
    if(block.getStyle().getXOffset() != null)
      xOffset = Integer.parseInt(block.getStyle().getXOffset());

    if(block.getStyle().getYOffset() != null)
      yOffset = Integer.parseInt(block.getStyle().getYOffset());
  }

	public Rectangle getRectangle()
	{
/*System.err.println("block.getClassName() = " + block.getClassName());
System.err.println("getWidth() = " + getWidth());*/
    return new Rectangle(0, 0, getWidth(), getHeight());
	}

	public Rectangle getRectangleInsideMargins()
	{
		Rectangle r = getRectangle();
    Style style = block.getStyle();
    r.shave(resolveInt(style.getTopMargin()), resolveInt(style.getRightMargin()), resolveInt(style.getBottomMargin()), resolveInt(style.getLeftMargin()));
		return r;
	}

	public Rectangle getRectangleInsideBorders()
	{
		Rectangle r = getRectangleInsideMargins();
		r.shave(resolveInt(block.getStyle().getTopBorderWidth()), resolveInt(block.getStyle().getRightBorderWidth()), resolveInt(block.getStyle().getBottomBorderWidth()), resolveInt(block.getStyle().getLeftBorderWidth()));
		return r;
	}

	public Rectangle getRectangleInsidePadding()
	{
    Rectangle r = getRectangleInsideBorders();
		r.shave(resolveInt(block.getStyle().getTopPadding()), resolveInt(block.getStyle().getRightPadding()), resolveInt(block.getStyle().getBottomPadding()), resolveInt(block.getStyle().getLeftPadding())); 
    return r;
	}

  public int getXOffset()
  {
    return xOffset;
  }
  
  public void sterilize()
  {
    sterilized = true;
  }

  public boolean isSterilized()
  {
    return sterilized;
  }

  public int getYOffset()
  {
    return yOffset;
  }
  
  private void buildPainters()
  {
    painters = new LinkedList<Painter>(); 
    painters.add(new BackgroundPainter(this));
    painters.add(new BorderPainter(this));
    painters.add(new TextPainter(this));
  }

  private int checksum()
  {
    int checksum = block.getStyle().checksum();
    if(block.getText() != null)
      checksum *= block.getText().hashCode();
    return checksum;
  }

  private int translateDimension(String sizeString, int maxSize)
	{
    if(sizeString == null)
      return 0;
    else if(sizeString.endsWith("%"))
		{
			double percentage = Double.parseDouble(sizeString.substring(0, sizeString.length() - 1));
			return (int)((percentage * 0.01) * (double)maxSize);
		}
		else
		{
			return Integer.parseInt(sizeString);
		}
	}

  private void applyAlphaComposite(Graphics graphics)
  {
    if(block.getStyle().getTransparency() != null)
    {
      float transparency = 1f - (Integer.parseInt(block.getStyle().getTransparency()) / 100.0f);
      Composite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency);
      ((Graphics2D)graphics).setComposite(alphaComposite);
    }
  }

	private int resolveInt(String value)
	{
		try
		{
			return Integer.parseInt(value);
		}
		catch(NumberFormatException e)
		{
			return 0;
		}
	}
}

class SterilePanelException extends Error
{
  SterilePanelException(String name)
  {
    super("The panel for block named '" + name + "' has been sterilized and child components may not be added.");
  }
}