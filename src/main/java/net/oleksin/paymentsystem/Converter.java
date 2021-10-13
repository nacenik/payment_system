package net.oleksin.paymentsystem;

public interface Converter <R, D, E> {
  D toResponseDto(E e);
  E fromRequestDto(R r);
}
