(ns valg11.core
  (:require [clojure.string :as str])
  (:use [net.cgrand.enlive-html]
        [clojure.java.io :only (reader)]
        [valg11.tools])
  (:import java.net.URL java.io.StringReader))

(defn file->map
  "Reads a html file and returns a html-resource"
  [file]
  (with-open [r (reader file)]
    (html-resource r)))

(defn str->map
  "Reads a html as string and returns a html-resource"
  [string]
  (-> string java.io.StringReader. html-resource))

(def kommune-filer
  ["0118" "0124" "0125" "0106" "0101" "0138" "0111" "0119" "0104" "0128"
   "0136" "0121" "0135" "0105" "0127" "0123" "0122" "0137" "0220" "0221"
   "0219" "0237" "0229" "0227" "0215" "0234" "0239" "0230" "0238" "0236"
   "0216" "0233" "0217" "0228" "0231" "0213" "0226" "0235" "0211" "0214"
   "0301" "0438" "0420" "0427" "0434" "0439" "0423" "0403" "0402" "0415"
   "0418" "0441" "0432" "0412" "0417" "0430" "0419" "0436" "0428" "0437"
   "0426" "0429" "0425" "0511" "0541" "0522" "0502" "0534" "0532" "0512"
   "0501" "0514" "0533" "0542" "0516" "0538" "0520" "0517" "0513" "0536"
   "0540" "0519" "0545" "0543" "0529" "0515" "0528" "0521" "0544" "0602"
   "0631" "0615" "0617" "0618" "0620" "0612" "0628" "0604" "0622" "0626"
   "0623" "0625" "0616" "0633" "0605" "0632" "0627" "0621" "0624" "0619"
   "0719" "0714" "0702" "0701" "0728" "0709" "0722" "0716" "0713" "0706"
   "0720" "0711" "0723" "0704" "0814" "0821" "0817" "0831" "0827" "0815"
   "0829" "0830" "0819" "0807" "0805" "0822" "0828" "0811" "0806" "0826"
   "0833" "0834" "0906" "0928" "0938" "0941" "0937" "0919" "0911" "0904"
   "0935" "0926" "0901" "0914" "0940" "0912" "0929" "1027" "1003" "1004"
   "1034" "1001" "1037" "1029" "1032" "1002" "1021" "1046" "1017" "1018"
   "1014" "1026" "1114" "1145" "1101" "1141" "1129" "1122" "1106" "1133"
   "1119" "1149" "1120" "1144" "1112" "1127" "1142" "1102" "1135" "1111"
   "1124" "1103" "1130" "1134" "1121" "1146" "1151" "1160" "1247" "1244"
   "1264" "1201" "1219" "1232" "1211" "1265" "1222" "1246" "1241" "1234"
   "1227" "1238" "1224" "1263" "1266" "1256" "1252" "1228" "1243" "1253"
   "1260" "1242" "1221" "1245" "1216" "1223" "1231" "1233" "1251" "1235"
   "1259" "1428" "1421" "1418" "1438" "1443" "1429" "1401" "1432" "1430"
   "1445" "1411" "1444" "1413" "1416" "1431" "1419" "1426" "1422" "1433"
   "1441" "1420" "1412" "1449" "1417" "1439" "1424" "1547" "1576" "1554"
   "1551" "1548" "1532" "1557" "1571" "1534" "1517" "1515" "1505" "1545"
   "1502" "1543" "1524" "1539" "1567" "1514" "1546" "1529" "1573" "1526"
   "1525" "1531" "1563" "1566" "1528" "1560" "1516" "1511" "1535" "1519"
   "1523" "1520" "1504" "1622" "1627" "1620" "1612" "1617" "1644" "1662"
   "1663" "1636" "1653" "1648" "1634" "1638" "1633" "1635" "1624" "1632"
   "1640" "1664" "1657" "1613" "1601" "1665" "1621" "1630" "1749" "1748"
   "1717" "1742" "1743" "1756" "1755" "1718" "1719" "1738" "1711" "1723"
   "1725" "1703" "1740" "1751" "1744" "1739" "1736" "1702" "1714" "1721"
   "1724" "1750" "1820" "1871" "1854" "1839" "1811" "1804" "1813" "1867"
   "1827" "1853" "1841" "1859" "1838" "1825" "1866" "1849" "1826" "1832"
   "1818" "1822" "1834" "1851" "1837" "1874" "1805" "1828" "1833" "1836"
   "1856" "1840" "1870" "1848" "1812" "1845" "1852" "1835" "1850" "1824"
   "1815" "1860" "1816" "1857" "1865" "1868" "1933" "1922" "1929" "1915"
   "1926" "1919" "1901" "1917" "1936" "1911" "1943" "1940" "1920" "1931"
   "1938" "1924" "1942" "1923" "1941" "1913" "1939" "1925" "1928" "1927"
   "1902" "2012" "2024" "2028" "2023" "2004" "2015" "2021" "2011" "2017"
   "2022" "2014" "2018" "2027" "2019" "2020" "2030" "2025" "2003" "2002"])

(def base-url "http://nrk.no")
(def site (html-resource (URL. "http://nrk.no/valg2011/valgresultat/kommune/")))

(def kommuner (select site [:div.municipality-list :ul :li :ul :li :a]))

(defn extract [node]
  (let [{title :title href :href} (:attrs node)]
    {:kommune-nr (-> (str/split href #"[/]") (last)) :kommune title :href href}))

(def parsed-kommuner (map extract kommuner))
(def make-url #(str base-url (:href %)))

(defn store-content [node]
  (let [url (make-url node)
        data (slurp url)]
    (spit (str "data/" (:kommune-nr node)) data)))

(defn fix-number [s]
  (->> (map (fn [x] x) s)
       (filter #(num? (str %)))
       (apply str)
       (parse-number)))

(defn parse-kommunevalgresultat [nodes]
  (letfn [(parse-fn [node] (let [navn (-> (select node [:td :a]) (first) (:content) (first))
               prosent (-> (select node [:td]) (second) (select [:span :strong]) (first) (:content) (first))
               prosent-endring (-> (select node [:td]) (second) (select [:span :span]) (first) (:content) (first))
               stemmer (-> (select node [:td]) (nth 2) (:content) (first))
               [rep rep-endring] (-> (select node [:td]) (nth 3) (:content) (first) (str/split #"[(]"))]
           {:navn navn
            :prosent prosent
            :prosent-endring (->> prosent-endring (drop 1) (drop-last 1) (apply str))
            :stemmer (fix-number stemmer)
            :representanter (fix-number rep)
            :representanter-endring (str (first rep-endring) (fix-number rep-endring))}))]
    (map parse-fn nodes)))

(defn parse-file [nr]
  (let [node (file->map (str "data/" nr))
        title (-> (select node [:h1]) (first) (:content) (first))
        fylke (-> (select node [:div.group-header :a]) (first) (:content) (first))
        oppdatert (-> (select node [:div.updated :strong]) (first) (:content) (first))
        [frammote stemmer forhandsstemmer] (->> (select node [:div.group-status :ul :strong]) (mapcat :content))
        kommunevalgresultat (-> (select node [:div#kommunevalg :table :tbody :tr]))]
    {:title title
     :fylke fylke
     :oppdatert oppdatert
     :frammote (->> frammote (reverse) (drop 2) (reverse) (apply str) (parse-number))
     :stemmer (fix-number stemmer)
     :forhandsstemmer (fix-number forhandsstemmer)
     :kommunevalgresultat (parse-kommunevalgresultat kommunevalgresultat)}))

;; node = 0118
