# RevelationaryJS
Use Revelationary API by KubeJS

## Quick Example
[Revelationary Wiki](https://github.com/DaFuqs/Revelationary/wiki/)

### Server script
```js
ServerEvents.revelation(event => {
    event.registerFromJson({
        "block_states": {
            "minecraft:grass": "minecraft:beacon",
            "minecraft:tall_grass": "minecraft:obsidian",
            "minecraft:tall_grass[half=upper]": "minecraft:netherite_block"
        },
        "items": {
            "minecraft:nether_star": "minecraft:gunpowder"
        },
        "advancement": "minecraft:nether/root"
    })

    event.register("minecraft:adventure/root", revBuilder => {
        revBuilder
            .cloakBlockState("minecraft:grass", "minecraft:beacon")
            .cloakItem("minecraft:nether_star", "minecraft:gunpowder")
            .cloakItem("minecraft:diamond_ore", "minecraft:stone")
            .replaceItemName("minecraft:nether_star", "block.minecraft.poppy")
            .replaceItemName("minecraft:diamond_ore", "Probably Stone?")
            .replaceItemName("minecraft:grass", "block.minecraft.dandelion")
            .replaceItemName("minecraft:gold_ore", "Since Gold Ore is not in the items tag above, you should never see this")
            .replaceBlockName("minecraft:grass", "block.minecraft.dandelion")
    })
    
    let count = 0;
    event.onReveal((doneAdvancements, revealedBlocks, revealedItems, isJoinPacket) => {
        console.log("Hey " + (++count))
    })
})
```

### Registering a revealable block or item
```js
// Register a cloaked block
StartupEvents.registry("block", event => {
    event
        .create("revjs:example_block", "cloaked")
        .cloakAdvancement("story/smelt_iron")
        .cloakedBlockTranslation("I'm stone stairs!")
        // Or just 'cloakBlock("stone_stiars")'
        .blockStateCloak(Block.id("revjs:example_block"), Block.id("stone_stairs", { "facing": "west" }))
        .onCloak((cloakedBlock, cloakAdvancement, cloakBlock) => { })
        .onUnCloak((cloakedBlock, cloakAdvancement, cloakBlock) => { })
})

// Register a cloaked item
StartupEvents.registry("item", event => {
    event
        .create("revjs:example_item", "cloaked")
        .cloakAdvancement("story/smelt_iron")
        .cloakedItemTranslation("I'm diamond!")
        .cloakItem("diamond")
        .onCloak((cloakedItem, cloakAdvancement, cloakItem) => { })
        .onUnCloak((cloakedItem, cloakAdvancement, cloakItem) => { })
})
```