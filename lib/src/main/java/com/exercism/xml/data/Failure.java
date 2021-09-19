package com.exercism.xml.data;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import com.google.common.base.MoreObjects;

public class Failure {
    @JacksonXmlProperty(isAttribute = true)
    public String message;
    @JacksonXmlProperty(isAttribute = true)
    public String type;
    @JacksonXmlText
    public String value;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("message", message)
            .add("type", type)
            .add("value", value)
            .toString(); 
    }
}
