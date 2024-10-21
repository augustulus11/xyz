(ns org.xyz.frontend.cg-art)

(def first-color "white")
(def second-color "black")

(def cell-width-default 32)
(def cell-height 32)

(defn other-color [color]
  (if (= color first-color) second-color first-color))

(defn draw-square! [context x y width height color]
  (set! (.-fillStyle context) color)
  (.fillRect context x y width height))

(defn movement-in-squares-next-row [context x y canvasHeight cell-width color]
  (when (< y canvasHeight)
    (let [cur-color (other-color color)]
      (draw-square! context x y cell-width cell-height cur-color)
      (movement-in-squares-next-row context x (+ y cell-height) canvasHeight cell-width cur-color))))


(defn movement-in-squares-next-column [context x canvasWidth canvasHeight color]
  (when (< x canvasWidth)
    (let [horizon (* canvasWidth 0.65)
          cell-width (max 2 (* cell-width-default (Math/sqrt (if (< x horizon)
                                                               (/ (- horizon x) horizon)
                                                               (/ (- x horizon) horizon)))))]
      (movement-in-squares-next-row context x 0 canvasHeight cell-width color)
      (movement-in-squares-next-column context (+ x cell-width) canvasWidth canvasHeight (other-color color)))))

(defn movement-in-squares! [canvas]
  (let [width (.-width canvas)
        height (.-height canvas)
        context (.getContext canvas "2d")]
    (.clearRect context 0 0 width height)
    (movement-in-squares-next-column context 0 width height first-color)))

