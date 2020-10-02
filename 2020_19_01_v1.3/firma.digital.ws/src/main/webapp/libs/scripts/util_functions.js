  
var validation = (function() { 

    function validarArchivos( id, extension, nombre, maxItems, maxSizeMB) {
        let errorId = 'error_' + id;

        hide(errorId);

        let files = jQuery("#" + id + ":eq(0)")[0].files;

        if (files.length === 0) {
            update(errorId, "El archivo de " + nombre + " es requerido");
            return 1;
        }

        if (noCumplenConExtension(files, extension)) {
            update(errorId, "La extensión del archivo(s) deber ser (" + extension + ")");
            return 1;
        }

        if (excedenMaxItems(files, maxItems)) {
            update(errorId, "El número del archivos no debe ser mayor a " + maxItems);
            return 1;
        }

        if (excedenMaxSizeMB(files, maxSizeMB)) {
            update(errorId, "El tamaño total del archivo(s) no debe exceder " + maxSizeMB + " MB");
            return 1;
        }

        return 0;
    }
        
    function validarTexto(id, nombre, maxLength) {
        let errorId = 'error_' + id;

        hide(errorId);

        let value = jQuery("#" + id).val().trim();
        if (value.length === 0) {
            update(errorId, "El campo " + nombre + " es requerido");
            return 1;
        }

        if (value.length > maxLength) {
            update(errorId, "El campo " + nombre + " no debe exceder de " + maxLength + " caracteres");
            return 1;
        }

        return 0;
    }

    function validarCorreo(id, nombre, maxLength) {
        let errorId = 'error_' + id;

        hide(errorId);

        let value = jQuery("#" + id).val().trim();

        if (value.length === 0) {
            return 0;
        }

        if (!isValidEmail(value)) {
            update(errorId, "El " + nombre + " no tiene un formato valido valido");
            return 1;
        }

        if (value.length > maxLength) {
            update(errorId, "El " + nombre + " no debe exceder de " + maxLength + " caracteres");
            return 1;
        }

        return 0;
    }

    function isValidEmail(value) {
        return /^\w+([\.\+\-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,4})+$/.test(value);
    }

    function noCumplenConExtension(files, extension) {
        let error = false;
        let file;

        for (let i = 0; i < files.length; ++i) {
            file = files[i];
            if (!file.name.toLowerCase().endsWith(extension)) {
                error = true;
            }
        }

        return error;
    }

    function excedenMaxItems(files, maxItems) {
        return files.length > maxItems;
    }

    function excedenMaxSizeMB(files, maxSizeMB) {
        let total = 0;
        let file;

        for (let i = 0; i < files.length; ++i) {
            file = files[i];
            total = total + file.size;
        }

        return total > (maxSizeMB * 1024 * 1024);
    }

    function hide(id) {
        jQuery('#' + id).attr('hidden', true);
    }

    function update(id, text) {
        jQuery('#' + id).text(text).attr('hidden', false);
    }
      
        
    return {
        validarArchivos: validarArchivos,
        validarTexto   : validarTexto,
        validarCorreo  : validarCorreo
    };       
})();


var navigation = (function() {
    
    function goto( page) {
        var link = document.getElementById('download');
        link.href = './' + page;
        link.click();
    }

    function logout() {
        var link = document.getElementById('download');
        link.href = './logout';
        link.click();
    }
    
    return {
        logout: logout,
        goto  : goto
    };
    
})();

        
       
        