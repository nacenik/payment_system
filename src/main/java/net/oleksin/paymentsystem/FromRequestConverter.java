package net.oleksin.paymentsystem;

public interface FromRequestConverter <R, E>{
    E fromRequestDto(R r);
}
