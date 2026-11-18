#!/usr/bin/env sh
# Compila y ejecuta el Sokoban (requiere javac y java en el PATH)
cd "$(dirname "$0")" || exit 1
rm -rf out
javac -encoding UTF-8 -d out $(find src -name "*.java") || exit 1
java -cp "out:src" sokoban.Main
