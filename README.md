# Cellular Automata - Pokemon Type (Gen 1) Battles

This <a href="https://en.wikipedia.org/wiki/Cellular_automaton" target="blank">Cellular Automata</a> (CA) builds up to a Pokemon Type battle simulation from simpler CA.

Simulations include: 
- 1. A balanced 3 Typed CA (Fire, Water, Fire).
- 2. A larger, balanced N Typed CA.
- 3. The full Pokemon Type CA based on the strength/weakness/immunity charts of <a href="http://bulbapedia.bulbagarden.net/wiki/Type" target="blank">Gen 1 Pokemon games</a>


## #1 Balanced 3 Type Simulation (Fire, Water, Fire)

This demonstration is a CA that simulates three balanced Pokemon types battling against each other. 

Balanced meaning that Type A can beat B, and only B. Type B can beat B, and only B. So on and so forth. 

### Rules for 3 Type Simulation

- Fire beats Grass.
- Grass beats Water.
- Water beats Fire.
- If neighboring cells are empty, the cell is occupied by the attacker.
- Each cell has a determined amount of HP to determine when it dies.
- Each loss, a cell loses one HP.
- Depending on mode, both the attacker and defender can mutually attack each other, else only the attacker attacks.

<img src="/data/fire_water_grass_balanced_large.gif?raw=true" width="600"/>

The below convergence graph shows how the populations starting from a random state tend towards homogeneousness. The end state is that each of the three elements represent approximately 33% of the occupied space. Note: The large variance in the beginning is because the map was initialized "sparsely" with a lot of empty space. All other experiments are "dense" with no empty space.

<img src="/data/fire_water_grass_balanced_convergence.png?raw=true" width="600"/>

A video of a larger simulation
<a href="https://v.usetapes.com/l58ybCs2bT" target="blank"/>



## #2 Balanced N Type Simulation 

This demonstration is very similar to demonstration #1 except for more than 3 types of balanced Pokemon types will compete with each other. The types are labeled "A" to "Z". The rules follow that "B" beats "A", "C" beats "B", ..., "Z" beats "A". In this simulation each cell is densely packed with no empty cell.

### N Types Convergence

For low N, the types quickly converge to order and balance. With the exception being that 2 types, since they attack each other, tend to end with a single winner.

Another interesting trend is the "bulge" pattern that appears in the center of the convergence graphs. The population distribution starts out balanced, and the end balanced. However, during these bulges, they are very off balance. The starting states are randomly initialized and could be described as "chaotic", and the end states are well "ordered" and portray very obvious patterns. This could be thought of as the "energy" required to bring order to the chaos of the system. 

It is possible for a type to "accidentally" go extinct, which brings the system out of balance. This imbalance can be seen when N = 2, or as N grows in size (~20+).

The 26 Typed CA tended to never escape the random initialization.

The convergence charts for select N Typed simulations.


<table>
<tr>
<td>2 Type<br/><img src="/data/balanced_2_types_convergence.png?raw=true" width="300"/></td>
<td>4 Type<br/><img src="/data/balanced_4_types_convergence.png?raw=true" width="300"/></td>
</tr>
<tr>
<td>5 Type<br/><img src="/data/balanced_5_types_convergence.png?raw=true" width="300"/></td>
<td>8 Type<br/><img src="/data/balanced_8_types_convergence.png?raw=true" width="300"/></td>
</tr>
<tr>
<td>10 Type<br/><img src="/data/balanced_10_types_convergence.png?raw=true" width="300"/></td>
<td>12 Type<br/><img src="/data/balanced_12_types_convergence.png?raw=true" width="300"/></td>
</tr>
<tr>
<td>16 Type<br/><img src="/data/balanced_16_types_convergence.png?raw=true" width="300"/></td>
<td>20 Type<br/><img src="/data/balanced_20_types_convergence.png?raw=true" width="300"/></td>
</tr>
</table>

### N Types GIFs

GIFs are large so I tried to not load all of them on one page. Below are some links (they open in new tabs).

<a href="/data/balanced_4_types.gif" target="blank">4 Type CA</a>, 
<a href="/data/balanced_8_types.gif" target="blank">8 Type CA</a>, 
<a href="/data/balanced_10_types.gif" target="blank">10 Type CA</a>, 
<a href="/data/balanced_12_types.gif" target="blank">12 Type CA</a>, 
<a href="/data/balanced_20_types.gif" target="blank">20 Type CA (Partial)</a>

12 Typed CA

<img src="/data/balanced_12_types.gif?raw=true" width="300"/>

### N Types Notable Images

- For large N (~20+) the likelihood of stagnation occurs, with little or no pattern emerging. It is also more likely that one type wins, and other types go extinct.

Note the clumps of randomly placed colors. Those represent Types that have remained stagnant since the the initialization of the simulation.

<img src="/data/balanced_16_types_1.png?raw=true" width="300"/>
<img src="/data/balanced_16_types_2.png?raw=true" width="300"/>

As mentioned above, large N Typed simulations have a hard time getting started due to the lower probability of having neighbors that they can win over. The below images show the formation of a hole that opened, and spread. Sadly however, the CA died a slow death as entropy won.

