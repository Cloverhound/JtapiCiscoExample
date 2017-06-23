package com.cloverhound;

import javax.telephony.JtapiPeer;
import javax.telephony.JtapiPeerFactory;
import javax.telephony.Provider;
import javax.telephony.events.CallEv;

import com.cisco.jtapi.extensions.CiscoAddress;

final public class JtapiCiscoExample {
	
	
	
	public static void main(String[] args) {
		
		int curArg = 0;
		String cucmUrl = args[curArg++];
		String cucmLogin = args[curArg++];
		String cucmPasswd = args[curArg++];
		String extension = args[curArg++];
		
		String providerString = cucmUrl + ";login=" + cucmLogin + ";passwd=" + cucmPasswd;
		try {
			JtapiPeer peer = JtapiPeerFactory.getJtapiPeer ( null );
			Provider provider = peer.getProvider(providerString);
		
			addCallObserver(provider, extension);
		} catch( Exception e ) {
			System.out.println("Failed to connect to cucm");
		}
	}
	
	static void addCallObserver(Provider provider, String extension) throws Exception {
		
		CiscoAddress address = (CiscoAddress)provider.getAddress(extension);
		address.addCallObserver((CallEv[] events) -> {
			(new PhoneListener(address.getName())).callChangedEvent(events);
		});
	}
	
	
	

}