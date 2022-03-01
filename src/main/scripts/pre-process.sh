#!/usr/bin/env bash

if [ $1 = "1" ]
then
  # Make valid JSON
  echo "[" > temp.txt
  cat src/main/java/resources/input.txt >> temp.txt
  echo "]" >> temp.txt

  # Convert to newline delimited JSON
  cat temp.txt | jq -c '.[]' > src/main/java/resources/input.txt

  $(rm temp.txt)
else
  # Make valid JSON
  sed -E 's/(steps |id|type|from|input|of|to|input)(:|=)/"\1":/g' src/main/java/resources/config.txt |
    sed -E 's/([a-zA-Z]*.class)/"\1"/g' | sed -E 's/("to".*),/\1/g' > temp.txt

  cat temp.txt > src/main/java/resources/config.txt

  $(rm temp.txt)
fi
