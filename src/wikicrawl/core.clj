(ns wikicrawl.core
  (:require [clojure.string :as s])
  (:import [org.jsoup Jsoup]))

(def url
    "https://en.wikipedia.org/wiki/clojure")

(defn fetch-url [url]
  (.get (Jsoup/connect url)))

(defn page-title [doc]
  (.text (.select doc "h1")))

(defn last-modified [doc]
  (last (s/split (.text (.select doc "#footer-info-lastmod")) #"last modified on ")))

(defn first-paragraph [doc]
  (.text (first (.select doc "p"))))

(defn table-of-content [doc]
  (.text (.select doc "#toc")))
