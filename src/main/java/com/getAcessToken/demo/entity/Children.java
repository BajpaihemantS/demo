package com.getAcessToken.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Children {
    private ChildrenData data;

    public ChildrenData getData() {
        return data;
    }

    public void setData(ChildrenData data) {
        this.data = data;
    }
}
