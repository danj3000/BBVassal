# BBVassal
This is the unofficial vassal module for Blood Bowl.

See http://www.garykrockover.com/BB/ for more details!

The repository contains the expanded contents of the .vmod. The vmod is not included here.

## Process
To use the batch files:
- you will need 7zip installed
- the batch file assumes that the path to the 7Zip executable is in your %PATH% environment variable

To build the vmod
- run the buildvmod.bat file

To edit using the Vassal editor
- build the vmod
- use vassal editor to make changes
- run extractbuildFile.bat
- commit changes

**Important:** Always ensure that any changes to the vmod using the editor are extracted and saved as component files, else your changes will be lost next time the build script is run.
