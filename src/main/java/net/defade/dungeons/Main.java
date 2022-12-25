package net.defade.dungeons;

import net.defade.bismuth.core.servers.GameType;
import net.defade.dungeons.game.GameManager;
import net.defade.dungeons.utils.DungeonsPlayer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.extensions.Extension;
import org.jetbrains.annotations.Nullable;

public class Main extends Extension {
    @Override
    public void initialize() {
        MinecraftServer.getConnectionManager().setPlayerProvider(DungeonsPlayer::new);
        new GameManager();
    }

    @Override
    public void terminate() {

    }

    @Override
    public @Nullable GameType serverGameType() {
        return new GameType("dungeons", "classic");
    }
}
