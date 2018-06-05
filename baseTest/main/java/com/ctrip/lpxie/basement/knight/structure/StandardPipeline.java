package com.ctrip.lpxie.basement.knight.structure;

import java.util.ArrayList;

/**
 * Created by lpxie on 2016/7/19.
 */
public class StandardPipeline implements Pipeline {
    protected Container container = null;
    protected Valve basic = null;
    protected Valve first = null;

    public StandardPipeline(Container container){
        this.container = container;
    }

    @Override
    public void setBasic(Valve valve) {
        this.basic = valve;
    }

    @Override
    public Valve getBasic() {
        return this.basic;
    }

    @Override
    public Valve getFirst() {
        if(first == null)
            return basic;
        return first;
    }

    @Override
    public void setContainer(Container container) {
        this.container = container;
    }

    @Override
    public Container getContainer() {
        return this.container;
    }

    @Override
    public void addValve(Valve valve) {
        if(first == null)//set first valve when it is the first valve
        {
            first = valve;
            first.setNext(basic);
        }else{
            Valve current = first;
            while (current != null){
                if(current == basic){
                    current.setNext(valve);
                }
                current = current.getNext();
            }
        }
    }

    @Override
    public Valve[] getValves() {
        ArrayList<Valve> list = new ArrayList<Valve>();

        Valve current = first;
        if(current == null)
            current = basic;
        while (current != null){
            list.add(current);
            current = current.getNext();
        }

        return list.toArray(new Valve[0]);
    }
}
