scoreboard objectives add matchainfused_hearts dummy
scoreboard objectives add matchainfused_deaths deathCount
scoreboard objectives add matchainfused_particle_timer dummy
scoreboard players set #timer matchainfused_particle_timer 0
execute as @a run function matchainfused:initialize_player
