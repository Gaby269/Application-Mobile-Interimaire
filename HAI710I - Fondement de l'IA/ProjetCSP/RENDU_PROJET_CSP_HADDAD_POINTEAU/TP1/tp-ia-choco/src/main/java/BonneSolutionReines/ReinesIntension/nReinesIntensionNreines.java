package BonneSolutionReines.ReinesIntension;
import java.util.Scanner;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
//import org.chocosolver.solver.constraints.extension.Tuples;


public class nReinesIntensionNreines{

	public static void main(String[] args) {
		
		// Cr√©ation du modele
		Model model = new Model("nReine");
		
		//Donner un entier pour faire le nombre de reines : 
		System.out.println("Veuillez entr√©e un nombre de reines : ");		//je demande a ce qu'on entre une solution
		@SuppressWarnings("resource")
		Scanner clavier = new Scanner(System.in);							//je recup√®re la valeur
		int n = clavier.nextInt();											//je le convertie en entier
		
		// Cr√©ation des variables
		int nbReine = n;
		int nbCases = n;
		//on considere ici les reines ‡ la ligne i placer dans les colonnes numerotÈ de 1 ‡ n
		IntVar [] reines = model.intVarArray("Ri", nbReine, 1, nbCases);	
		
		
        
        /************************************************************************
         *                                                                      *
         *    Compl√©ter en ajoutant les contraintes mod√©lisant les contraintes  *
         *                                                                      *
         ************************************************************************/

		
		//Contraintes d'egalite : les reines ne doivent pas Ítre sur la mÍme colonne donc qu'elle soit Ègal
		model.allDifferent(reines).post();
		
			
		
		
		//contraintes diago
		//a droite
		for (int i=0; i<n; i++) {		//la reines ‡ la ligne i
			for (int j=i+1; j<n; j++) {	//la reine ‡ la ligne j
				//Pour ne pas utiliser les valeurs absolue on choisis de tester tous les cas possible selon i et j
				model.arithm(reines[j], "-", reines[i], "!=", j-i).post();
				model.arithm(reines[j], "-", reines[i], "!=", i-j).post();
				model.arithm(reines[i], "-", reines[j], "!=", j-i).post();
				model.arithm(reines[i], "-", reines[j], "!=", i-j).post();
			}
		}
		
		
		
		
		
		
        // Affichage du r√©seau de contraintes cr√©√©
        System.out.println("*** R√©seau Initial ***");
        System.out.println("Model : " + model);
        

        // Calcul de la premi√®re solution
        if(model.getSolver().solve()) {
        	System.out.println("\n\n*** Premi√®re solution ***");        
        	System.out.println("Sol "+ model.getSolver().getSolutionCount()+"\n"+model);
        }

        
   
    	// Calcul de toutes les solutions
    	System.out.println("\n\n*** Autres solutions *** (non affich√©)");        
        while(model.getSolver().solve()) {    	
            //System.out.println("Sol "+ model.getSolver().getSolutionCount()+"\n"+model);
	    }
	    
 
        
        // Affichage de l'ensemble des caract√©ristiques de r√©solution
      	System.out.println("\n\n*** Bilan ***");        
        model.getSolver().printStatistics();
	}
}
