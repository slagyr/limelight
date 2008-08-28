
class Animation < Limelight::AnimationTask

  def initialize(prop, block, options={})
    @block = block
    name = options[:name] || "#{prop.to_s} animation"
    updates_per_second = options[:updates_per_second] || 60
    super(name, updates_per_second, prop.panel)
  end

  def doPerform
    @block.call 
  end

end