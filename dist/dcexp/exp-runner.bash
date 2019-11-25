#!/bin/bash

# S = [1, 2, 3]
S=3

for U in 5 10 15 20 25 30
do

for G in 1 2 3 4 5
do 

echo "start run S=$S U=$U  G=$G"
java -jar dcexec-runner.jar $S $U $G > logs/run-s$S-u$U-g$G.log

done
done


