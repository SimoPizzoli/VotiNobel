package it.polito.tdp.nobel.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {

	private List<Esame> esami;
	private Set<Esame> migliore;
	private double mediaMigliore;
	
	public Model() {
		EsameDAO esameDao = new EsameDAO();
		this.esami = esameDao.getTuttiEsami();
	}
	
	public Set<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
	//	ripristino soluzione migliore
		migliore = new HashSet<Esame>();
		mediaMigliore = 0;
		
		Set<Esame> parziale = new HashSet<Esame>();
	//	cerca1(0, numeroCrediti, parziale);
		cerca2(0, numeroCrediti, parziale);
		return migliore;	
	}
	
	/*
	 *  COMPLESSITA' DI QUESTO ALGORITMO E' : N! (fattoriale)
	 */
						 //L = livello
	private void cerca1(int L, int numeroCrediti, Set<Esame> parziale) {
		// Controllare i casi terminali (altrimenti la ricorsione continua all'infinito)
		
		int sommaCrediti = sommaCrediti(parziale);
		if(sommaCrediti > numeroCrediti)
			return;
		if(sommaCrediti == numeroCrediti) {
			//soluzione valida, controlliamo se è la migliore (fino a qui)
			double mediaVoti = calcolaMedia(parziale);
			if(mediaVoti > this.mediaMigliore) {
				mediaMigliore = mediaVoti;
				migliore = new HashSet<Esame>(parziale);	//non voglio il riferimento ma una copia
			}
			return;
		}
		
		//se arriviamo qui, sicuramente i crediti sono < numeroCrediti
		//non è ancora una soluzione migliore, potrebbe diventarla però
		if(L == this.esami.size())
			return;	//me ne vado perché non può essere una soluzione migliore, i crediti sono < di numeroCrediti e non ci sono altri esami da poter aggiungere
		
		//altrimenti generiamo i sotto-problemi
		for(Esame e : esami) {
			if(!parziale.contains(e)) {
				parziale.add(e);
				cerca1(L+1,numeroCrediti,parziale);
				parziale.remove(e);	//è possibile fare backtracking in questo modo perché stiamo lavorando con un Set e quindi non ci sono copie
									//se avessimo lavorato con le liste invece, essa può contenere doppioni e quindi si sarebbe dovuto togliere l'ultimo elemento
				//parziale.remove(parziale.get(L-1));	se fosse stata una lista (non sono sicuro fosse così)
			}
		}
	}
	
	private void cerca2(int L, int numeroCrediti, Set<Esame> parziale) {
		
		int sommaCrediti = sommaCrediti(parziale);
		if(sommaCrediti > numeroCrediti)
			return;
		if(sommaCrediti == numeroCrediti) {
			double mediaVoti = calcolaMedia(parziale);
			if(mediaVoti > this.mediaMigliore) {
				mediaMigliore = mediaVoti;
				migliore = new HashSet<Esame>(parziale);	
				}
			return;
		}
		
		if(L == this.esami.size())
			return;
		
		//provo ad aggiungere esami[L]
		parziale.add(esami.get(L));	//ci serve una lista per fare questo
		cerca2(L+1, numeroCrediti, parziale);
		
		//provo a "non aggiungere" esami[L]
		parziale.remove(esami.get(L));
		cerca2(L+1, numeroCrediti, parziale);

	}
	
	public double calcolaMedia(Set<Esame> esami) {
		
		int crediti = 0;
		int somma = 0;
		
		for(Esame e : esami){
			crediti += e.getCrediti();
			somma += (e.getVoto() * e.getCrediti());
		}
		
		return somma/crediti;
	}
	
	public int sommaCrediti(Set<Esame> esami) {
		int somma = 0;
		
		for(Esame e : esami)
			somma += e.getCrediti();
		
		return somma;
	}

}
