(ns limelight.scene
  (:use [limelight.prop :only (prop-fns PropHelp)]
        [limelight.casting-director :only (new-casting-director)]
        [limelight.common])
  (:import [limelight.casting-director CastingDirector]))

(deftype Scene [peer casting-director]
  limelight.model.api.SceneProxy
  (getPeer [this] @peer)
  (applyOptions [this options] nil))

(extend Scene
  PropHelp
  prop-fns)


(defn new-scene [options]
  (let [scene (Scene. (atom nil) (atom nil))
        casting-director (new-casting-director scene)
        peer (limelight.ui.model.ScenePanel. scene)]
    (swap! (.peer scene) (fn [_] peer))
    (swap! (.casting-director scene) (fn [_] casting-director))
    (.addOptions peer (limelight.util.OptionsMap. options))
    (.setCastingDirector peer casting-director)
    scene))

;(defn production [scene]
;  (.getProxy (.getProduction @(.peer scene))))

(defmethod path-to Scene [scene path]
  (.pathTo (.getResourceLoader @(.peer scene)) path))
