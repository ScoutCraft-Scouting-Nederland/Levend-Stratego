package nl.scoutcraft.levendstratego.data.strategodata;

import nl.scoutcraft.levendstratego.data.Keys;
import nl.scoutcraft.levendstratego.game.Rank;
import nl.scoutcraft.levendstratego.game.team.Team;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class StrategoDataTagType implements PersistentDataType<PersistentDataContainer, StrategoData> {

    @Override
    public Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    @Override
    public Class<StrategoData> getComplexType() {
        return StrategoData.class;
    }

    @Override
    public PersistentDataContainer toPrimitive(StrategoData data, PersistentDataAdapterContext context) {
        PersistentDataContainer container = context.newPersistentDataContainer();

        container.set(Keys.TEAM, PersistentDataType.INTEGER, data.getTeam().getTeamNumber());
        if (data.getRank() != null)
            container.set(Keys.RANK, PersistentDataType.INTEGER, data.getRank().getBasePower());
        if (data.hasRedFlag())
            container.set(Keys.RED_FLAG, PersistentDataType.INTEGER, 1);
        if (data.hasBlueFlag())
            container.set(Keys.BLUE_FLAG, PersistentDataType.INTEGER, 1);

        return container;
    }

    @Override
    public StrategoData fromPrimitive(PersistentDataContainer container, PersistentDataAdapterContext context) {
        return new StrategoData(
                Team.of(container.get(Keys.TEAM, PersistentDataType.INTEGER)),
                Rank.of(container.get(Keys.RANK, PersistentDataType.INTEGER)),
                container.has(Keys.RED_FLAG, PersistentDataType.INTEGER),
                container.has(Keys.BLUE_FLAG, PersistentDataType.INTEGER)
        );
    }
}