<table>
<tr><td>
<img src="/data/balanced_20_types_1.png?raw=true" width="300"/>
<img src="/data/balanced_20_types_2.png?raw=true" width="300"/>
</td></tr>
<tr><td>
<img src="/data/balanced_20_types_3.png?raw=true" width="300"/>
<img src="/data/balanced_20_types_4.png?raw=true" width="300"/>
</td></tr>
<tr><td>
<img src="/data/balanced_20_types_5.png?raw=true" width="300"/>
<img src="/data/balanced_20_types_6.png?raw=true" width="300"/>
</td></tr>
<tr><td>
<img src="/data/balanced_20_types_7.png?raw=true" width="300"/>
<img src="/data/balanced_20_types_8.png?raw=true" width="300"/>
</td></tr>
</table>



## #3 Full CA Simulation using the Pokemon Gen 1 Types

This CA is a direct Type vs type simulation. It ignores all other variables, Such as:

- Each Pokemon's stats (attack, defense, hp) are exactly the same. In the game, a Dragon Pokemon has much higher stats than another type such as a Normal Pokemon.
- The distribution of each Pokemon is equal. Certain In the game, Pokemon types are more rare/common.
- A Pokemon will attack any neighboring Pokemon, regardless of the type, to include same typed Pokemon.

The full type system can be found <a href="http://bulbapedia.bulbagarden.net/wiki/Type" target="blank">here</a> on Bulbapedia.

<img src="/data/pokemon_gen1_type_chart.png?raw=true" width="500"/>

In this CA, an attacking Pokemon will attack each of it's neighbors. The damage delivered will be determined based on both the type of the attacker and the defender, per the above chart. For example Fire does 2x damage to Grass, and Normal does 0 damage to Ghost type because Ghost types are immune to physical attack. 

Each color represents a type. The color mapping can be found in the source code.

<img src="/data/pokemon_gen1_types.gif?raw=true" width="500"/>

Some full speed simmulations I recorded with Usetapes.

2 minute instense simulation: <a href="https://v.usetapes.com/i49nEZprwd" target="blank">here</a> (Normal Wins)

A long simulation in which I eventually gave up hoping for a winner. However, 40 minutes later, Normal makes an epic comeback and starts conquering from the top right corner. (I did now have world-wrap at the time of this simulation)

<a href="https://v.usetapes.com/DhbhrRAZn7" target="blank">Part 1</a>, 
<a href="https://v.usetapes.com/yDDjMxuLnI" target="blank">Part 2</a>, 
<a href="https://v.usetapes.com/Bv44dpeyxy" target="blank">Part 3</a>, 
<a href="https://v.usetapes.com/szVjcwluAS" target="blank">Part 4</a>

The epic comeback of Normal

<img src="/data/pokemon_gen1_types_normal_epic_comeback.gif?raw=true" width="300"/>

More GIFs in the below are some links (they open in new tabs).

<a href="/data/pokemon_gen1_types.gif" target="blank">Run 1</a>

<a href="/data/pokemon_gen1_types2a.gif" target="blank">Run 2a</a>, 
<a href="/data/pokemon_gen1_types2b.gif" target="blank">Run 2b</a>

<a href="/data/pokemon_gen1_types_no_immunity.gif" target="blank">Run with no immunities</a>

### Sample Convergence Rates

<table>
<tr>
<td>Run 1<br/><img src="/data/pokemon_gen1_types_run1_convergence.png?raw=true" width="300"/></td>
<td>Run 4<br/><img src="/data/pokemon_gen1_types_run4_convergence.png?raw=true" width="300"/></td>
</tr>
<tr>
<td>Run 2a<br/><img src="/data/pokemon_gen1_types_run2a_convergence.png?raw=true" width="300"/></td>
<td>Run 2b<br/><img src="/data/pokemon_gen1_types_run2b_convergence.png?raw=true" width="300"/></td>
</tr>
</table>

### Conclusion

- Ghost wins the vast majority of the fights. This is largely due to it's immunities and relatively low overlap with other types. Specifically, nothing has an advantage over Ghost, except other Ghost.
- Most simulations come down between Bug and Ghost. I did not expect Bug to be so strong.
- Sometimes Ghost and Normal press against each other (unmoving as they are immune to each other). This grid-lock typically ends when one side gets cannibalized from the inside by another type.
- Many of the end games end with Water, Lightning, Ground fighting very aggressively back and forth while Bug and Ghost fight a slower battle in and around the three.
- It seems that Bug due to number and position is almost always prime to beat Ghost. However, Bug is regularly being wiped out in mass by other types. Though Bug seems to recover in most places. I saw one simulation where Bug would destroy much of Ghost, and then Fire would instantly devour 99% of Bug Pokemon. Then Ghost would devour most of Fire Pokemon. This would then give Bug a chance to rebound, until Fire took it out again.
- Turning on and off "friendly fire" seemed to slow, but not stop Bug's progression. Though the simulations do seem to drag out more. (Friendly fire == attacking same typed Pokemon)

Thanks for reading!
