package com.eureka.firma.digital.ws.service;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.7.6
 * 2013-10-29T19:29:07.795-06:00
 * Generated source version: 2.7.6
 * 
 */
@WebServiceClient(
	name = "FirmaElectronicaService", 
    wsdlLocation = "http://localhost:8080/digitalSignWSSS/servicios/FirmaElectronica?wsdl",
    targetNamespace = "http://service.ws.digital.firma.eureka.com/"
)
public class FirmaElectronicaService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://service.ws.digital.firma.eureka.com/", "FirmaElectronicaService");
    public final static QName FirmaElectronicaServiceImplPort = new QName("http://service.ws.digital.firma.eureka.com/", "FirmaElectronicaServiceImplPort");
    static {
        URL url = null;
        try {
            url = new URL("http://localhost:8080/digitalSignWSSS/servicios/FirmaElectronica?wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(FirmaElectronicaService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "http://localhost:8080/digitalSignWSSS/servicios/FirmaElectronica?wsdl");
        }
        WSDL_LOCATION = url;
    }

    public FirmaElectronicaService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public FirmaElectronicaService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public FirmaElectronicaService() {
        super(WSDL_LOCATION, SERVICE);
    }
    

    /**
     *
     * @return
     *     returns IFirmaElectronica
     */
    @WebEndpoint(name = "FirmaElectronicaServiceImplPort")
    public IFirmaElectronica getFirmaElectronicaServiceImplPort() {
        return super.getPort(FirmaElectronicaServiceImplPort, IFirmaElectronica.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns IFirmaElectronica
     */
    @WebEndpoint(name = "FirmaElectronicaServiceImplPort")
    public IFirmaElectronica getFirmaElectronicaServiceImplPort(WebServiceFeature... features) {
        return super.getPort(FirmaElectronicaServiceImplPort, IFirmaElectronica.class, features);
    }

}
