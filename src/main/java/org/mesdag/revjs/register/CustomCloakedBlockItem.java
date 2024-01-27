package org.mesdag.revjs.register;

import de.dafuqs.revelationary.api.revelations.RevelationAware;
import dev.latvian.mods.kubejs.block.BlockItemBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.Nullable;
import org.mesdag.revjs.revelation.RevBuilder;

import java.util.Hashtable;
import java.util.Map;

public class CustomCloakedBlockItem extends BlockItem implements RevelationAware {
    private final Identifier cloakAdvancement;
    private final Block cloakBlock;
    private final Map<Object, Object> objectMap;
    private Map<BlockState, BlockState> blockStateMap;

    public CustomCloakedBlockItem(Block block, Item.Settings settings, Identifier cloakAdvancement, Block cloakBlock, Map<Object, Object> objectMap) {
        super(block, settings);
        this.cloakAdvancement = cloakAdvancement;
        this.cloakBlock = cloakBlock;
        this.objectMap = objectMap;
        RevelationAware.register(this);
    }

    @Override
    public Identifier getCloakAdvancementIdentifier() {
        return cloakAdvancement;
    }

    @Override
    public Map<BlockState, BlockState> getBlockStateCloaks() {
        if (objectMap == null) {
            return Map.of(getBlock().getDefaultState(), cloakBlock.getDefaultState());
        } else if (blockStateMap == null) {
            blockStateMap = new Hashtable<>();
            objectMap.forEach((key, value) -> blockStateMap.put(RevBuilder.getState(key), RevBuilder.getState(value)));
        }
        return blockStateMap;
    }

    @Override
    public @Nullable Pair<Item, Item> getItemCloak() {
        return new Pair<>(this, cloakBlock.asItem());
    }

    public static class Builder extends BlockItemBuilder {
        private Identifier cloakAdvancement;
        private Block cloakBlock;
        private Map<Object, Object> objectMap;

        public Builder(Identifier identifier) {
            super(identifier);
        }

        public Builder syncData(Identifier cloakAdvancement, Block cloakBlock, Map<Object, Object> objectMap) {
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
