package AutreSolutionReines.ReinesExtension;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.constraints.extension.Tuples;


public class nReinesExtensionVersion2 {

	public static void main(String[] args) {
		
		// Création du modele
		Model model = new Model("nReine");
		
		
		// Création des variables
		int nb_reine = 5;
		int nb_cases = 25;
		IntVar [] reines = model.intVarArray("Ri", nb_reine, 1, nb_cases);	//on considere ici les reines placer de la case 1 � la 25eme
		
		

		        
		        
		//Creation contraintes pour les reines sur la meme ligne et la m�me case
		int [][] tEqL = new int[][] {{1,1},{1,2},{1,3},{1,4},{1,5},{2,2},{2,3},{2,4},{2,5},{3,3},{3,4},{3,5},{4,4},{4,5},{5,5},
									{6,6},{6,7},{6,8},{6,9},{6,10},{7,7},{7,8},{7,9},{7,10},{8,8},{8,9},{8,10},{9,9},{9,10},{10,10},
									{11,11},{11,12},{11,13},{11,14},{11,15},{12,12},{12,13},{12,14},{12,15},{13,13},{13,14},{13,15},{14,14},{14,15}, {15,15},
									{16,16},{16,17},{16,18},{16,19},{16,20},{17,17},{17,18},{17,19},{17,20},{18,18},{18,19},{18,20},{19,19},{19,20}, {20,20},
									{21,21},{21,22},{21,23},{21,24},{21,25},{22,22},{22,23},{22,24},{22,25},{23,23},{23,24},{23,25},{24,24},{24,25}, {25,25}};
        Tuples tuplesInterditsLignes = new Tuples(tEqL,false);		// création de Tuples de valeurs interdits
        
        
        //Cretion contraintes pour les reines sur la meme colonne on voit ici qu'on a une difference de 5 cases entre chaque valeur pour mod�lis� la m�me colonne en changeant de ligne
      	int [][] tEqC = new int[][] {{1,6},{1,11},{1,16},{1,21},{6,11},{6,16},{6,21},{11,16},{11,21},{16,21},
      										{2,7},{2,12},{2,17},{2,22},{7,12},{7,17},{7,22},{12,17},{12,22},{17,22},
      										{3,8},{3,13},{3,18},{3,23},{8,13},{8,18},{8,23},{13,18},{13,23},{18,23},
      										{4,9},{4,14},{4,19},{4,24},{9,14},{9,19},{9,24},{14,19},{14,24},{19,24},
      										{5,10},{5,15},{5,20},{5,25},{10,15},{10,20},{10,25},{15,20},{15,25},{20,25}};
        Tuples tuplesInterditsCol = new Tuples(tEqC,false);		// création de Tuples de valeurs interdits
           
        
        //Creation contraintes pour les reines les memes diagos : 
        								//dans diago en bas � droite (difference de n+1 cases)
  		int [][] tEqD = new int[][] {{1,7},{1,13},{1,19},{1,25},{7,13},{7,19},{7,25},{13,19},{13,25},{19,25},
  										{6,12},{6,18},{6,24},{12,18},{12,24},{18,24},
  										{11,17},{11,23},{17,23},
  										{16,22},	
  										{2,8},{2,14},{2,20},{8,14},{8,20},{14,20},
  										{3,9},{3,15},{9,15},
  										{4,10},
  										//diago en bas � gauche (difference de n-1 cases)
  										{5,9},{5,13},{5,17},{5,21},{9,13},{9,17},{9,21},{13,17},{13,21},{17,21},
  										{10,14},{10,18},{10,22},{14,18},{14,22},{18,22},
  										{15,19},{15,23},{19,23},
  										{20,24},
  										{4,8},{4,12},{4,16},{8,12},{8,16},{12,16},
  										{3,7},{3,11},{7,11},		
  										{2,6}};
  											
          Tuples tuplesInterditsDiago = new Tuples(tEqD,false);		// création de Tuples de valeurs interdits
         
          
          //Creation contraintes pour les lignes Ri : chaque reine peut prendre que certains n� de cases
          
          int [][] tR0 = new int[][] {{1},{2},{3},{4},{5}};
          Tuples tuplesAutoriseR0 = new Tuples(tR0, true);
          
          int [][] tR1 = new int[][] {{6},{7},{8},{9},{10}};
          Tuples tuplesAutoriseR1 = new Tuples(tR1, true);
          
          int [][] tR2 = new int[][] {{11},{12},{13},{14},{15}};
          Tuples tuplesAutoriseR2 = new Tuples(tR2, true);
          
          int [][] tR3 = new int[][] {{16},{17},{18},{19},{20}};
          Tuples tuplesAutoriseR3 = new Tuples(tR3, true);
          
          int [][] tR4 = new int[][] {{21},{22},{23},{24},{25}};
          Tuples tuplesAutoriseR4 = new Tuples(tR4, true);
        
        /************************************************************************
         *                                                                      *
         *    Compléter en ajoutant les contraintes modélisant les contraintes  *
         *                                                                      *
         ************************************************************************/
        
        //Contrainte des lignes en fonction des reines
          model.table(new IntVar[]{reines[0]}, tuplesAutoriseR0).post();
          model.table(new IntVar[]{reines[1]}, tuplesAutoriseR1).post();
          model.table(new IntVar[]{reines[2]}, tuplesAutoriseR2).post();
          model.table(new IntVar[]{reines[3]}, tuplesAutoriseR3).post();
          model.table(new IntVar[]{reines[4]}, tuplesAutoriseR4).post();
          
          
        for (int i=0; i<5; i++) {
        	for (int j=i+1; j<5; j++) {
        		//pas les lignes
        		model.table(new IntVar[]{reines[i],reines[j]}, tuplesInterditsLignes).post();	//dans un sens
        		model.table(new IntVar[]{reines[j],reines[i]}, tuplesInterditsLignes).post();	//et dans l'autre
        		//pas les colonnes
        		model.table(new IntVar[]{reines[i],reines[j]}, tuplesInterditsCol).post();
        		model.table(new IntVar[]{reines[j],reines[i]}, tuplesInterditsCol).post();
        		//pas les diagos
        		model.table(new IntVar[]{reines[i],reines[i]}, tuplesInterditsDiago).post();
        		model.table(new IntVar[]{reines[i],reines[j]}, tuplesInterditsDiago).post();
        		model.table(new IntVar[]{reines[j],reines[i]}, tuplesInterditsDiago).post();
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
