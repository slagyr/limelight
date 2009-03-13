#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module DownloadButton

  def mouse_clicked(e)
    url = scene.find("url_field").text
    begin
      puts "url: #{url}"
      file = Limelight::Util::Downloader.download(url.strip)
      scene.open_production(file)
    rescue Exception => e
      scene.stage.alert(e.to_s)
    end
  end

end