//- Copyright 2008 8th Light, Inc.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.*;
import limelight.util.Box;
import limelight.ui.api.Prop;
import limelight.styles.Style;
import limelight.ui.painting.BorderPainter;
import limelight.ui.painting.BackgroundPainter;
import limelight.ui.painting.Border;
import limelight.ui.painting.PaintAction;
import limelight.ui.Painter;
import limelight.ui.PaintablePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

public class Panel extends JPanel implements PaintablePanel
{                                                  
  private Prop prop;
  private BufferedImage buffer;
  private List<Painter> painters;
  private boolean sterilized;
  private TextAccessor textAccessor;
  private Style style;
  private PropEventListener listener;
  private Box box;
  private Box boxInsideMargins;
  private Box boxInsideBorders;
  private Box boxInsidePadding;
  private Border borderShaper;
  private PaintAction afterPaintAction;

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
      throw new SterilePanelException(prop.getName());
    if(((PropLayout)getLayout()).isInScrollMode())      // TODO MDM HACK HACK HACK!!!!
      return ((PropLayout)getLayout()).getScrollView().add(comp);
    else
      return super.add(comp);
  }

  public void remove(Component comp)   // TODO HACK
  {
    if(((PropLayout)getLayout()).isInScrollMode())      // TODO MDM HACK HACK HACK!!!!
      ((PropLayout)getLayout()).getScrollView().remove(comp);
    else
      super.remove(comp);
  }

  public boolean hasChild(Component child)
  {
    Component[] components = getComponents();
    for (Component component : components)
      if (child == component)
        return true;
    return false;
  }

  public void setSize(int width, int height)
  {
    width = height == 0 ? 0 : width;
    height = width == 0 ? 0 : height;
    if (width != getWidth() || height != getHeight())
      clearLayoutCache();
    super.setSize(width, height);
  }

  public void snapToSize()
  {
    setSize(getPreferredSize());
  }

  public void doLayout()
  {
    if (borderShaper != null)
      borderShaper.updateDimentions();
   
    super.doLayout();

    //TODO MDM added because it's needed... kinda fishy though.  There'a a better way.
    if (borderShaper != null)
      borderShaper.setBounds(getBoxInsideMargins());
  }

  // TODO - MDM - This is silly inefficient.  It forces panels to relayout all the time.  Particulary when the frame is resized.
  // The correct way to handle this is to add observer ability to the styles and invalidate the panel when appropriate styles change.
  public void validate()
  {
    super.validate();
    invalidate();
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

  public void setText(String text) throws LimelightException
  {
    textAccessor.setText(text);
  }

  public String getText()
  {
    return textAccessor.getText();
  }

  public TextAccessor getTextAccessor()
  {
    return textAccessor;
  }

  public void setTextAccessor(TextAccessor textAccessor)
  {
    this.textAccessor = textAccessor;
  }

  public void setAfterPaintAction(PaintAction action)
  {
    afterPaintAction = action;
  }

  public PaintAction getAfterPaintAction()
  {
    return afterPaintAction;
  }

  public void paint(Graphics graphics)
  {
if("application_list".equals(prop.getName()))
{
  System.err.println("paint");
//  new Exception().printStackTrace();
}
    if (shouldBuildBuffer())
      buildBuffer();

    Graphics2D graphics2d = (Graphics2D) graphics;
    Composite originalComposite = graphics2d.getComposite();
    applyAlphaComposite(graphics);
    Box clip = new Box(graphics.getClipBounds());
    graphics.drawImage(buffer, clip.x, clip.y, clip.x + clip.width, clip.y + clip.height, clip.x, clip.y, clip.x + clip.width, clip.y + clip.height, null);
    graphics2d.setComposite(originalComposite);
    paintChildren(graphics);
    if(afterPaintAction != null)
      afterPaintAction.invoke(graphics2d);
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
    boolean should_repaint = false;
    if (buffer == null)
      should_repaint = true;
    else if (style.changed() && !(style.getChangedCount() == 1 && style.changed(Style.TRANSPARENCY)))
      should_repaint = true;
    else if (getWidth() != buffer.getWidth() || getHeight() != buffer.getHeight())
      should_repaint = true;

    return should_repaint;
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
    Box r;
    if (getParent().getClass() == Panel.class)
      r = ((Panel) getParent()).getBoxInsidePadding();
    else
      r = new Box(0, 0, getParent().getWidth(), getParent().getHeight());

    int width = translateDimension(getStyle().getWidth(), r.width);
    int height = translateDimension(getStyle().getHeight(), r.height);
    Dimension dimension = new Dimension(width, height);
    return dimension;
  }

  public Box getBox()
  {
    if (box == null)
      box = new Box(0, 0, getWidth(), getHeight());
    return box;
  }

  public Box getBoxInsideMargins()
  {
    if (boxInsideMargins == null)
    {
      boxInsideMargins = (Box)getBox().clone();
      boxInsideMargins.shave(getStyle().asInt(getStyle().getTopMargin()), getStyle().asInt(getStyle().getRightMargin()), getStyle().asInt(getStyle().getBottomMargin()), getStyle().asInt(getStyle().getLeftMargin()));
    }
    return boxInsideMargins;
  }

  public Box getBoxInsideBorders()
  {
    if (boxInsideBorders == null)
    {
      boxInsideBorders = (Box)getBoxInsideMargins().clone();
      boxInsideBorders.shave(getStyle().asInt(getStyle().getTopBorderWidth()), getStyle().asInt(getStyle().getRightBorderWidth()), getStyle().asInt(getStyle().getBottomBorderWidth()), getStyle().asInt(getStyle().getLeftBorderWidth()));
    }
    return boxInsideBorders;
  }

  public Box getBoxInsidePadding()
  {
    if (boxInsidePadding == null)
    {
      boxInsidePadding = (Box)getBoxInsideBorders().clone();
      boxInsidePadding.shave(getStyle().asInt(getStyle().getTopPadding()), getStyle().asInt(getStyle().getRightPadding()), getStyle().asInt(getStyle().getBottomPadding()), getStyle().asInt(getStyle().getLeftPadding()));
    }
    return boxInsidePadding;
  }

  public Border getBorderShaper()
  {
    if (borderShaper == null)
      borderShaper = new Border(getStyle(), getBoxInsideMargins());
    return borderShaper;
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
    if (!"0".equals(getStyle().getTransparency()))
    {
      float transparency = 1f - (Integer.parseInt(getStyle().getTransparency()) / 100.0f);
      Composite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency);
      ((Graphics2D) graphics).setComposite(alphaComposite);
    }
  }

  private void clearLayoutCache()
  {
    box = null;
    boxInsideMargins = null;
    boxInsideBorders = null;
    boxInsidePadding = null;
  }

  public static class SterilePanelException extends LimelightError
  {
    SterilePanelException(String name)
    {
      super("The panel for prop named '" + name + "' has been sterilized and child components may not be added.");
    }
  }

  public void playSound(String filename)
  {
    Context.instance().audioPlayer.playAuFile(filename);
  }
}

