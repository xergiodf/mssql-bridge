/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.minicubic.mssqlbridge.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author hectorvillalba
 */

@Data
public class Request<T> {
    
    @Getter
    @Setter
    private String type;
    
    @Getter
    @Setter
    private T data;
}
