# GameSkeleton

## Structure

 * **[jog](/src/jog):**
 This contains the core classes. These handle window creation, graphics rendering, input handling, audio playing, and network communications.
 * **[lib](/src/lib):** 
 This contains useful libraries, which add gamepad support, rudimentary GUI elements, cameras, and helper classes like the animation class.
 * **[run](/src/run):** 
 This contains the indended superclass for any Main class.
 * **[scn](/src/scn):** 
 This contains an abstract Scene class for games to override, and a SceneManager class which handles which scene is currently being updated and drawn.