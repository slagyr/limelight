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

(defprotocol Buildable
  (build-props [this src context]))

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

(defn remove-all [prop]
  (.removeAll (peer prop)))

(defn remove-child [parent child]
  (.remove (peer parent) (peer child)))

(defn add-child
  ([parent child] (.add (peer parent) (peer child)))
  ([parent child index] (.add (peer parent) index (peer child))))

(defn build [root src & context]
  (build-props root src (->options context)))

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

; Styles --------------------------------------------------

(def ^{:private true} STYLE-MAP
  (reduce
    (fn [h d]
      (let [name (limelight.util.StringUtil/spearCase (.getName d))]
        (assoc h name d (keyword name) d)))
    {} limelight.styles.Style/STYLE_LIST))

(defn style-descriptor [key]
  (if-let [descriptor (get STYLE-MAP key)]
    descriptor
    (throw (limelight.LimelightException. (str "Unknown style: " key)))))

; MDM - Would rather not create a wrapper like this... but Clojure doesn't allow the use of extend-type
; to make limelight.styles.Style implement the clojure.lang.ILoopup.  It would be nice if Clojure
; used a protocol in addition to interfaces.
(deftype Style [_peer]
  clojure.lang.ILookup
  (valAt [this key] (.get _peer (style-descriptor key)))
  (valAt [this key not-found] (or (.valAt this key) nil)))

(defn style
  ([prop] (Style. (.getStyle @(._peer prop))))
  ([prop key] (.get (.getStyle @(._peer prop)) (style-descriptor key))))

(defn style= [prop & args]
  (when (odd? (count args))
    (throw (limelight.LimelightException. (str "missing value for key: " (last args)))))
  (let [style (.getStyle @(._peer prop))]
    (doseq [[key value] (partition 2 args)]
      (.put style (style-descriptor key) value))))