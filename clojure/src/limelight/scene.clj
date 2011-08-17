(ns limelight.scene
  (:use
    [limelight.casting :only (new-casting-director)]
    [limelight.common]))

(deftype Scene [peer casting-director]

  limelight.model.api.SceneProxy
  (getPeer [this] @peer)
  (applyOptions [this options] nil)

  ResourceRoot
  (resource-path [this resource]
    (log "(.getResourceLoader @(.peer this)): " (.getRoot (.getResourceLoader @(.peer this))))
    (.pathTo (.getResourceLoader @(.peer this)) resource)))


(defn new-scene [options]
  (let [scene (Scene. (atom nil) (atom nil))
        casting-director (new-casting-director scene)
        peer (limelight.ui.model.ScenePanel. scene)]
    (reset! (.peer scene) peer)
    (reset! (.casting-director scene) casting-director)
    (.addOptions peer (limelight.util.OptionsMap. options))
    (.setCastingDirector peer casting-director)
    scene))

