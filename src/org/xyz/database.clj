(ns org.xyz.database
  (:require
   [next.jdbc :as jdbc]
   [honey.sql :as honey]
   [ring.util.codec :as ring-codec]
   [clojure.tools.logging :as log]
   [org.xyz :as xyz]))

(defn exec-sql
  "Execute the sql string."
  [command]
  (log/info "Performing SQL query: " command)
  (jdbc/execute! xyz/db command))

(defn exec-honey-sql
  "Execute honey sql."
  [command]
  (exec-sql (honey/format command)))

(defn get-url-from-title
  "."
  [title]
  (let [encoded-title (ring-codec/url-encode title)]
    (subs encoded-title (min 255 (count encoded-title)))))

(defn create-blog-post
  "Create a blog post."
  [title content & {:keys [image-path author previous-blog-url next-blog-url]}]
  (let [url (get-url-from-title title)]
    (log/info "Creating blog post at '"
              url
              "' from author '" author "'"
              " with image '" image-path "'"
              " with previous blog '" previous-blog-url "'"
              " and next blog '" next-blog-url "'")
    (exec-honey-sql {:insert-into [:blog]
                     :columns [:url-name :content :post-type :image-path :author :previous-blog :next-blog]
                     :values [[url content [:cast "blog" :post_type]
                               image-path author previous-blog-url next-blog-url]]})))
(defn get-n-blog-posts
  "Get all the blog posts."
  [n]
  (exec-honey-sql {:select [:*]
                   :from [:blog]
                   :order-by [:created]
                   :limit n}))

(defn get-blog-by-url
  "Get blog post by its url."
  [url]
  (exec-honey-sql {:select [:blog]
                   :where [:= :url-name url]}))

(defn get-blog-by-title
  "Get blog post by its title."
  [name]
  (get-blog-by-url (get-url-from-title name)))