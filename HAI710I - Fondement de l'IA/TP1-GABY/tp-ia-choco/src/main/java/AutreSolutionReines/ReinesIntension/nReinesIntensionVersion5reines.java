package AutreSolutionReines.ReinesIntension;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
//import org.chocosolver.solver.constraints.extension.Tuples;


public class nReinesIntensionVersion5reines{

	public static void main(String[] args) {
		
		// Création du modele
		Model model = new Model("nReine");
		
		
		// Création des variables
		int nbReine = 5;	//on fixe le nombre de reines � 5
		int nbCases = 25;	//on fixe le nombre de case de l'echiquier
		//ainsi reines[i] = "n�case de l'echiquier" (voir pdf)
		//on considere ici les reines placer de la case 1 a la 25eme case
		IntVar [] reines = model.intVarArray("Ri", nbReine, 1, nbCases);	
		
		
        
        /************************************************************************
         *                                                                      *
         *    Compléter en ajoutant les contraintes modélisant les contraintes  *
         *                                                                      *
         ************************************************************************/

		
		//Contraintes d'egalite entre les reines elles ne doivent pas �tre sur la m�me case (contrainte pas tr�s utile mais qui utilise allDifferent):
		model.allDifferent(new IntVar[]{reines[0],reines[1],reines[2],reines[3],reines[4]}).post();
		
		//Contraintes pour que Ri soit a la ligne i
		model.arithm(reines[0], ">=", 1).post();
		model.arithm(reines[0], "<=", 5).post();
		
		model.arithm(reines[1], ">=", 6).post();
		model.arithm(reines[1], "<=", 10).post();
		
		model.arithm(reines[2], ">=", 11).post();
		model.arithm(reines[2], "<=", 15).post();
		
		model.arithm(reines[3], ">=", 16).post();
		model.arithm(reines[3], "<=", 20).post();
		
		model.arithm(reines[4], ">=", 21).post();
		model.arithm(reines[4], "<=", 25).post();
		
		
		
		
		
		
		//Contraintes de colonnes la reine i ne doit pas etre sur la m�me colonne que la reine j
		for (int i=0; i<5; i++) {				//on prend une reine � la ligne i
			for (int j=i+1; j<5; j++) {			//on prend une reine � la ligne j
				for (int k=5; k<21; k=k+5) {	//on parcours les lignes en parcourant par 5 les cases (voir fichier pdf pour l'explication)
					//on dit donc que le numero de la case de la reine � la ligne j ne doit pas �tre �gal au numero � la case de la reine � la ligne i + k 
					//(ou k est le compteur de 5 en 5 pour modeliser le saut � la ligne suivante)
					model.arithm(reines[j], "!=", reines[i], "+", k).post();
				}
			}
		}
		
		
		
		//contraintes diago
		//a droite
		for (int i=0; i<5; i++) {
			int k=6;			//on a un compteur pour les diagonales en bas � droite qui va parcourir de n+1 en n+1 ici n=5
			int t=4;			//et un compteur pour les diagonales en bas � gauche qui va parcourir de n-1 en n-1 ici n=5
			for (int j=i+1; j<5; j++) {		//on regarde la ligne j
				//et on compare pour les deux diagonales
				model.arithm(reines[j], "!=", reines[i], "+", k).post();	
				model.arithm(reines[j], "!=", reines[i], "+", t).post();
				//et on incremete pour passer � la ligne suivante (diagonale de la ligne suivante)
				k+=6;		
				t+=4;
			}
		}
		
		
		
		
		
		
        // Affichage du réseau de contraintes créé
        System.out.println("*** Réseau Initial ***");
        System.out.println(model);
        

        // Calcul de la première solution
        if(model.getSolver().solve()) {
        	System.out.println("\n\n*** Première solution ***");        
        	System.out.println(model);
        }

        
   
    	// Calcul de toutes les solutions
    	System.out.println("\n\n*** Autres solutions ***");        
        while(model.getSolver().solve()) {    	
            System.out.println("Sol "+ model.getSolver().getSolutionCount()+"\n"+model);
	    }
	    
 
        
        // Affichage de l'ensemble des caractéristiques de résolution
      	System.out.println("\n\n*** Bilan ***");        
        model.getSolver().printStatistics();
	}
}
