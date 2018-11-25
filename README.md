# 3D Graphics Project
This project is for A-Level Computer Science, completed in 2017.

# What is it?
The project is a low-poly, basic, tank simulation sort of a game.

# Running the program
## Eclipse
1. Create a new workspace or naviagate to an existing workspace
2. Clone the project into the workspace folder
3. In Eclipse, right click in the `Package Explorer` window and select `New` -> `Java Project`
4. For `Project Name` enter the name of the folder you cloned the project into exactly, it should say use existing sources to build project. Click finish
5. Right click on the project folder in Eclipse and click `Build Path` -> `Configure Build Path...`
6. Under the `Libraries` tab click `Add External JARs...`
7. Navigate to `project_folder/lib/lwjgl/jar` and select `lwjgl.jar`
8. Expand the new `lwjgl.jar` and add the following:
   1. Source attachment - `project_folder/lib/lwjgl/src.zip`
   2. Javadoc location - `project_folder/lib/lwjgl/doc/javadoc.zip`
   3. Native library location - `project_folder/lib/lwjgl/native/`
9. Run the Main.java file

# Libraries & Version Information
Java Version: `1.8.0`

IDE: `Eclipse Juno`

LWJGL: `3.0.0 SNAPSHOT build 90`
