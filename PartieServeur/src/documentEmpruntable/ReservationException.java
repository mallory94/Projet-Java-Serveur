package documentEmpruntable;

import bibliotheque.Abonne;

public class ReservationException extends Exception {
	private static final long serialVersionUID = 1L;
	private Abonne aboVoulantEmprunter;
	private DocumentEmpruntable doc;
	
	public ReservationException() {
	}
	
	public ReservationException(Abonne aboVoulantEmprunter, DocumentEmpruntable doc) {
		super();
		this.aboVoulantEmprunter = aboVoulantEmprunter;
		this.doc = doc;
	}
	
	//renvoit le nom de l'abonne souhaitant Emprunter
	public Abonne getAboVoulantEmprunter() {
		return aboVoulantEmprunter;
	}
	
	public DocumentEmpruntable getDocConcerne() {
		return doc;
	}
}
