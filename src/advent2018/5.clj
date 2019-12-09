(require '[clojure.string :as str])

(def in (slurp "src/advent2018/5.in"))

(def in "cAaCd")

(str/replace in #"ab|bc" "")

(def alphabet (map #(char %) (range (int \a) (+ (int \a) 26))))


(def pairs (mapcat #(vector
                      (str % (str/upper-case %))
                      (str (str/upper-case %) %)) alphabet))

(def reg (str/join "|" pairs))

(defn reduce-once [in] (str/replace in (re-pattern reg) ""))

;(dotimes [n 2000]
;  (def in (reduce-once in)))

(defn reduce-it [in]
  (loop [inn in]
    (let [reduced (reduce-once inn)]
      (if (= reduced inn)
        reduced
        (recur reduced)))))

(count (reduce-it in))

(def without-letter #(str/replace in (re-pattern (str % "|" (str/upper-case %))) ""))

(sort (map count (map reduce-it (map without-letter alphabet))))

(count in)
(def result_from_1 in)

