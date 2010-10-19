(ns limelight.scene)

(deftype Scene []
  limelight.model.api.SceneProxy
  (getPeer [this] nil))