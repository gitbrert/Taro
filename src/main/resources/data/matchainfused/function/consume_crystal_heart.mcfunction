execute if score @s matchainfused_hearts matches ..58 if items entity @s container.* matchainfused:crystal_heart run scoreboard players add @s matchainfused_hearts 2
execute if score @s matchainfused_hearts matches ..60 if items entity @s container.* matchainfused:crystal_heart run effect give @s minecraft:regeneration 3 10 true
execute if score @s matchainfused_hearts matches ..60 if items entity @s container.* matchainfused:crystal_heart run clear @s matchainfused:crystal_heart 1
execute if score @s matchainfused_hearts matches 22.. run function matchainfused:set_max_health
playsound minecraft:item.totem.use player @s ~ ~ ~ 0.5 1 0
advancement revoke @s only matchainfused:crystal_heart_obtained
