# BBVassal
This is a fork of the unofficial vassal module for Blood Bowl.

See http://www.garykrockover.com/BB/ for more details!

Since v1.9.0 there have been additional features added to support NTBBL Pbem use. See http://www.plasmoids.dk/pbem/

The repository contains the expanded contents of the .vmod. The vmod is not included here.

## Build Process
Builds are done using Ant scripts as defined in build.xml

The buildall target creates the standard version, then the lowres (_LR) version


##To build a new version
- define a new configuration in intellij by copying the last release and updating version parameter
or
- run buildall target passing in version parameter
- you should get two vmods in the build directory


##To edit using the Vassal editor
- build the vmod
- use vassal editor to make changes
- run extractxml build target
- commit changes

**Important:** Always ensure that any changes to the vmod using the editor are extracted and saved as component files, else your changes will be lost next time the build script is run.
