# Cellular Automata - Competing Elements


Cells compete with neighboring cells:

- Fire beats Grass.
- Grass beats Water.
- Water beats Fire.
- If neighboring cells are empty, the cell is occupied by the attacker.
- Each cell has a determined amount of HP to determine when it dies.
- Each loss, a cell loses one HP.

A small gif demonstrating the rules being applied.

![](data/fire_water_grass_small.gif?raw=true)

A large gif demonstrating the rules being applied.

![](data/fire_water_grass_large.gif?raw=true)

The below convergence graph shows how the populations starting from a random state tend towards homogeneousness. The end state is that each of the three elements represent approximately 33% of the occupied space.

![](data/fire_water_grass_convergence.png?raw=true)

A video of a larger simulation
https://v.usetapes.com/l58ybCs2bT
