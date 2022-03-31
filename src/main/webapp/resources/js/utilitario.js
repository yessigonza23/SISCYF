/**
 * 
 */
function mayusculas(elemento) {
	elemento.value = elemento.value.toUpperCase();
}

function minusculas(elemento) {
	elemento.value = elemento.value.toLowerCase();
}

function solonumeros(e) {
	key = e.keyCode || e.which;
	teclado = String.fromCharCode(key);
	numeros = "0123456789";
	especiales = "8-37-38-46";
	teclado_especial = false;
	for ( var i in especiales) {
		if (key == especiales[i]) {
			teclado_especial = true;
		}
	}
	if (numeros.indexOf(teclado) == -1 && !teclado_especial) {
		return false;
	}
}

function soloLetras(e) {
    var key = e.keyCode || e.which,
      tecla = String.fromCharCode(key).toLowerCase(),
      letras = " áéíóúabcdefghijklmnñopqrstuvwxyz",
      especiales = [8, 37, 39, 46],
      tecla_especial = false;

    for (var i in especiales) {
      if (key == especiales[i]) {
        tecla_especial = true;
        break;
      }
    }

    if (letras.indexOf(tecla) == -1 && !tecla_especial) {
      return false;
    }
  }

function deshabilitarEnter(e) {
	var k = window.event ? window.event.keyCode : e.which;
	return (k != 13);
}

function exportar(tabla, nombre) {
	var table = document.getElementById(tabla);
	var sheet = XLSX.utils.table_to_sheet(table); // Convertir un objeto de
	// tabla en un objeto de
	// hoja
	openDownloadDialog(sheet2blob(sheet), nombre + '.xlsx'); // Nombre del
	// archivo
}



function sheet2blob(sheet, sheetName) {
	sheetName = sheetName || 'sheet1';
	var workbook = {
		SheetNames : [ sheetName ],
		Sheets : {}
	};
	workbook.Sheets[sheetName] = sheet; // Generar elementos de configuración de
	// Excel

	var wopts = {
		bookType : 'xlsx', // El tipo de archivo que se generará
		bookSST : false, // Ya sea para generar una tabla de cadenas
		// compartidas, la explicación oficial es que si
		// activa la velocidad de generación, disminuirá,
		// pero tiene una mejor compatibilidad en
		// dispositivos IOS inferiores
		type : 'binary'
	};
	var wbout = XLSX.write(workbook, wopts);
	var blob = new Blob([ s2ab(wbout) ], {
		// var blob = new Blob([ wbout ], {
		type : "application/octet-stream"
	}); // Cadena a ArrayBuffer

	function s2ab(s) {
		var buf = new ArrayBuffer(s.length);
		var view = new Uint8Array(buf);
		for (var i = 0; i != s.length; ++i)
			view[i] = s.charCodeAt(i) & 0xFF;
		return buf;
	}
	return blob;
}