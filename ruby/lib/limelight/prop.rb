#- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the MIT License.

require 'limelight/java_util'
require 'limelight/optionable'
require 'limelight/pen'
require 'limelight/paint_action'
require 'limelight/animation'
require 'limelight/util/hashes'

module Limelight

  # Prop is the fundamental building block of a scene.  A prop represents a rectangular area in the scene, of almost any dimension.
  # It may have borders, backgrounds, margin, padding, and it may contain other props or text.  However it is the props'
  # Styles that determine their size and appearance.
  #
  # A Prop may have one parent and many children.  Hence, when put together, they form a tree structure.  The Scene is
  # the root Prop of a tree.
  #
  class Prop

    include Optionable

    include Java::limelight.model.api.PropProxy

    attr_reader :peer #:nodoc:
    getters :loader #:nodoc:

    alias :getPeer :peer


    # When creating a Prop, an optional Hash is accepted. These are called initialization options.
    # The key/value pairs in the initialization options will be used to
    # set properties on the Prop, it Style, or included Player properties. These properties are not set
    # until the prop is added to a Prop tree with a Scene.
    #
    def initialize(options={})
      @peer = Java::limelight.ui.model.PropPanel.new(self)
      @peer.add_options(Util::Hashes.for_java(options))
    end

    def id
      @peer.id
    end

    def name
      @peer.name
    end

    def players
      @peer.players
    end

    def backstage
      if @backstage == nil
        @backstage = Util::Hashes.for_ruby(@peer.backstage)
      end
      @backstage
    end

    def style
      @peer.style
    end

    def hover_style
      @peer.hover_style
    end

    # Add a Prop as a child of this Prop.
    #
    def add(child)
#      child.set_parent(self)
#      @children << child
      @peer.add(child.peer)
    end

    # Same as add.  Returns self so adding may be chained.
    #
    #   prop << child1 << child2 << child3
    #
    def <<(child)
      add(child)
      self
    end

    # Allows the adding of child Props using the PropBuilder DSL.
    #
    #    prop.build do
    #      child1 do
    #        grand_child
    #      end
    #      child2
    #    end
    #
    def build(options = {}, & block)
      require 'limelight/dsl/prop_builder'
      builder = Limelight::DSL::PropBuilder.new(self)
      builder.__install_instance_variables(options)
      builder.__root_path__ = scene.path
      builder.instance_eval(& block)
    end

    # Removes a child Prop.  The child Prop will be parent-less after removal.
    #
    def remove(child)
#      if children.delete(child)
#        scene.unindex_prop(child) if scene
        @peer.remove(child.peer)
#      end
    end

    # Removes all child Props.
    #
    def remove_all
      @peer.remove_all
#      @children.each { |child| scene.unindex_prop(child) } if scene
#      @children = []
    end

    # A hook to invoke behavior after a Prop is painted.
    #
    def after_painting(flag = true, & block)
      if flag
        @peer.after_paint_action = PaintAction.new(& block)
      else
        @peer.after_paint_action = nil
      end
    end

    # Searches all descendant of the Prop (including itself) for Props with the specified name.
    # Returns an Array of matching Props. Returns an empty Array if none are found.
    #
    def find_by_name(name)
      @peer.find_by_name(name).map { |descendant| descendant.proxy }
    end

    # Sets the text of this Prop.  If a prop is given text, it will become sterilized (it may not have any more children).
    # Some Players such as text_box, will cause the text to appear in the text_box.
    #
    def text=(value)
      @peer.text = value.to_s
    end

    # Returns the text of the Prop.
    #
    def text
      @peer.text
    end

    def parent
      @peer.parent.proxy if @peer.parent
    end

    def children
      @peer.child_prop_panels.map { |child| child.proxy }
    end

    # Returns the scene to which this prop belongs to.
    #
    def scene
