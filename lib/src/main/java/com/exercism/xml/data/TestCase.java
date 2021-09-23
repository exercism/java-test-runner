package com.exercism.xml.data;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.google.common.base.MoreObjects;

public class TestCase {
    @JacksonXmlProperty(isAttribute = true)
    public String name;
    @JacksonXmlProperty(isAttribute = true)
    public String classname;
    @JacksonXmlProperty(isAttribute = true)
    public double time;
    public Failure failure;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("name", name)
            .add("classname", classname)
            .add("time", time)
            .add("failure", failure)
            .toString(); 
    }
}
