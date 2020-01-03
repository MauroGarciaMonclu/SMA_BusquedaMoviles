package SMA_BusquedaMoviles.dominio;

import jade.core.Agent;
import jade.core.AID;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ScrapCarrefour extends Agent {

	public class Manejador extends AchieveREResponder {

		public Manejador(Agent agente, MessageTemplate plantilla) {
			super(agente,plantilla);
		}

		protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException{
			try {
				String movil = request.getContent();
				if(!movil.equalsIgnoreCase("")){
					ACLMessage agree = request.createReply();
					agree.setPerformative(ACLMessage.AGREE);
					return agree;
				}
				else{
					throw new NotUnderstoodException(getLocalName() + ": no se ha indicado el movil a buscar");
				}
			} catch (Exception e) {
				throw new RefuseException(getLocalName() + ": no se ha podido conectar");
			}
		}
		protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) throws FailureException{
			List<Movil> todos = scrapCarrefour(request.getContent());
			if(todos.size()==0){
				throw new FailureException(getLocalName() + ": no se ha podido encontrar ningun movil");
			}
			else{
				ACLMessage inform = request.createReply();
				inform.setPerformative(ACLMessage.INFORM);
				try{
					inform.setContentObject((Serializable)todos);
				}catch(Exception e){
					System.out.println(getLocalName() + ": error en lectura del mensaje con excepcion: " + e.toString());
				}
				return inform;
			}
		}
	}

	public List<Movil> scrapCarrefour(String nombre_movil) {
		String baseUrl = "https://www.carrefour.es/global/?Dy=1&Nty=1&Ntx=mode+matchallany&Ntt=";
		@SuppressWarnings("deprecation")
		String urlPage = baseUrl + URLEncoder.encode(nombre_movil) + "&search=Buscar";
		List<Movil> listmoviles = new ArrayList<>();

		System.out.println(getLocalName() + ": comprobando entradas de: " + urlPage);

		if (ScrapFunciones.getStatusConnectionCode(urlPage) == 200) {

			Document document = ScrapFunciones.getHtmlDocument(urlPage);
			Elements entradas = document.select("div.col-sm-6");

			Pattern patron = Pattern.compile("(.*Funda.*)|(.*Protection.*)|(.*Silicona.*)|(.*Carcasa.*)|(.*Protector.*)|(.*Cristal.*)|(.*Carcas.*)|(.*Pulsera.*)|(.*Pantalla.*)");

			for (Element elem : entradas) {
				String precio_string = elem.select("p.precio-nuevo").text();

				if ((precio_string != null) && (!precio_string.isEmpty())) {
					if (precio_string.contains(".")) {
						precio_string = precio_string.replace(".", "");
					}
					double precio = Double.parseDouble(precio_string.replaceAll("\u20ac", "").replace(",", "."));

					String nombre = elem.select("a.track-click").text();

					Matcher igual = patron.matcher(nombre);

					if (!igual.matches()) {

						if (ScrapFunciones.AnadirMovil(nombre, nombre_movil)) {
							Movil movil = new Movil(nombre, precio, "Carrefour");
							listmoviles.add(movil);
						}
					}
				}
			}

		} else {
			System.out.println(getLocalName() + ": el Status Code no es OK es: " + ScrapFunciones.getStatusConnectionCode(urlPage));
		}
		return listmoviles;

	}

	protected void setup() {
		MessageTemplate protocol = MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		MessageTemplate performative = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
		MessageTemplate plantilla = MessageTemplate.and(protocol,performative);

		System.out.println(getLocalName() + ": esperando solicitud del buscador");

		addBehaviour(new Manejador(this,plantilla));
	}
}

