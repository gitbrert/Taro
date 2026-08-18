execute as @e[type=item,nbt={Item:{id:"taroflavoured:divine_crystal"}}] run data merge entity @s {NoGravity:1b}
execute as @e[type=item,nbt={Item:{id:"taroflavoured:divine_crystal"}}] at @s run particle minecraft:end_rod ~ ~0.4 ~ 0 0 0 0.05 1 force
execute as @e[type=item,nbt={Item:{id:"taroflavoured:divine_crystal"}}] at @s run particle minecraft:electric_spark ~ ~0.4 ~ 0.15 0.15 0.15 0 1 force
execute as @e[type=item,nbt={Item:{id:"taroflavoured:divine_fragment"}}] at @s run particle minecraft:electric_spark ~ ~0.4 ~ 0.1 0.1 0.1 0 1 normal
execute as @e[type=item,nbt={Item:{id:"taroflavoured:crystal_heart"}}] at @s run particle minecraft:electric_spark ~ ~0.4 ~ 0.1 0.1 0.1 0 1 normal
