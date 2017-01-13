package com.minicubic.mssqlbridge.util;

/**
 *
 * @author xergio
 */
public class Query {
    
    public static final String LOGIN_SQL = "select id, nombre, apellido from usuarios where user_login = ? and passwd = ? and activo = 1;";
    
    /**
     * 
     * @param userId
     * @return 
     */
    
    public static final String getListSQLHojaRuta(Integer userId) {
        return "select distinct hr.id as id, hr.descripcion as descripcion, hr.fecha_impresion, z.zona from gestiones g join materiales m on ( g.id_material = m.id ) join estados_gestiones eg on ( g.id_estado = eg.id ) join gestiones_hojas_de_ruta ghr on ( g.id = ghr.id_gestion ) join hojas_de_ruta hr on ( ghr.id_hoja_ruta = hr.id ) join zonas z on ( hr.id_zona = z.id ) join usuarios u on ( hr.id_usuario_mensajero = u.id ) where hr.id_estado_hoja_de_ruta = 2 and u.id = " + userId + " and g.id_estado = 4 order by hr.fecha_impresion desc; ";
    }
    
    public static final String getListSQLGestion(Integer hojaRutaId) {
        return "select distinct g.id, m.numero_guia, m.numero_orden, m.codigo_barra, eg.descripcion from gestiones g join materiales m on ( g.id_material = m.id ) join estados_gestiones eg on ( g.id_estado = eg.id ) join gestiones_hojas_de_ruta ghr on ( g.id = ghr.id_gestion ) join hojas_de_ruta hr on ( ghr.id_hoja_ruta = hr.id ) join usuarios u on ( hr.id_usuario_mensajero = u.id ) where hr.id_estado_hoja_de_ruta = 2 and g.id_estado = 4 and hr.id = " + hojaRutaId + " order by m.codigo_barra; ";
    }
    
    public static final String getDetailSQL(Integer gestionId) {
        return "select distinct m.id, m.numero_guia, m.numero_orden, m.codigo_barra, m.codigo, m.nombre_apellido, m.telefono_contacto, m.direccion_entrega, m.detalle from gestiones g join materiales m on ( g.id_material = m.id ) join estados_gestiones eg on ( g.id_estado = eg.id ) join gestiones_hojas_de_ruta ghr on ( g.id = ghr.id_gestion ) join hojas_de_ruta hr on ( ghr.id_hoja_ruta = hr.id ) join usuarios u on ( hr.id_usuario_mensajero = u.id ) where hr.id_estado_hoja_de_ruta = 2 and g.id_estado = 4 and g.id = " + gestionId;
    }
}
