package limelight.ui.model.inputs;

import limelight.ui.model.*;
import limelight.util.Box;
import limelight.styles.Style;
import limelight.styles.HorizontalAlignment;
import limelight.styles.VerticalAlignment;
import limelight.styles.styling.SimpleHorizontalAlignmentAttribute;
import limelight.styles.styling.SimpleVerticalAlignmentAttribute;
import limelight.Context;
import java.awt.event.MouseEvent;
import java.awt.event.FocusEvent;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.*;
import java.awt.font.TextLayout;

public class TextBox2Panel extends BasePanel implements TextAccessor, InputPanel
{
  private boolean focused;
  private String text;
  private static SimpleHorizontalAlignmentAttribute horizontalTextAlignment = new SimpleHorizontalAlignmentAttribute(HorizontalAlignment.LEFT);
  private static SimpleVerticalAlignmentAttribute verticalTextAlignment = new SimpleVerticalAlignmentAttribute(VerticalAlignment.CENTER);
  private Dimension textDimensions;
  private TextLayout textLayout;
  private static Font font = new Font("Arial", Font.PLAIN, 12);

  public TextBox2Panel()
  {
    setSize(150, 25);
  }

  public void setParent(limelight.ui.Panel panel)
  {
    if(panel == null)
      Context.instance().keyboardFocusManager.focusFrame((StageFrame) getRoot().getStageFrame());
    super.setParent(panel);
    if(panel instanceof PropPanel)
    {
      PropPanel propPanel = (PropPanel) panel;
      propPanel.sterilize();
      propPanel.setTextAccessor(this);
    }
  }

  public Box getBoxInsidePadding()
  {
    return getBoundingBox();
  }

  public Box getChildConsumableArea()
  {
    return getBoundingBox();
  }

  public void paintOn(Graphics2D graphics)
  {
    if(focused)
      graphics.setColor(Color.green);
    else
      graphics.setColor(Color.gray);

    graphics.drawRect(0, 0, width - 1, height - 1);

    if(text != null)
    {
      graphics.setColor(Color.BLACK);
      calculateTextDimentions();
      int textX = horizontalTextAlignment.getX(textDimensions.width, getBoundingBox()) + 2;
      float textY = verticalTextAlignment.getY(textDimensions.height, getBoundingBox()) + textLayout.getAscent();
      textLayout.draw(graphics, textX, textY + 1);
    }
  }

  private void calculateTextDimentions()
  {
    if(text != null)
    {
      textLayout = new TextLayout(text, font, TextPanel.staticFontRenderingContext);
      int height = (int) ((textLayout.getAscent() + textLayout.getDescent() + textLayout.getLeading()) + 0.5);
      int width = (int) (textLayout.getBounds().getWidth() + textLayout.getBounds().getX() + 0.5);
      textDimensions = new Dimension(width, height);
    }
  }

  public Style getStyle()
  {
    return getParent().getStyle();
  }

  public void setText(String text)
  {
    this.text = text;
  }

  public String getText()
  {
    return text;
  }

  public void mousePressed(MouseEvent e)
  {
    super.mousePressed(e);
  }

  public void mouseReleased(MouseEvent e)
  {
    super.mouseReleased(e);
    Context.instance().keyboardFocusManager.focusPanel(this);
//    focusGained(new FocusEvent(getRoot().getStageFrame().getWindow(), 0));
    super.buttonPressed(new ActionEvent(this, 0, "blah"));
  }

  public void focusGained(FocusEvent e)
  {
    focused = true;
    repaint();
    startCursor();
    super.focusGained(e);
  }

  public void focusLost(FocusEvent e)
  {
    focused = false;
    stopCursor();
    repaint();
    super.focusLost(e);

  }

  public void keyPressed(KeyEvent e)
  {

  }

  private void startCursor()
  {
    ;      
  }

  private void stopCursor()
  {

  }
}
