package org.mesdag.revjs.revelation;

import com.google.gson.JsonObject;
import de.dafuqs.revelationary.RevelationRegistry;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class RevelationEventJS extends EventJS {
    @Info("https://github.com/DaFuqs/Revelationary/wiki/Revelations-via-Data-Pack")
    public void registerFromJson(JsonObject jsonObject) {
        RevelationRegistry.registerFromJson(jsonObject);
    }

    @Info(params = {
            @Param(name = "advancementId"),
            @Param(name = "RevBuilderConsumer")
    })
    public void register(Identifier advancementId, Consumer<RevBuilder> consumer) {
        RevBuilder builder = new RevBuilder(advancementId);
        consumer.accept(builder);
        RevelationRegistry.registerFromJson(builder.toJson());
    }
}
