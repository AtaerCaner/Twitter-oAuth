package com.sahibinden.ataercanercelik.data.cache.serializer;

import com.sahibinden.ataercanercelik.data.di.ActivityScope;
import com.domain.model.ErrorResponse;
import com.google.gson.Gson;

import javax.inject.Inject;



@ActivityScope
public class JsonSerializer {
    private final Gson gson = new Gson();

    @Inject
    public JsonSerializer() {
    }

    public ErrorResponse deserializeError(String jsonString) {
        ErrorResponse entities = gson.fromJson(jsonString, ErrorResponse.class);
        return entities;
    }


}
