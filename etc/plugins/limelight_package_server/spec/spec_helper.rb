#- Copyright 2008 8th Light, Inc.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.dirname(__FILE__) + '/../../../../spec/spec_helper'

plugin_spec_dir = File.dirname(__FILE__)
ActiveRecord::Base.logger = Logger.new(plugin_spec_dir + "/debug.log")

