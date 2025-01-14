package empruntAvecSanction;

import java.util.HashMap;
import bibliotheque.Abonne;
import bibliotheque.Document;
import bibliotheque.EmpruntException;
import bibliotheque.RetourException;
import documentEmpruntable.DocumentEmpruntable;
import documentEmpruntable.ReservationException;

public class DocumentAvecEmpruntSanctionnable extends DocumentEmpruntable implements Document{
	private static HashMap<Abonne, MinuteurInterdictionDemprunt> abonnesInterditsDemprunt =
			new HashMap<Abonne, MinuteurInterdictionDemprunt>();
	private EtatDegradation etatDegradation;
	private Abonne emprunteur;
	private boolean enRetard = false;
	private MinuteurRetard minuteurRetard = null;
	
	
	public DocumentAvecEmpruntSanctionnable(String nom) {
		super(nom);
	}
	
	@Override
	public void reserver(Abonne ab) throws EmpruntException {
		if (!ParamAboEstInterdit(ab)) {
			super.reserver(ab);
		}
		else {
			throw new EmpruntException(new InterditDempruntException(
					new ReservationException() , ab, this.getNumero()));
		}
		
	}
	
	@Override
	public void emprunter(Abonne ab) throws EmpruntException  {
		int dureeEmpruntStandarde = 1;
		if (!ParamAboEstInterdit(ab)) {
			super.emprunter(ab);
			emprunteur = ab;
			minuteurRetard = new MinuteurRetard(ab,this, dureeEmpruntStandarde);
		}
		else {
			throw new EmpruntException(new InterditDempruntException(ab, this.getNumero()));
		}
	}
	
	@Override
	public void retour() throws RetourException {
		try {
			minuteurRetard.annuler();
		}
		catch (NullPointerException e) {
		}
		minuteurRetard = null;
		Abonne abonneRetourneur = getEmprunteur();
		int nombreAlea = (int) (Math.random() * ( 3 - 0 ) + 1);
		if (nombreAlea == 1) {
			this.degrade();
		}
		if (super.estEmprunte() && (this.aSubiDegradation() || this.enRetard)) {
				sanctionnerEmprunteur("d�gradation de document");
		}
		super.retour();
		emprunteur = null;
		if (this.aSubiDegradation()) {
			throw new RetourException(
					new InterditDempruntException("d�grad�", abonneRetourneur, this)
					);
		}
		if (this.enRetard) {
			throw new RetourException( new InterditDempruntException("en retard", abonneRetourneur, this));
		}
		setEnRetard(false);
	}

	public boolean aSubiDegradation() {
		return(etatDegradation == EtatDegradation.DEGRADE);
	}
	
	public synchronized Abonne getEmprunteur() {
		return(emprunteur);
	}
	
	
	public void sanctionnerEmprunteur(String motif) {
		int sanctionStandarde = 30;
		synchronized (abonnesInterditsDemprunt) {
			abonnesInterditsDemprunt.put(getEmprunteur(), new MinuteurInterdictionDemprunt(getEmprunteur(), 
					motif, sanctionStandarde));
		}
	}
	
	public static void retirerInterdictionAbonne(Abonne ab) {
		synchronized (abonnesInterditsDemprunt) {
			abonnesInterditsDemprunt.remove(ab);
		}
	}
	
	//renvoit vrai si l'abonne en param�tre est interdit d'emprunt
	public static boolean ParamAboEstInterdit(Abonne ab) {
		boolean estInterdit = false;
		synchronized (abonnesInterditsDemprunt) {
			estInterdit = abonnesInterditsDemprunt.containsKey(ab);
		}
		return(estInterdit);
	}
	
	public void reparerDocument() {
		synchronized (this) {
			etatDegradation = EtatDegradation.NONDEGRADE;
		}
	}
	
	public void degrade() {
		synchronized (this) {
			etatDegradation = EtatDegradation.DEGRADE;
		}
	}
	
	public synchronized void setEnRetard(boolean enRetard) {
		this.enRetard = enRetard; 
	}
}
