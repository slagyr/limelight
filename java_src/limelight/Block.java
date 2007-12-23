package limelight;

interface Block
{
  Panel getPanel();
  Style getStyle();
  String getName();
  String getText();
  void setText(String value);

  void mouseClicked();
  void hoverOn();
  void mouseEntered();
  void mouseExited();
  void hoverOff();
}
