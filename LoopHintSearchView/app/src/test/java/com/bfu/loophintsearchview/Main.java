package com.bfu.loophintsearchview;

import androidx.annotation.NonNull;

import kotlin.Function;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;

public class Main {

    public static void main(String[] args) {
        final Function1<Continuation<? super String>, Object> a = ThreadVsCoroutine.HttpClient.a;
        final Function0<String> a2 = ThreadVsCoroutine.HttpClient.a2;
        ThreadVsCoroutine.HttpClient.request2(1, new Continuation<Unit>() {
            @NonNull
            @Override
            public CoroutineContext getContext() {
                return null;
            }

            @Override
            public void resumeWith(@NonNull Object o) {

            }
        });
    }
}
