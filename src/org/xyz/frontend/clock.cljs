(ns org.xyz.frontend.clock
  (:require [goog.string :as gstring]
            [goog.string.format]))

(defn formatted-time [date-component]
  (.padStart (.toString date-component) 2 "0"))

(defn tick-clock! [clock-tag]
  (let [now (js/Date.)]
    (set! (.-textContent clock-tag)
          (gstring/format "%s:%s:%s %s" (formatted-time (.getHours now))
                          (formatted-time (.getMinutes now))
                          (formatted-time (.getSeconds now))
                          (if (< (.getHours now) 12) "am" "pm")))))
