#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Limelight

  Main = Java::limelight.Main
  ResourceLoader = Java::limelight.ResourceLoader
  Context = Java::limelight.Context
  Studio = Java::limelight.Studio

  module Background
    Animation = Java::limelight.background.Animation
  end

  module Styles #:nodoc:
    Style = Java::limelight.styles.Style
    FlatStyle = Java::limelight.styles.FlatStyle
    RichStyle = Java::limelight.styles.RichStyle
    ScreenableStyle = Java::limelight.styles.ScreenableStyle
  end

  module Util #:nodoc:
    Colors = Java::limelight.util.Colors
    Version = Java::limelight.util.Version
    Debug = Java::limelight.util.Debug
  end

  module UI #:nodoc:

    ButtonGroupCache = Java::limelight.ui.ButtonGroupCache

    module Model #:nodoc:
      Frame = Java::limelight.ui.model.StageFrame
      PropPanel = Java::limelight.ui.model.PropPanel
      ScenePanel = Java::limelight.ui.model.ScenePanel
      ImagePanel = Java::limelight.ui.model.ImagePanel

      module Inputs #:nodoc:
        Button2Panel = Java::limelight.ui.model.inputs.Button2Panel
        ComboBox2Panel = Java::limelight.ui.model.inputs.ComboBox2Panel
        RadioButton2Panel = Java::limelight.ui.model.inputs.RadioButton2Panel
        CheckBox2Panel = Java::limelight.ui.model.inputs.CheckBox2Panel
        TextBox2Panel = Java::limelight.ui.model.inputs.TextBox2Panel
        TextArea2Panel = Java::limelight.ui.model.inputs.TextArea2Panel

        ButtonPanel = Java::limelight.ui.model.inputs.ButtonPanel
        CheckBoxPanel = Java::limelight.ui.model.inputs.CheckBoxPanel
        ComboBoxPanel = Java::limelight.ui.model.inputs.ComboBoxPanel
        RadioButtonPanel = Java::limelight.ui.model.inputs.RadioButtonPanel
        TextAreaPanel = Java::limelight.ui.model.inputs.TextAreaPanel
        TextBoxPanel = Java::limelight.ui.model.inputs.TextBoxPanel
        PasswordBoxPanel = Java::limelight.ui.model.inputs.PasswordBoxPanel
      end
    end

    module Api #:nodoc:
      Scene = Java::limelight.ui.api.Scene
      Prop = Java::limelight.ui.api.Prop
      Stage = Java::limelight.ui.api.Stage
      Theater = Java::limelight.ui.api.Theater
      Production = Java::limelight.ui.api.Production
    end

    module Painting #:nodoc:
      PaintAction = Java::limelight.ui.painting.PaintAction
    end

  end

  module Util #:nodoc:

    Packer = Java::limelight.io.Packer

  end
end