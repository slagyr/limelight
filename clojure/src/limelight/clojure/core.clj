;- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
;- Limelight and all included source files are distributed under terms of the MIT License.

(ns limelight.clojure.core
  (:use
    [limelight.clojure.util :only (->options map-for-java)]))

(def *log* (java.util.logging.Logger/getLogger "clojure"))

(defn log [& messages]
  (.info *log* (apply str messages)))

(defn peer [thing]
  (let [result (._peer thing)]
    (if (= clojure.lang.Atom (class result))
      @result
      result)))

; Protocols -----------------------------------------------

(defprotocol ResourceRoot
  (resource-path [this resource]))

(defprotocol Pathed
  (path [this]))

(defprotocol ProductionSource
  (production [this]))

(defprotocol TheaterSource
  (theater [this]))

(defprotocol SceneSource
  (scene [this]))

(defprotocol StageSource
  (stage [this]))

(defprotocol PropSource
  (prop [this]))

(defprotocol Identified
  (id [this]))

(defprotocol Textable
  (text [this])
  (text= [this value]))

(defprotocol Backstaged
  (backstage [this])
  (backstage-get [this key])
  (backstage-put [this key value]))

; TODO extend events

; Prop functions ------------------------------------------

(defn- as-proxies [peer-props]
  (map (fn [child] (.getProxy child)) peer-props))

(defn children [parent]
  (as-proxies (.getChildPropPanels @(._peer parent))))

(defn add [parent & children]
  (let [peer-prop @(._peer parent)
        children (flatten children)]
    (doseq [child children]
      (if child
        (.add peer-prop @(._peer child))))
    children))

(defn parent [prop]
  (.getProxy (.getParent @(._peer prop))))

(defn find-by-id [prop id]
  (if-let [peer-result (.find @(._peer (scene prop)) id)]
    (.getProxy peer-result)
    nil))

(defn find-by-name [root name]
  (as-proxies (.findByName @(._peer root) name)))

(defn players [prop]
  (.getPlayers (peer prop)))

; Production functions ------------------------------------

(defn open-scene
  ([production scene-name] (.getProxy (.openScene (._peer production) scene-name {})))
  ([production scene-name stage-or-options]
    (if (string? stage-or-options)
      (.getProxy (.openScene (._peer production) scene-name stage-or-options {}))
      (.getProxy (.openScene (._peer production) scene-name (map-for-java stage-or-options)))))
  ([production scene-name stage-name options]
    (.getProxy (.openScene (._peer production) scene-name stage-name (map-for-java options)))))

; Theater functions ---------------------------------------

(defn build-stage [theater name & options]
  (let [options (->options options)
        stage-peer (.buildStage theater name options)]
    (.getProxy stage-peer)))

(defn add-stage [theater stage]
  (.add (._peer theater) @(._peer stage)))

(defn build-and-add-stage [theater name & options]
  (let [stage (apply build-stage theater name options)]
    (add-stage theater stage)
    stage))

(defn get-stage [theater name]
  (if-let [peer (.get (._peer theater) name)]
    (.getProxy peer)
    nil))

(defn default-stage [theater]
  (.getProxy (.getDefaultStage (._peer theater))))

(defn stages [theater]
  (map #(.getProxy %) (.getStages (._peer theater))))

; Event Extension -----------------------------------------

(extend-type limelight.ui.events.panel.PanelEvent
  PropSource
  (prop [this] (.getProp this))

  SceneSource
  (scene [this] (scene (prop this)))

  StageSource
  (stage [this] (stage (prop this)))

  ProductionSource
  (production [this] (production (prop this)))
  )

(extend-type limelight.model.events.ProductionEvent
  ProductionSource
  (production [this] (.getProxy (.getProduction this)))
  )