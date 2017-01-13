package com.minicubic.voiapp.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.minicubic.mssqlbridge.dto.Detalle;
import com.minicubic.mssqlbridge.dto.Gestion;
import com.minicubic.mssqlbridge.dto.HojaRuta;
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
            List<UsuarioDTO> usuario = gson.fromJson(json, new TypeToken<List<UsuarioDTO>>() {
            }.getType());

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

    public Response<List<HojaRuta>> getListHojaRuta(Integer userId) {
        Response<List<HojaRuta>> response = new Response<>();
        try {
            String json = dbUtil.resultSetToJson(Query.getListSQLHojaRuta(userId), null);
            
            List<HojaRuta> lista = gson.fromJson(json, new TypeToken<List<HojaRuta>>() {
            }.getType());
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

    public Response<List<Gestion>> getListGestion(Integer hojaRutaId) {
        Response<List<Gestion>> response = new Response<>();
        try {
            String json = dbUtil.resultSetToJson(Query.getListSQLGestion(hojaRutaId), null);

            List<Gestion> lista = gson.fromJson(json, new TypeToken<List<Gestion>>() {
            }.getType());
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
    
    public Response<List<Detalle>> getListDetalle(Integer gestionId) {
        Response<List<Detalle>> response = new Response<>();
        try {
            String json = dbUtil.resultSetToJson(Query.getDetailSQL(gestionId), null);

            List<Detalle> lista = gson.fromJson(json, new TypeToken<List<Detalle>>() {
            }.getType());
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
