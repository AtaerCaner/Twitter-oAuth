package com.domain.intercators;

import com.domain.views.View;

public interface UseCase<T extends View> {
    void attachView(T view);
}
