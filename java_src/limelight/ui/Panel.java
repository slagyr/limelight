package limelight.ui;

import limelight.*;
import limelight.ui.painting.BorderPainter;
import limelight.ui.painting.BackgroundPainter;
import limelight.ui.painting.Border;

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
  private Rectangle rectangle;
  private Rectangle rectangleInsideMargins;
  private Rectangle rectangleInsideBorders;
  private Rectangle rectangleInsidePadding;
  private Border borderShaper;

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
    for (Component component : components)
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
    if (width != getWidth() || height != getHeight())
      clearLayoutCache();
    super.setSize(width, height);
  }

  public void doLayout()
  {
    if (borderShaper != null)
      borderShaper.updateDimentions();

    super.doLayout();

    //TODO MDM added because it's needed... kinda fishy though.  There'a a better way.
    if (borderShaper != null)
      borderShaper.setBounds(getRectangleInsideMargins());
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
    if (style == null)
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
    paintChildren(graphics);
  }

  protected void paintChildren(Graphics graphics)
  {
    Component[] components = getComponents();
    for (Component child : components)
    {
      if(!isFloater(child))
        paintChild(graphics, child);
    }
    for (Component child : components)
    {
      if(isFloater(child))
        paintChild(graphics, child);
    }
  }

  private void paintChild(Graphics graphics, Component child)
  {
    if (graphics.hitClip(child.getX(), child.getY(), child.getWidth(), child.getHeight()))
    {
      Graphics childGraphics = graphics.create(child.getX(), child.getY(), child.getWidth(), child.getHeight());
      child.paint(childGraphics);
    }
  }

  private boolean isFloater(Component component)
  {
    return (component instanceof Panel) && ((Panel)component).isFloater();
  }

  protected boolean shouldBuildBuffer()
  {
    Style style = getStyle();
    if (buffer == null)
      return true;
    if (style.changed() && !(style.getChangedCount() == 1 && style.changed(Style.TRANSPARENCY)))
      return true;
    if (getWidth() != buffer.getWidth() || getHeight() != buffer.getHeight())
      return true;
    return false;
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
    Rectangle r;
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
    if (rectangle == null)
      rectangle = new Rectangle(0, 0, getWidth(), getHeight());
    return rectangle;
  }

  public limelight.ui.Rectangle getRectangleInsideMargins()
  {
    if (rectangleInsideMargins == null)
    {
      rectangleInsideMargins = (Rectangle) getRectangle().clone();
      rectangleInsideMargins.shave(getStyle().asInt(getStyle().getTopMargin()), getStyle().asInt(getStyle().getRightMargin()), getStyle().asInt(getStyle().getBottomMargin()), getStyle().asInt(getStyle().getLeftMargin()));
    }
    return rectangleInsideMargins;
  }

  public limelight.ui.Rectangle getRectangleInsideBorders()
  {
    if (rectangleInsideBorders == null)
    {
      rectangleInsideBorders = (Rectangle) getRectangleInsideMargins().clone();
      rectangleInsideBorders.shave(getStyle().asInt(getStyle().getTopBorderWidth()), getStyle().asInt(getStyle().getRightBorderWidth()), getStyle().asInt(getStyle().getBottomBorderWidth()), getStyle().asInt(getStyle().getLeftBorderWidth()));
    }
    return rectangleInsideBorders;
  }

  public limelight.ui.Rectangle getRectangleInsidePadding()
  {
    if (rectangleInsidePadding == null)
    {
      rectangleInsidePadding = (Rectangle) getRectangleInsideBorders().clone();
      rectangleInsidePadding.shave(getStyle().asInt(getStyle().getTopPadding()), getStyle().asInt(getStyle().getRightPadding()), getStyle().asInt(getStyle().getBottomPadding()), getStyle().asInt(getStyle().getLeftPadding()));
    }
    return rectangleInsidePadding;
  }

  public Border getBorderShaper()
  {
    if (borderShaper == null)
      borderShaper = new Border(getStyle(), getRectangleInsideMargins());
    return borderShaper;
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

  public boolean isFloater()
  {
    return "on".equals(getStyle().getFloat());
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
    else if ("auto".equals(sizeString))
      return maxSize;
    else if (sizeString.endsWith("%"))
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

  private void clearLayoutCache()
  {
    rectangle = null;
    rectangleInsideMargins = null;
    rectangleInsideBorders = null;
    rectangleInsidePadding = null;
  }

  public static class SterilePanelException extends LimelightError
  {
    SterilePanelException(String name)
    {
      super("The panel for prop named '" + name + "' has been sterilized and child components may not be added.");
    }
  }
}

