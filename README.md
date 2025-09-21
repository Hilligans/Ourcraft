# Ourcraft
A minecraft inspired game engine.

https://discord.gg/BUhTVnS5ua

## How to Build & Run (for Windows)
1. Change the `lwjgl.natives` flag in pom.xml to your OS. (default is set to Linux)  
2. Add the `--enable-preview` flag to your compiler.  
3. Run `package.sh` or run the package command in Maven.  
4. Run ourcraft-X.X.X-jar-with-dependencies.jar with the JVM flag `--enable-preview` and the program arguements 
`--devBuild --integratedServer`. Note: --devBuild should only be needed when running the application through Intellij.
5. Double click localhost:<port\> to launch into the demo.

## YourKit
![](https://www.yourkit.com/images/yklogo.png)

YourKit has provided a project developers licenses for its profiler to help improve performance.

YourKit supports open source projects with innovative and intelligent tools
for monitoring and profiling Java and .NET applications.
YourKit is the creator of [YourKit Java Profiler](https://www.yourkit.com/java/profiler/),
[YourKit .NET Profiler](https://www.yourkit.com/.net/profiler/) and
[YourKit YouMonitor](https://www.yourkit.com/youmonitor), tools for profiling Java and .NET applications.
