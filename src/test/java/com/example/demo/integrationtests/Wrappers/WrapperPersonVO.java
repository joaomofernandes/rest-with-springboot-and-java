package com.example.demo.integrationtests.Wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.util.Objects;

@XmlRootElement
public class WrapperPersonVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("_embedded")
    private PersonEmbeddedVO embeddedVO;

    public WrapperPersonVO() {
    }

    public PersonEmbeddedVO getEmbeddedVO() {
        return embeddedVO;
    }

    public void setEmbeddedVO(PersonEmbeddedVO embeddedVO) {
        this.embeddedVO = embeddedVO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WrapperPersonVO that = (WrapperPersonVO) o;
        return embeddedVO.equals(that.embeddedVO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(embeddedVO);
    }
}
