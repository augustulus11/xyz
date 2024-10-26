(ns org.xyz.blerbs
  (:require [clj-http.client :as http]
            [com.biffweb :as biff]
            [org.xyz.middleware :as mid]
            [org.xyz.ui :as ui]
            [org.xyz.settings :as settings]
            [rum.core :as rum]))

(defn blerbs-page [ctx & body]
  (ui/page
   ctx
   [:p "These are my blerbs"]))

(def module
  {:routes [["/blerbs"
             ["/" {:get blerbs-page}]]]})
