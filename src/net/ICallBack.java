package net;

@FunctionalInterface
public interface ICallBack<T> {
    public void invoke(T msg);
}