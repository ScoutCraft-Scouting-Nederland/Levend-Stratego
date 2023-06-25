package nl.scoutcraft.levendstratego.data.locale;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class LocaleTagType implements PersistentDataType<String, Locale> {

    @NotNull
    @Override
    public Class<String> getPrimitiveType() {
        return String.class;
    }

    @NotNull
    @Override
    public Class<Locale> getComplexType() {
        return Locale.class;
    }

    @NotNull
    @Override
    public String toPrimitive(@NotNull Locale complex, @NotNull PersistentDataAdapterContext context) {
        return complex.toLanguageTag();
    }

    @NotNull
    @Override
    public Locale fromPrimitive(@NotNull String primitive, @NotNull PersistentDataAdapterContext context) {
        return Locale.forLanguageTag(primitive);
    }
}
