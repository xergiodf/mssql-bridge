/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.minicubic.mssqlbridge.dto;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author hectorvillalba
 */
public class GestionHojaRuta {
    @Getter
    @Setter
    Integer g_id;
    @Getter
    @Setter
    Integer m_id;
    @Getter
    @Setter
    Integer m_numero_guia; 
    @Getter
    @Setter
    Integer m_numero_orden;
    @Getter
    @Setter    
    String  m_codigo_barra;
    @Getter
    @Setter    
    Integer m_codigo;
    @Getter
    @Setter    
    String m_nombre_apellido;
    @Getter
    @Setter    
    String m_telefono_contacto;
    @Getter
    @Setter    
    String m_direccion_entrega;
    @Getter
    @Setter    
    String m_detalle;
    @Getter
    @Setter    
    String estado_gestion;
    @Getter
    @Setter    
    Integer ghr_id;
    @Getter
    @Setter    
    Boolean grh_rendido;
    @Getter
    @Setter    
    Date ghr_fecha_entrega;
    @Getter
    @Setter    
    String ghr_recibido_por;
    @Getter
    @Setter    
    Double ghr_monto;
    @Getter
    @Setter    
    String ghr_lugar_entrega;
    @Getter
    @Setter    
    Integer hr_id;
    @Getter
    @Setter    
    String hr_descripcion;
    @Getter
    @Setter    
    Integer u_id;
    @Getter
    @Setter    
    String u_user_login;
    
    
}
