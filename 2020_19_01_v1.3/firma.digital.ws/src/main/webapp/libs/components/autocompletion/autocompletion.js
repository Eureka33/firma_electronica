/* global dom, modMngr, server, array, util, env */

/**
 *
 *	@param id 				'identificador', debe existir un contenedor 'contenedor_{identificador}'
 *	@param configuration 	bean que acepta las siguientes propiedades	  
 *		{
 *          module      : '<nombre_modulo>'  al dar click en un elemento: 
 *                          env[module].item = <idSeleccionado>
 *                          env[module].operation = 'edit'
 *                          moduleMngr.execAction( 'edit_' + module)
 *                           
 *	 		template 	: '<idTemplate>'	(default: 'autocompletion_template')
 *			itemTemplate: '<idItemTemplate>'(default: 'autocompletion_item'    )
 *	 		placeholder : '<placeholder>'	(default: 'Palabras de búsqueda'   )
 *	 		minLength  	: <NN> 				(default:  3)
 *	 		searchProps	: {propiedades ...} (default: {})
 *	 		resource	: '<modulo>/<entidad>'
 			item		: {
                value: '<field_value>',         propiedad utilizada como identificador
                label: '<template_string>'      template para la etiqueta de cada resultado
            }
 * 		}
 */
var AutoCompletion = function ( id, configuration, callbacks) {
	this.id = id;
	this.configuration  = configuration;
	
	this.id_contenedor  = 'container_' + id;
	this.id_input       = 'drpdwn_txt_' + id;
	this.id_list        = 'drpdwn_lst_' + id;
    this.callbacks      = callbacks || {};
	
	this.tarea          = null;
	
	this.init();	
};

AutoCompletion.prototype = (function() {
	
	function init() {
		let $ = this;
		
		layout_component( $);
		init_component(   $);
	}
			
	function layout_component( $) {
		let jContenedor = dom.getById( $.id_contenedor);
		jContenedor.empty();
		
		let configuration = $.configuration;
		let template = configuration.template || 'autocompletion_template';
		
		dom.append( jContenedor, template, { id: $.id, placeholder: configuration.placeholder || 'Palabras de búsqueda'});			
	}
		
	function init_component( $) {
		dom.getById( $.id_input).on( 'keyup keydown', generate_on_keyup_down_text( $));
		dom.onClick( $.id_list, generate_on_click_list_item( $));
	}
	
	function generate_on_keyup_down_text( $) {
		return function( event) {
		
			let nombreEvento = event.originalEvent.type;
			let minLength = $.configuration.minLength || 1;
			if (nombreEvento === 'keyup') { // may start search
				let valor = event.target.value;
				if (valor.length > minLength) {
					$.tarea = setTimeout( generate_on_start_search_callback( $), 600);
				}
			} else { // stops current search
				if ($.tarea !== null) {
					clearTimeout($.tarea);
					$.tarea = null;
				}
			}
		};
	}
	
	function generate_on_start_search_callback( $) {
		return function() {
			$.tarea = null;
			
			let configuration = $.configuration;
			let tokens = util.getTokens( dom.getText( $.id_input));
			
            let propiedades;
            
            if( $.callbacks && $.callbacks.requestProperties) {
                propiedades = $.callbacks.requestProperties();
            
            } else {
                propiedades = configuration.searchProps || {};
            
            }
            
			let bean = {tipo: "AUTOCOMPLEATADA", tokens: tokens, propiedades: propiedades};
			
			server.data.list( configuration.resource, bean, function(respuesta) {
				let jElement = dom.getById( $.id_list);
				jElement.empty();
				
				let longitud = respuesta.items.length;
				let template = $.itemTemplate || 'autocompletion_item';
				
				if (longitud === 0) {
					dom.append(jElement, template, {value: '', label: 'No se han encontrado resultados.', tokens: []});
				} else {
					let etiqueta = '`' + configuration.item.label.replace( /\${/g, "$\{item.") + '`';
					array.each(respuesta.items, function(indice, item) {
						let bean = {};
						
						bean.value	= item[ configuration.item.value];
						bean.label	= eval( etiqueta);
						bean.tokens = tokens;
						
						dom.append(jElement, template, bean);
					});
				}
				
				jElement.css("display", "").addClass( "show");
			});
		};
	}

	function generate_on_click_list_item($) {
			
		return function(event) {
			let anchor = event.target;
			
			if (anchor.nodeName !== 'A') {
				anchor = anchor.parentNode;
				if (anchor.nodeName !== 'A') {
					return;
				}
			}
            
            idItem = anchor.dataset.value;
            if( $.configuration.targetType === 'int') {
                idItem = util.parseInt( idItem);
            }
            
			if ( typeof(idItem) !== 'undefined' && (idItem !== null)) {
                let module = $.configuration.module;
                
                if( $.callbacks && $.callbacks.on_click_autocomplete) {
                    if( module) {
                        setEditProperties( $, env[module], idItem);
                    
                    } else {
                        setEditProperties( $, env, idItem);
                    
                    }
                    
                    setTimeout( $.callbacks.on_click_autocomplete, 0);
                    
                } else {
                    if( module) {
                        setEditProperties( $, env[module], idItem);
                        modMngr.execAction( 'edit_' + module);
                
                    } else {
                        setEditProperties( $, env, idItem);
                        modMngr.execAction( 'edit');
                    }
                }
			} else {
				return false;
			}
			
			dom.getById( $.id_list).css("display", "").removeClass( "show");
		};
	}
    
    function setEditProperties($, bean, idItem) {
        bean.idItem = idItem;
        
        let callback = $.callbacks.editProperties;
        if ( callback) {
            let props = callback();
            for( let p in props) {
                bean[ p] = props[ p];
            }
            
        } else {
            bean.operation = 'edit';
        }
    }
	
	return {
		init : init
	};
	
})();