package limelight.ui;

import limelight.*;
import limelight.ui.painting.BorderPainter;
import limelight.ui.painting.BackgroundPainter;

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
  private Prop prop;
  private BufferedImage buffer;
  private List<Painter> painters;
  private boolean sterilized;
  private TextAccessor textAccessor;
  private Style style;
  private PropEventListener listener;

  public Panel(Prop owner)
  {
    this.prop = owner;
    setOpaque(false);
    setDoubleBuffered(false);
    setLayout(new PropLayout(this));
    listener = new PropEventListener(prop);
    addKeyListener(listener);
    addMouseListener(listener);
    addMouseMotionListener(listener);
    buildPainters();
    textAccessor = new TextPaneTextAccessor(this);
  }

  public Component add(Component comp)
  {
    if (sterilized)
      throw new SterilePanelException(prop.getClassName());
    return super.add(comp);
  }

  public boolean hasChild(Component child)
  {
    Component[] components = getComponents();
    for(Component component : components)
      if (child == component)
        return true;
    return false;
  }

  public void setLocation(int x, int y)
  {
    super.setLocation(x + getXOffset(), y + getYOffset());
  }

  public void setSize(int width, int height)
  {
    width = height == 0 ? 0 : width;
    height = width == 0 ? 0 : height;
    super.setSize(width, height);
  }

  public Prop getProp()
  {
    return prop;
  }

  public PropEventListener getListener()
  {
    return listener;
  }

  public Style getStyle()
  {
    if(style == null)
      style = getProp().getStyle();
    return style;
  }

  public List<Painter> getPainters()
  {
    return painters;
  }

  public void clearEventListeners()
  {
    for (MouseListener listner : getMouseListeners())
      removeMouseListener(listner);
    for (MouseMotionListener listner : getMouseMotionListeners())
      removeMouseMotionListener(listner);
    for (KeyListener listner : getKeyListeners())
      removeKeyListener(listner);
  }

  public TextAccessor getTextAccessor()
  {
    return textAccessor;
  }

  public void setTextAccessor(TextAccessor textAccessor)
  {
    this.textAccessor = textAccessor;
  }

  public void paint(Graphics graphics)
  {
    if (shouldBuildBuffer())
      buildBuffer();

    Composite originalComposite = ((Graphics2D) graphics).getComposite();
    applyAlphaComposite(graphics);
    limelight.ui.Rectangle clip = new limelight.ui.Rectangle(graphics.getClipBounds());
    graphics.drawImage(buffer, clip.x, clip.y, clip.x + clip.width, clip.y + clip.height, clip.x, clip.y, clip.x + clip.width, clip.y + clip.height, null);
    ((Graphics2D) graphics).setComposite(originalComposite);
    super.paintChildren(graphics);
  }

  protected boolean shouldBuildBuffer()
  {
    Style style = getStyle();
    return buffer == null || style.changed() && !(style.getChangedCount() == 1 && style.changed(Style.TRANSPARENCY));
  }

  protected void buildBuffer()
  {
    buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
    Graphics2D bufferGraphics = (Graphics2D) buffer.getGraphics();

    for (Painter painter : painters)
      painter.paint(bufferGraphics);

    getStyle().flushChanges();
  }

  public Dimension getPreferredSize()
  {
    return getMaximumSize();
  }

  public Dimension getMaximumSize()
  {
    Rectangle r = null;
    if (getParent().getClass() == Panel.class)
      r = ((Panel) getParent()).getRectangleInsidePadding();
    else
      r = new Rectangle(0, 0, getParent().getWidth(), getParent().getHeight());
    int width = translateDimension(getStyle().getWidth(), r.width);
    int height = translateDimension(getStyle().getHeight(), r.height);
    Dimension dimension = new Dimension(width, height);
    return dimension;
  }

  public limelight.ui.Rectangle getRectangle()
  {
    return new limelight.ui.Rectangle(0, 0, getWidth(), getHeight());
  }

  public limelight.ui.Rectangle getRectangleInsideMargins()
  {
    limelight.ui.Rectangle r = getRectangle();
    Style style = getStyle();
    r.shave(style.asInt(style.getTopMargin()), style.asInt(style.getRightMargin()), style.asInt(style.getBottomMargin()), style.asInt(style.getLeftMargin()));
    return r;
  }

  public limelight.ui.Rectangle getRectangleInsideBorders()
  {
    limelight.ui.Rectangle r = getRectangleInsideMargins();
    Style style = getStyle();
    r.shave(style.asInt(style.getTopBorderWidth()), style.asInt(style.getRightBorderWidth()), style.asInt(style.getBottomBorderWidth()), style.asInt(style.getLeftBorderWidth()));
    return r;
  }

  public limelight.ui.Rectangle getRectangleInsidePadding()
  {
    limelight.ui.Rectangle r = getRectangleInsideBorders();
    Style style = getStyle();
    r.shave(style.asInt(style.getTopPadding()), style.asInt(style.getRightPadding()), style.asInt(style.getBottomPadding()), style.asInt(style.getLeftPadding()));
    return r;
  }

  public int getXOffset()
  {
    if (getStyle().getXOffset() != null)
      return Integer.parseInt(getStyle().getXOffset());
    return 0;
  }

  public int getYOffset()
  {
    if (getStyle().getYOffset() != null)
      return Integer.parseInt(getStyle().getYOffset());
    return 0;
  }

  public void sterilize()
  {
    sterilized = true;
  }

  public void replaceChildren(Component[] components)
  {
    boolean sterilizedTemp = sterilized;
    sterilized = false;
    removeAll();
    for (Component component : components)
      add(component);
    sterilized = sterilizedTemp;
  }

  public boolean isSterilized()
  {
    return sterilized;
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
    else if("auto".equals(sizeString))
      return maxSize;
    else if(sizeString.endsWith("%"))
    {
      double percentage = Double.parseDouble(sizeString.substring(0, sizeString.length() - 1));
      int result = (int) ((percentage * 0.01) * (double) maxSize);      
      return result;
    }
    else
    {
      return Integer.parseInt(sizeString);
    }
  }

  private void applyAlphaComposite(Graphics graphics)
  {
    if (getStyle().getTransparency() != null)
    {
      float transparency = 1f - (Integer.parseInt(getStyle().getTransparency()) / 100.0f);
      Composite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency);
      ((Graphics2D) graphics).setComposite(alphaComposite);
    }
  }

  public static class SterilePanelException extends LimelightError
  {
    SterilePanelException(String name)
    {
      super("The panel for prop named '" + name + "' has been sterilized and child components may not be added.");
    }
  }
}

