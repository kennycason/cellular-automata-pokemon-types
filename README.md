# Cellular Automata - Competing Elements


Cells compete with neighboring cells:

- Fire beats Grass.
- Grass beats Water.
- Water beats Fire.
- If neighboring cells are empty, the cell is occupied by the attacker.
- Each cell has a determined amount of HP to determine when it dies.
- Each loss, a cell loses one HP.
- Depending on mode, both the attacker and defender can mutually attack each other, else only the attacker attacks.



A large gif demonstrating the rules being applied with mutual attacks

![](data/fire_water_grass_large_mutual_attacks.gif?raw=true)

A large gif demonstrating the rules being applied with only the attacker attacking. Note how much more smooth the patterns are.

![](data/fire_water_grass_large_only_attacker_attacks.gif?raw=true)

The below convergence graph shows how the populations starting from a random state tend towards homogeneousness. The end state is that each of the three elements represent approximately 33% of the occupied space.

The below graph shows when both the attacker and defender exchange an attack.

![](data/fire_water_grass_convergence_mutual_attacks_.png?raw=true)

The below graph shows when only the attacker attacks each step.

![](data/fire_water_grass_convergence_only_attacker_attacks_.png?raw=true)

A video of a larger simulation
https://v.usetapes.com/l58ybCs2bT
