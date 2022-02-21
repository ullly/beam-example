#!/usr/bin/env bash

# Make valid JSON
echo "[" > temp.txt
cat src/main/java/resources/input.txt >> temp.txt
echo "]" >> temp.txt

# Convert to newline delimited JSON
cat temp.txt | jq -c '.[]' > src/main/java/resources/input.txt

$(rm temp.txt)
