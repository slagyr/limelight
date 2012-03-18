;- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
;- Limelight and all included source files are distributed under terms of the MIT License.

(ns limelight.clojure.scene
  (:use
    [limelight.clojure.casting :only (new-player-recruiter)]
    [limelight.clojure.util :only (map-for-java ->options read-src)]
    [limelight.clojure.core]))

(deftype Scene [_peer _player-recruiter _ns]

  limelight.model.api.SceneProxy
  (getPeer [this] @_peer)
  (applyOptions [this options] nil)

  clojure.lang.Named
  (getName [this] (.getName @_peer))

  limelight.clojure.core.ResourceRoot
  (resource-path [this resource] (.pathTo (limelight.Context/fs) (.getPath @_peer) resource))

  limelight.clojure.core.Pathed
  (path [this] (.getPath @_peer))

  limelight.clojure.core.ProductionSource
  (production [this] (if-let [prod (.getProduction @_peer)] (.getProxy prod) nil))

  limelight.clojure.core.SceneSource
  (scene [this] this)

  limelight.clojure.core.PropSource
  (prop [this] this)

  limelight.clojure.core.StageSource
  (stage [this] (.getProxy (.getStage @_peer)))

  limelight.clojure.core.Identified
  (id [this] (.getId @_peer))

  limelight.clojure.core.Textable
  (text [this] (.getText @_peer))
  (text= [this value] (.setText @_peer value))

  limelight.clojure.core.Backstaged
  (backstage [this] (.getBackstage @_peer))
  (backstage-get [this key] (.get (.getBackstage @_peer) (name key)))
  (backstage-put [this key value] (.put (.getBackstage @_peer) (name key) value))
  )

(defn- prepare-ns [scene production]
  (binding [*ns* (._ns scene)]
    (refer 'clojure.core)
    (refer 'limelight.clojure.core)
    (refer 'limelight.clojure.prop-building)
    (when production (alias 'production (.getName @(._ns production))))
    (doseq [player (clj-players scene)] (alias (symbol (._name player)) (.getName (._ns player))))
    ))

(defn new-scene [& args]
  (let [options (->options args)
        ns (create-ns (gensym "limelight.dynamic.scene-"))
        scene (Scene. (atom nil) (atom nil) ns)
        player-recruiter (or (:player-recruiter options) (new-player-recruiter scene))
        options (dissoc options :player-recruiter)
        peer (limelight.ui.model.ScenePanel. scene player-recruiter)
        production (:production options)
        options (dissoc options :production)]
    (reset! (._peer scene) peer)
    (reset! (._player-recruiter scene) player-recruiter)
    (when production (.setProduction peer (._peer production)))
    (.addOptions peer (map-for-java options))
    (prepare-ns scene production)
    scene))



