package com.meve.ofspapel.firma.digital.core.service;

import com.eurk.core.beans.consulta.Consulta;
import com.meve.ofspapel.firma.digital.core.components.DocumentoSolicitadoBsnsComponent;
import java.util.List;
import mx.com.neogen.commons.bean.Propiedades;
import mx.com.neogen.commons.interfaces.IConsultableService;
import mx.com.neogen.commons.interfaces.Invoker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class DocumentoSolicitadoServiceImpl implements IConsultableService {
    
    @Autowired private DocumentoSolicitadoBsnsComponent bsnsComponent;
    
    @Transactional( readOnly = true)
    @Override
    public Object getItem( String claveOrganizacion, Invoker invocador, String idItem, Propiedades propiedades) {
        return bsnsComponent.obtenerItem( claveOrganizacion, invocador, idItem, propiedades);
    }

    @Transactional( readOnly = true)
    @Override    
    public List<?> getItems(String claveOrganizacion, Invoker invocador, Consulta consulta) {
        return bsnsComponent.listarItems( claveOrganizacion, invocador, consulta);
    }
    
}
