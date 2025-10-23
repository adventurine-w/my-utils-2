package org.adv.clickerflex.ultimate_utils;

import java.util.List;
import java.util.function.Function;

public class ListRotation<T>{
    private final List<T> list;

    public ListRotation(List<T> list){
        this.list=list;
    }
    public T getPrevious(T current){
        T prev = null;
        for(int i = 0;i<list.size();i++){
            T t = list.get(i);
            if(current.equals(t)){
                if(i==0) {
                    prev = list.get(list.size() - 1);
                }else{
                    prev = list.get(i-1);
                }
                break;
            }
        }
        return prev;
    }
    public T getNext(T current){
        T next = null;
        for(int i = 0; i< this.list.size(); i++){
            T t = list.get(i);
            if(current.equals(t)){
                if(i== this.list.size()-1) {
                    next = list.get(0);
                }else{
                    next = list.get(i+1);
                }
                break;
            }
        }
        return next;
    }
    public String toString(T current){
        return toString(current, T::toString);
    }
    public String toString(T current, Function<T, String> toString){
        StringBuilder sb = new StringBuilder("<8>");
        for(int i = 0;i<list.size();i++){
            T t = list.get(i);
            String tText = toString.apply(t);
            if(t.equals(current)) {
                sb.append("<a>").append(tText).append("</a>");
            }else{
                sb.append(tText);
            }
            if(i!=list.size()-1){
                sb.append(" → ");
            }
        }
        return sb.toString();
    }
}
