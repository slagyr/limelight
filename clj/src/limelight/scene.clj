(ns limelight.scene
  (:use
    [limelight.casting-director :only (new-casting-director)]
    [limelight.common])
  (:import
    [limelight.casting-director CastingDirector]))

(deftype Scene [peer casting-director]

  limelight.model.api.SceneProxy
  (getPeer [this] @peer)
  (applyOptions [this options] nil)

  ResourceRoot
  (resource-path [this resource]
    (.pathTo (.getResourceLoader @(.peer this)) resource)))


(defn new-scene [options]
  (let [scene (Scene. (atom nil) (atom nil))
        casting-director (new-casting-director scene)
        peer (limelight.ui.model.ScenePanel. scene)]
    (swap! (.peer scene) (fn [_] peer))
    (swap! (.casting-director scene) (fn [_] casting-director))
    (.addOptions peer (limelight.util.OptionsMap. options))
    (.setCastingDirector peer casting-director)
    scene))

