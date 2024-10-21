(ns org.xyz.ui
  (:require [cheshire.core :as cheshire]
            [clojure.java.io :as io]
            [org.xyz.settings :as settings]
            [com.biffweb :as biff]
            [ring.middleware.anti-forgery :as csrf]
            [ring.util.response :as ring-response]
            [rum.core :as rum]))

(defn tailwind-css-path []
  (if-some [last-modified (some-> (io/resource "public/css/main.css")
                                  ring-response/resource-data
                                  :last-modified
                                  (.getTime))]
    (str "/css/main.css?t=" last-modified)
    "/css/main.css"))

(defn style-css-path []
  (if-some [last-modified (some-> (io/resource "public/css/style.css")
                                  ring-response/resource-data
                                  :last-modified
                                  (.getTime))]
    (str "/css/style.css?t=" last-modified)
    "/css/style.css"))

(defn js-path []
  (if-some [last-modified (some-> (io/resource "public/js/main.js")
                                  ring-response/resource-data
                                  :last-modified
                                  (.getTime))]
    (str "/js/main.js?t=" last-modified)
    "/js/main.js"))

(defn base [{:keys [::recaptcha] :as ctx} & body]
  (apply
   biff/base-html
   (-> ctx
       (merge #:base{:title settings/app-name
                     :lang "en-US"
                     :icon "/img/glider.png"
                     :description "Just an epic website."
                     :image "https://clojure.org/images/clojure-logo-120b.png"})
       (update :base/head (fn [head]
                            (concat [[:link {:rel "stylesheet" :href (tailwind-css-path)}]
                                     [:link {:rel "stylesheet" :href (style-css-path)}]
                                     [:script {:src (js-path)}]
                                     [:script {:src "https://unpkg.com/htmx.org@1.9.12"}]
                                     [:script {:src "https://unpkg.com/htmx.org@1.9.12/dist/ext/ws.js"}]
                                     [:script {:src "https://unpkg.com/hyperscript.org@0.9.8"}]]
                                    head))))
   body))


(defn nav []
  [:.top-row
   [:.container-fluid
    [:a.link {:href "/blog"} "blog"]
    [:a.link {:href "/blerbs"} "blerbs"]
    [:a.link {:href "/buttons"} "buttons"]
    [:a.link {:href "/credits"} "credits"]]])

(defn page [ctx & body]
  (base
   ctx
   [:.page
    [:.sidebar (nav)]
    [:.p-3.mx-auto.max-w-screen-sm.w-full
     (when (bound? #'csrf/*anti-forgery-token*)
       {:hx-headers (cheshire/generate-string
                     {:x-csrf-token csrf/*anti-forgery-token*})})
     [:main
      [:article body]]]]))

(defn on-error [{:keys [status ex] :as ctx}]
  {:status status
   :headers {"content-type" "text/html"}
   :body (rum/render-static-markup
          (page
           ctx
           [:h1.text-lg.font-bold
            (if (= status 404)
              "Page not found."
              "Something went wrong.")]))})
