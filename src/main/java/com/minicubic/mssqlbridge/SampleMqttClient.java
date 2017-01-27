/*
 * Copyright 2016 Ken.Barr
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.minicubic.mssqlbridge;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.minicubic.mssqlbridge.dto.Detalle;
import com.minicubic.mssqlbridge.dto.Gestion;
import com.minicubic.mssqlbridge.dto.GestionHojaRuta;
import com.minicubic.mssqlbridge.dto.HojaRuta;
import com.minicubic.mssqlbridge.dto.Motivo;
import com.minicubic.mssqlbridge.dto.Request;
import com.minicubic.mssqlbridge.dto.Response;
import com.minicubic.mssqlbridge.dto.UsuarioDTO;
import com.minicubic.voiapp.services.UserService;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class SampleMqttClient implements MqttCallbackExtended {

    MqttAsyncClient myClient;
    MqttConnectOptions connOpt;

    static SyncronizeObj sync;
    static String BROKER_URL1 = "tcp://localhost:1883";
    static String BROKER_URL2;
    static final String M2MIO_DOMAIN = "testDomain";
    static final String M2MIO_STUFF = "things";
    static final String M2MIO_THING = "driver-voicenter";
    static final String M2MIO_USERNAME = System.getenv("MQTT_USERNAME");
    static final String M2MIO_PASSWORD_MD5 = System.getenv("MQTT_PASSWORD");
    static final String M2MIO_CLIENTNAME_TOPIC = "$SYS/client/client-name";
    static final String M2MIO_REPLYTO_TOPIC = "$SYS/client/reply-to";

    // the following two flags control whether this example is a PUBLISHER, a SUBSCRIBER or both
    static final Boolean SUBSCRIBER = true;
    static final Boolean PUBLISHER = true;
    String clientName = "";
    String replyToTopic = "";
    static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger("MAIN");
    Gson gson = new Gson();

    public SampleMqttClient() {
        //BROKER_URL1 = System.getenv("MQTT_BROKER_URL1");
        //BROKER_URL2 = System.getenv("MQTT_BROKER_URL2");
        System.out.println("Broker list will be: " + BROKER_URL1);
        sync = new SyncronizeObj();
    }

    /**
     *
     */
    public void addSubscriptions() {
        try {
            // topics on m2m.io are in the form <domain>/<stuff>/<thing>
            ///zString myTopic = M2MIO_DOMAIN + "/" + M2MIO_STUFF + "/" + M2MIO_THING;
            ///myClient.subscribe(M2MIO_REPLYTO_TOPIC, 0);
            ///myClient.subscribe(M2MIO_CLIENTNAME_TOPIC, 0);
            myClient.subscribe("/api/#", 0);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     *
     * MAIN
     *
     * @param args
     */
    public static void main(String[] args) {
        Thread t = new Thread(() -> {
            SampleMqttClient smc = new SampleMqttClient();
            smc.runClient();
        });
        t.start();
    }

    /**
     *
     * runClient The main functionality of this simple example. Create a MQTT
     * client, connect to broker, pub/sub, disconnect.
     *
     */
    public void runClient() {
        // setup MQTT Client
        String clientID = M2MIO_THING;
        connOpt = new MqttConnectOptions();

        connOpt.setCleanSession(true);
        connOpt.setKeepAliveInterval(300);
        connOpt.setConnectionTimeout(60);
        connOpt.setAutomaticReconnect(true);
        Properties props = new Properties();
        connOpt.setSSLProperties(props);
        //connOpt.setUserName(M2MIO_USERNAME);
        //connOpt.setPassword(M2MIO_PASSWORD_MD5.toCharArray());
        //connOpt.setWill("mqtt/disconnect/ungracefull", ("MQTT Client: " + M2MIO_USERNAME + " ungracefull disconnect").getBytes(), 0, false);
        MemoryPersistence persistence = new MemoryPersistence();
        String[] brokerList = new String[1];
        brokerList[0] = BROKER_URL1;
        //brokerList[1] = BROKER_URL2;
        connOpt.setServerURIs(brokerList);

        DisconnectedBufferOptions bufferOpts = new DisconnectedBufferOptions();
        bufferOpts.setBufferEnabled(true);
        bufferOpts.setBufferSize(100);            // 100 message buffer
        bufferOpts.setDeleteOldestMessages(true); // Purge oldest messages when buffer is full
        bufferOpts.setPersistBuffer(false);       // Do not buffer to disk

        // Connect to Broker
        try {
            myClient = new MqttAsyncClient(brokerList[0], clientID, persistence);
            //myClient.setBufferOpts(bufferOpts);
            myClient.setCallback(this);
            System.out.println("Connection attempt! To: " + brokerList[0]);
            myClient.connect(connOpt);
            System.out.println("Blocking for known issue: #233");
            sync.doWait((long) 1 * 1000);
            System.out.println("Received initial connection signal, continuing");
        } catch (MqttException ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }

        String myTopic = M2MIO_DOMAIN + "/" + M2MIO_STUFF + "/" + M2MIO_THING;
        MqttMessage message = new MqttMessage("TEST MESSAGE".getBytes());
        message.setQos(0);
        try {
            System.out.println("Publish1");
            myClient.publish(myTopic, message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void deliveryComplete(IMqttDeliveryToken token) {
        try {
            System.out.println("Pub complete" + new String(token.getMessage().getPayload()));
        } catch (MqttException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

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
                myClient.publish("loginResponse", new MqttMessage(gson.toJson(response).getBytes()));
            } else if (topic.contains("/listHojaRutaRequest")) {
                
                LOG.info("Obteniendo Hojas de Ruta...");
                Type listType = new TypeToken<Request<HojaRuta>>() {
                }.getType();
                Request<HojaRuta> request = gson.fromJson(message.toString(), listType);
                HojaRuta hojaRuta = request.getData();
                Integer userId = hojaRuta.getUserId();
                Response<List<HojaRuta>> response = userSvc.getListHojaRuta(userId);
                myClient.publish("listHojaRutaResponse", new MqttMessage(gson.toJson(response).getBytes()));
            } else if (topic.contains("/listGestionesRequest")) {
                
                LOG.info("Obteniendo Gestiones...");
                Type listType = new TypeToken<Request<Gestion>>() {
                }.getType();
                Request<Gestion> request = gson.fromJson(message.toString(), listType);
                Gestion gestion = request.getData();
                Integer hojaRutaId = gestion.getHojaRutaId();
                Response<List<Gestion>> response = userSvc.getListGestion(hojaRutaId);
                myClient.publish("listGestionesResponse", new MqttMessage(gson.toJson(response).getBytes()));
            }   else if (topic.contains("/detailRequest")) {

                LOG.info("Obteniendo Detalle...");
                Type listType = new TypeToken<Request<Detalle>>() {
                }.getType();
                Request<Detalle> request = gson.fromJson(message.toString(), listType);
                Detalle detalle = request.getData();
                Integer gestionId = detalle.getGestionId();
                Response<List<Detalle>> response = userSvc.getListDetalle(gestionId);
                myClient.publish("detailResponse", new MqttMessage(gson.toJson(response).getBytes()));
            } else if ( topic.contains("/updateEstadoRequest") ) {
                
                LOG.info("Actualizando Estado Gestion");
                Type listType = new TypeToken<Request<Gestion>>() {
                }.getType();
                Request<Gestion> request = gson.fromJson(message.toString(), listType);
                Gestion gestion = request.getData();
                Response<List<Gestion>> response = userSvc.updateGestionEstado(gestion.getId(), gestion.getUserId(), gestion.getEntregado(), gestion.getMotivoId(), gestion.getComentario());
                myClient.publish("updateEstadoResponse", new MqttMessage(gson.toJson(response).getBytes()));
            } else if ( topic.contains("/listMotivosRequest") ) {
                
                LOG.info("List Motivos Request");
                Response<List<Motivo>> response = userSvc.listMotivos();
                myClient.publish("listMotivosResponse", new MqttMessage(gson.toJson(response).getBytes()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void connectionLost(Throwable t) {
        System.out.println("Connection lost!" + t.toString());
    }

    public void connectComplete(boolean reconnect, String serverURI) {
        if (reconnect) {
            System.out.println("Connection Reconnected! To: " + serverURI);
        } else {
            System.out.println("Initial Connection! To: " + serverURI);
            sync.doNotify();
        }
        addSubscriptions();
    }

    public class SyncronizeObj {

        public void doWait(long l) {
            synchronized (this) {
                try {
                    this.wait(l);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        public void doNotify() {
            synchronized (this) {
                this.notify();
            }
        }

        public void doWait() {
            synchronized (this) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

}
