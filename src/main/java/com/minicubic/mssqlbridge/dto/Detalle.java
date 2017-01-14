package com.minicubic.mssqlbridge.dto;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author xergio
 */
public class Detalle {
    
    @Getter
    @Setter
    private Integer id;
    
    @Getter
    @Setter
    private Integer numero_guia;
    
    @Getter
    @Setter
    private Integer numero_orden;
    
    @Getter
    @Setter
    private String codigo_barra;
    
    @Getter
    @Setter
    private String codigo;
    
    @Getter
    @Setter
    private String nombre_apellido;
    
    @Getter
    @Setter
    private String telefono_contacto;
    
    @Getter
    @Setter
    private String direccion_entrega;
    
    @Getter
    @Setter
    private String detalle;
    
    @Getter
    @Setter
    private Integer gestionId;
}
