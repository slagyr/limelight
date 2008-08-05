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

  module UI

    ButtonGroupCache = Java::limelight.ui.ButtonGroupCache

    module Model
      Frame = Java::limelight.ui.model2.Frame
      Panel = Java::limelight.ui.model2.PropPanel
      module Painting
        TextBoxPainter = Java::limelight.ui.model2.painting.TextBoxPainter
        ButtonPainter = Java::limelight.ui.model2.painting.ButtonPainter
        CheckBoxPainter = Java::limelight.ui.model2.painting.CheckBoxPainter
        ComboBoxPainter = Java::limelight.ui.model2.painting.ComboBoxPainter
        RadioButtonPainter = Java::limelight.ui.model2.painting.RadioButtonPainter
        TextAreaPainter = Java::limelight.ui.model2.painting.TextAreaPainter
      end
      module Inputs
       ButtonPanel = Java::limelight.ui.model2.inputs.ButtonPanel
       CheckBoxPanel = Java::limelight.ui.model2.inputs.CheckBoxPanel
       ComboBoxPanel = Java::limelight.ui.model2.inputs.ComboBoxPanel
       RadioButtonPanel = Java::limelight.ui.model2.inputs.RadioButtonPanel
       TextAreaPanel = Java::limelight.ui.model2.inputs.TextAreaPanel
       TextBoxPanel = Java::limelight.ui.model2.inputs.TextBoxPanel
      end
    end

    module Api
      Scene = Java::limelight.ui.api.Scene
      Prop = Java::limelight.ui.api.Prop
      Stage = Java::limelight.ui.api.Stage
      Theater = Java::limelight.ui.api.Theater
    end

    module Painting
      PaintAction = Java::limelight.ui.painting.PaintAction
    end

  end

  module Util

    Packer = Java::limelight.io.Packer

  end
end