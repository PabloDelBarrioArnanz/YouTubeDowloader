package com.youtube.downloader.YTDownloader.util;


import java.io.IOException;
import java.util.function.Consumer;

public interface ThrowingConsumer<T> {

  static <T, E extends Throwable> Consumer<T> uncheckedConsumer(ThrowingConsumer<T> f) {
    return t -> {
      try {
        f.accept(t);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    };
  }

  void accept(T t) throws IOException;
}


