execute as @a[tag=!matchainfused_initialized] run function matchainfused:initialize_player
execute as @a[scores={matchainfused_deaths=1..,matchainfused_hearts=22..}] run scoreboard players remove @s matchainfused_hearts 2
execute as @a[scores={matchainfused_deaths=1..}] run scoreboard players set @s matchainfused_deaths 0
scoreboard players add #timer matchainfused_particle_timer 1
execute if score #timer matchainfused_particle_timer matches 10.. run function matchainfused:particle_tick
execute if score #timer matchainfused_particle_timer matches 10.. run scoreboard players set #timer matchainfused_particle_timer 0
