package br.com.delmobispo.comunicacao;

import java.io.IOException;
import java.util.Iterator;

import javax.xml.soap.MimeHeader;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

public class SoapClient {
	
	private String soapEndpointUrl;
    private String soapAction;
    private boolean debug;
    private SOAPMessage soapRequest;
    private SOAPMessage soapResponse;
    
    public SoapClient(String soapEndpointUrl) {
    	this(soapEndpointUrl, "");
	}
    
    public SoapClient(String soapEndpointUrl, String soapAction) {
    	setSoapEndpointUrl(soapEndpointUrl);
    	setSoapAction(soapAction);
    	debug(true);
	}
    
    public SoapClient debug(boolean debug) {
    	this.debug = debug;
    	return this;
    }
    
    /**
     * {@link https://docs.oracle.com/javaee/5/tutorial/doc/bnbhg.html}
     * <br>
     * <br>
     * Exemplo montagem da mensagem SOAP usando o webservice dos correios:
     * <br>
     * <br>
     * <code>
     * MessageFactory messageFactory = MessageFactory.newInstance();
     * SOAPMessage soapRequest = messageFactory.createMessage();
     * SOAPPart soapPart = soapRequest.getSOAPPart();
     * SOAPEnvelope envelope = soapPart.getEnvelope();
     * envelope.addNamespaceDeclaration("cli", "http://cliente.bean.master.sigep.bsb.correios.com.br/");
     * SOAPBody soapBody = envelope.getBody();
     * SOAPElement soapBodyElem = soapBody.addChildElement("consultaCEP", "cli");
     * soapBodyElem.addChildElement("cep").addTextNode("73368470");
     * </code>
     * @param soapRequest
     * @return {@link SoapClient}
     * @throws SOAPException
     * @throws IOException
     */
    public SoapClient request(SOAPMessage soapRequest) throws SOAPException, IOException {
    	MimeHeaders headers = soapRequest.getMimeHeaders();
        headers.addHeader("SOAPAction", getSoapAction());
        soapRequest.saveChanges();
        if(debug) {
        	System.out.println("Request Headers:");
        	imprimeMimeHeaders(headers);
        	System.out.println("\nRequest SOAP Message:");
        	soapRequest.writeTo(System.out);
        	System.out.println("\n");
        }
        setSoapRequest(soapRequest);
    	
    	return this;
    }
    
    public SoapClient send() throws UnsupportedOperationException, SOAPException, IOException {
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();
        
        setSoapResponse(soapConnection.call(request(), getSoapEndpointUrl()));
        
        if(debug) {
        	System.out.println("Response Headers:");
            imprimeMimeHeaders(response().getMimeHeaders());
        	System.out.println("\nResponse SOAP Message:");            	
            response().writeTo(System.out);
            System.out.println("\n");
        }
        soapConnection.close();
        if(response().getSOAPBody().getFault() != null) {
        	throw new SOAPException("O servidor remoto retornou um erro!");
        }
        return this;
    }

	private void imprimeMimeHeaders(MimeHeaders mimeHeaders) {
		@SuppressWarnings("rawtypes")
		Iterator allHeaders = mimeHeaders.getAllHeaders();
		while(allHeaders.hasNext()) {
			MimeHeader header = (MimeHeader) allHeaders.next();
			System.out.println(header.getName() + " - " + header.getValue());
		}
	}

	public String getSoapEndpointUrl() {
		return soapEndpointUrl;
	}

	private void setSoapEndpointUrl(String soapEndpointUrl) {
		this.soapEndpointUrl = soapEndpointUrl;
	}

	public String getSoapAction() {
		return soapAction;
	}

	private void setSoapAction(String soapAction) {
		this.soapAction = soapAction;
	}

	public SOAPMessage request() {
		return soapRequest;
	}

	private void setSoapRequest(SOAPMessage soapRequest) {
		this.soapRequest = soapRequest;
	}

	public SOAPMessage response() {
		return soapResponse;
	}

	private void setSoapResponse(SOAPMessage soapResponse) {
		this.soapResponse = soapResponse;
	}

}
