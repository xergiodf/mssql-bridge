/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.minicubic.mssqlbridge;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import static com.minicubic.mssqlbridge.SampleMqttClient.LOG;
import com.minicubic.mssqlbridge.dto.Detalle;
import com.minicubic.mssqlbridge.dto.Gestion;
import com.minicubic.mssqlbridge.dto.HojaRuta;
import com.minicubic.mssqlbridge.dto.Motivo;
import com.minicubic.mssqlbridge.dto.Request;
import com.minicubic.mssqlbridge.dto.Response;
import com.minicubic.mssqlbridge.dto.UsuarioDTO;
import com.minicubic.voiapp.services.UserService;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.LoggerFactory;

/**
 *
 * @author hectorvillalba
 */
public class BrokerMQTTClient implements MqttCallback {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(BrokerMQTTClient.class);

    private String msg = "";
    MqttClient client;
    private Gson gson = new GsonBuilder().create();


    private static final String BROKER_ID = "driver-voicenter";

    MqttConnectOptions connOpts = new MqttConnectOptions();

    private static final String MQTT_TOPIC = "/api/#";
    public static void main(String[] args) {
        BrokerMQTTClient brokerMQTTClient = new BrokerMQTTClient();
        brokerMQTTClient.connect();
        brokerMQTTClient.subscribeToRegisters();
    }
    //Conexion MQTT
    public synchronized void connect() {
        
        try
        {
            client = new MqttClient("tcp://localhost:1883", BROKER_ID, null);
            client.connect();

            System.out.println("Conectado a " + client);

            LOGGER.info("Conectado a " + client);
        }
        catch (MqttException ex)
        {
            System.out.println("Fallo la conexion a MQTT " + ex);
            LOGGER.error("Fallo conexion a MQTT.Error: " + ex );
            
            
            //Thread.sleep(60000);
            connect();
            subscribeToRegisters();
        }

        
    }


    @Override
    public void connectionLost(Throwable thrwbl) {
        LOGGER.info("Conexion perdida, intentando recuperar");
        connect();
        subscribeToRegisters();
    }
    
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("-------------------------------------------------");
        System.out.println("| Topic:" + topic);
        System.out.println("| QoS: " + message.getQos());
        System.out.println("| Message: " + new String(message.getPayload()));
        System.out.println("-------------------------------------------------");
        try {
            LOG.info("Recepcionando mensaje...");
            LOG.log(Level.INFO, "Topico: {0}", topic);
            LOG.log(Level.INFO, "Mensaje: {0}", Arrays.toString(message.getPayload()));
            UserService userSvc = new UserService();
            if (topic.contains("/login")) {
                Type listType = new TypeToken<Request<UsuarioDTO>>() {
                }.getType();
                Request<UsuarioDTO> request = gson.fromJson(message.toString(), listType);
                UsuarioDTO usuario = request.getData();

                Response<UsuarioDTO> response = userSvc.doLogin(usuario.getUser(), usuario.getPasswd());
                client.publish("loginResponse", new MqttMessage(gson.toJson(response).getBytes()));
                client.close();
            } else if (topic.contains("/listHojaRutaRequest")) {
                
                LOG.info("Obteniendo Hojas de Ruta...");
                Type listType = new TypeToken<Request<HojaRuta>>() {
                }.getType();
                Request<HojaRuta> request = gson.fromJson(message.toString(), listType);
                HojaRuta hojaRuta = request.getData();
                Integer userId = hojaRuta.getUserId();
                Response<List<HojaRuta>> response = userSvc.getListHojaRuta(userId);
                client.publish("listHojaRutaResponse", new MqttMessage(gson.toJson(response).getBytes()));
                client.close();
            } else if (topic.contains("/listGestionesRequest")) {
                
                LOG.info("Obteniendo Gestiones...");
                Type listType = new TypeToken<Request<Gestion>>() {
                }.getType();
                Request<Gestion> request = gson.fromJson(message.toString(), listType);
                Gestion gestion = request.getData();
                Integer hojaRutaId = gestion.getHojaRutaId();
                Response<List<Gestion>> response = userSvc.getListGestion(hojaRutaId);
                client.publish("listGestionesResponse", new MqttMessage(gson.toJson(response).getBytes()));
                client.close();
            }   else if (topic.contains("/detailRequest")) {

                LOG.info("Obteniendo Detalle...");
                Type listType = new TypeToken<Request<Detalle>>() {
                }.getType();
                Request<Detalle> request = gson.fromJson(message.toString(), listType);
                Detalle detalle = request.getData();
                Integer gestionId = detalle.getGestionId();
                Response<List<Detalle>> response = userSvc.getListDetalle(gestionId);
                client.publish("detailResponse", new MqttMessage(gson.toJson(response).getBytes()));
                client.close();
            } else if ( topic.contains("/updateEstadoRequest") ) {
                
                LOG.info("Actualizando Estado Gestion");
                Type listType = new TypeToken<Request<Gestion>>() {
                }.getType();
                Request<Gestion> request = gson.fromJson(message.toString(), listType);
                Gestion gestion = request.getData();
                Response<List<Gestion>> response = userSvc.updateGestionEstado(gestion.getId(), gestion.getUserId(), gestion.getEntregado(), gestion.getMotivoId(), gestion.getComentario());
            
                client.publish("updateEstadoResponse", new MqttMessage(gson.toJson(response).getBytes()));
                client.close();
            } else if ( topic.contains("/listMotivosRequest") ) {
                
                LOG.info("List Motivos Request");
                Response<List<Motivo>> response = userSvc.listMotivos();
                client.publish("listMotivosResponse", new MqttMessage(gson.toJson(response).getBytes()));
                client.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    public void subscribeToRegisters() {
        try {
            client.setCallback(this);
            client.subscribe(MQTT_TOPIC,2);
            System.out.println("Suscribiendose a " + MQTT_TOPIC);
            LOGGER.info("Suscribiendose a  " + MQTT_TOPIC);
            
            client.close();
            
            client.disconnect();
        } catch (MqttException ex) {
            LOGGER.error("Error al suscribirse " + ex, ex);
        }

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken imdt) {
        System.out.println("el mensaje del broker se entrego.. ");
    }

}
