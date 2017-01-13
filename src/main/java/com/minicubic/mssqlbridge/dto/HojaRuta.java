package com.minicubic.mssqlbridge.dto;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author xergio
 */
public class HojaRuta {
    @Getter
    @Setter
    private Integer id; 
    
    @Getter
    @Setter
    private String descripcion;
    
    @Getter
    @Setter
    private String fecha_impresion;
    
    @Getter
    @Setter
    private String zona;
    
}
