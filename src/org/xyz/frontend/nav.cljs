(ns org.xyz.frontend.nav
  (:require [goog.string :as gstring]
            [goog.string.format]))

(defn pathname
  "Get the path name of the current page; i.e. javascript's window.location.pathname."
  []
  (.-pathname (.-location js/window)))

(defn highlight-tag-with-id!
  "Highlight the tag with the given id."
  [name]
  (let [tag (.getElementById js/document name)]
    (if tag
      (set! (.-id tag) "nav-selected")
      (println "Could not find tag with name " name))))

(defn highlight-navbar!
  "Set the navigation tag for the current page to the #nav-selected id."
  []
  (cond
    (= (pathname) "/")                          (highlight-tag-with-id! "nav-home")
    (gstring/starts-with? (pathname) "/blog")   (highlight-tag-with-id! "nav-blog")
    :else (println "The path name " (pathname) " is not recognized!")))

