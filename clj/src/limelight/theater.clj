(ns limelight.theater)

(deftype Theater [peer production]
  limelight.model.api.TheaterProxy
  (buildStage [this name options] nil))
