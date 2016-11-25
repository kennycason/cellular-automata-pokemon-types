# Cellular Automata - Pokemon Type Battles


Pokemon compete with neighboring Pokemon. This project has a couple experiments:

## #1 Balanced Type Simulation (3 Types)

This demonstration is a Cellular Automata that simulates three balanced Pokemon types battling against each other. 

Balanced meaning that Type A can beat B, and only B. Type B can beat B, and only B. So on and so forth. 

### Rules for 3 Type Simulation

- Fire beats Grass.
- Grass beats Water.
- Water beats Fire.
- If neighboring cells are empty, the cell is occupied by the attacker.
- Each cell has a determined amount of HP to determine when it dies.
- Each loss, a cell loses one HP.
- Depending on mode, both the attacker and defender can mutually attack each other, else only the attacker attacks.

<img src="/data/fire_water_grass_balanced_large.gif?raw=true" width="500"/>

The below convergence graph shows how the populations starting from a random state tend towards homogeneousness. The end state is that each of the three elements represent approximately 33% of the occupied space. Note: The large variance in the beginning is because the map was initialized "sparsely" with a lot of empty space. All other experiments are "dense" with no empty space.

<img src="/data/fire_water_grass_balanced_convergence.png?raw=true" width="500"/>

A video of a larger simulation
https://v.usetapes.com/l58ybCs2bT


## #2 Balanced Type Simulation (N Types)

This demonstration is very similar to demonstration #1 except for more than 3 types of Balanced Pokemon types will compete

