# Ninja release automation
With Ninja you to build your entire solution using [Ant](http://ant.apache.org)-script. You can include multiple chunks of different types into a single distribution package:
* Pega-archive
* SQL-scripts
* AppServer management scripts  

Ninja creates a single executable archive that allows you to install your solution in one-click manner.  
## Example description
Build.xml defines content of your distribution package, component.ini maps aliases used by extensions processing different types of chunks at deployment-time to system properties. For each environment you create properties file.
