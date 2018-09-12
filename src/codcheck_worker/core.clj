(ns codcheck-worker.core
  (:require [clojure.core.async :as async]
            [langohr.consumers :as langohr-consumers]
            [langohr.channel :as langohr-chan]
            [codcheck-worker.handler :as handler]
            [codcheck-worker.rmq :as rmq]))

(def chan (langohr-chan/open rmq/conn))

(defn -main
  []
  (let [pr-queue (:gh-pr-code-check rmq/queues)
        options {:auto-ack false}]

    (langohr-consumers/subscribe chan pr-queue handler/on-new-pr options)))

(-main)
;; (langohr.core/close chan)
