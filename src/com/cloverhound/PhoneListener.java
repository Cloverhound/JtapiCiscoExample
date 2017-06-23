package com.cloverhound;

import javax.telephony.CallObserver;
import javax.telephony.Connection;
import javax.telephony.callcontrol.CallControlTerminalConnection;
import javax.telephony.events.CallEv;

import com.cisco.jtapi.CallImpl;

final class PhoneListener implements CallObserver {
	
	private String extension;
	
	public PhoneListener(String extension) {
		this.extension = extension;
	}

    @Override
    public void callChangedEvent(CallEv[] events) {
    	for (CallEv callEvent : events){
        	try {
				handleCallEvent(callEvent);
			} catch (Exception e) {	
				e.printStackTrace();
			}
        }
    }
    
    
    private void handleCallEvent(CallEv callEvent) throws Exception { 
    	
    	String[] eventDetails = callEvent.toString().split(" ");
        if(eventDetails.length < 2) {
        	System.out.println("Current event has no extension info..ignoring");
        	return;
        }
        
        String eventName = eventDetails[0].toString();
        String eventExtn = eventDetails[1].split(":")[0];
        
        if (eventName.equals("ConnAlertingEv") && eventExtn.equals(extension) ){
        	System.out.println("Phone is ringing...answering");
        	
        	CallImpl call = (CallImpl)callEvent.getCall();
        	getTerminalConnection(call).answer();
        }
        
        
    }
    
    
    private CallControlTerminalConnection getTerminalConnection(CallImpl call) {
    	System.out.println("Getting CallControlTerminalConnection");
    	
    	Connection[] connections = call.getConnections();
        for(Connection connection : connections) {
        	if(connection.getAddress().getName().equals(extension)) {
        		return (CallControlTerminalConnection)(connection.getTerminalConnections()[0]);
        	}
        }
        
        System.out.println("No terminal connection found");
        return null;
    }
    
 
  
    
}