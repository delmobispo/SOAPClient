package br.com.delmobispo.comunicacao;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.junit.Assert;
import org.junit.Test;

public class SOAPClientTest {

	@Test
	public void integracaoBasica() {
		SOAPClient client = new SOAPClient(
				"https://apps.correios.com.br/SigepMasterJPA/AtendeClienteService/AtendeCliente"
				).debug(true);
		try {
			MessageFactory messageFactory = MessageFactory.newInstance();
	        SOAPMessage soapRequest = messageFactory.createMessage();
	        SOAPPart soapPart = soapRequest.getSOAPPart();
	        SOAPEnvelope envelope = soapPart.getEnvelope();
	        envelope.addNamespaceDeclaration("cli", "http://cliente.bean.master.sigep.bsb.correios.com.br/");
	        SOAPBody soapBody = envelope.getBody();
	        SOAPElement soapBodyElem = soapBody.addChildElement("consultaCEP", "cli");
	        soapBodyElem.addChildElement("cep").addTextNode("73368470");
			client.request(soapRequest).send();
			Assert.assertEquals(client.response() != null, true);
		}catch (Exception exc) {
			exc.printStackTrace();
			Assert.assertEquals(true, false);
		}
	}
	
}
