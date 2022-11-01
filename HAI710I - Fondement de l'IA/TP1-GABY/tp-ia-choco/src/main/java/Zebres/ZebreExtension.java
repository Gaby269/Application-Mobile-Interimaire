package Zebres;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.constraints.extension.Tuples;


public class ZebreExtension {

	public static void main(String[] args) {
		
		// Cr�ation du modele
		Model model = new Model("Zebre");
		
		
		// Cr�ation des variables
		IntVar blu = model.intVar("Blue", 1, 5);	// blu est une variable enti�re dont le nom est "Blue" est le domaine [1,5]
		IntVar gre = model.intVar("Green", 1, 5); 
		IntVar ivo = model.intVar("Ivory", 1, 5);         
		IntVar red = model.intVar("Red", 1, 5);         
		IntVar yel = model.intVar("Yellow", 1, 5);   
		
		IntVar eng = model.intVar("English", 1, 5);         
		IntVar jap = model.intVar("Japanese", 1, 5);    
		//IntVar nor = model.intVar("Norwegian", 1, 5); 
		IntVar nor = model.intVar("Norwegian", 1, 1);         	//modification du domaine pour une contrainte
		IntVar spa = model.intVar("Spanish", 1, 5);         
		IntVar ukr = model.intVar("Ukrainian", 1, 5);         
		
		IntVar cof = model.intVar("Coffee", 1, 5); 
		//IntVar mil = model.intVar("Milk", 1, 5);  
		IntVar mil = model.intVar("Milk", 3, 3);         		//modification du domaine pour une contrainte
		IntVar ora = model.intVar("Orange Juice", 1, 5);         
		IntVar tea = model.intVar("Tea", 1, 5);         
		IntVar wat = model.intVar("Water", 1, 5);         
		
	    IntVar dog = model.intVar("Dog", 1, 5);         
	    IntVar fox = model.intVar("Fox", 1, 5);         
	    IntVar hor = model.intVar("Horse", 1, 5);         
	    IntVar sna = model.intVar("Snail", 1, 5);         
	    IntVar zeb = model.intVar("Zebra", 1, 5);         
	    
	    IntVar che = model.intVar("Chesterfield", 1, 5);         
	    IntVar koo = model.intVar("Kool", 1, 5);         
	    IntVar luc = model.intVar("Lucky Strike", 1, 5);         
	    IntVar old = model.intVar("Old Gold", 1, 5);         
	    IntVar par = model.intVar("Parliament", 1, 5);         

	    
	    // Cr�ation des contraintes �galit�s
        int [][] tEq = new int[][] {{1,1},{2,2},{3,3},{4,4},{5,5}};
        Tuples tuplesAutorises = new Tuples(tEq,true);		// cr�ation de Tuples de valeurs autoris�s
        Tuples tuplesInterdits = new Tuples(tEq,false);		// cr�ation de Tuples de valeurs interdits
        
        // Cr�ation des contraintes +1
        int [][] tEq2 = new int[][] {{1,2},{2,3},{3,4},{4,5}};
        Tuples tuplesAutorises2 = new Tuples(tEq2,true);		// cr�ation de Tuples de valeurs autoris�s
        //Tuples tuplesInterdits2 = new Tuples(tEq2,false);		// cr�ation de Tuples de valeurs interdits
        
        // Cr�ation des contraintes -1 ou +1
        int [][] tEq3 = new int[][] {{1,2},{2,1},{2,3}, {3,2},{3,4},{4,3}, {5,4}, {4,5}};
        Tuples tuplesAutorises3 = new Tuples(tEq3,true);		// cr�ation de Tuples de valeurs autoris�s
        //Tuples tuplesInterdits2 = new Tuples(tEq2,false);		// cr�ation de Tuples de valeurs interdits
        
        
        
        model.table(new IntVar[]{blu,gre}, tuplesInterdits).post();
        // cr�ation d'une contrainte en extension de port�e <blu,gre>
        // dont les tuples autoris�s/interdits sont donn�es par tuplesInterdits
        model.table(new IntVar[]{blu,ivo}, tuplesInterdits).post();
        model.table(new IntVar[]{blu,red}, tuplesInterdits).post();
        model.table(new IntVar[]{blu,yel}, tuplesInterdits).post();
        model.table(new IntVar[]{gre,ivo}, tuplesInterdits).post();
        model.table(new IntVar[]{gre,red}, tuplesInterdits).post();
        model.table(new IntVar[]{gre,yel}, tuplesInterdits).post();
        model.table(new IntVar[]{ivo,red}, tuplesInterdits).post();
        model.table(new IntVar[]{ivo,yel}, tuplesInterdits).post();
        model.table(new IntVar[]{red,yel}, tuplesInterdits).post();

        model.table(new IntVar[]{eng,jap}, tuplesInterdits).post();
        model.table(new IntVar[]{eng,nor}, tuplesInterdits).post();
        model.table(new IntVar[]{eng,spa}, tuplesInterdits).post();
        model.table(new IntVar[]{eng,ukr}, tuplesInterdits).post();
        model.table(new IntVar[]{jap,nor}, tuplesInterdits).post();
        model.table(new IntVar[]{jap,spa}, tuplesInterdits).post();
        model.table(new IntVar[]{jap,ukr}, tuplesInterdits).post();
        model.table(new IntVar[]{nor,spa}, tuplesInterdits).post();
        model.table(new IntVar[]{nor,ukr}, tuplesInterdits).post();
        model.table(new IntVar[]{spa,ukr}, tuplesInterdits).post();

        model.table(new IntVar[]{cof,mil}, tuplesInterdits).post();
        model.table(new IntVar[]{cof,ora}, tuplesInterdits).post();
        model.table(new IntVar[]{cof,tea}, tuplesInterdits).post();
        model.table(new IntVar[]{cof,wat}, tuplesInterdits).post();
        model.table(new IntVar[]{mil,ora}, tuplesInterdits).post();
        model.table(new IntVar[]{mil,tea}, tuplesInterdits).post();
        model.table(new IntVar[]{mil,wat}, tuplesInterdits).post();
        model.table(new IntVar[]{ora,tea}, tuplesInterdits).post();
        model.table(new IntVar[]{ora,wat}, tuplesInterdits).post();
        model.table(new IntVar[]{tea,wat}, tuplesInterdits).post();

        model.table(new IntVar[]{dog,fox}, tuplesInterdits).post();
        model.table(new IntVar[]{dog,hor}, tuplesInterdits).post();
        model.table(new IntVar[]{dog,sna}, tuplesInterdits).post();
        model.table(new IntVar[]{dog,zeb}, tuplesInterdits).post();
        model.table(new IntVar[]{fox,hor}, tuplesInterdits).post();
        model.table(new IntVar[]{fox,sna}, tuplesInterdits).post();
        model.table(new IntVar[]{fox,zeb}, tuplesInterdits).post();
        model.table(new IntVar[]{hor,sna}, tuplesInterdits).post();
        model.table(new IntVar[]{hor,zeb}, tuplesInterdits).post();
        model.table(new IntVar[]{sna,zeb}, tuplesInterdits).post();

        model.table(new IntVar[]{che,koo}, tuplesInterdits).post();
        model.table(new IntVar[]{che,luc}, tuplesInterdits).post();
        model.table(new IntVar[]{che,old}, tuplesInterdits).post();
        model.table(new IntVar[]{che,par}, tuplesInterdits).post();
        model.table(new IntVar[]{koo,luc}, tuplesInterdits).post();
        model.table(new IntVar[]{koo,old}, tuplesInterdits).post();
        model.table(new IntVar[]{koo,par}, tuplesInterdits).post();
        model.table(new IntVar[]{luc,old}, tuplesInterdits).post();
        model.table(new IntVar[]{luc,par}, tuplesInterdits).post();
        model.table(new IntVar[]{old,par}, tuplesInterdits).post();

        
        /************************************************************************
         *                                                                      *
         * Compl�ter en ajoutant les contraintes mod�lisant les phrases 2 � 15  *
         *                                                                      *
         ************************************************************************/
        
        //PHRASE 2 : anglais vie dans la maison rouge
        model.table(new IntVar[]{eng,red}, tuplesAutorises).post();	//post active contrainte
        
        //PHRASE 3 : espagnol a le chien
        model.table(new IntVar[]{spa,dog}, tuplesAutorises).post();
        
        //PHRASE 4 : caf� dans la maison verte
        model.table(new IntVar[]{cof,gre}, tuplesAutorises).post();
        
        //PHRASE 5 : ukranien boit du tea
        model.table(new IntVar[]{ukr,tea}, tuplesAutorises).post();
        
        //PHRASE 6 : maison verte juste � la droite de la maison ivoirienne
        model.table(new IntVar[]{ivo, gre}, tuplesAutorises2).post();
        
        
        //PHRASE 7 : le old gold fume le snails
        model.table(new IntVar[]{old,sna}, tuplesAutorises).post();
        
        //PHRASE 8 : kools smocke dans maison jaune
        model.table(new IntVar[]{koo, yel}, tuplesAutorises).post();
        
        //PHRASE 9 : lait dans la maison du milieu
        //modif le domaine
        
        //PHRASE 10 : le norvegien fit dans la premi�re maison
        //modif le domaine
        
        //PHRASE 11 : chesterfileds vie dans la maison apres la maison avec le fox
        model.table(new IntVar[]{che, fox}, tuplesAutorises3).post();
        
        //PHRASE 12 : kools smook dans la maison apres la ou ya le cheval
        model.table(new IntVar[]{koo, hor}, tuplesAutorises3).post();
        
        //PHRASE 13 : lucky strike boit du jus dorange
        model.table(new IntVar[]{luc,ora}, tuplesAutorises).post();
        
        //PHRASE 14 : japon smocke parliaments
        model.table(new IntVar[]{jap,par}, tuplesAutorises).post();
        
        //PHRASE 15 : norvegien vie apres la maison bleu
        model.table(new IntVar[]{nor, blu}, tuplesAutorises3).post();
        
        
        
        
        
        // Affichage du r�seau de contraintes cr��
        System.out.println("*** R�seau Initial ***");
        System.out.println(model);
        

        // Calcul de la premi�re solution
        if(model.getSolver().solve()) {
        	System.out.println("\n\n*** Premi�re solution ***");        
        	System.out.println(model);
        }

        
   
    	// Calcul de toutes les solutions
    	System.out.println("\n\n*** Autres solutions ***");        
        while(model.getSolver().solve()) {    	
            System.out.println("Sol "+ model.getSolver().getSolutionCount()+"\n"+model);
	    }
	    
 
        
        // Affichage de l'ensemble des caract�ristiques de r�solution
      	System.out.println("\n\n*** Bilan ***");        
        model.getSolver().printStatistics();
	}
}
