package net.defade.dungeons.game;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSDownloadOptions;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import net.defade.yokura.amethyst.AmethystSource;
import net.minestom.server.MinecraftServer;
import org.bson.Document;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class AmethystMapSource implements AmethystSource {
    @Override
    public InputStream getSource() {
        GridFSBucket bucket = GridFSBuckets.create(MinecraftServer.getMongoDatabase(), "maps");

        Document mapDocument = MinecraftServer.getMongoDatabase().getCollection("maps.files").aggregate(
                Arrays.asList(
                        Aggregates.match(Filters.eq("metadata.game", "dungeons")),
                        Aggregates.sample(1)
                )
        ).first();

        if(mapDocument == null) {
            throw new NullPointerException("Couldn't find a map.");
        }

        try (GridFSDownloadStream downloadStream = bucket.openDownloadStream(mapDocument.get("filename", String.class), new GridFSDownloadOptions().revision(0))) {
            return new ByteArrayInputStream(downloadStream.readAllBytes());
        } catch (IOException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public OutputStream getOutputStream(CompletableFuture<Void> writeFuture) {
        return null; // Write is disabled.
    }
}
