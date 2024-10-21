(ns org.xyz.credits
  (:require [clj-http.client :as http]
            [com.biffweb :as biff]
            [org.xyz.middleware :as mid]
            [org.xyz.ui :as ui]
            [org.xyz.settings :as settings]
            [rum.core :as rum]
            [xtdb.api :as xt]))

(defn credits-page [ctx & body]
  (ui/page
   ctx
   [:p "I didn't make this alone!"]))

(def module
  {:routes [["/credits"
             ["/" {:get credits-page}]]]})
