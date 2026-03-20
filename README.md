### Ever felt that getting to ride your pokemon is too easy?
#### Maybe you wished for being able to define particular rules for riding?

This server-side mod does just that!

By default, after installation and first launch, it generates config that requires pokemon to hold a saddle:

`cobblemon_conditional_riding.json`

```json
{  
  "debug": false,  
  "enabled": true,  
  "rulesets": {  
    "global": {  
      "enabled": true,  
      "message": "conditional_riding.failed_ruleset.global",  
      "rules": [  
        {
          "variant": "held_item",
          "itemCondition": {  
            "items": "minecraft:saddle"  
          }
        }
      ]
    }
  }
}
```

Seems easy, right? But, what if you want more?

Don't worry, this mod got your back!

### Currently supported rules:

* LuckPerms node presence for owner
* Held item
* Cosmetic item
* Owner's item in main hand
* Owner's advancement completion
* Structure nearby
* Current area
* Current biome
* Current dimension
* Current time
* Current weather
* Current moon phase
* Current hp of pokemon in percent
* Pokemon level
* Friendship level
* Members in party besides that pokemon 
* Known moves by elemental type (pokemon must know specified move type)
* Benched move (pokemon must have specified move selected)
* Must use specified move at least once
* Stat check requirement
* Blocks traveled outside ball
* Numeric property range requirement (what if you could ride gholdengo after feeding them 200 netherite scrap?)
* Pokemon property (you can target shiny pokemon only!)
* Chance requirement (let's go gambling!)


### Credits
* Icon made by wundati
