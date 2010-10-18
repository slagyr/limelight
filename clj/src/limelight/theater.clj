(ns limelight.theater)

(deftype Theater []
  limelight.model.api.TheaterProxy
  (buildStage [this name options] nil))
