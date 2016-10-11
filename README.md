# Enterprise Engine

The Enterprise Engine is a "game engine" created in Java and LWJGL3 that is developed in Intellij IDEA that uses the shader pipeline. This engine is nowhere near being complete, and is 
extremely primitive at this stage of development.

Lighting
--------
Currently the Enterprise Engine uses the cook-torrance lighting model for specular lighting, and the lambertian lighting model for the diffuse lighting. It was the original plan to implement PBR,
but the frame buffer object system needs more work. 

Post Processing
---------------
The post processing system just displays a textured (fbo) quad directly in front of the camera, allowing a variety of effects to be cast over the scene, such as bloom, underwater effects, and
many more. This system has a hard time handling more than one default filter on at a time.

Model Importing / Animation
---------------------------
All models are imported using a java port of Assimp. This means that all of the model formats that Assimp supports, this engine supports as well. Animation is being developed, but is not
currently working.

Input
-----
The input system currently uses the LWJGL3 input callback system, so it should be fine for cross-platform.

Collision
---------
Collision detection hasn't been implemented yet simply because my node and component classes aren't fully developed. When there is a better-managed node hierarchy, it will become more pragmatic
for me to implement this. Also, AABB's are already implemented, as are their respective collision functions.

GUI
---
The ui is very primitive at this stage, and only consists of drawing 2D textures to the screen. There is a button system being devleoped, but it is buggy and not fully realized yet.

Shading
-------
The shading system stems from the class `RawShader`, which allows any instance or implementation to set uniforms, and stop and start at will. It is recommended that one extends the class if the 
shader is using custom attributes.

Bugs
----
- The new abstract method of creating FBO's doesn't integrate with the water system; the water's reflection FBO is not functioning
- There are sudden drops in FPS when entering shadow-heavy areas
- The post processing system cannot handle two filters on at the same time

Credits
-------
I built off of the OpenGL from the post processing and particle code from ThinMatrix's videos.

