//- Copyright 2008 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.Context;
import limelight.LimelightError;
import limelight.styles.Style;
import limelight.styles.StyleDescriptor;
import limelight.styles.StyleObserver;
import limelight.styles.abstrstyling.StyleAttribute;
import limelight.ui.PaintablePanel;
import limelight.ui.Painter;
import limelight.ui.Panel;
import limelight.ui.api.Prop;
import limelight.ui.api.PropablePanel;
import limelight.ui.model.inputs.ScrollBarPanel;
import limelight.ui.painting.BackgroundPainter;
import limelight.ui.painting.Border;
import limelight.ui.painting.BorderPainter;
import limelight.ui.painting.PaintAction;
import limelight.util.Box;
import limelight.util.Util;

import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;

public class PropPanel extends BasePanel implements PropablePanel, PaintablePanel, StyleObserver
{
  private final Prop prop;
  private final PropPanelLayout layout;
  private LinkedList<Painter> painters;
  private Border borderShaper;
  private TextAccessor textAccessor;
  private Box boxInsideMargins;
  private Box boxInsideBorders;
  private Box boxInsidePadding;
  private Box childConsumableArea;
  private Style style;
  private PaintAction afterPaintAction;
  private ScrollBarPanel verticalScrollBar;
  private ScrollBarPanel horizontalScrollBar;
  private boolean sizeChanged = true;
  private boolean borderChanged = true;

  public PropPanel(Prop prop)
  {
    this.prop = prop;
    buildPainters();
    layout = new PropPanelLayout(this);
    textAccessor = new TextPaneTextAccessor(this);
    getStyle().addObserver(this);
  }

  private void buildPainters()
  {
    painters = new LinkedList<Painter>();
    painters.add(new BackgroundPainter(this));
    painters.add(new BorderPainter(this));
  }

  public String getText()
  {
    return textAccessor.getText();
  }

  public void setText(String text) throws LimelightError
  {
    if(!Util.equal(text, getText()))
    {
      setNeedsLayout(); // This is questionable...  The text panel would know if layout is needed.
    }
    textAccessor.setText(text);
  }

  public TextAccessor getTextAccessor()
  {
    return textAccessor;
  }

  public void setTextAccessor(TextAccessor textAccessor)
  {
    this.textAccessor = textAccessor;
  }

  public void snapToSize()
  {
    Box r = getParent().getChildConsumableArea();
    Style style = getProp().getStyle();
    int newWidth = style.getCompiledWidth().calculateDimension(r.width, style.getCompiledMinWidth(), style.getCompiledMaxWidth());
    int newHeight = style.getCompiledHeight().calculateDimension(r.height, style.getCompiledMinHeight(), style.getCompiledMaxHeight());
    setSize(newWidth, newHeight);
    sizeChanged = false;
  }

  public Prop getProp()
  {
    return prop;
  }

  public Panel getOwnerOfPoint(Point point)
  {
    Point relativePoint = new Point(point.x - getX(), point.y - getY());
    if(verticalScrollBar != null && verticalScrollBar.containsRelativePoint(relativePoint))
      return verticalScrollBar;
    else if(horizontalScrollBar != null && horizontalScrollBar.containsRelativePoint(relativePoint))
      return horizontalScrollBar;

    return super.getOwnerOfPoint(point);
  }

  public Box getBoxInsideMargins()
  {
    if(boxInsideMargins == null)
    {
      boxInsideMargins = (Box) getBoundingBox().clone();
      Style style = getStyle();
      boxInsideMargins.shave(style.getCompiledTopMargin().getValue(), style.getCompiledRightMargin().getValue(), style.getCompiledBottomMargin().getValue(), style.getCompiledLeftMargin().getValue());
    }
    return boxInsideMargins;
  }

  public Box getBoxInsideBorders()
  {
    if(boxInsideBorders == null)
    {
      boxInsideBorders = (Box) getBoxInsideMargins().clone();
      Style style = getStyle();
      boxInsideBorders.shave(style.getCompiledTopBorderWidth().getValue(), style.getCompiledRightBorderWidth().getValue(), style.getCompiledBottomBorderWidth().getValue(), style.getCompiledLeftBorderWidth().getValue());
    }
    return boxInsideBorders;
  }

  public Box getBoxInsidePadding()
  {
    if(boxInsidePadding == null)
    {
      boxInsidePadding = (Box) getBoxInsideBorders().clone();
      Style style = getStyle();
      boxInsidePadding.shave(style.getCompiledTopPadding().getValue(), style.getCompiledRightPadding().getValue(), style.getCompiledBottomPadding().getValue(), style.getCompiledLeftPadding().getValue());
    }
    return boxInsidePadding;
  }

