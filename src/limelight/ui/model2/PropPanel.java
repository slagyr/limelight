package limelight.ui.model2;

import limelight.ui.Painter;
import limelight.ui.PaintablePanel;
import limelight.ui.api.PropablePanel;
import limelight.ui.api.Prop;
import limelight.ui.painting.BackgroundPainter;
import limelight.ui.painting.BorderPainter;
import limelight.ui.painting.Border;
import limelight.ui.painting.PaintAction;
import limelight.util.Box;
import limelight.LimelightError;
import limelight.styles.Style;

import java.util.LinkedList;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class PropPanel extends BasePanel implements PropablePanel, PaintablePanel
{
  private Prop prop;
  private PropPanelLayout layout;
  private LinkedList<Painter> painters;
  private Border borderShaper;
  private TextPaneTextAccessor textAccessor;
  private Box boxInsideMargins;
  private Box boxInsideBorders;
  private Box boxInsidePadding;
  private Style style;
  private PaintAction afterPaintAction;

  public PropPanel(Prop prop)
  {
    this.prop = prop;
    buildPainters();
    layout = new PropPanelLayout(this);
    textAccessor = new TextPaneTextAccessor(this);
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
    textAccessor.setText(text);
  }

  public TextPaneTextAccessor getTextAccessor()
  {
    return textAccessor;
  }

  public void setTextAccessor(TextPaneTextAccessor textAccessor)
  {
    this.textAccessor = textAccessor;
  }

  public void snapToSize()
  {
    Box r = getParent().getChildConsumableArea();
    int newWidth = translateDimension(getProp().getStyle().getWidth(), r.width);
    int newHeight = translateDimension(getProp().getStyle().getHeight(), r.height);
    setSize(newWidth, newHeight);
  }

  private int translateDimension(String sizeString, int maxSize)
  {
    if(sizeString == null)
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

  public Prop getProp()
  {
    return prop;
  }

  public Box getBoxInsideMargins()
  {
    if(boxInsideMargins == null)
    {
      boxInsideMargins = (Box) getBoundingBox().clone();
      boxInsideMargins.shave(getStyle().asInt(getStyle().getTopMargin()), getStyle().asInt(getStyle().getRightMargin()), getStyle().asInt(getStyle().getBottomMargin()), getStyle().asInt(getStyle().getLeftMargin()));
    }
    return boxInsideMargins;
  }

  public Box getBoxInsideBorders()
  {
    if(boxInsideBorders == null)
    {
      boxInsideBorders = (Box) getBoxInsideMargins().clone();
      boxInsideBorders.shave(getStyle().asInt(getStyle().getTopBorderWidth()), getStyle().asInt(getStyle().getRightBorderWidth()), getStyle().asInt(getStyle().getBottomBorderWidth()), getStyle().asInt(getStyle().getLeftBorderWidth()));
    }
    return boxInsideBorders;
  }

  public Box getBoxInsidePadding()
  {
    if(boxInsidePadding == null)
    {
      boxInsidePadding = (Box) getBoxInsideBorders().clone();
      boxInsidePadding.shave(getStyle().asInt(getStyle().getTopPadding()), getStyle().asInt(getStyle().getRightPadding()), getStyle().asInt(getStyle().getBottomPadding()), getStyle().asInt(getStyle().getLeftPadding()));
    }
    return boxInsidePadding;
  }

  public Box getChildConsumableArea()
  {
    return getBoxInsidePadding();
  }

  public void doLayout()
  {                   
    if(borderShaper != null)
      borderShaper.updateDimentions();

    layout.doLayout();

    //TODO MDM added because it's needed... kinda fishy though.  There'a a better way.
    if (borderShaper != null)
      borderShaper.setBounds(getBoxInsideMargins());
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
    {
      style = prop.getStyle();
    }
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
    getProp().hover_on();
  }

  public void mouseExited(MouseEvent e)
  {
    getProp().mouse_exited(translatedEvent(e));
    getProp().hover_off();
  }

  public void mouseMoved(MouseEvent e)
  {
    getProp().mouse_moved(translatedEvent(e));
  }

  //TODO This is little inefficient.  Reconsider what get's passed to props.
  private MouseEvent translatedEvent(MouseEvent e)
  {
    e = new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiers(), e.getX(), e.getY(), e.getClickCount(), false);
    Point absoluteLocation = getAbsoluteLocation();
    e.translatePoint(absoluteLocation.x * -1, absoluteLocation.y * -1);
    return e;
  }

  public void mouseWheelMoved(MouseWheelEvent e)
  {                
    getParent().mouseWheelMoved(e);
  }

  public void setCursor(Cursor cursor)
  {
    getRoot().setCursor(cursor);
  }

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
      job.paint(((RootPanel)getRoot()).getPanel()); //TODO - cast should not be neccessary here.
      job.applyTo(getRoot().getGraphics());
//    }
  }

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
    return "on".equals(getStyle().getFloat());
  }

  protected void clearCache()
  {
    super.clearCache();
    boxInsideMargins = null;
    boxInsideBorders = null;
    boxInsidePadding = null;
  }
}

