//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.Context;
import limelight.LimelightError;
import limelight.styles.Style;
import limelight.styles.StyleDescriptor;
import limelight.styles.StyleObserver;
import limelight.styles.abstrstyling.StyleAttribute;
import limelight.styles.abstrstyling.PixelsAttribute;
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
  private LinkedList<Painter> painters;
  private Border borderShaper;
  private TextAccessor textAccessor;
  private Box boxInsideMargins;
  private Box boxInsideBorders;
  private Box boxInsidePadding;
  private Box childConsumableArea;
  private Style style;
  private PaintAction afterPaintAction;
  private ScrollBarPanel verticalScrollbar;
  private ScrollBarPanel horizontalScrollbar;
  private boolean sizeChangePending = true;
  public boolean borderChanged = true;
  public Dimension greediness = new Dimension(0, 0);

  public PropPanel(Prop prop)
  {
    this.prop = prop;
    buildPainters();
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
      markAsNeedingLayout(); // This is questionable...  The text panel would know if layout is needed.
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

  public Prop getProp()
  {
    return prop;
  }

  public Panel getOwnerOfPoint(Point point)
  {
    Point relativePoint = new Point(point.x - getX(), point.y - getY());
    if(verticalScrollbar != null && verticalScrollbar.containsRelativePoint(relativePoint))
      return verticalScrollbar;
    else if(horizontalScrollbar != null && horizontalScrollbar.containsRelativePoint(relativePoint))
      return horizontalScrollbar;

    return super.getOwnerOfPoint(point);
  }

  public synchronized Box getBoxInsideMargins()
  {
    if(boxInsideMargins == null)
    {
      Box bounds = getBoundingBox();
      boxInsideMargins = (Box) bounds.clone();
      Style style = getStyle();
      boxInsideMargins.shave(style.getCompiledTopMargin().pixelsFor(bounds.height),
          style.getCompiledRightMargin().pixelsFor(bounds.width),
          style.getCompiledBottomMargin().pixelsFor(bounds.height),
          style.getCompiledLeftMargin().pixelsFor(bounds.width));
    }
    return boxInsideMargins;
  }

  public synchronized Box getBoxInsideBorders()
  {
    if(boxInsideBorders == null)
    {
      Box bounds = getBoxInsideMargins();
      boxInsideBorders = (Box) bounds.clone();
      Style style = getStyle();
      boxInsideBorders.shave(style.getCompiledTopBorderWidth().pixelsFor(bounds.height),
          style.getCompiledRightBorderWidth().pixelsFor(bounds.width),
          style.getCompiledBottomBorderWidth().pixelsFor(bounds.height),
          style.getCompiledLeftBorderWidth().pixelsFor(bounds.width));
    }
    return boxInsideBorders;
  }

  public synchronized Box getBoxInsidePadding()
  {
    if(boxInsidePadding == null)
    {
      Box bounds = getBoxInsideBorders();
      boxInsidePadding = (Box) bounds.clone();
      Style style = getStyle();
      boxInsidePadding.shave(style.getCompiledTopPadding().pixelsFor(bounds.height),
          style.getCompiledRightPadding().pixelsFor(bounds.width),
          style.getCompiledBottomPadding().pixelsFor(bounds.height),
          style.getCompiledLeftPadding().pixelsFor(bounds.width));
    }
    return boxInsidePadding;
  }

  public Box getChildConsumableArea()
  {
    if(childConsumableArea == null)
    {
      getBoxInsidePadding();
      Box boxInsidePadding = getBoxInsidePadding();
      int width = verticalScrollbar == null ? boxInsidePadding.width : boxInsidePadding.width - verticalScrollbar.getWidth();
      int height = horizontalScrollbar == null ? boxInsidePadding.height : boxInsidePadding.height - horizontalScrollbar.getHeight();
      childConsumableArea = new Box(boxInsidePadding.x, boxInsidePadding.y, width, height);
    }
    return childConsumableArea;
  }

  public Layout getDefaultLayout()
  {
    return PropPanelLayout.instance;
  }

  public void updateBorder()
  {
    if(borderShaper != null)
    {
      borderShaper.setBounds(getBoxInsideMargins());
      if(borderChanged)
      {
        borderShaper.updateDimentions();
        borderChanged = false;
      }
    }
  }

  public void paintOn(Graphics2D graphics)
  {
    if(!laidOut)
      return;
    for(Painter painter : painters)
      painter.paint(graphics);

    if(afterPaintAction != null)
    {
      afterPaintAction.invoke(graphics);      
    }
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
    if(getProp().accepts_mouse_pressed())
      getProp().mouse_pressed(translatedEvent(e));
    else
      super.mousePressed(e);
  }

  public void mouseReleased(MouseEvent e)
  {
    if(getProp().accepts_mouse_released())
      getProp().mouse_released(translatedEvent(e));
    else
      super.mouseReleased(e);
  }

  public void mouseClicked(MouseEvent e)
  {
    if(getProp().accepts_mouse_clicked())
      getProp().mouse_clicked(translatedEvent(e));
    else
      super.mouseClicked(e);
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
    ScrollBarPanel scrollBar = isVertical ? verticalScrollbar : horizontalScrollbar;
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
    PaintJob job = new PaintJob(getAbsoluteBounds(), getRoot().getGraphics().getBackground());
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

  public LinkedList<Painter> getPainters()
  {
    return painters;
  }

  public boolean isFloater()
  {
    return getStyle().getCompiledFloat().isOn();
  }

  public void doFloatLayout()
  {
   FloaterLayout.instance.doLayout(this);
  }

  //TODO super.clearCache() deals with absolute positioning.  Here the boxes are all relative.  They're uneccessarily being cleared.
  public synchronized void clearCache()
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
        sizeChangePending = true;
        markAsNeedingLayout();
        propagateSizeChangeUp(getParent());
        propagateSizeChangeDown();
      }
      else if(isBorderDescriptor(descriptor) || isMarginPaddingOrBorder(value))
      {
        borderChanged = true;
        markAsNeedingLayout();
        clearCache();
        propagateSizeChangeDown();
      }
      else if(descriptor == Style.X || descriptor == Style.Y)
      {
        markAsNeedingLayout(FloaterLayout.instance);
      }
      else if(descriptor == Style.HORIZONTAL_ALIGNMENT || descriptor == Style.VERTICAL_ALIGNMENT)
        markAsNeedingLayout();
      else
        markAsDirty();
    }
  }

  private boolean isMarginPaddingOrBorder(StyleAttribute attribute)
  {

    //TODO Huh?  This doesn't make sense.
    return attribute instanceof PixelsAttribute;
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

  public ScrollBarPanel getVerticalScrollbar()
  {
    return verticalScrollbar;
  }

  public ScrollBarPanel getHorizontalScrollbar()
  {
    return horizontalScrollbar;
  }

  public void addVerticalScrollBar()
  {
    verticalScrollbar = new ScrollBarPanel(ScrollBarPanel.VERTICAL);
    add(verticalScrollbar);
    childConsumableArea = null;
  }

  public void addHorizontalScrollBar()
  {
    horizontalScrollbar = new ScrollBarPanel(ScrollBarPanel.HORIZONTAL);
    add(horizontalScrollbar);
    childConsumableArea = null;
  }

  public void removeVerticalScrollBar()
  {
    remove(verticalScrollbar);
    verticalScrollbar = null;
    childConsumableArea = null;
  }

  public void removeHorizontalScrollBar()
  {
    remove(horizontalScrollbar);
    horizontalScrollbar = null;
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
    if(getProp().getStyle().hasScreen())
    {
      getProp().getStyle().removeScreen();
      RootPanel root = getRoot();
      if(root != null)
        root.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
  }

  public boolean sizeChangePending()
  {
    return sizeChangePending;
  }

  public void resetPendingSizeChange()
  {
    sizeChangePending = false;                                                      
  }

  public boolean borderChanged()
  {
    return borderChanged;
  }

  protected boolean canRemove(Panel child)
  {
    return child != verticalScrollbar && child != horizontalScrollbar;
  }
}

