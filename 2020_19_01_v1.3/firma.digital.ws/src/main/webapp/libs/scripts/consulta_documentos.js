/* global validation, server, navigation, dom */

var env = {documento: {}, solicitud: {}, autoComplete: {}, config: {}, autocompletion: {}, callbacks: {}};

var componente = (function() {
    
    function init( datos) {
        env.datos = datos;
        
        if ( !env.datos.usuarioAutenticado) {
            return;
        }
        
        server.resource.load( 'componente_autocompletion', './libs/components/autocompletion/component', () => {
            server.resource.load( 'componente_tabla', './libs/components/tabla/component', () => {
                componente.initListado();
            });
        });
        
        dom.onClick( ".toggle", env.callbacks.toggle.onclick);
    }
    
    function init_listado() {
        document.getElementById( 'mensajeResultado').style.display= env.datos.showErrors? "" : "none";
                
        env.documentos = new Tabla( 'documento', env.config.documentos, env.callbacks.documentos);
        env.autoComplete.documentos = new AutoCompletion( 'autoComplete_documento', env.autocompletion.documentos, env.callbacks.documentos);
        env.documentos.update();
                
        env.solicitudes = new Tabla( "solicitud", env.config.solicitudes, env.callbacks.solicitudes);
        env.autoComplete.solicitudes = new AutoCompletion( 'autoComplete_solicitud', env.autocompletion.solicitudes, env.callbacks.solicitudes);
        env.solicitudes.update();        
    }
        
    function validar_form( event) {
        event.stopPropagation;
                        
        let errores = 0;
            
        errores += validation.validarArchivos(  'certificado', '.cer',   'Certificado',   1, 1);
        errores += validation.validarArchivos( 'llavePrivada', '.key', 'Llave Privada',   1, 1);        
        errores += validation.validarTexto( 'password', 'Contraseña',  20);
        
        let submit = (errores === 0);
            
        if( submit) {
            setTimeout( function() { jQuery( '#processing').modal( 'show');}, 0);
        }
            
        return submit;
    }
        
    return  {
        init       : init,
        initListado: init_listado,
        validar    : validar_form 
    };
})();

componente.documento = {
    obtener_pagina: function ( pagina) {
        env.documentos.mostrarPagina( pagina);
    }
};

componente.solicitud = {
    obtener_pagina: function ( pagina) {
        env.solicitudes.mostrarPagina( pagina);
    }
};

env.config.documentos = {
    module    : "documento",
    id        : "id",
    pagination: true,
    isEditable: false,
    columns   : [
        {value: "fechaHora", label: "Fecha Hora"},
        {value:     "folio", label:      "Folio"},
        {value:    "nombre", label:  "Documento"}
    ],
    actions   : [
        {value: 'view', icon: "download", title: "Descarga y validación del documento"}
    ],
    resource: "documento/firmado",
    search: {
        tipo: "PAGINADA", "propiedades": {}, "ordenacion": [{"campo": "fechaHora", "direccion": "desc"}]
    }
};

env.autocompletion.documentos = {
    resource: "documento/firmado",
    item: {
        value: "id",
        label: "(${fechaHora}) ${nombre} : ${folio}"
    }
};

env.config.solicitudes = {
    module    : "solicitud",
    id        : "id",
    pagination: true,
    isEditable: false,
    columns   : [
        {value:      "fechaHora", label: "Fecha Hora Solicitud"},
        {value:          "folio", label:                "Folio"},
        {value:         "nombre", label:            "Documento"},
        {value:        "estatus", label:              "Estatus"},
        {value:   "destinatario", label:         "Destinatario"},
        {value: "fechaHoraFirma", label:     "Fecha Hora Firma"}
    ],
    actions   : [
        {value: 'view', icon: "info-circle", title: "Descarga y validación del documento"}
    ],
    resource: "documento/solicitado",
    search: {
        tipo: "PAGINADA", "propiedades": {}, "ordenacion": [{"campo": "fechaHora", "direccion": "desc"}]
    }
};

env.autocompletion.solicitudes = {
    resource: "documento/firmado",
    item: {
        value: "id",
        label: "(${fechaHora}) ${nombre} : ${folio}"
    }
};

env.callbacks.documentos = {
    show_action_view: function( item, items) {
        return true;
    },

    on_click_action: function( info) {
        env.callbacks.documentos.consulta_documento( info.id);
    },

    on_click_autocomplete : function() {
        env.callbacks.documentos.consulta_documento( env.idItem);
    },
    
    request_data: function( search, callback) {
        let resource = env.config.documentos.resource;
        search.propiedades.actualizarTotalItems=true;
    
        server.data.list( resource, search, r => {
            dom.setText( "badgeDocumento", r.consulta.paginacion.totalItems);
            callback( r.items, r.consulta);
        });
    },
    
    consulta_documento: function( id) {
        server.data.get( 'documento/firmado/' + id, resp => {
            let item = resp.item;
            let folio = item.folio;
            let encodedName = encodeURIComponent( item.nombre);

            navigation.goto( 'validacionDocumento?folio='+ folio + '&nombre=' + encodedName);
        });
    }
};

env.callbacks.solicitudes = {
    show_action_view: function( item, items) {
        return true;
    },

    on_click_action: function( info) {
        env.callbacks.solicitudes.consulta_solicitud( info.id);
    },

    on_click_autocomplete : function() {
        env.callbacks.solicitudes.consulta_solicitud( env.idItem);
    },
    
    request_data: function( search, callback) {
        let resource = env.config.solicitudes.resource;
        search.propiedades.actualizarTotalItems=true;
    
        server.data.list( resource, search, function( r) {
            if( r.consulta.paginacion.totalItems === 0) {
                dom.getById( 'badgeSolicitud').parents( ".card").hide();
            } else {
                dom.setText( "badgeSolicitud", r.consulta.paginacion.totalItems);
                callback( r.items, r.consulta);
            }
        });
    },
    
    consulta_solicitud: function( id) {
        server.data.get( 'documento/solicitado/' + id, resp => {
            let item = resp.item;
            let folio = item.folio;
            let encodedName = encodeURIComponent( item.nombre);

            navigation.goto( 'validacionDocumento?folio='+ folio + '&nombre=' + encodedName);
        });
    }
};

env.callbacks.toggle =(function() {
    function on_click( event) {
        let item = jQuery( event.target);
        item = item.is( "a")? item : item.parent(':eq(0)');
        
        let toggle = item.data( 'toggle');
        let target = item.data( 'target');
    
        close_all();
        
        if ( toggle === 0) {
            dom.show("list_" + target);
            
            item.children( ':eq(0)').removeClass( 'fa-chevron-circle-down');
            item.children( ':eq(0)').addClass( 'fa-chevron-circle-up');
        
            item.data( 'toggle', 1);
        } 
    }
    
    function close_all() {
        let items = jQuery( ".toggle");
        
        dom.hide( ".toggleable");
        
        items.children().removeClass( 'fa-chevron-circle-up');
        items.children().addClass( 'fa-chevron-circle-down');
      
        items.data( 'toggle', 0);
    }
    
    return {
        onclick : on_click
    };
})();
