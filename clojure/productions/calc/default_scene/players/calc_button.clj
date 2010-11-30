(defn enter [lcd]
  (prop-text= lcd (str (load-string (str (prop-text lcd))))))

(defn press [prop lcd]
  (let [current-text (prop-text lcd)
        middle-text (.substring current-text
                      1 (- (.length current-text) 1))]
    (prop-text= lcd (str "(" middle-text (prop-text prop) ")"))))

(defn clear [lcd]
  (prop-text= lcd "()"))

(on-mouse-clicked
  (fn [e]
    (let [prop (.getProp e)
          lcd (find-prop prop "lcd")
          button-text (prop-text prop)]
      (cond
        (= "c" button-text)
          (clear lcd)
        (= "enter" button-text)
          (enter lcd)
        :else
          (press prop lcd)))))

