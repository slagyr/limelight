#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require File.expand_path(File.dirname(__FILE__) + "/../lib/init")
require 'spec'
require 'limelight/mouse'

Limelight::Main.new.configureTestContext
context = Limelight::Context.instance
context.frameManager = Java::limelight.ui.model.InertFrameManager.new

class Object
  def stubs!(stubs = {})
    stubs.each_pair { |key, value| self.stub!(key).and_return(value) }
  end
end


require 'fileutils'
class TestDir

  class << self

    def root
      @root = File.expand_path(File.dirname(__FILE__) + "/../etc/tmp") if @filename.nil?
      return @root
    end

    def path(path)
      return File.join(root, path)
    end  

    def clean
      Dir.entries(root).each do |file|
        FileUtils.rm_r(File.join(root, file), :force => true) unless (file == '.' || file == '..')
      end
    end

    def create_file(path, content)
      filename =  self.path(path)
      establish_dir(File.dirname(filename))
      File.open(filename, 'w') { |file| file.write content }
      return filename
    end

    def establish_dir(path)
      if !File.exists?(path)
        establish_dir(File.dirname(path))
        Dir.mkdir(path)
      end
    end
  end
end

module Spec #:nodoc:
  module Example
    class ExampleGroup

      def mouse
        if @mouse.nil?
          @mouse = Limelight::Mouse.new
        end
        return @mouse
      end

    end
  end
end



# TODO Delete me
class MouseEvent

  attr_accessor :x, :y

  def initialize(x, y)
    @x = x
    @y = y
  end

end
