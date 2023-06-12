package com.getAcessToken.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.exc.IgnoredPropertyException;

import java.util.List;

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
