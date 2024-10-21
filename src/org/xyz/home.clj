(ns org.xyz.home
  (:require [clj-http.client :as http]
            [com.biffweb :as biff]
            [org.xyz.middleware :as mid]
            [org.xyz.ui :as ui]
            [org.xyz.settings :as settings]
            [rum.core :as rum]
            [xtdb.api :as xt]))

(defn home-page [{:keys [recaptcha/site-key params] :as ctx}]
  (ui/page
   ctx
   [:p "Hello world"]
   [:p "Check out this cool art!"]
   [:canvas#artCanvas {
                       :width 600
                       :height 300
                       }]
   [:p "Check out this cool clock!"]
   [:p#dynamicClock "00:00:00 AM"]))

(def module
  {:routes [[""
             ["/"                  {:get home-page}]]]})
