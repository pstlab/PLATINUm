#!/bin/bash

# H = [100, 200, 300]
for H in 100 200 300
do

# U = [10, 20, 30]
for U in 10 20 30
do 

# W = [1, 2, 3]
for W in 1 2 3
do

# I = [1, 2, 3]
for I in 1 2 3
do

# G = [1, 2, 3, 4, 5]
for G in 1 2 3 4 5
do


echo "start run H=$H U=$U W=$W I=$I G=$G"
java -jar dcexec-icaps20-runner.jar $H $U $W $I $G > logs/run-h$H-u$U-w$W-i$I-g$G.log


done
done
done
done
done






