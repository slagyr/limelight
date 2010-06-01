//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.Context;
import limelight.LimelightError;
import limelight.styles.*;
import limelight.styles.abstrstyling.StyleValue;
import limelight.styles.abstrstyling.PixelsValue;
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
import java.util.Map;

public class PropPanel extends BasePanel implements PropablePanel, PaintablePanel, StyleObserver
{
  private final Prop prop;
  private final ScreenableStyle style;
  private final RichStyle hoverStyle;
  private String styles;
  private final LinkedList<Painter> painters; // TODO MDM - This silly now.  We don't need a list for the painters.
  private Border borderShaper;
  private TextAccessor textAccessor;
  private Box boxInsideMargins;
  private Box boxInsideBorders;
  private Box boxInsidePadding;
  private Box childConsumableArea;
  private PaintAction afterPaintAction;
  private ScrollBarPanel verticalScrollbar;
  private ScrollBarPanel horizontalScrollbar;
  private boolean sizeChangePending = true;
  public boolean borderChanged = true;
  public Dimension greediness = new Dimension(0, 0);

  public PropPanel(Prop prop)
  {
    this.prop = prop;
    painters = new LinkedList<Painter>();
    style = new ScreenableStyle();
    hoverStyle = new RichStyle();
    textAccessor = new TextPaneTextAccessor(this);
    buildPainters();
    style.addObserver(this); // TODO MDM - Is this a memory leak?  When a prop is deleted, we'll have to delete that reference.
  }

  private void buildPainters()
  {
    // TODO MDM - These painters can be static.  They don't need to store the panel as an instance variable.
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
      markAsNeedingLayout(); // TODO MDM - This is questionable...  The text panel would know if layout is needed.
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

  @Override
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

  @Override
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

  public ScreenableStyle getStyle()
  {
    return style;
  }

  public RichStyle getHoverStyle()
  {
    return hoverStyle;
  }

  public void setStyles(String styles)
  {
    this.styles = styles;
  }

  public String getStyles()
  {
    return styles;
  }

  public Border getBorderShaper()
  {
    if(borderShaper == null)
      borderShaper = new Border(getStyle(), getBoxInsideMargins());
    return borderShaper;
  }

  @Override
  public void mousePressed(MouseEvent e)
  {
    if(getProp().accepts_mouse_pressed())
      getProp().mouse_pressed(translatedEvent(e));
    else
      super.mousePressed(e);
  }

  @Override
  public void mouseReleased(MouseEvent e)
  {
    if(getProp().accepts_mouse_released())
      getProp().mouse_released(translatedEvent(e));
    else
      super.mouseReleased(e);
  }

  @Override
  public void mouseClicked(MouseEvent e)
  {
    if(getProp().accepts_mouse_clicked())
      getProp().mouse_clicked(translatedEvent(e));
    else
      super.mouseClicked(e);
  }

  @Override
  public void mouseDragged(MouseEvent e)
  {
    getProp().mouse_dragged(translatedEvent(e));
  }

  @Override
  public void mouseEntered(MouseEvent e)
  {
    getProp().mouse_entered(translatedEvent(e));
    hoverOn();
  }

  @Override
  public void mouseExited(MouseEvent e)
  {
    getProp().mouse_exited(translatedEvent(e));
    hoverOff();
  }

  @Override
  public void mouseMoved(MouseEvent e)
  {
    getProp().mouse_moved(translatedEvent(e));
  }

  @Override
  public void mouseWheelMoved(MouseWheelEvent e)
  {
    boolean isVertical = e.getModifiers() % 2 == 0;
    ScrollBarPanel scrollBar = isVertical ? verticalScrollbar : horizontalScrollbar;
    if(scrollBar != null)
      scrollBar.setValue(scrollBar.getValue() + e.getUnitsToScroll());
    else
      getParent().mouseWheelMoved(e);
  }

  @Override
  public void focusLost(FocusEvent e)
  {
    getProp().focus_lost(e);
  }

  @Override
  public void focusGained(FocusEvent e)
  {
    getProp().focus_gained(e);
  }

  @Override
  public void keyTyped(KeyEvent e)
  {
    getProp().key_typed(e);
  }

  @Override
  public void keyPressed(KeyEvent e)
  {
    getProp().key_pressed(e);
  }

  @Override
  public void keyReleased(KeyEvent e)
  {
    getProp().key_released(e);
  }

  @Override
  public void buttonPressed(ActionEvent e)
  {
    getProp().button_pressed(e);
  }

  @Override
  public void valueChanged(Object e)
  {
    getProp().value_changed(e);
  }

  public void setCursor(Cursor cursor)
  {
    getRoot().setCursor(cursor);
  }

  //TODO I don't think this is needed any longer.
  @Override
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
    job.paint(getRoot().getPanel());
    job.applyTo(getRoot().getGraphics());
//    }
  }

