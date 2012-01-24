package limelight.model;

import limelight.styles.RichStyle;

import java.util.Map;

public interface StylesSource
{
  String getPath();
  Map<String, RichStyle> getStyles();
}
