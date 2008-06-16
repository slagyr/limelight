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

  module Util
    Colors = Java::limelight.util.Colors
  end

  module Rapi
    Scene = Java::limelight.rapi.Scene
    Prop = Java::limelight.rapi.Prop
    Stage = Java::limelight.rapi.Stage
    Theater = Java::limelight.rapi.Theater
  end

  module UI

    ButtonGroupCache = Java::limelight.ui.ButtonGroupCache
    Frame = Java::limelight.ui.Frame
    Panel = Java::limelight.ui.Panel

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