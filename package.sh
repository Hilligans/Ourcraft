#!/bin/bash

mvn package
mkdir build -p
rm -rf build/ourcraft
cp target/ourcraft*-jar-with-dependencies.jar build/
cd build
jpackage --input ./ --main-jar ourcraft-1.0.3-jar-with-dependencies.jar --type app-image --name ourcraft