#      return nil if @parent.nil?
#      @scene = @parent.scene if @scene.nil?
      @peer.root.proxy
    end

    # Returns the current Production this Prop lives in.
    #
    def production
      scene.production
    end

    def to_s #:nodoc:
      "#{self.class.name}[id: #{id}, name: #{name}]"
    end

    def inspect #:nodoc:
      self.to_s
    end

    # Returns a Point representing the location of the Prop's top-left corner within its parent.
    #
    #   location = prop.location
    #   location.x # the Prop's distance from the left of its parent's bounds
    #   location.y # the Prop's distance from the top of its parent's bounds
    #
    def location
      peer.get_location
    end

    # Returns a Point representing the location of the Prop's top-left corner within its Stage (Window).
    #
    #   location = prop.absolute_location
    #   location.x # the Prop's distance from the left edge of the stage
    #   location.y # the Prop's distance from the top edge of the stage
    #
    def absolute_location
      peer.get_absolute_location.clone
    end

    # Returns a Box representing the relative bounds of the Prop. Is useful with using the Pen.
    #
    #   bounds = prop.bounds
    #   bounds.x, box.y # represents the Prop's location within its parent Prop
    #   bounds.width, box.height # represents the Prop's dimensions
    #
    def bounds
      peer.get_bounds.clone
    end

    # Returns a Box representing the bounds inside the borders of the prop.
    #
    #   bounds = prop.bordered_bounds
    #   bounds.x # the distance between the left edge of the Prop and the inside edge of its left border
    #   bounds.y # the distance between the top edge of the Prop and the inside edge of its top border
    #   bounds.width # the distance between the inside edges of the left and right border
    #   bounds.height # the distance between the inside edges of the top and bottom borders
    #
    def bordered_bounds
      peer.get_bordered_bounds.clone
    end

    # Returns a Box representing the bounds inside the padding of the prop.
    #
    #   bounds = prop.padded_bounds
    #   bounds.x # the distance between the left edge of the Prop and the inside edge of its left padding
    #   bounds.y # the distance between the top edge of the Prop and the inside edge of its top padding
    #   bounds.width # the distance between the inside edges of the left and right padding
    #   bounds.height # the distance between the inside edges of the top and bottom padding
    #
    def padded_bounds
      peer.get_padded_bounds.clone
    end

    # Returns a Pen object. Pen objects allow to you to draw directly on the screen, withing to bounds of this Prop.
    #
    def pen
      Pen.new(peer.getGraphics)
    end

    # Initiate an animation loop.  Options may include :name (string), :updates_per_second (int: defaults to 60)
    # An Animation object is returned.
    # The provided block will be invoked :updates_per_second times per second until the Animation is stopped.
    #
    #    @animation = prop.animate(:updates_per_second => 20) do
    #      prop.style.border_width = (prop.style.top_border_width.to_i + 1).to_s
    #      @animation.stop if prop.style.top_border_width.to_i > 60
    #    end
    #
    # This above example will cause the Prop's border to grow until it is 60 pixels wide.
    #
    def animate(options={}, & block)
      animation = Animation.new(self, block, options)
      animation.start
      animation
    end

    # Plays a sound on the computers audio output.  The parameter is the filename of a .au sound file.
    # This filename should relative to the root directory of the current Production, or an absolute path.
    #
    def play_sound(filename)
      path = Java::limelight.Context.fs.path_to(scene.path, filename)
      @peer.play_sound(path)
    end

    # Luanches the spcified URL using the OS's default handlers. For example, opening a URL in a browser:
    #
    #   launch('http://www.google.com')
    #
    # To create a link prop add an accessor on the player (say url) and use that in the prop definition
    # Ex:
    #
    #   link :text => "I am a link", :url => "http://www.8thlight.com"
    def launch(url)
      Java::limelight.Context.instance.os.launch(url)
    end

    # GUI Events ##########################################

    def on_mouse_pressed(& action)
      @peer.event_handler.add(Java::limelight.ui.events.panel.MousePressedEvent, action)
    end

    def on_mouse_released(& action)
      @peer.event_handler.add(Java::limelight.ui.events.panel.MouseReleasedEvent, action)
    end

    def on_mouse_clicked(& action)
      @peer.event_handler.add(Java::limelight.ui.events.panel.MouseClickedEvent, action)
    end

    def on_mouse_moved(& action)
      @peer.event_handler.add(Java::limelight.ui.events.panel.MouseMovedEvent, action)
    end

    def on_mouse_dragged(& action)
      @peer.event_handler.add(Java::limelight.ui.events.panel.MouseDraggedEvent, action)
    end

    def on_mouse_entered(& action)
      @peer.event_handler.add(Java::limelight.ui.events.panel.MouseEnteredEvent, action)
    end

    def on_mouse_exited(& action)
      @peer.event_handler.add(Java::limelight.ui.events.panel.MouseExitedEvent, action)
    end

    def on_mouse_wheel(& action)
      @peer.event_handler.add(Java::limelight.ui.events.panel.MouseWheelEvent, action)
    end

    def on_key_pressed(& action)
      @peer.event_handler.add(Java::limelight.ui.events.panel.KeyPressedEvent, action)
    end

    def on_key_released(& action)
      @peer.event_handler.add(Java::limelight.ui.events.panel.KeyReleasedEvent, action)
    end

    def on_char_typed(& action)
      @peer.event_handler.add(Java::limelight.ui.events.panel.CharTypedEvent, action)
    end

    def on_focus_gained(& action)
      @peer.event_handler.add(Java::limelight.ui.events.panel.FocusGainedEvent, action)
    end

    def on_focus_lost(& action)
      @peer.event_handler.add(Java::limelight.ui.events.panel.FocusLostEvent, action)
    end

    def on_button_pushed(& action)
      @peer.event_handler.add(Java::limelight.ui.events.panel.ButtonPushedEvent, action)
    end

    def on_value_changed(& action)
      @peer.event_handler.add(Java::limelight.ui.events.panel.ValueChangedEvent, action)
    end

    def on_illuminated(& action)
      @peer.event_handler.add(Java::limelight.ui.events.panel.IlluminatedEvent, action)
    end

  end
end
