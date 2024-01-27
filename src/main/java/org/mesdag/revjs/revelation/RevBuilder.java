package org.mesdag.revjs.revelation;

import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.block.predicate.BlockIDPredicate;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.command.argument.BlockArgumentParser;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.Hashtable;

public class RevBuilder {
    private final Identifier advancement;
    private final Hashtable<Item, Item> items = new Hashtable<>();
    private final Hashtable<BlockState, BlockState> blocks_states = new Hashtable<>();
    private final Hashtable<Item, String> item_name_replacements = new Hashtable<>();
    private final Hashtable<Item, String> block_name_replacements = new Hashtable<>();

    RevBuilder(Identifier advancement) {
        this.advancement = advancement;
    }

    @Info(params = {
            @Param(name = "sourceItem"),
            @Param(name = "targetItem")
    })
    public RevBuilder cloakItem(Item sourceItem, Item targetItem) {
        items.put(sourceItem, targetItem);
        return this;
    }

    @Info(value = """
            Accepts

                BlockState,

                BlockIDPredicate: Block.id()

                Stringify blockState: 'blockId[state=value,]'

                Block

                BlockContainerJS
            """,
            params = {
                    @Param(name = "sourceBlock"),
                    @Param(name = "targetBlock")
            })
    public RevBuilder cloakBlockState(Object sourceBlock, Object targetBlock) {
        blocks_states.put(getState(sourceBlock), getState(targetBlock));
        return this;
    }

    public static BlockState getState(Object object) {
        if (object instanceof BlockState state) {
            return state;
        } else if (object instanceof BlockIDPredicate predicate) {
            return predicate.getBlockState();
        } else if (object instanceof String str) {
            try {
                return BlockArgumentParser.block(Registries.BLOCK.getReadOnlyWrapper(), str, true).blockState();
            } catch (Exception e) {
                ConsoleJS.SERVER.error("Error parsing block state: " + e);
            }
        } else if (object instanceof Block block) {
            return block.getDefaultState();
        } else if (object instanceof BlockContainerJS containerJS) {
            return containerJS.getBlockState();
        }
        return Blocks.AIR.getDefaultState();
    }

    @Info(params = {
            @Param(name = "sourceItem"),
            @Param(name = "targetText")
    })
    public RevBuilder replaceItemName(Item sourceItem, String targetText) {
        item_name_replacements.put(sourceItem, targetText);
        return this;
    }

    @Info(params = {
            @Param(name = "sourceBlock"),
            @Param(name = "targetText")
    })
    public RevBuilder replaceBlockName(Block sourceBlock, String targetText) {
        block_name_replacements.put(sourceBlock.asItem(), targetText);
        return this;
    }

    JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("advancement", advancement.toString());

        JsonObject itemsObject = new JsonObject();
        items.forEach((item, item2) -> itemsObject.addProperty(item.kjs$getId(), item2.kjs$getId()));
        jsonObject.add("items", itemsObject);

        JsonObject blockStatesObject = new JsonObject();
        blocks_states.forEach((blockState, blockState2) -> blockStatesObject.addProperty(BlockArgumentParser.stringifyBlockState(blockState), BlockArgumentParser.stringifyBlockState(blockState2)));
        jsonObject.add("block_states", blockStatesObject);

        JsonObject itemNameObject = new JsonObject();
        item_name_replacements.forEach((item, s) -> itemNameObject.addProperty(item.kjs$getId(), s));
        jsonObject.add("item_name_replacements", itemNameObject);

        JsonObject blockNameObject = new JsonObject();
        block_name_replacements.forEach((item, s) -> blockNameObject.addProperty(item.kjs$getId(), s));
        jsonObject.add("block_name_replacements", blockNameObject);

        return jsonObject;
    }
}
