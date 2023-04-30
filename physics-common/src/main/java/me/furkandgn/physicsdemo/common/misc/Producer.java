package me.furkandgn.physicsdemo.common.misc;

/**
 * @author Furkan Doğan
 */
public interface Producer<I, R> {

  R produce(I input);
}
