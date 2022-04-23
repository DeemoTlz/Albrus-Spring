package com.deemo.service;

public interface IGameService {

    boolean insert(String game, double price);

    boolean insert(String game, double price, boolean error);

}
