#!/usr/bin/env bash

# Make valid JSON
echo "[" > temp.txt
cat input.txt >> temp.txt
echo "]" >> temp.txt

# Convert to newline delimited JSON
cat temp.txt | jq -c '.[]' > input.txt

$(rm temp.txt)
