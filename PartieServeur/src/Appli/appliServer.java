package Appli;

import java.io.IOException;

import Serveur.Mediatheque;
import Serveur.ServeurEmprunt;
import Serveur.ServeurReservation;
import Serveur.ServeurRetour;
import bibliotheque.Bibliotheque;

public class appliServer {

	public static void main(String[] args) {
		Bibliotheque bibliotheque = new Bibliotheque();
		bibliotheque.ajouterAbonne("Jean");
		bibliotheque.ajouterAbonne("Claude");
		bibliotheque.ajouterLivre("Luc va a la plage");
		bibliotheque.ajouterLivre("Comment r�ussir tous ses DST en 10 �tapes");
		Mediatheque mediatheque = bibliotheque;
		try {
			
			ServeurEmprunt serveurEmprunt = new ServeurEmprunt(mediatheque);
			ServeurReservation serveurReservation = new ServeurReservation(mediatheque);
			ServeurRetour serveurRetour = new ServeurRetour(mediatheque);
			serveurEmprunt.lancer();
			serveurReservation.lancer();
			serveurRetour.lancer();
			System.out.println("Le serveur a bien �t� lanc�");
		} catch (IOException e) {
			System.out.println("Une ou plusieurs sockets relatives"
					+ " aux serveurs n'ont pas pu �tre instanci�es");
			e.printStackTrace();
		}
	}
}
