(ns wikicrawl.core
  (:import [org.jsoup Jsoup]))

(def url
    "https://en.wikipedia.org/wiki/clojure")

(defn fetch-url [url]
  (.get (Jsoup/connect url)))

(defn page-title [doc]
  (.text (.select doc "h1")))

(defn last-modified [doc]
  (.text (.select doc "#footer-info-lastmod")))


