(ns codcheck-worker.handler
  (:require [langohr.basic :as langohr-basic]))

(defn on-new-pr
  [chan {:keys [headers delivery-tag]} ^bytes payload]
  (println headers)
  (Thread/sleep 4000)
  (langohr-basic/reject chan delivery-tag true))
