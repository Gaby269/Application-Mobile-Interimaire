for ((i=202; i>=188; i-=1))
do
    ./urbcsp 35 17 240 $i 10 > "../reseaux/bench2/csp_$i.txt"
done
