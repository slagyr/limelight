;- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
;- Limelight and all included source files are distributed under terms of the MIT License.

(ns limelight.clojure.scene
  (:use
    [limelight.clojure.casting :only (new-player-recruiter)]
    [limelight.clojure.util :only (map-for-java ->options read-src)]
    [limelight.clojure.core]))

(deftype Scene [_peer _player-recruiter _helper-ns _props-ns]

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
  (backstage-get [this key] (.get (.getBackstage @_peer) key))
  (backstage-put [this key value] (.put (.getBackstage @_peer) key value))
  )

(defn- load-scene-helper [scene production]
  (let [helper-path (resource-path scene "helper.clj")
        fs (limelight.Context/fs)
        helper-src (if (.exists fs helper-path) (.readTextFile fs helper-path) nil)]
    (when helper-src
      (binding [*ns* (._helper-ns scene)]
        (refer 'clojure.core)
        (refer 'limelight.clojure.core)
        (when production (refer (.getName @(._helper-ns production))))
        (read-src helper-path helper-src)))))

(defn- prepare-props-ns [scene production]
  (binding [*ns* (._props-ns scene)]
    (refer 'clojure.core)
    (refer 'limelight.clojure.core)
    (refer 'limelight.clojure.prop-building)
    (refer (.getName (._helper-ns scene)))
    (when production (refer (.getName @(._helper-ns production))))))

(defn new-scene [& args]
  (let [options (->options args)
        helper-ns (create-ns (gensym "limelight.dynamic.scene.helper-"))
        props-ns (create-ns (gensym "limelight.dynamic.scene.props-"))
        scene (Scene. (atom nil) (atom nil) helper-ns props-ns)
        player-recruiter (new-player-recruiter scene)
        peer (limelight.ui.model.ScenePanel. scene)
        production (:production options)
        options (dissoc options :production)]
    (reset! (._peer scene) peer)
    (reset! (._player-recruiter scene) player-recruiter)
    (.addOptions peer (map-for-java options))
    (.setPlayerRecruiter peer player-recruiter)
    (when production (.setProduction peer (._peer production)))
    (load-scene-helper scene production)
    (prepare-props-ns scene production)
    scene))



