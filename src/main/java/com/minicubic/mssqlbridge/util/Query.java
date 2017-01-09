package com.minicubic.mssqlbridge.util;

/**
 *
 * @author xergio
 */
public class Query {
    
    public static final String LOGIN_SQL = "select id, nombre, apellido from usuarios where user_login = ? and passwd = ? and activo = 1;";
    
    /**
     * select 	g.id as g_id, 
		m.id as m_id, 
		m.numero_guia as m_numero_guia, 
		m.numero_orden as m_numero_orden, 
		m.codigo_barra as m_codigo_barra, 
		m.codigo as m_codigo, 
		m.nombre_apellido as m_nombre_apellido, 
		m.telefono_contacto as m_telefono_contacto, 
		m.direccion_entrega as m_direccion_entrega, 
		m.detalle as m_detalle, 
		eg.descripcion as estado_gestion, 
		ghr.id as ghr_id, 
		ghr.rendido as grh_rendido, 
		ghr.fecha_entrega as ghr_fecha_entrega, 
		ghr.recibido_por as ghr_recibido_por, 
		ghr.monto as ghr_monto,
		ghr.lugar_entrega as ghr_lugar_entrega, 
		hr.id as hr_id, 
		hr.descripcion as hr_descripcion, 
		u.id as u_id, 
		u.user_login as u_user_login 
        from 	gestiones g 
        join 	materiales m on ( g.id_material = m.id ) 
        join 	estados_gestiones eg on ( g.id_estado = eg.id ) 
        join 	gestiones_hojas_de_ruta ghr on ( g.id = ghr.id_gestion ) 
        join 	hojas_de_ruta hr on ( ghr.id_hoja_ruta = hr.id ) 
        join 	usuarios u on ( hr.id_usuario_mensajero = u.id ) 
        where 	hr.id_estado_hoja_de_ruta = 1 
        and 	u.id = 123 
        and	g.id_estado != 6;
     * @param userId
     * @return 
     */
    
    public static final String getListSQL(Integer userId) {
        return "select g.id as g_id, m.id as m_id, m.numero_guia as m_numero_guia, m.numero_orden as m_numero_orden, m.codigo_barra as m_codigo_barra, m.codigo as m_codigo, m.nombre_apellido as m_nombre_apellido, m.telefono_contacto as m_telefono_contacto, m.direccion_entrega as m_direccion_entrega, m.detalle as m_detalle, eg.descripcion as estado_gestion, ghr.id as ghr_id, ghr.rendido as grh_rendido, ghr.fecha_entrega as ghr_fecha_entrega, ghr.recibido_por as ghr_recibido_por, ghr.monto as ghr_monto,ghr.lugar_entrega as ghr_lugar_entrega, hr.id as hr_id, hr.descripcion as hr_descripcion, u.id as u_id, u.user_login as u_user_login from gestiones g join materiales m on ( g.id_material = m.id ) join estados_gestiones eg on ( g.id_estado = eg.id ) join gestiones_hojas_de_ruta ghr on ( g.id = ghr.id_gestion ) join hojas_de_ruta hr on ( ghr.id_hoja_ruta = hr.id ) join usuarios u on ( hr.id_usuario_mensajero = u.id ) where hr.id_estado_hoja_de_ruta = 1 and u.id = " + userId + " and g.id_estado != 6";
    }
    
    public static final String getDetailSQL(Integer materialId) {
        return "select g.id as g_id, m.id as m_id, m.numero_guia as m_numero_guia, m.numero_orden as m_numero_orden, m.codigo_barra as m_codigo_barra, m.codigo as m_codigo, m.nombre_apellido as m_nombre_apellido, m.telefono_contacto as m_telefono_contacto, m.direccion_entrega as m_direccion_entrega, m.detalle as m_detalle, eg.descripcion as estado_gestion, ghr.id as ghr_id, ghr.rendido as grh_rendido, ghr.fecha_entrega as ghr_fecha_entrega, ghr.recibido_por as ghr_recibido_por, ghr.monto as ghr_monto,ghr.lugar_entrega as ghr_lugar_entrega, hr.id as hr_id, hr.descripcion as hr_descripcion, u.id as u_id, u.user_login as u_user_login from gestiones g join materiales m on ( g.id_material = m.id ) join estados_gestiones eg on ( g.id_estado = eg.id ) join gestiones_hojas_de_ruta ghr on ( g.id = ghr.id_gestion ) join hojas_de_ruta hr on ( ghr.id_hoja_ruta = hr.id ) join usuarios u on ( hr.id_usuario_mensajero = u.id ) where hr.id_estado_hoja_de_ruta = 1 and m.id = " + materialId + " and g.id_estado != 6";
    }
}
