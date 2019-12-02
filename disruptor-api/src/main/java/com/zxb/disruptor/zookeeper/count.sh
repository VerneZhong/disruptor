#!/bin/bash
count=0
while [ true ]; do
let count+=1
echo Count: $count using cat output.txt
sleep 5
done
