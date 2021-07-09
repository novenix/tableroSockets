/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.escuelaing.interactivebalckboardlife.endpoints;


import java.io.IOException;
import java.util.logging.Level;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import co.edu.escuelaing.interactivebalckboardlife.entitie.TickMemory;
import org.springframework.stereotype.Component;


@Component
@ServerEndpoint("/bbService")
public class BBEndpoint {


    private static final Logger logger = Logger.getLogger(BBEndpoint.class.getName());
    /* Queue for all open WebSocket sessions */
    static Queue<Session> queue = new ConcurrentLinkedQueue<>();

    Session ownSession = null;
    private HttpServletRequest request;
    private TickMemory ticketMemory = TickMemory.getInstance();
    /* Call this method to send a message to all clients */
    public void send(String msg) {
        try {
            /* Send updates to all open WebSocket sessions */
            for (Session session : queue) {
                if (!session.equals(this.ownSession)) {
                    session.getBasicRemote().sendText(msg);
                }
                logger.log(Level.INFO, "Sent: {0}", msg);
            }
        } catch (IOException e) {
            logger.log(Level.INFO, e.toString());
        }
    }

    @OnMessage
    public void processPoint(String message, Session session) {
        logger.log(Level.INFO, "Point received:" + message + ". From session: " + session);
        this.send(message);
    }

    @OnOpen
    public void openConnection(Session session) throws IOException{
        /* Register this connection in the queue */
        if(ticketMemory.checkTicket(request.getRemoteHost()+"password")){
            queue.add(session);
            ownSession = session;
            logger.log(Level.INFO, "Connection opened.");
            try {
                session.getBasicRemote().sendText("Connection established.");
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }else {
            ownSession.close();
        }
    }


    @OnClose
    public void closedConnection(Session session) {
        /* Remove this connection from the queue */
        queue.remove(session);
        logger.log(Level.INFO, "Connection closed for session " + session);
    }


    @OnError
    public void error(Session session, Throwable t) {
        /* Remove this connection from the queue */
        queue.remove(session);
        logger.log(Level.INFO, t.toString());
        logger.log(Level.INFO, "Connection error.");
    }
}