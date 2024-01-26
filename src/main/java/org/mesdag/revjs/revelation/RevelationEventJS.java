package org.mesdag.revjs.revelation;

import com.google.gson.JsonObject;
import de.dafuqs.revelationary.RevelationRegistry;
import dev.latvian.mods.kubejs.event.EventJS;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class RevelationEventJS extends EventJS {
    public void registerFromJson(JsonObject jsonObject) {
        RevelationRegistry.registerFromJson(jsonObject);
    }

    public void register(Identifier advancement, Consumer<RevBuilder> consumer) {
        RevBuilder builder = new RevBuilder(advancement);
        consumer.accept(builder);
        RevelationRegistry.registerFromJson(builder.toJson());
    }
}
