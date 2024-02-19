package org.mesdag.revjs.revelation;

import com.google.gson.JsonObject;
import de.dafuqs.revelationary.RevelationRegistry;
import de.dafuqs.revelationary.api.revelations.RevealingCallback;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.util.Set;
import java.util.function.Consumer;

public class RevelationEventJS extends EventJS implements RevealingCallback {
    public static final RevelationEventJS INSTANCE = new RevelationEventJS();
    private OnRevealCallback onRevealCallback;

    public RevelationEventJS() {
        RevealingCallback.register(this);
    }

    @Info("https://github.com/DaFuqs/Revelationary/wiki/Revelations-via-Data-Pack")
    public void registerFromJson(JsonObject jsonObject) {
        RevelationRegistry.registerFromJson(jsonObject);
    }

    @Info(params = {
            @Param(name = "advancementId"),
            @Param(name = "consumer")
    })
    public void register(Identifier advancementId, Consumer<RevBuilder> consumer) {
        RevBuilder builder = new RevBuilder(advancementId);
        consumer.accept(builder);
        RevelationRegistry.registerFromJson(builder.toJson());
    }

    @Info("Receive notifications when revelations happen.")
    public void onReveal(OnRevealCallback callback) {
        this.onRevealCallback = callback;
    }

    @HideFromJS
    @Override
    public void trigger(Set<Identifier> doneAdvancements, Set<Block> revealedBlocks, Set<Item> revealedItems, boolean isJoinPacket) {
        if (onRevealCallback != null) {
            onRevealCallback.revealing(doneAdvancements, revealedBlocks, revealedItems, isJoinPacket);
        }
    }

    @FunctionalInterface
    public interface OnRevealCallback {
        void revealing(Set<Identifier> doneAdvancements, Set<Block> revealedBlocks, Set<Item> revealedItems, boolean isJoinPacket);
    }
}
