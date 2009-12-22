package limelight.ui.model.inputs;

import limelight.ui.model.*;
import limelight.ui.Panel;
import limelight.util.Box;
import limelight.styles.Style;
import limelight.styles.HorizontalAlignment;
import limelight.styles.VerticalAlignment;
import limelight.styles.styling.SimpleHorizontalAlignmentAttribute;
import limelight.styles.styling.SimpleVerticalAlignmentAttribute;
import limelight.Context;

import javax.imageio.ImageIO;
import java.awt.event.MouseEvent;
import java.awt.event.FocusEvent;
import java.awt.event.ActionEvent;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.IOException;

public class ComboBox2Panel extends BasePanel implements TextAccessor, InputPanel
{
  private static Font font = new Font("Arial", Font.BOLD, 12);
  private static ComboBoxStyle normal = new ComboBoxStyle("combo_box");
  private static ComboBoxStyle focusedStyle = new ComboBoxStyle("combo_box_focus");

  private static SimpleHorizontalAlignmentAttribute horizontalTextAlignment = new SimpleHorizontalAlignmentAttribute(HorizontalAlignment.LEFT);
  private static SimpleVerticalAlignmentAttribute verticalTextAlignment = new SimpleVerticalAlignmentAttribute(VerticalAlignment.CENTER);
  private String text;
  private ComboBoxStyle style;
  private Rectangle textBounds;
  private Dimension textDimensions;
  private TextLayout textLayout;
  private boolean focused;

  public ComboBox2Panel()
  {
    style = normal;
    setSize(128, 29);
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
      focusedStyle.paintOn(graphics, this);
    style.paintOn(graphics, this);

    if(text != null)
    {
      graphics.setColor(Color.BLACK);
      calculateTextDimentions();
      int textX = horizontalTextAlignment.getX(textDimensions.width, getBoundingBox()) + style.innerLeftX;
      float textY = verticalTextAlignment.getY(textDimensions.height, getBoundingBox()) + textLayout.getAscent();
      textLayout.draw(graphics, textX, textY + 1);
    }
  }

  private void calculateTextDimentions()
  {
    if(textBounds == null && text != null)
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
    textBounds = null;
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
    super.buttonPressed(new ActionEvent(this, 0, "blah"));
  }

  public void focusGained(FocusEvent e)
  {
    focused = true;
    repaint();
    super.focusGained(e);
  }

  public void focusLost(FocusEvent e)
  {
    focused = false;
    repaint();
    super.focusLost(e);
  }

  private static class ComboBoxStyle
  {
    private String prefix;
    private boolean loaded;
    public int innerLeftX;
    public BufferedImage leftImage;
    public BufferedImage middleImage;
    public BufferedImage rightImage;

    private ComboBoxStyle(String prefix)
    {
      this.prefix = prefix;
    }


    public void load()
    {
      if(loaded)
        return;
      try
      {
        leftImage = ImageIO.read(getClass().getClassLoader().getResource("limelight/ui/images/" + prefix + "_l.png"));
        middleImage = ImageIO.read(getClass().getClassLoader().getResource("limelight/ui/images/" + prefix + "_m.png"));
        rightImage = ImageIO.read(getClass().getClassLoader().getResource("limelight/ui/images/" + prefix + "_r.png"));
        innerLeftX = leftImage.getWidth();
        loaded = true;
      }
      catch(IOException e)
      {
        throw new RuntimeException(e);
      }
    }

    public void paintOn(Graphics2D graphics, Panel button)
    {
      load();
      int innerRightX = button.getWidth() - rightImage.getWidth();

      graphics.drawImage(leftImage, 0, 0, null);
      graphics.drawImage(rightImage, innerRightX, 0, null);
      for(int x = innerLeftX; x < innerRightX; x++)
        graphics.drawImage(middleImage, x, 0, null);
    }
  }
}
