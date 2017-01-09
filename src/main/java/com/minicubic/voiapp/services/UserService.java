package com.minicubic.voiapp.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.minicubic.mssqlbridge.dto.GestionHojaRuta;
import com.minicubic.mssqlbridge.dto.Response;
import com.minicubic.mssqlbridge.dto.UsuarioDTO;
import com.minicubic.mssqlbridge.util.DBUtil;
import com.minicubic.mssqlbridge.util.Query;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author xergio
 */
public class UserService {
    private final DBUtil dbUtil;
    Gson gson = new GsonBuilder().create();
    Logger log = Logger.getLogger("UserService");
    
    public UserService() {
        dbUtil = new DBUtil();
    }

    public Response<UsuarioDTO> doLogin(String user, String passwd) {
        Response<UsuarioDTO> response = new Response<>();
        try {
            log.info("User: " + user);
            log.info("Password: " + passwd);
            String json = dbUtil.resultSetToJson(Query.LOGIN_SQL, new Object[]{user, passwd});
            log.info("JSON: " + json);
            List<UsuarioDTO> usuario = gson.fromJson(json, new TypeToken<List<UsuarioDTO>>(){}.getType());
            
            response.setCodigo(200);
            response.setData(usuario.get(0));
            response.setMensaje("Success");
            return response;
        } catch (Exception ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
            response.setCodigo(600);
            response.setMensaje(ex.getMessage());
            return response;
        }
    }
    
    public Response<List<GestionHojaRuta>> getList(Integer userId) {
        Response<List<GestionHojaRuta>> response= new Response<>();
        try {
            String json =  dbUtil.resultSetToJson(Query.getListSQL(userId), null);
                    //"[{\"g_id\":987072,\"m_id\":1976383,\"m_numero_guia\":14532,\"m_numero_orden\":153,\"m_codigo_barra\":\"0014532000153\",\"m_codigo\":\"14532\",\"m_nombre_apellido\":\"Antena 75  PS75G + kit de instalacion - 02-06-15\",\"m_telefono_contacto\":\"02/06/2015\",\"m_direccion_entrega\":\"KIT DE TUERCAS\",\"m_detalle\":\"CLARO - TV\",\"estado_gestion\":\"EN RUTA\",\"ghr_id\":997213,\"grh_rendido\":false,\"ghr_monto\":0,\"hr_id\":43184,\"hr_descripcion\":\"DTH\",\"u_id\":123,\"u_user_login\":\"jrolon\"}]";
            List<GestionHojaRuta> lista = gson.fromJson(json, new TypeToken<List<GestionHojaRuta>>(){}.getType());
            response.setCodigo(200);
            response.setData(lista);
            response.setMensaje("Success");
            log.info("success...");
            return response;
        } catch (Exception ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
            response.setCodigo(600);
            response.setMensaje(ex.getMessage());
            return response;
        }
    }
    
    public String getDetail(Integer materialId) {
        try {
            return dbUtil.resultSetToJson(Query.getDetailSQL(materialId), null);
        } catch (Exception ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
            return "{\"error\":\"error_detail\"}";
        }
    }
}
