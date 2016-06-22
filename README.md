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


# Configuration

### Before going into the individual options: 

Due to the nature of the Simplex functions it's very hard to predict results beforehand. If you want a decent config you should start off with the default one, roughly modify it for your needs and then test and improve it until the result is decent. Caveworm supports config reloading during runtime, simply run "/cwreload" to reload the config. Ingame "/cwgen" will allow you to generate a single cave starting at your current location to give you an example what your config will create. This is a great tool to quickly test many different configs and play around with the settings. If you end up finding any new relations between generated caves and config options, which are not specified here, feel free to create an issue or a PR to contribute your knowledge.

## Wormbehavior:

The first section you will encounter when looking at the default config is "wormBehavior". Those settings specify the parameters used for worms to find their paths. These settings are not related to determining start locations for caves or hollowing them out, they are simply used to determine a list of locations based on a starting location, which will make up the center of the cave later on.

The first setting is "wormType". This setting is meant to allow using different algorithms for the pathfinding, but currently none aside from "Simplexworm" are implemented. So for now simply ignore this option.

The next options are xMovementOctaveCount, yMovementOctaveCount and zMovementOctaveCount, which are an integer and all do the same, but for a different dimension. As explained above a simplex generator is used to determine movement in dimension and the setting specified here is the amount of octaves the generator will use for that dimension. During testing only 3 octaves seemed to lead to decent results, others would lead to very straight caves, which didn't seem natural, because their direction lacked any randomness.

Next up are xSpreadAmplitude, ySpreadAmplitude and zSpreadAmplitude, which are double values and again each specify a value for the simplex generator for their specific dimensions. As described above, while the movement of the worm depends on whether the noise value at the given point is positive or negative, there is an additional "neutral" zone in the middle for which the worm wont move in this dimension. Per default noise value will be between -1.0 and 1.0 with a spreadAmplitude of 2.0, which creates a certain chance for a noise value to be inside this neutral zone. By modifying the spreadAmpltiude you can indirectly decrease or increase this chance, for example if it is changed from 2.0 to 4.0, the value generated will range from -2.0 to 2.0 instead of from -1.0 to 1.0, so the chance to end up in the same "neutral area" is only half as big. Increasing the neutral zone will lead to straighter caves, while decreasing (or even setting it to 0) will possibly lead tovery long caves which will only end either when they reach their given maximum length or encounter a cycle. Easiest is to just leave all spreadAmpltiudes at 2.0 though, because the next set of value allows you to modify the size of the "neutral area" itself.

xSpreadThreshHold, ySpreadThreshHold zSpreadThreshHold are double values as well and allow you to set the size of the "neutral area" for a dimension for which the worm wont move, if the noise value generated for this dimension is inside the area. For a specified spreadThreshHold s, the neutral area will be [-s,s], meaning from -s to s and inclusive on both ends. Changing this value influences 2 things:
The first one is how soon the the cave generation will run into a dead end or cycle. Due to the nature of Simplex noise, the worm's movement is completly deterministic, which means if it ever gets back to a point it already visited previously, it would infinitely loop back towards that point. This plugin will detect those plugin and stop path finding once a cycle has been completed, which saves resources, but doesnt solve the underlying issue, which will makes caves end a lot easier than intended. Here is where the spread threshhold comes into play, because increasing it means less movement and thus a higher chance to encounter a cycle or end up in a dead end, where each dimension is inside the gray area.

The second is how random the cave path seems to be. Higher spreadThreshHolds will mostly prevent slight movements for a few blocks, because the noise value needs to be decently either in the positives or the negatives for something to happen. As a result movement in one dimension becomes linear for a lot longer distances.

0.1 is a good starting point for this value (assuming the spreadamplitude is set to 2.0), values higher than 0.1 get way too restrictive very quickly and already 0.2 will prevent most caves from generating (due to dead ends and cycles), so increase this value slowly. Decreasing it comes without issues though, if you want caves as long and random as possible, just set the threshHold to 0.0. 


Next up are xSpreadFrequency, ySpreadFrequency and zSpreadFrequency, which again are double values and will be your main tool to control path finding for a certain dimension. They are simply the frequency used for the underlying simplex generator for a dimension, but have the most important influence on the final result. 
The higher you set your frequency, the faster your noise value will change and as a result you get less and less straight cave segments, which get more and more random. High values (like 0.5) will make the noise function identical to a random generator for our use, so the path chosen just completly randomly moves around the starting location and very quickly creates cycles. 
The lower you set your frequency, the slower your noise value will change and as a result you get straighter cave segements which will less and less change direction for one dimension and overall create long straight diagional tunnels, which might as well just be worldedited out if you set this value to low, because it will lack any randomness.
As you can see the difficulty for this setting lies within finding the right balance between "straight enough to make sense" and "random enough be interesting and not predictable". The default value 0.1 tends to rather long caves than random ones, values down to 0.05 can yield interesting results for that though, for anything below that it gets difficult.

Finally the last setting for the worms behavior are xSeed, ySeed and zSeed which are the seeds used for the simplex generator for each dimension. Working with seeds will allow you to recreate all cave paths you generated at any later point again, which can be very useful. A disadvantage is that if you work with a specific seed, any collisions between cave paths will make the colliding cave follow the path of the cave it's colliding in (because the function is deterministic and the movement the same), so less interesting cave intersections will be created and overall less cave path than without seeds. Additionally if your seeds and config settings get leaked, anyone can recreate the paths of your caves which might be abusable.
Because of those reasons I wouldn't recommend working with seeds, but it's still a valid option. You may set any long as seed except for -1, which is the default value and will make the plugin use a random value every time a new cave is generated.
One important thing when using seeds is using different ones for the different dimensions, as otherwise movement will be mirrored for all dimensions and all caves can only extend diagionally in all dimensions and any noise value falling into the neutral area is automatically a dead end.
