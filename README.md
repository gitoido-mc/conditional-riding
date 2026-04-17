<div align="center">
  <h1>Conditional Riding</h1>
</div>
<div align="center">
  <a href="https://www.curseforge.com/minecraft/mc-mods/conditional-riding-cobblemon" title="Conditional Riding on Curseforge" target="_blank"><img src="https://img.shields.io/curseforge/dt/1489855?style=for-the-badge&logo=curseforge&logoColor=%23F16436&logoSize=auto&label=&labelColor=%23222222&color=%23F16436"/></a>
  <a href="https://modrinth.com/mod/conditional-riding-cobblemon" title="Conditional Riding on Modrinth" target="_blank"><img src="https://img.shields.io/modrinth/dt/bS53FEgC?style=for-the-badge&logo=modrinth&logoColor=%2300AF5C&color=%2300AF5C&logoSize=auto&labelColor=%23222222&label="/></a>
  <a href="https://gito.lol/docs/#category-conditional-riding" title="Documentation" target="_blank"><img src="https://img.shields.io/badge/docs-v0.1.3-brightgreen?style=for-the-badge&labelColor=%23222222&color=%2300AF5C"></a> 
  <img alt="GitHub License" src="https://img.shields.io/github/license/gitoido-mc/rad-gyms?style=for-the-badge&logoSize=auto&labelColor=%23222222&color=%2300AF5C">
</div>

## Ever felt that getting to ride your pokemon is too easy?
### Maybe you wished for being able to define particular rules for riding?

This server-side mod does just that!

By default, after installation and first launch, it generates config that requires pokemon to hold a saddle:

`cobblemon_conditional_riding.json`

```json
{
  "debug": false,
  "enabled": true,
  "messagesInHotbar": false,
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

* Permission node presence for owner
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
* Blocks traveled outside ball
* Pokemon property (you can target shiny pokemon only!)


### Credits
* Icon made by wundati
