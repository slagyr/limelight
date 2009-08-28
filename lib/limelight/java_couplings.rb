#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.
    
module Limelight #:nodoc:

  Main = Java::limelight.Main
  ResourceLoader = Java::limelight.ResourceLoader
  Context = Java::limelight.Context

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
      Panel = Java::limelight.ui.model.PropPanel
      ImagePanel = Java::limelight.ui.model.ImagePanel

      module Inputs #:nodoc:
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
      Studio = Java::limelight.ui.api.Studio
    end

    module Painting #:nodoc:
      PaintAction = Java::limelight.ui.painting.PaintAction
    end

  end

  module Util #:nodoc:

    Packer = Java::limelight.io.Packer

  end
end