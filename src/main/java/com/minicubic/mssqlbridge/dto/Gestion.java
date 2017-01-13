package com.minicubic.mssqlbridge.dto;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author xergio
 */
public class Gestion {
    
    @Getter
    @Setter
    private Integer id;
    
    @Getter
    @Setter
    private Integer numero_guia;
    
    @Getter
    @Setter
    private String codigo_barra;
    
    @Getter
    @Setter
    private String descripcion;
}
