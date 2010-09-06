#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'monitor'

module Production #:nodoc:

  def production_opening
    @monitor = Monitor.new
    @alert_monitor = @monitor.new_cond
    @incompatible_version_monitor = @monitor.new_cond
  end

  def allow_close?
    return false
  end

  def default_scene
    nil
  end

  def process_incompatible_version_response(response)
    @incompatible_version_response = response
    @monitor.synchronize { @incompatible_version_monitor.signal }
    @incompatible_version_stage.close
  end

  def proceed_with_incompatible_version?(production_name, required_version)
    @incompatible_version_response = nil
    load_incompatible_version_scene(production_name, required_version)
    @monitor.synchronize { @incompatible_version_monitor.wait } if @incompatible_version_response.nil?
    return @incompatible_version_response
  end

  def load_incompatible_version_scene(production_name, required_version)
    if !theater["Incompatible Version"]
      @incompatible_version_stage = theater.add_stage("Incompatible Version", :location => [:center, :center], :size => [400, :auto],
            :background_color => :white, :framed => false, :always_on_top => true, :vital => false)
    end
    producer.open_scene("incompatible_version", @incompatible_version_stage, :instance_variables => { :production_name => production_name, :required_version => required_version })
  end

  def alert(message)
    @alert_response = nil
    @alert_monitor = @monitor.new_cond
    load_alert_scene(message.to_s)
    @monitor.synchronize{ @alert_monitor.wait }
    @alert_stage.close
    return @alert_response
  end

  def process_alert_response(response)
    @alert_response = response
    @monitor.synchronize{ @alert_monitor.signal } if @alert_monitor
  end

  def load_alert_scene(message)
    if !theater["Alert"]
      @alert_stage = theater.add_stage("Alert", :location => [:center, :center], :size => [400, :auto],
            :background_color => :white, :framed => false, :always_on_top => true, :vital => false)
    end
    producer.open_scene("alert", @alert_stage, :instance_variables => { :message => message })
  end

end