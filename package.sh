#!/bin/bash

mvn package
mkdir build -p
cp target/ourcraft*-jar-with-dependencies.jar build/
jpackage --input build/ --main-jar ourcraft-1.0.3-jar-with-dependencies.jar --type app-image --name ourcraft