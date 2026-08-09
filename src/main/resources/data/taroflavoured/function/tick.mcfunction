execute as @a[tag=!taroflavoured_initialized] run function taroflavoured:initialize_player
execute as @a[scores={taroflavoured_deaths=1..,taroflavoured_hearts=22..}] run scoreboard players remove @s taroflavoured_hearts 2
execute as @a[scores={taroflavoured_deaths=1..}] run scoreboard players set @s taroflavoured_deaths 0
scoreboard players add #timer taroflavoured_particle_timer 1
execute if score #timer taroflavoured_particle_timer matches 10.. run function taroflavoured:particle_tick
execute if score #timer taroflavoured_particle_timer matches 10.. run scoreboard players set #timer taroflavoured_particle_timer 0
