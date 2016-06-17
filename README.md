# Caveworm

Bukkkit/Spigot plugin to generate randomized vanilla like caves

This is compiled with Spigot 1.10, but should work on most older versions as well, as it is not using version bound NMS files.

Caves are created by first using a Simplex worm, which is moved by one Simplex generator for each dimension, 
to determine a path and then clearing out a sphere around the chosen points. 
The size of the cleared our sphere is determined by another independent Simplex function, which further randomizes the caves.


# Simplex algorithms

To get the best results possible out of this plugin, it's recommended to first understand [how Simplex algorithms work](https://en.wikipedia.org/wiki/Simplex_algorithm).
This is no requirement, but will help you to better understand what the different parameters specified in the config do.

**Simplified version:**

Usually random generators will return completly random results and every time you restart the programm, you will (most likely)
get a different result. It is possible though to use "seeds" in random generators, which will make all other random generators initialized
with the same seed, return the same random numbers. Utilizing this, Simplex algorithms can calculate a specific noise value for any
point in an n-dimensional space, which will be the same for all points across all instances intitialized with the same seed, but
still completly randomized.

The noise value calculated for a specific point isn't completly random and independent from other points though, instead continuos
functions are created, which are still seemingly randomly. For example the top grassblock in vanilla terrain generation isn't just at
a random y-level for every block, but instead blocks close to each other will also have a similar y-level. This is achieved by feeding
the x and z coordinate into a 2-dimensional Simplex generator, where the other parameters of the generator influence how rough the
terrain is or how many mountains it has.

You might also encounter the term "Perlin algorithm" in this context, which is simply an
older version of Simplex with similar results.


# Simplex worms

First of all, while Simplex worm is the correct term here, it's seemingly not the commonly used one. If you want to find more about 
this topic, you'll have to google "Perlin worms" instead, which are a similar application, but use Perlin as underlying algorithm
instead (which we are not doing here).

The way this worm is implemented is not the textbook way, but similar to it:

Initially a starting point for the worm is chosen. 3 independent Simplex generators are initialized with different (sometimes randomized)
seeds. Each generator determines movement in one direction based on the noise value of the current block (which is different for each
generator). 
Given the amplitude 2x, a generator will return noise values y, for which: -x <= y <= x
If the calculated noise value is positive, the current coordinate in the dimension this generator is assigned to will be increased by 
one, if it's negative it will be decreased by one.
Additionally there is a "neutral" zone around 0, which is determined by a configurable (positive) threshhold t with t < x
If -t < y < t is true for the calculated noise value y, then the assigned coordinate will be neither decreased, nor increased

This is done for all 3 dimensions and as a result an adjacent block is chosen as the next block for the worm's path from which 
the same is repeated. Sometimes the noise value for all three dimensions will be within the chosen threshhold, which means 
no coordinate changed. In that case this worm wont move from it's current position and simply end it's path right there.

# From worm path to actual cave

The path created by a worm will only be a one block wide outline and the actual cave still needs to be cleared out around it
afterwards. This is done by simply clearing out a sphere of a specific size around each of the locations chosen by the worm.
Here a Simplex noise function of a fourth independent generator are being used to calculate the radius of the sphere hollowed out,
based on the coordinate of the center. Because of the way Simplex functions work, this leads to caves which randomly decrease/increase
in radius, but only do so slowly, without any "hard" size changes visible.

The radius chosen will be within a configured lowerBound and upperBound and is randomly distributed inbetween those.
