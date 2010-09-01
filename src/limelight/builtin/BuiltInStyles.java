package limelight.builtin;

import limelight.styles.RichStyle;
import limelight.util.Colors;

import java.util.HashMap;

public class BuiltInStyles
{
  private static HashMap<String, RichStyle> styles;

  public static HashMap<String, RichStyle> all()
  {
    if(styles != null)
      return styles;

    styles = new HashMap<String, RichStyle>();
    styles.put("limelight_builtin_curtains", buildCurtains());
    styles.put("limelight_builtin_combo_box_popup_list", buildComboBoxPopupList());
    styles.put("limelight_builtin_combo_box_popup_list_item", buildComboBoxPopupListItem());
    styles.put("limelight_builtin_combo_box_popup_list_item_selected", buildComboBoxPopupListItemHover());

    return styles;
  }

  private static RichStyle buildCurtains()
  {
    RichStyle style = new RichStyle();
    style.setFloat("on");
    style.setX(0);
    style.setY(0);
    style.setWidth("100%");
    style.setHeight("100%");
    style.setBackgroundColor(Colors.toString(Colors.TRANSPARENT));
    return style;
  }

  private static RichStyle buildComboBoxPopupList()
  {
    RichStyle style = new RichStyle();
    style.setFloat("on");
    style.setBackgroundColor("#EEED");
    style.setBorderWidth(1);
    style.setRoundedCornerRadius(5);
    style.setBorderColor("#dcdcdc");
    style.setVerticalScrollbar("on");
    style.setMinHeight(50);
    style.setMaxHeight(200);
    return style;
  }

  private static RichStyle buildComboBoxPopupListItem()
  {
    RichStyle style = new RichStyle();
    style.setWidth("100%");
    style.setPadding(3);
    style.setLeftPadding(10);
    return style;
  }

  private static RichStyle buildComboBoxPopupListItemHover()
  {
    RichStyle style = new RichStyle();
    style.setTextColor("white");
    style.setBackgroundColor("#bbd453");
    style.setSecondaryBackgroundColor("#9fb454");
    style.setGradientAngle(270);
    style.setGradient("on");
    return style;
  }
}
