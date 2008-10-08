#- Copyright 2008 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'limelight/prop'
require 'limelight/scene'
require 'limelight/dsl/build_exception'
require 'limelight/util'

module Limelight

  # A trigger to build a Limelight::Scene using the PropBuilder DSL.
  #
  # See Limelight::PropBuilder
  #
  def self.build_scene(options={}, &block)
    loader = options.delete(:build_loader)
    builder = DSL::SceneBuilder.new(options)
    builder.__loader__ = loader
    builder.instance_eval(&block) if block
    return builder.__prop__
  end

  module DSL

    # The basis of the DSL for building Limelight::Prop objects.
    #
    # Sample usage:
    #
    #   builder = Limelight::PropBuilder.new(a_prop)
    #   builder.instance_eval(&block)
    #
    # The prop passed into the constructor will be the root of the contructed Prop tree.
    # The block passed into instance_eval contains the DSL for building props.
    #
    # Example block/DSL:
    #
    #   parent :id => "the_parent" do
    #     child_one do
    #       grand_child_one :id => "gc_1", :styles => "grand_child"
    #       grand_child_two :id => "gc_2", :styles => "grand_child"
    #     end
    #     child_two
    #   end
    #
    # The above example will create a Limelight::Prop named 'parent' and add it to the root prop passed into the builder.
    # The 'parent' prop will contain two props named 'child_one' and 'child_two'.  'child_one' will contain two props named
    # 'grand_child_one' and 'grand_child_two'.  'child_two' has no child props nor do 'grand_child_one' or 'grand_child_two'.
    #
    # An options Hash may be passed into each prop.  The key, value pairs in the hash will be used to set properties on the prop
    # when it is added to a Limelight::Scene.
    #
    # See Limelight::Prop
    #
    class PropBuilder

      Limelight::Util.lobotomize(self)

      # Returns the root prop either passed in or created by this builder.
      #
      attr_reader :__prop__
      attr_accessor :__loader__

      # Creates a new builder.  If a prop is passed it, it will be the root on which props are created.
      # If the paramter is a Hash, the Hash will be used to construct a prop that will be used as the root.
      #
      def initialize(options)
        if options.is_a?(Prop)
          @__prop__ = options
        else
          @__prop__ = Prop.new(options)
        end
      end

      # Add extra initialization options to the prop currently under construction.
      #
      #   tree :id => "stump" do
      #     __ :height => "100%", :width => "30", :background_color => :brown
      #     branch :height => "100", :width => "20"
      #     branch do
      #       __ :height => "100", :width => "20"
      #     end
      #   end
      #
      # In the above example, the 'tree' prop has the following initialization options: id, height, width, background_color.
      # The two 'branch' child props are identical.
      #
      def __(options)
        @__prop__.add_options(options)
      end

      # Installs props from another file using the prop DSL.  The path will be relative to the
      # root directory of the current production.
      #
      def __install(file)
        raise "Cannot install external props because no loader was provided" if @__loader__.nil?
        raise "External prop file: '#{file}' doesn't exist" if !@__loader__.exists?(file)
        content = @__loader__.load(file)
        begin
          self.instance_eval(content)
        rescue Exception => e
          raise BuildException.new(file, content, e)
        end
      end


      def method_missing(sym, options={}, &prop) # :nodoc:
        options[:name] ||= sym.to_s
        builder = PropBuilder.new(options)
        builder.__loader__ = @__loader__
        builder.instance_eval(&prop) if prop
        @__prop__.add(builder.__prop__)
      end
    end

    class SceneBuilder < PropBuilder # :nodoc:
      def initialize(options)
        @__prop__ = Scene.new(options)
      end
    end

  end
end