execute if score @s matchainfused_hearts matches ..58 if items entity @s container.* matchainfused:crystal_heart run scoreboard players add @s matchainfused_hearts 2
execute if score @s matchainfused_hearts matches 22..60 if items entity @s container.* matchainfused:crystal_heart run function matchainfused:finish_crystal_heart
advancement revoke @s only matchainfused:crystal_heart_obtained
