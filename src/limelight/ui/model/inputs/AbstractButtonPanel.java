package limelight.ui.model.inputs;

import limelight.styles.ScreenableStyle;
import limelight.ui.EventAction;
import limelight.ui.Fonts;
import limelight.ui.Panel;
import limelight.ui.events.ButtonPushedEvent;
import limelight.ui.events.CharTypedEvent;
import limelight.ui.events.Event;
import limelight.ui.events.MouseClickedEvent;
import limelight.ui.model.TextPanel;

import java.awt.*;
import java.awt.font.TextLayout;

public abstract class AbstractButtonPanel extends InputPanel
{
  protected AbstractButtonPanel()
  {
    getEventHandler().add(MouseClickedEvent.class, PushAction.instance);
    getEventHandler().add(CharTypedEvent.class, PushAction.instance);
    getEventHandler().add(ButtonPushedEvent.class, PropogateToParentAction.instance);
  }

  public void paintOn(Graphics2D graphics)
  {
    ButtonTextPainter.paintOn(graphics, this);
  }

  private static boolean isPushEvent(Event event)
  {
    return event instanceof MouseClickedEvent || ((event instanceof CharTypedEvent) && ((CharTypedEvent) event).getChar() == ' ');
  }

  private static class PushAction implements EventAction
  {
    public static PushAction instance = new PushAction();

    public void invoke(Event event)
    {
      if(event.isConsumed())
        return;

      if(isPushEvent(event))
      {
        final Panel panel = event.getRecipient();
        new ButtonPushedEvent(panel).dispatch(panel);
      }
    }
  }

  protected static class ButtonTextPainter
  {
    public static void paintOn(Graphics2D graphics, AbstractButtonPanel panel)
    {
      final ScreenableStyle style = panel.getStyle();
      graphics.setColor(style.getCompiledTextColor().getColor());

      TextLayout textLayout = new TextLayout(panel.getText(), Fonts.fromStyle(style), TextPanel.staticFontRenderingContext);
      int height = (int) ((textLayout.getAscent() + textLayout.getDescent() + textLayout.getLeading()) + 0.5);
      int width = (int) (textLayout.getBounds().getWidth() + textLayout.getBounds().getX() + 0.5);
      final Dimension textDimensions = new Dimension(width, height);

      int textX = style.getCompiledHorizontalAlignment().getX(textDimensions.width, panel.getBounds());
      float textY = style.getCompiledVerticalAlignment().getY(textDimensions.height, panel.getBounds()) + textLayout.getAscent();
      textLayout.draw(graphics, textX, textY + 1);
    }
  }

}