  public Box getChildConsumableArea()
  {
    if(childConsumableArea == null)
    {
      getBoxInsidePadding();
      int width = verticalScrollBar == null ? boxInsidePadding.width : boxInsidePadding.width - verticalScrollBar.getWidth();
      int height = horizontalScrollBar == null ? boxInsidePadding.height : boxInsidePadding.height - horizontalScrollBar.getHeight();
      childConsumableArea = new Box(boxInsidePadding.x, boxInsidePadding.y, width, height);
    }
    return childConsumableArea;
  }

  public void doLayout()
  {
    super.doLayout(); // First to avaoid race condition
    if(borderShaper != null && borderChanged)
    {
      borderShaper.updateDimentions();
      borderChanged = false;
    }

    layout.doLayout();

    //TODO MDM added because it's needed... kinda fishy though.  There'a a better way.
    if(borderShaper != null)
      borderShaper.setBounds(getBoxInsideMargins());

    markAsDirty();
  }

  private void markAsDirty()
  {
    RootPanel rootPanel = getRoot();
    if(rootPanel != null)
      rootPanel.addDirtyRegion(getAbsoluteBounds());
  }

  public void paintOn(Graphics2D graphics)
  {
    for(Painter painter : painters)
      painter.paint(graphics);

    if(afterPaintAction != null)
      afterPaintAction.invoke(graphics);
  }

  public Style getStyle()
  {
    if(style == null)
      style = prop.getStyle();
    return style;
  }

  public Border getBorderShaper()
  {
    if(borderShaper == null)
      borderShaper = new Border(getStyle(), getBoxInsideMargins());
    return borderShaper;
  }

  public void mousePressed(MouseEvent e)
  {
    getProp().mouse_pressed(translatedEvent(e));
  }

  public void mouseReleased(MouseEvent e)
  {
    getProp().mouse_released(translatedEvent(e));
  }

  public void mouseClicked(MouseEvent e)
  {
    getProp().mouse_clicked(translatedEvent(e));
  }

  public void mouseDragged(MouseEvent e)
  {
    getProp().mouse_dragged(translatedEvent(e));
  }

  public void mouseEntered(MouseEvent e)
  {
    getProp().mouse_entered(translatedEvent(e));
    hoverOn();
  }

  public void mouseExited(MouseEvent e)
  {
    getProp().mouse_exited(translatedEvent(e));
    hoverOff();
  }

  public void mouseMoved(MouseEvent e)
  {
    getProp().mouse_moved(translatedEvent(e));
  }

  public void mouseWheelMoved(MouseWheelEvent e)
  {
    boolean isVertical = e.getModifiers() % 2 == 0;
    ScrollBarPanel scrollBar = isVertical ? verticalScrollBar : horizontalScrollBar;
    if(scrollBar != null)
      scrollBar.setValue(scrollBar.getValue() + e.getUnitsToScroll());
    else
      getParent().mouseWheelMoved(e);
  }

  public void focusLost(FocusEvent e)
  {
    getProp().focus_lost(e);
  }

  public void focusGained(FocusEvent e)
  {
    getProp().focus_gained(e);
  }

  public void keyTyped(KeyEvent e)
  {
    getProp().key_typed(e);
  }

  public void keyPressed(KeyEvent e)
  {
    getProp().key_pressed(e);
  }

  public void keyReleased(KeyEvent e)
  {
    getProp().key_released(e);
  }

  public void buttonPressed(ActionEvent e)
  {
    getProp().button_pressed(e);
  }

  public void valueChanged(Object e)
  {
    getProp().value_changed(e);
  }

  public void setCursor(Cursor cursor)
  {
    getRoot().setCursor(cursor);
  }

  //TODO I don't think this is needed any longer.
  public void repaint()
  {
//System.err.println("repaint: " + this + ": " + (getParent() != null) + ", " + (getStyle().changed(Style.WIDTH) || getStyle().changed(Style.WIDTH)));
    //TODO Handle the case when the parent needs to repaint.
//    if(getParent() != null && (getStyle().changed(Style.WIDTH) || getStyle().changed(Style.WIDTH)))
//      getParent().repaint();
//    else
//    {
    doLayout();
    PaintJob job = new PaintJob(getAbsoluteBounds());
    //TODO Why are we painting the root panel here?  So wastful! Maybe. Transparency?
    job.paint((getRoot()).getPanel()); //TODO - cast should not be neccessary here.
    job.applyTo(getRoot().getGraphics());
//    }
  }

