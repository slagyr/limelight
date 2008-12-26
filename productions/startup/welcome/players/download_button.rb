#- Copyright 2008 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module DownloadButton

  def mouse_clicked(e)
    url = scene.find("url_field").text
    downloader = Limelight::Context.instance.downloader
    begin
      file = downloader.download(url)
      scene.open_production(file.absolute_path)
    rescue Exception => e
      scene.stage.alert(e.to_s)
    end
  end

end