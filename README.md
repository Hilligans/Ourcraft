A minecraft inspired game

If you intend on playing this you will probably have to build your own server jar, you can do this by changing the ClientMain in the pom.xml file to ServerMain and packaging the project
  
How to build a jar on windows:

Ensure you have apache maven installed and open command prompt in the folder with all the ourcraft contents

Type mvn package into command prompt

A file will be created in the target folder called Ourcraft-1.0-SNAPSHOT-jar-with-dependencies, this is the full game