(ns org.xyz.frontend.main
  (:require [org.xyz.frontend.clock :as clock]
            [org.xyz.frontend.cg-art :as cg-art]))

(defn ^:export do-art-canvas! [canvas]
  (cg-art/movement-in-squares! canvas))

(defn ^:export start-ticking! [clock-tag]
  (clock/tick-clock! clock-tag)
  (js/setInterval clock/tick-clock! 1000 clock-tag))

(defn ^:export stop-ticking! [interval-id]
  (js/clearInterval interval-id))

;; Main function for initing script resources.
(defn main! []
  (println "Loaded CLJS script")
  (if (nil? (some-> (.getElementById js/document "artCanvas")
                    (do-art-canvas!)))
    (println "Failed to find art canvas!")
    (println "Found art canvas."))
  (if (nil?(some-> (.getElementById js/document "dynamicClock")
                   (start-ticking!)))
    (println "Failed to find dynamicClock!")
    (println "Found dynamic clock."))
  (println "Done loading CLJS script."))

;; Init test function used for development in the REPL.
(defn test-init! []
  (println "Init test")
  (do-art-canvas! (.getElementById js/document "artCanvas"))
  (start-ticking! (.getElementById js/document "testClock")))