  //TODO I don't think this is needed any longer.
  public void paintImmediately(int a, int b, int c, int d)
  {
    repaint();
  }

  public String toString()
  {
    return "PropPanel - " + getProp().getName();
  }

  public void setAfterPaintAction(PaintAction action)
  {
    afterPaintAction = action;
  }

  public PaintAction getAfterPaintAction()
  {
    return afterPaintAction;
  }

  public PropPanelLayout getLayout()
  {
    return layout;
  }

  public LinkedList<Painter> getPainters()
  {
    return painters;
  }

  public boolean isFloater()
  {
    return getStyle().getCompiledFloat().isOn();
  }

  //TODO super.clearCache() deals with absolute positioning.  Here the boxes are all relative.  They're uneccessarily being cleared.
  public void clearCache()
  {
    super.clearCache();
    boxInsideMargins = null;
    boxInsideBorders = null;
    boxInsidePadding = null;
    childConsumableArea = null;
  }

  public void styleChanged(StyleDescriptor descriptor, StyleAttribute value)
  {
    if(Context.instance().bufferedImageCache != null &&
        descriptor != Style.TRANSPARENCY &&
        descriptor != Style.X &&
        descriptor != Style.Y)
      Context.instance().bufferedImageCache.expire(this);

    if(getParent() != null && getRoot() != null)
    {
      if(descriptor == Style.WIDTH || descriptor == Style.HEIGHT)
      {
        sizeChanged = true;
        setNeedsLayout();
        getParent().setNeedsLayout();
        propogateSizeChange(getParent());
      }
      else if(isBorderDescriptor(descriptor))
      {
        borderChanged = true;
        setNeedsLayout();
      }
      else if(descriptor == Style.X || descriptor == Style.Y)
        getParent().setNeedsLayout();
      else
        markAsDirty();
    }

  }

  private boolean isBorderDescriptor(StyleDescriptor descriptor)
  {
    return descriptor == Style.TOP_BORDER_WIDTH ||
           descriptor == Style.RIGHT_BORDER_WIDTH ||
           descriptor == Style.BOTTOM_BORDER_WIDTH ||
           descriptor == Style.LEFT_BORDER_WIDTH ||
           descriptor == Style.TOP_RIGHT_BORDER_WIDTH ||
           descriptor == Style.BOTTOM_RIGHT_BORDER_WIDTH ||
           descriptor == Style.BOTTOM_LEFT_BORDER_WIDTH ||
           descriptor == Style.TOP_LEFT_BORDER_WIDTH ||
           descriptor == Style.TOP_RIGHT_ROUNDED_CORNER_RADIUS ||
           descriptor == Style.BOTTOM_RIGHT_ROUNDED_CORNER_RADIUS ||
           descriptor == Style.BOTTOM_LEFT_ROUNDED_CORNER_RADIUS ||
           descriptor == Style.TOP_LEFT_ROUNDED_CORNER_RADIUS;
  }

  public ScrollBarPanel getVerticalScrollBar()
  {
    return verticalScrollBar;
  }

  public ScrollBarPanel getHorizontalScrollBar()
  {
    return horizontalScrollBar;
  }

  public void addVerticalScrollBar()
  {
    verticalScrollBar = new ScrollBarPanel(ScrollBarPanel.VERTICAL);
    add(verticalScrollBar);
    childConsumableArea = null;
  }

  public void addHorizontalScrollBar()
  {
    horizontalScrollBar = new ScrollBarPanel(ScrollBarPanel.HORIZONTAL);
    add(horizontalScrollBar);
    childConsumableArea = null;
  }

  public void removeVerticalScrollBar()
  {
    remove(verticalScrollBar);
    verticalScrollBar = null;
    childConsumableArea = null;
  }

  public void removeHorizontalScrollBar()
  {
    remove(horizontalScrollBar);
    horizontalScrollBar = null;
    childConsumableArea = null;
  }

  public void playSound(String filename)
  {
    Context.instance().audioPlayer.playAuFile(filename);
  }

  private void hoverOn()
  {
    Style hoverStyle = getProp().getHoverStyle();
    if(hoverStyle != null)
    {
      getProp().getStyle().applyScreen(hoverStyle);
      getRoot().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
  }

  private void hoverOff()
  {
    Style hoverStyle = getProp().getHoverStyle();
    if(hoverStyle != null)
    {
      getProp().getStyle().removeScreen();
      getRoot().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
  }

  public boolean sizeChanged()
  {
    return sizeChanged;
  }

  public boolean borderChanged()
  {
    return borderChanged;
  }
}

