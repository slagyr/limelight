module Limelight

  Main = Java::limelight.Main
  SceneLoader = Java::limelight.SceneLoader
  Context = Java::limelight.Context

  module Styles
    Style = Java::limelight.styles.Style
    FlatStyle = Java::limelight.styles.FlatStyle
    RichStyle = Java::limelight.styles.RichStyle
    ScreenableStyle = Java::limelight.styles.ScreenableStyle 
  end

  module UI

    Prop = Java::limelight.ui.Prop
    ButtonGroupCache = Java::limelight.ui.ButtonGroupCache
    Colors = Java::limelight.ui.Colors
    Panel = Java::limelight.ui.Panel
    Scene = Java::limelight.ui.Scene
    Stage = Java::limelight.ui.Stage
    Frame = Java::limelight.ui.Frame
    Theater = Java::limelight.ui.Theater

    module Painting
      PaintAction = Java::limelight.ui.painting.PaintAction
      TextBoxPainter = Java::limelight.ui.painting.TextBoxPainter
      ButtonPainter = Java::limelight.ui.painting.ButtonPainter
      CheckBoxPainter = Java::limelight.ui.painting.CheckBoxPainter
      ComboBoxPainter = Java::limelight.ui.painting.ComboBoxPainter
      RadioButtonPainter = Java::limelight.ui.painting.RadioButtonPainter
      TextAreaPainter = Java::limelight.ui.painting.TextAreaPainter
    end

  end

  module Util

    Packer = Java::limelight.io.Packer

  end
end