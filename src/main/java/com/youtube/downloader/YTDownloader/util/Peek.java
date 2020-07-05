package com.youtube.downloader.YTDownloader.util;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public interface Peek {

  static <T> UnaryOperator<T> peek(Consumer<T> c) {
    return x -> {
      c.accept(x);
      return x;
    };
  }
}
