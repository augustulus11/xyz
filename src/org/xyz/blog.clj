(ns org.xyz.blog
  (:require [clj-http.client :as http]
            [com.biffweb :as biff]
            [org.xyz.middleware :as mid]
            [org.xyz.ui :as ui]
            [org.xyz.settings :as settings]
            [rum.core :as rum]))

(defn blog-page [ctx & body]
  (ui/page
   ctx
   [:p "This is my blog"]))

(def module
  {:routes [["/blog"
             ["/" {:get blog-page}]]]})
