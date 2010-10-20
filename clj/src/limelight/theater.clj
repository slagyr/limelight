(ns limelight.theater
  (:use [limelight.stage :only (new-stage)]))

(deftype Theater [peer production]
  limelight.model.api.TheaterProxy
  (buildStage [this name options]
    @(.peer (new-stage this name options))))