  //TODO I don't think this is needed any longer.
  public void paintImmediately(int a, int b, int c, int d)
  {
    repaint();
  }

  @Override
  public String toString()
  {
    return getClass().getSimpleName() + " - " + getProp().getName();
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

  @Override
  public boolean isFloater()
  {
    return getStyle().getCompiledFloat().isOn();
  }

  @Override
  public void doFloatLayout()
  {
    FloaterLayout.instance.doLayout(this);
  }

  //TODO super.clearCache() deals with absolute positioning.  Here the boxes are all relative.  They're uneccessarily being cleared.
  @Override
  public synchronized void clearCache()
  {
    super.clearCache();
    boxInsideMargins = null;
    boxInsideBorders = null;
    boxInsidePadding = null;
    childConsumableArea = null;
  }

  public void styleChanged(StyleAttribute attribute, StyleValue value)
  {
    if(!isIlluminated())
      return;

    // TODO MDM - move all this login into the StyleDescriptors

    if(getParent() != null && getRoot() != null)
    {
      if(Context.instance().bufferedImageCache != null &&
          attribute != Style.TRANSPARENCY &&
          attribute != Style.X &&
          attribute != Style.Y)
        Context.instance().bufferedImageCache.expire(this);

      if(attribute == Style.WIDTH || attribute == Style.HEIGHT)
      {
        sizeChangePending = true;
        markAsNeedingLayout();
        propagateSizeChangeUp(getParent());
        propagateSizeChangeDown();
      }
      else if(isBorderDescriptor(attribute) || isMarginPaddingOrBorder(value))
      {
        propagateSizeChangeDown();
        borderChanged = true;
        markAsNeedingLayout();
        clearCache();
      }
      else if(attribute == Style.X || attribute == Style.Y)
      {
        markAsNeedingLayout(FloaterLayout.instance);
      }
      else if(attribute == Style.HORIZONTAL_ALIGNMENT || attribute == Style.VERTICAL_ALIGNMENT)
      {
        markAsNeedingLayout();
      }
      else if(isTextDescriptor(attribute))
      {
        for(Panel child : getChildren())
        {
          if(child instanceof TextPanel)
          {
            sizeChangePending = true;
            ((TextPanel) child).styleChanged(attribute, value);
          }
        }
      }
      else
        markAsDirty();
    }
  }

  private boolean isTextDescriptor(StyleAttribute attribute)
  {
    return attribute == Style.TEXT_COLOR ||
        attribute == Style.FONT_FACE ||
        attribute == Style.FONT_SIZE ||
        attribute == Style.FONT_STYLE;
  }

  private boolean isMarginPaddingOrBorder(StyleValue value)
  {

    //TODO Huh?  This doesn't make sense.
    return value instanceof PixelsValue;
  }

  private boolean isBorderDescriptor(StyleAttribute attribute)
  {
    return attribute == Style.TOP_BORDER_WIDTH ||
        attribute == Style.RIGHT_BORDER_WIDTH ||
        attribute == Style.BOTTOM_BORDER_WIDTH ||
        attribute == Style.LEFT_BORDER_WIDTH ||
        attribute == Style.TOP_RIGHT_BORDER_WIDTH ||
        attribute == Style.BOTTOM_RIGHT_BORDER_WIDTH ||
        attribute == Style.BOTTOM_LEFT_BORDER_WIDTH ||
        attribute == Style.TOP_LEFT_BORDER_WIDTH ||
        attribute == Style.TOP_RIGHT_ROUNDED_CORNER_RADIUS ||
        attribute == Style.BOTTOM_RIGHT_ROUNDED_CORNER_RADIUS ||
        attribute == Style.BOTTOM_LEFT_ROUNDED_CORNER_RADIUS ||
        attribute == Style.TOP_LEFT_ROUNDED_CORNER_RADIUS;
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
    getStyle().applyScreen(getHoverStyle());
    getRoot().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
  }

  private void hoverOff()
  {
    if(getStyle().hasScreen())
    {
      getStyle().removeScreen();
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

  @Override
  protected boolean canRemove(Panel child)
  {
    return child != verticalScrollbar && child != horizontalScrollbar;
  }

  @Override
  public void illuminate()
  {
    if(styles != null)
    {
      Map<String, RichStyle> store = getRoot().getStylesStore();
      String[] styleNames = styles.split("[ ,]+");
      for(String styleName : styleNames)
      {
        RichStyle style = store.get(styleName);
        if(style != null)
          getStyle().addExtension(style);

        RichStyle hoverStyle = store.get(styleName + ".hover");
        if(hoverStyle != null)
          getHoverStyle().addExtension(hoverStyle);
      }
    }
    super.illuminate();
  }

  @Override
  public void delluminate()
  {
    getStyle().tearDown();
    getHoverStyle().tearDown();
    super.delluminate();
  }
}

