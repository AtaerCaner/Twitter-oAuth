package com.sahibinden.ataercanercelik.data.cache;

import android.content.Context;

import com.sahibinden.ataercanercelik.data.cache.serializer.JsonSerializer;
import com.sahibinden.ataercanercelik.data.di.ActivityScope;
import com.domain.executor.ThreadExecutor;

import java.io.File;

import javax.inject.Inject;

@ActivityScope
public class CacheImpl implements Cache {

    private static final String SETTINGS_FILE_NAME = "vn.SETTINGS";
    private static final String SETTINGS_KEY_LAST_CACHE_UPDATE = "last_cache_update";
    private static final String DEFAULT_FILE_NAME = "iwealth_";
    private static final long EXPIRATION_TIME =  24 * 60 * 60 * 1000;

    private final Context context;
    private final File cacheDir;
    private final JsonSerializer serializer;
    private final FileManager fileManager;
    private final ThreadExecutor threadExecutor;

    @Inject
    public CacheImpl(Context context, JsonSerializer jsonSerializer, FileManager fileManager, ThreadExecutor executor) {
        if (context == null || jsonSerializer == null || fileManager == null || executor == null) {
            throw new IllegalArgumentException("Invalid null parameter");
        }
        this.context = context.getApplicationContext();
        this.cacheDir = this.context.getCacheDir();
        this.serializer = jsonSerializer;
        this.fileManager = fileManager;
        this.threadExecutor = executor;
    }


    @Override
    public boolean isCached(String name) {
        File userEntitiyFile = this.buildFile(name);
        return this.fileManager.exists(userEntitiyFile);
    }

    @Override
    public boolean isExpired(String key) {
        long currentTime = System.currentTimeMillis();
        long lastUpdateTime = this.getLastCacheUpdateTimeMillis(key);

        boolean expired = ((currentTime - lastUpdateTime) > EXPIRATION_TIME);

        if (expired) {
            this.evictFile(key);
        }

        return expired;
    }

    @Override
    public void evictFile(String key) {
        File fileToDelete = this.buildFile(key);
        this.executeAsynchronously(new CacheEvictor(this.fileManager, fileToDelete));
    }

    @Override
    public void evictAllFile() {
        this.executeAsynchronously(new CacheAllFileEvictor(this.fileManager, cacheDir, context));
    }

    private File buildFile(String name) {
        StringBuilder fileNameBuilder = new StringBuilder();
        fileNameBuilder.append(this.cacheDir.getPath());
        fileNameBuilder.append(File.separator);
        fileNameBuilder.append(DEFAULT_FILE_NAME);
        fileNameBuilder.append(name);

        return new File(fileNameBuilder.toString());
    }

    private void setLastCacheUpdateTimeMillis(String key) {
        long currentMillis = System.currentTimeMillis();
        this.fileManager.writeToPreferences(this.context, SETTINGS_FILE_NAME, SETTINGS_KEY_LAST_CACHE_UPDATE + "_" + key, currentMillis);
    }

    private void clearLastCacheUpdateTimeMillis(String key) {
        this.fileManager.writeToPreferences(this.context, SETTINGS_FILE_NAME, SETTINGS_KEY_LAST_CACHE_UPDATE + "_" + key, 0);
    }

    private long getLastCacheUpdateTimeMillis(String key) {
        return this.fileManager.getFromPreferences(this.context, SETTINGS_FILE_NAME, SETTINGS_KEY_LAST_CACHE_UPDATE + "_" + key);
    }

    private void executeAsynchronously(Runnable runnable) {
        this.threadExecutor.execute(runnable);
    }

    private static class CacheWriter implements Runnable {
        private final FileManager fileManager;
        private final File fileToWrite;
        private final String fileContent;

        CacheWriter(FileManager fileManager, File fileToWrite, String fileContent) {
            this.fileManager = fileManager;
            this.fileToWrite = fileToWrite;
            this.fileContent = fileContent;
        }

        @Override
        public void run() {
            this.fileManager.writeToFile(fileToWrite, fileContent);
        }
    }

    private static class CacheEvictor implements Runnable {
        private final FileManager fileManager;
        private final File fileToDelete;

        CacheEvictor(FileManager fileManager, File fileToDelete) {
            this.fileManager = fileManager;
            this.fileToDelete = fileToDelete;
        }

        @Override
        public void run() {
            this.fileManager.deleteFile(this.fileToDelete);
        }
    }

    private static class CacheAllFileEvictor implements Runnable {
        private final FileManager fileManager;
        private final File fileToDelete;
        private final Context context;

        CacheAllFileEvictor(FileManager fileManager, File fileToDelete, Context context) {
            this.fileManager = fileManager;
            this.fileToDelete = fileToDelete;
            this.context = context;
        }

        @Override
        public void run() {
            this.fileManager.deleteFolder(this.fileToDelete);
            this.fileManager.writeToPreferences(context, SETTINGS_FILE_NAME, SETTINGS_KEY_LAST_CACHE_UPDATE, 0);
        }
    }
}
