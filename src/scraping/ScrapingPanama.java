package scraping;

import java.io.IOException;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Attr;



public class ScrapingPanama {

	public static final String xmlFilePath = "xmlfile.xml";

	public static void main(String[] args) throws ParserConfigurationException, TransformerConfigurationException {
		// TODO Auto-generated method stub

		String url = "http://visas.migracion.gob.pa/SIVA/verif_citas/";
		String minutos = args[0];
		String email = args[1];
		int minutosInt = Integer.parseInt(minutos);
		Date Inicio = new java.util.Date();
		Date utilDate = new java.util.Date();
		int diferencia;
		System.out.println("Info cada "+minutos+" minutos");
		System.out.println("Info a email "+email);
		
		Report rep = new Report();

		while (true) {
			if (getStatusConnectionCode(url) == 200) {
				Document documento = getHtmlDocument(url);
				utilDate = new java.util.Date();
				Elements elementos = documento.select("a");
				if(elementos.size() > 1) {
					rep.sendMail(email,"Alerta aparecio el Link");
				}
				diferencia=(int) ((utilDate.getTime()-Inicio.getTime()));
				System.out.print(elementos.size());
				System.out.println("-"+utilDate.toString());
				if(diferencia > 3600000) {
					rep.sendMail(email,"Funcionando sin novedad");
					Inicio = utilDate;
				}
				
				
			} else {
				System.out.println("no conectado");
			}
			
			try {
				Thread.sleep(60000*minutosInt);
				//Thread.sleep(10000*minutosInt);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * Con esta método compruebo el Status code de la respuesta que recibo al hacer
	 * la petición EJM: 200 OK 300 Multiple Choices 301 Moved Permanently 305 Use
	 * Proxy 400 Bad Request 403 Forbidden 404 Not Found 500 Internal Server Error
	 * 502 Bad Gateway 503 Service Unavailable
	 * 
	 * @param url
	 * @return Status Code
	 */
	public static int getStatusConnectionCode(String url) {

		Response response = null;

		try {
			response = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).ignoreHttpErrors(true).execute();
		} catch (IOException ex) {
			System.out.println("Excepción al obtener el Status Code: " + ex.getMessage());
		}
		return response.statusCode();
	}

	/**
	 * Con este método devuelvo un objeto de la clase Document con el contenido del
	 * HTML de la web que me permitirá parsearlo con los métodos de la librelia
	 * JSoup
	 * 
	 * @param url
	 * @return Documento con el HTML
	 */
	public static Document getHtmlDocument(String url) {

		Document doc = null;
		try {
			doc = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).get();
		} catch (IOException ex) {
			System.out.println("Excepción al obtener el HTML de la página" + ex.getMessage());
		}
		return doc;
	}
}
