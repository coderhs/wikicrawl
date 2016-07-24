(ns wikicrawl.core
  (:require [clojure.string :as s])
  (:import [org.jsoup Jsoup]
                [org.jsoup.nodes Document Element]))

(defn text [^Element x]
  (.text x))

(def base-url
  "https://en.wikipedia.org")

(defn attr [x]
  (.attr x "href"))

(def url
    "https://en.wikipedia.org/wiki/clojure")

(defn fetch-url [page-link]
  (.get (Jsoup/connect (str base-url page-link))))

(defn title [^Document doc]
  ( text (first (.select doc "h1"))))

(defn last-modified [^Document doc]
  (let [lm-text (text (first (.select doc "#footer-info-lastmod")))]
      (last (s/split lm-text #"last modified on "))))

(defn first-paragraph-doc [^Document doc]
  (first (.select doc "#mw-content-text p"))
  )

(defn first-paragraph [^Document doc]
  (let [p (first-paragraph-doc doc)]
      (text p)))

(defn table-of-content [^Document doc]
   (map text (.select doc ".toctext")))

(defn links-to-follow [^Document doc]
  (filter #(.startsWith % "/wiki")
    (map attr (.select doc "#mw-content-text p a"))))

(defn info[url num-links]
  (let [doc (fetch-url url)]
    {
      :title (title doc)
      :last-modified (last-modified doc)
      :first-paragraph (first-paragraph doc)
      :table-of-content (table-of-content doc)
      :links-to-follow (take num-links (links-to-follow doc))
    }
  ))

(defn write [{:keys [title] :as page}]
  (let [filename (s/replace title #"\s|/" "_")]
    (spit (str "scraped/" filename ".edn") (pr-str page))))

(defn scrape [wiki-link num-links-to-follow depth]
  (let [page (info wiki-link num-links-to-follow)]
    (write page)
    (when-not (zero? depth)
      (doall
        (pmap (fn [link] (scrape link num-links-to-follow (dec depth)))
          (:links-to-follow page)
        )
      )
    )
))
