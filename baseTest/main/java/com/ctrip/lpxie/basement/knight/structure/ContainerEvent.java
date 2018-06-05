package com.ctrip.lpxie.basement.knight.structure;

import java.util.EventObject;

/**
 * Created by lpxie on 2016/7/21.
 */
public class ContainerEvent extends EventObject {
    private static final long serialVersionUID = 1L;

    private Object data = null;

    private String type = null;

    public ContainerEvent(Container container, String type, Object data) {

        super(container);
        this.type = type;
        this.data = data;

    }

    public Object getData() {

        return (this.data);

    }

    public Container getContainer() {

        return (Container) getSource();

    }

    public String getType() {

        return (this.type);

    }
}
