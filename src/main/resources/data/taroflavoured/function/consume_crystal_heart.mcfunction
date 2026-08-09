execute if score @s taroflavoured_hearts matches ..58 if items entity @s container.* taroflavoured:crystal_heart run scoreboard players add @s taroflavoured_hearts 2
execute if score @s taroflavoured_hearts matches 60.. run scoreboard players set @s taroflavoured_hearts 60
execute if score @s taroflavoured_hearts matches 22..60 if items entity @s container.* taroflavoured:crystal_heart run function taroflavoured:finish_crystal_heart
advancement revoke @s only taroflavoured:crystal_heart_obtained
