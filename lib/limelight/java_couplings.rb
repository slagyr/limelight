#- Copyright 2008 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.
    
module Limelight #:nodoc:

  Main = Java::limelight.Main
  ResourceLoader = Java::limelight.ResourceLoader
  Context = Java::limelight.Context
  JAnimation = Java::limelight.Animation

  module Styles #:nodoc:
    Style = Java::limelight.styles.Style
    FlatStyle = Java::limelight.styles.FlatStyle
    RichStyle = Java::limelight.styles.RichStyle
    ScreenableStyle = Java::limelight.styles.ScreenableStyle
  end

  module Util #:nodoc:
    Colors = Java::limelight.util.Colors
  end

  module UI #:nodoc:

    ButtonGroupCache = Java::limelight.ui.ButtonGroupCache

    module Model #:nodoc:
      Frame = Java::limelight.ui.model.Frame
      Panel = Java::limelight.ui.model.PropPanel

      module Inputs #:nodoc:
       ButtonPanel = Java::limelight.ui.model.inputs.ButtonPanel
       CheckBoxPanel = Java::limelight.ui.model.inputs.CheckBoxPanel
       ComboBoxPanel = Java::limelight.ui.model.inputs.ComboBoxPanel
       RadioButtonPanel = Java::limelight.ui.model.inputs.RadioButtonPanel
       TextAreaPanel = Java::limelight.ui.model.inputs.TextAreaPanel
       TextBoxPanel = Java::limelight.ui.model.inputs.TextBoxPanel
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