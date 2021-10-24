package net.oleksin.paymentsystem;

public interface ToResponseConverter <D, E> {
    D toResponseDto(E e);
}
