package net.oleksin.paymentsystem;

public interface Converter <R, D, E> extends FromRequestConverter<R, E>, ToResponseConverter<D, E> {
}
