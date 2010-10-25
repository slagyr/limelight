(defn enter [lcd]
  (.setText lcd (str (load-string (str (.getText lcd))))))

(defn press [prop lcd]
  (let [current-text (.getText lcd)
        middle-text (.substring current-text
                      1 (- (.length current-text) 1))]
    (.setText lcd (str "(" middle-text (.getText prop) ")"))))

(defn clear [lcd]
  (.setText lcd "()"))

(on-mouse-clicked
  (fn [e]
    (let [prop (.getRecipient e)
          lcd (.find (.getRoot prop) "lcd")
          button-text (.getText prop)]
      (cond
        (= "c" button-text)
          (clear lcd)
        (= "enter" button-text)
          (enter lcd)
        :else
          (press prop lcd)))))

