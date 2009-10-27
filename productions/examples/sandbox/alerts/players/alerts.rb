#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Alerts

  prop_reader :message_input

  def open_alert
    stage.alert(message_input.text)
  end

  def open_incompatible_production
    production_path = File.join(path, "incompatible_production")
    Thread.new { Limelight::Context.instance.studio.open(production_path) }
  end

end