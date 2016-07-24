(ns wikicrawl.core
  (:require [clojure.string :as s])
  (:import [org.jsoup Jsoup]
                [org.jsoup.nodes Document Element]))

(defn text [^Element x]
  (.text x))

(def url
    "https://en.wikipedia.org/wiki/clojure")

(defn fetch-url [url]
  (.get (Jsoup/connect url)))

(defn page-title [^Document doc]
  (text (.select doc "h1")))

(defn last-modified [^Document doc]
  (last (s/split (text (.select doc "#footer-info-lastmod")) #"last modified on ")))

(defn first-paragraph [^Document doc]
  (text (first (.select doc "#mw-content-text p"))))

(defn table-of-content [^Document doc]
   (map text (.select doc ".toctext")))
