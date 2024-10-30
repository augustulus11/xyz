(ns org.xyz.blog
  (:require [clj-http.client :as http]
            [com.biffweb :as biff]
            [ring.util.codec :as ring-codec]
            [org.xyz.middleware :as mid]
            [org.xyz.ui :as ui]
            [org.xyz.settings :as settings]
            [rum.core :as rum]
            [org.xyz.database :as database]
            [clojure.tools.logging :as log]))

(defn generate-blog-entry
  "Generate a blog entry from a blog from the database's blog table."
  [blog]
  [:.blog-preview
   [:a.link {:href (blog :url-name)} [:h3 (ring-codec/url-decode (blog :url-name))]]
   [:p.blog-preview (subs (blog :content) (min 255 (count (blog :content))))]])

(defn generate-blog-entries
  "Generate blog page shortcuts from a vector of blog entries from the database's blog table."
  [blogs]
  (if (= 1 (count blogs))
    (generate-blog-entry (first blogs))
    (list (generate-blog-entries (rest blogs)))))

(defn blog-home [{:keys [example/ds] :as ctx} & body]
  (ui/page
   ctx
   [:h2 ""]
   [:p "This is my blog"]
   [:.blog-list (generate-blog-entries (database/get-n-blog-posts ds 10))]
   [:h3 "End blog list"]))

(defn blog-page [{:keys [path-params] :as ctx}]
  (ui/page
   ctx
   ))

(def module
  {:routes ["/blog" {:middleware [mid/wrap-site-defaults]}
            ["/" {:get blog-home}]
            ["/:blog-name" {:get blog-page}]]})
