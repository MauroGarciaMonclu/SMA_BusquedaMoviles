package SMA_BusquedaMoviles.dominio;

import java.io.IOException;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ScrapFunciones {

	public static boolean AnadirMovil(String nombre, String nombre_movil) {
		boolean anadir = false;

		String nombreMinus = nombre.toLowerCase();
		String[] listNombre = nombreMinus.split(" ");

		String nombreBusquedaMinus = nombre_movil.toLowerCase();
		String[] listNombreBusqueda = nombreBusquedaMinus.split(" ");

		boolean igual = true;
		for (int i = 0; i < listNombreBusqueda.length && igual; i++) {
			igual = false;
			for (int j = 0; j < listNombre.length; j++) {
				if (listNombre[j].contains(",")) {
					if (listNombreBusqueda[i].equals(listNombre[j].replace(",", ""))) {
						igual = true;
					}
				} else {
					if (listNombreBusqueda[i].equals(listNombre[j])) {
						igual = true;
					}
				}
			}
			if (i == listNombreBusqueda.length - 1 && igual) {
				anadir = true;
			}
		}
		return anadir;
	}

	public static int getStatusConnectionCode(String url) {
		Response response = null;

		try {
			response = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).ignoreHttpErrors(true).execute();
		} catch (IOException ex) {
			System.out.println("Excepcion al obtener el Status Code: " + ex.getMessage());
		}
		return response.statusCode();
	}

	public static Document getHtmlDocument(String url) {
		Document doc = null;

		try {
			doc = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).get();
		} catch (IOException ex) {
			System.out.println("Excepcion al obtener el HTML de la pagina" + ex.getMessage());
		}

		return doc;
	}
}

