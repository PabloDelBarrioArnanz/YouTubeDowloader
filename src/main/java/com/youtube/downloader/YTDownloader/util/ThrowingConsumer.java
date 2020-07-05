package com.youtube.downloader.YTDownloader.util;


import java.util.function.Consumer;

public interface ThrowingConsumer<T, E extends Throwable> {

  static <T, E extends Throwable> Consumer<T> uncheckedConsumer(ThrowingConsumer<T, E> f) {
    return t -> {
      try {
        f.accept(t);
      } catch (Throwable e) {
        throw new RuntimeException(e);
      }
    };
  }

  void accept(T t) throws E;
}


