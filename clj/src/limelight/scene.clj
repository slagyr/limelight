(ns limelight.scene
  (:use [limelight.prop :only (prop-fns PropHelp)]))

(deftype Scene [peer]
  limelight.model.api.SceneProxy
  (getPeer [this] @peer)
  (applyOptions [this options] nil))

(extend Scene
  PropHelp
  prop-fns)

(defn new-scene [options]
  (let [scene (Scene. (atom nil))
        peer (limelight.ui.model.ScenePanel. scene)]
    (swap! (.peer scene) (fn [_] peer))
    (.addOptions peer (limelight.util.OptionsMap. options))
    scene))