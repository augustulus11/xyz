(ns org.xyz.webrings-and-buttons
  (:require [clj-http.client :as http]
            [com.biffweb :as biff]
            [org.xyz.middleware :as mid]
            [org.xyz.ui :as ui]
            [org.xyz.settings :as settings]
            [rum.core :as rum]
            [xtdb.api :as xt]))

(defn webring-and-buttons-page [ctx & body]
  (ui/page
   ctx
   [:p "This has my webrings and buttons"]))

(def module
  {:routes [["/webrings-and-buttons"
             ["/" {:get webring-and-buttons-page}]]]})
