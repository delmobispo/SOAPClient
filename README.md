[![Build Status](https://travis-ci.org/delmobispo/SOAPClient.svg?branch=master)](https://travis-ci.org/delmobispo/SOAPClient)

# Exemplo de Uso da classe

```java

public class SOAPCorreiosClient {
	
	public static void main(String[] args) {
		SoapClient client = new SoapClient(
				"https://apps.correios.com.br/SigepMasterJPA/AtendeClienteService/AtendeCliente"
				).debug(true);
		try {
			//Complexidade para montar a mensagem soap
			MessageFactory messageFactory = MessageFactory.newInstance();
	    SOAPMessage soapRequest = messageFactory.createMessage();
	    SOAPPart soapPart = soapRequest.getSOAPPart();
	        
	    SOAPEnvelope envelope = soapPart.getEnvelope();
	    envelope.addNamespaceDeclaration("cli", "http://cliente.bean.master.sigep.bsb.correios.com.br/");
	        
	    SOAPBody soapBody = envelope.getBody();
	    SOAPElement soapBodyElem = soapBody.addChildElement("consultaCEP", "cli");
	    soapBodyElem.addChildElement("cep").addTextNode("73368470");
			SOAPMessage response = client.request(soapRequest).send().response();
		}catch (Exception exc) {
			exc.printStackTrace();
		}

	}	
}
```
