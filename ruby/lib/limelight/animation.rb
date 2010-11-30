#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.
                    
module Limelight

  # An object to manage a seqeunce of changes, assumedly to a prop.  The Animation object is constructed with options
  # and a block.  Once the Animation is started, the block is invoked repeatedly until the Animation is stopped.
  #
  # Although, this object does not update the screen, it provides a means to perform sequential updates in evenly
  # spaced time frames.
  #
  class Animation < Limelight::Background::Animation

    # A Prop and block are required to construct an Animation.  Options may include
    # 1. :name (string)
    # 2. :updates_per_second (int defaults to 60)
    #
    #   animation = Animation.new(prop, Proc.new { "do something"}, :updates_per_second => 20)
    #   animation.start
    #   # time passes
    #   animation.stop
    #
    def initialize(prop, block, options={})
      @block = block
      name = options[:name] || "#{prop.to_s} animation"
      updates_per_second = options[:updates_per_second] || 60
      super(updates_per_second)
    end

    def doUpdate #:nodoc:
      @block.call
    end

    # Lets you know if the animation is currently running or not.
    #
    def running?
      return is_running
    end
  end

end