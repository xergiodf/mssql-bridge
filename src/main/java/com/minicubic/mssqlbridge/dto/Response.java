/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.minicubic.mssqlbridge.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author hectorvillalba
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Response<T> {
    @Getter
    @Setter
    private Integer codigo;

    @Getter
    @Setter
    private String mensaje;

    @Getter
    @Setter
    private T data;

}
