package org.mesdag.revjs.revelation;

import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.block.predicate.BlockIDPredicate;
import dev.latvian.mods.kubejs.core.ItemKJS;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Hashtable;

public class RevBuilder {
    private final ResourceLocation advancement;
    private final Hashtable<Item, Item> items = new Hashtable<>();
    private final Hashtable<BlockState, BlockState> blocks_states = new Hashtable<>();
    private final Hashtable<Item, String> item_name_replacements = new Hashtable<>();
    private final Hashtable<Item, String> block_name_replacements = new Hashtable<>();

    RevBuilder(ResourceLocation advancement) {
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
        } else if (object instanceof CharSequence str) {
            try {
                return BlockStateParser.parseForBlock(BuiltInRegistries.BLOCK.asLookup(), str.toString(), true).blockState();
            } catch (Exception e) {
                ConsoleJS.SERVER.error("Error parsing block state: " + e);
            }
        } else if (object instanceof Block block) {
            return block.defaultBlockState();
        } else if (object instanceof BlockContainerJS containerJS) {
            return containerJS.getBlockState();
        }
        return Blocks.AIR.defaultBlockState();
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
        items.forEach((item, item2) -> itemsObject.addProperty(((ItemKJS) item).kjs$getId(), ((ItemKJS) item2).kjs$getId()));
        jsonObject.add("items", itemsObject);

        JsonObject blockStatesObject = new JsonObject();
        blocks_states.forEach((blockState, blockState2) -> blockStatesObject.addProperty(BlockStateParser.serialize(blockState), BlockStateParser.serialize(blockState2)));
        jsonObject.add("block_states", blockStatesObject);

        JsonObject itemNameObject = new JsonObject();
        item_name_replacements.forEach((item, s) -> itemNameObject.addProperty(((ItemKJS) item).kjs$getId(), s));
        jsonObject.add("item_name_replacements", itemNameObject);

        JsonObject blockNameObject = new JsonObject();
        block_name_replacements.forEach((item, s) -> blockNameObject.addProperty(((ItemKJS) item).kjs$getId(), s));
        jsonObject.add("block_name_replacements", blockNameObject);

        return jsonObject;
    }
}
