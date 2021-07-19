(ns ^:figwheel-hooks narracion.core
  (:require [narracion.main :as main]))

(main/start!)

;; ## Figwheel Reload

(defn ^:after-load on-reload
  []
  (main/start!))
