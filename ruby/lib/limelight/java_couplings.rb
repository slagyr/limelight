#- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the MIT License.

module Limelight

  About = Java::limelight.About
  Context = Java::limelight.Context
  Studio = Java::limelight.model.Studio

  module BuiltIn
    Styles = Java::limelight.builtin.BuiltInStyles
  end

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
#      Frame = Java::limelight.ui.model.Stage
#      PropPanel = Java::limelight.ui.model.PropPanel
#      ScenePanel = Java::limelight.ui.model.ScenePanel
      ImagePanel = Java::limelight.ui.model.ImagePanel

      module Inputs #:nodoc:
        ButtonPanel = Java::limelight.ui.model.inputs.ButtonPanel
        CheckBoxPanel = Java::limelight.ui.model.inputs.CheckBoxPanel
        DropDownPanel = Java::limelight.ui.model.inputs.DropDownPanel
        RadioButtonPanel = Java::limelight.ui.model.inputs.RadioButtonPanel
        TextAreaPanel = Java::limelight.ui.model.inputs.TextAreaPanel
        TextBoxPanel = Java::limelight.ui.model.inputs.TextBoxPanel
      end
    end

    module Api #:nodoc:
#      Scene = Java::limelight.ui.api.Scene
#      Prop = Java::limelight.ui.api.Prop
#      StageProxy = Java::limelight.ui.api.StageProxy
#      Theater = Java::limelight.ui.api.Theater
#      Production = Java::limelight.ui.api.Production
    end

    module Painting #:nodoc:
      PaintAction = Java::limelight.ui.painting.PaintAction
    end

  end

  module Util #:nodoc:

    Packer = Java::limelight.io.Packer

  end
end