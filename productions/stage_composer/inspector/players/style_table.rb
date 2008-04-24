module StyleTable
  
  def populate(style)
    find_by_name('style_value').each do |style_value|
      style_value.populate(style)
    end
  end
  
end