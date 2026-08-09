scoreboard objectives add taroflavoured_hearts dummy
scoreboard objectives add taroflavoured_deaths deathCount
scoreboard objectives add taroflavoured_particle_timer dummy
scoreboard players set #timer taroflavoured_particle_timer 0
execute as @a run function taroflavoured:initialize_player
