package com.sahibinden.ataercanercelik.data.cache;

public interface Cache {
    boolean isCached(final String name);
    boolean isExpired(String key);
    void evictFile(String key);
    void evictAllFile();
}
