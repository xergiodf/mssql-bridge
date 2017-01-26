package com.minicubic.mssqlbridge;

import com.google.gson.Gson;
import com.minicubic.mssqlbridge.util.DBUtil;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 *
 * @author xergio
 */
public class Main implements MqttCallback {

    /**
     * @param args the command line arguments
     */
    static MqttClient client;
    Logger log = Logger.getLogger("MAIN");
    Gson gson = new Gson();

    public Main() {
    }

    public static void main(String[] args) throws Exception {
        while(true){
            if (args.length > 0) {
                DBUtil dbUtil = new DBUtil();
                System.out.print(dbUtil.resultSetToJson(args[0], null));
            } else {
                new Main().doDemo();
            }
        }

    }

    public void doDemo() throws InterruptedException {
        try {
            if (client == null) {
                log.info("Intentando conectar al Broker...");
                //client = new MqttClient("tcp://190.128.229.6:1883", "loginDriver");
                client = new MqttClient("tcp://localhost:1883", "loginDriver");
                client.setCallback(this);
                client.connect();
                client.subscribe(new String[]{"/api/#"});
            }else{
                if (!client.isConnected()) {
                    log.info("Reconectamos al broker...");
                    reconectar();
                }else{
                    log.info("Ya esta conectado...Dormimos 5 segundos");
                    Thread.sleep(5000);
                }

            }

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable thrwbl) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        try {
            log.info("Recepcionando mensaje...");
            log.info("Topico: " + topic);
            log.info("Mensaje: " + message.getPayload().toString());
//            UserService userSvc = new UserService();
//            if (topic.contains("/login")) {
//                Type listType = new TypeToken<Request<UsuarioDTO>>(){}.getType();
//                Request<UsuarioDTO> request = gson.fromJson(message.toString(), listType);
//                UsuarioDTO usuario = request.getData();
//
//                Response<UsuarioDTO> response = userSvc.doLogin(usuario.getUser(), usuario.getPasswd());
//                if (client.isConnected()) {
//                    client.publish("loginResponse", new MqttMessage(gson.toJson(response).getBytes()));
//                }else{
//                    reconectar();
//                    client.publish("loginResponse", new MqttMessage(gson.toJson(response).getBytes()));
//                }            
//            } else if (topic.contains("/list")) {
//                log.info("Obteniendo Hoja de Ruta...");
//                Type listType = new TypeToken<Request<GestionHojaRuta>>(){}.getType();
//                Request<GestionHojaRuta> request = gson.fromJson(message.toString(), listType);
//                GestionHojaRuta gestionHojaRuta = request.getData();
//                Integer userId = gestionHojaRuta.getU_id();
//
//    //            String strMsg = userSvc.getList(userId);
//    //            this.output(topic, strMsg);
//                System.out.print(userSvc.getList(userId));
//            } else if (topic.contains("/detail")) {
//
//                Integer materialId = new Integer(topic.split("/")[3]);
//
//                //String strMsg = userSvc.getDetail(materialId);
//                //this.output(topic, strMsg);
//                System.out.print(userSvc.getDetail(materialId));
//            } else {
//
//                this.output(topic, new String(message.getPayload()));
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken imdt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void output(String topic, String message) {
        System.out.println("-------------------------------------------------");
        System.out.println("| Topic: " + topic);
        System.out.println("| Message: " + message);
        System.out.println("-------------------------------------------------");
    }
    
    private void reconectar() throws MqttException{
        client = new MqttClient("tcp://190.128.229.6:1883", "loginDriver");
        client.connect();
    }
}
