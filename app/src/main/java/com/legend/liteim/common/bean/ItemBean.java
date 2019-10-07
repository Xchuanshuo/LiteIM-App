package com.legend.liteim.common.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author Legend
 * @data by on 18-7-14.
 * @description
 */
public class ItemBean implements Serializable, IFlowDataBean {

    private int id;

    public ItemBean(int id, String name) {
        this.id = id;
        this.name = name;
    }

    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Type> getTypes() {
        return types;
    }

    public void setTypes(List<Type> types) {
        this.types = types;
    }

    private List<Type> types;

    public static class Type implements Serializable, IFlowDataBean {

        private int id;

        public Type(int id, String name) {
            this.id = id;
            this.name = name;
        }

        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @Override
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}
