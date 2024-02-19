package org.mesdag.revjs.register;

import de.dafuqs.revelationary.api.revelations.RevealingCallback;
import de.dafuqs.revelationary.api.revelations.RevelationAware;
import dev.latvian.mods.kubejs.block.BlockItemBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.mesdag.revjs.revelation.RevBuilder;

import java.util.Hashtable;
import java.util.Map;

public class CustomCloakedBlockItem extends BlockItem implements RevelationAware {
    private final ResourceLocation cloakAdvancement;
    private final Block cloakBlock;
    private final Map<Object, Object> objectMap;
    private Map<BlockState, BlockState> blockStateMap;

    public CustomCloakedBlockItem(Block block, Item.Properties settings, ResourceLocation cloakAdvancement, Block cloakBlock, Map<Object, Object> objectMap) {
        super(block, settings);
        this.cloakAdvancement = cloakAdvancement;
        this.cloakBlock = cloakBlock;
        this.objectMap = objectMap;
        RevelationAware.register(this);
    }

    @Override
    public ResourceLocation getCloakAdvancementIdentifier() {
        return cloakAdvancement;
    }

    @Override
    public Map<BlockState, BlockState> getBlockStateCloaks() {
        if (objectMap == null) {
            return Map.of(getBlock().defaultBlockState(), cloakBlock.defaultBlockState());
        } else if (blockStateMap == null) {
            blockStateMap = new Hashtable<>();
            objectMap.forEach((key, value) -> blockStateMap.put(RevBuilder.getState(key), RevBuilder.getState(value)));
        }
        return blockStateMap;
    }

    @Override
    public @Nullable Tuple<Item, Item> getItemCloak() {
        return new Tuple<>(this, cloakBlock.asItem());
    }

    public static class Builder extends BlockItemBuilder {
        private ResourceLocation cloakAdvancement;
        private Block cloakBlock;
        private Map<Object, Object> objectMap;

        Builder(ResourceLocation identifier) {
            super(identifier);
        }

        Builder syncData(ResourceLocation cloakAdvancement, Block cloakBlock, Map<Object, Object> objectMap) {
            this.cloakAdvancement = cloakAdvancement;
            this.cloakBlock = cloakBlock;
            this.objectMap = objectMap;
            return this;
        }

        @Override
        public Item createObject() {
            return new CustomCloakedBlockItem(blockBuilder.get(), createItemProperties(), cloakAdvancement, cloakBlock, objectMap);
        }
    }
}
