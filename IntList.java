package org.adv.clickerflex.utils.generic;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IntList {
    private final List<Integer> list = new ArrayList<>();

    public static IntList of(int... integers){
        IntList set = new IntList();
        for(int i : integers){
            set.add(i);
        }
        return set;
    }
    public static IntList ofBetween(int from, int to){
        IntList set = new IntList();
        for(int i = from;i<=to;i++){
            set.add(i);
        }
        return set;
    }
    public IntList add(int... integers){
        for(int i : integers){
            list.add(i);
        }
        return this;
    }
    public IntList addBetween(int from, int to){
        if(to>from) {
            for (int i = from; i <= to; i++) {
                list.add(i);
            }
        }else {
            for (int i = from; i >= to; i--) {
                list.add(i);
            }
        }
        return this;
    }
    public void doAll(Consumer<Integer> consumer){
        for(int x : list){
            consumer.accept(x);
        }
    }
    public List<Integer> get(){
        return list;
    }
    public List<Integer> sorted(){
        return list.stream().sorted().toList();
    }
}
