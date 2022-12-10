package net.defade.dungeons.game.utils;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSDownloadOptions;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import net.defade.yokura.amethyst.AmethystSource;
import net.minestom.server.MinecraftServer;
import org.bson.Document;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.concurrent.CompletableFuture;

public class AmethystMapSource implements AmethystSource {
    private static final Path TEMP_DIR = Path.of(System.getProperty("java.io.tmpdir") + File.separator + "defade");

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

        String fileName = mapDocument.getString("filename");
        Path file = Path.of(TEMP_DIR + File.separator + fileName);

        try {
            if (!Files.exists(TEMP_DIR)) {
                Files.createDirectory(TEMP_DIR);
            } else if (Files.exists(file)) {
                if (getFileMd5(file.toFile()).equals(mapDocument.getString("md5"))) {
                    return new FileInputStream(file.toFile());
                }
            }

            Files.delete(file); // In case the file has changed and the md5 doesn't match anymore
            Files.createFile(file);
            FileOutputStream fileOutputStream = new FileOutputStream(file.toFile());
            GridFSDownloadStream downloadStream = bucket.openDownloadStream(fileName, new GridFSDownloadOptions().revision(0));

            byte[] buffer = new byte[256 * 1024]; // 256ko
            int read;
            while ((read = downloadStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, read);
            }

            downloadStream.close();
            fileOutputStream.close();

            return new FileInputStream(file.toFile());
        } catch (IOException exception) {
            exception.printStackTrace(); // TODO Use logger and be more descriptive
            return null;
        }
    }

    @Override
    public OutputStream getOutputStream(CompletableFuture<Void> writeFuture) {
        return null; // Write is disabled.
    }

    private static String getFileMd5(File file) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[256 * 1024]; // 256 ko

            int read;
            while ((read = fileInputStream.read(buffer)) != -1) {
                messageDigest.update(buffer, 0, read);
            }

            fileInputStream.close();
            return HexFormat.of().formatHex(messageDigest.digest());
        } catch (NoSuchAlgorithmException | IOException exception) {
            exception.printStackTrace(); // Should never happen
        }

        return "";
    }
}
