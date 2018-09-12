(defproject codcheck-worker "0.0.1"
  :description "Codcheck worker for linting Clojure projects"

  :dependencies [[org.clojure/clojure "1.10.0-alpha6"]
                 [compojure "1.6.1"]
                 [org.clojure/core.async "0.4.474"]
                 [com.taoensso/timbre "4.10.0"]
                 [org.clojure/core.async "0.4.474"]
                 [ring/ring-json "0.4.0"]
                 [clj-time "0.14.4"]
                 [buddy/buddy-core "1.5.0"]
                 [buddy/buddy-auth "2.1.0"]
                 [commons-codec/commons-codec "1.11"]
                 [ring/ring-defaults "0.3.2"]
                 [org.slf4j/slf4j-simple "1.8.0-beta2"]
                 [com.novemberain/langohr "5.0.0"]
                 [irresponsible/tentacles "0.6.2"]]

  :main ^:skip-aot codcheck-worker.core

  :source-paths ["src"]

  :plugins [[lein-ring "0.12.4"]
            [lein-bikeshed "0.5.1"]
            [lein-kibit "0.1.6"]
            [lein-shell "0.5.0"]
            [jonase/eastwood "0.2.7"]]

  :aliases {"ci-check" ["do" ["kibit"] ["eastwood"] ["bikeshed"]]})
