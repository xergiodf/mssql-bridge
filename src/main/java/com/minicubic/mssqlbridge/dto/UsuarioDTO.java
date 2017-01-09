    package com.minicubic.mssqlbridge.dto;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author xergio
 */
public class UsuarioDTO {
    
    @Getter
    @Setter
    private String id;
    
    @Getter
    @Setter
    private String user;
    
    @Getter
    @Setter
    private String passwd;
    
    @Getter
    @Setter
    private String nombre;
    
    @Getter
    @Setter
    private String apellido;
    
}
