(ns org.xyz.home
  (:require [clj-http.client :as http]
            [com.biffweb :as biff]
            [org.xyz.middleware :as mid]
            [org.xyz.ui :as ui]
            [org.xyz.settings :as settings]
            [rum.core :as rum]))

(defn home-page [{:keys [recaptcha/site-key params] :as ctx}]
  (ui/page
   ctx
   [:h5 "- Welcome to my 100% FREE Website!"]
   [:p "Check out this cool art!"]
   [:canvas#art-canvas {
                       :width 600
                       :height 300
                       }]
   [:p "Your current time is:"]
   [:p#dynamic-clock "00:00:00 AM"]))

(def module
  {:routes [[""
             ["/"                  {:get home-page}]]]})
