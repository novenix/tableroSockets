package co.edu.escuelaing.interactivebalckboardlife.entitie;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class TickMemory {
    private static final TickMemory instance = new TickMemory();
    private final List<String> ticketList;
    private String ticketNumber;

    public TickMemory(){
        ticketList = new CopyOnWriteArrayList<>();
        ticketNumber = UUID.randomUUID().toString();
    }

    public static TickMemory getInstance(){
        return instance;
    }

    public synchronized String setTicketNumber(String ipAddress){
        ticketNumber = ipAddress + "password";
        ticketList.add(ticketNumber);
        return ticketNumber;
    }

    public boolean checkTicket(String ticketNumber){
        boolean isValid = false;
        if (ticketList.contains(ticketNumber)) {
            isValid = true;
        }
        return isValid;
    }
}