import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.extension.Tuples;
import org.chocosolver.solver.variables.IntVar;

public class Expe {

	private static Model lireReseau(BufferedReader in) throws Exception{
			Model model = new Model("Expe");
			// System.out.println(in.readLine());
			int nbVariables = Integer.parseInt(in.readLine());				// le nombre de variables
			int tailleDom = Integer.parseInt(in.readLine());				// la valeur max des domaines
			IntVar []var = model.intVarArray("x",nbVariables,0,tailleDom-1); 	
			int nbConstraints = Integer.parseInt(in.readLine());			// le nombre de contraintes binaires		
			for(int k=1;k<=nbConstraints;k++) { 
				String chaine[] = in.readLine().split(";");
				IntVar portee[] = new IntVar[]{var[Integer.parseInt(chaine[0])],var[Integer.parseInt(chaine[1])]}; 
				int nbTuples = Integer.parseInt(in.readLine());				// le nombre de tuples		
				Tuples tuples = new Tuples(new int[][]{},true);
				for(int nb=1;nb<=nbTuples;nb++) { 
					chaine = in.readLine().split(";");
					int t[] = new int[]{Integer.parseInt(chaine[0]), Integer.parseInt(chaine[1])};
					tuples.add(t);
				}
				model.table(portee,tuples).post();	
			}
			in.readLine();
			return model;
	}	
		
			
	public static void main(String[] args) throws Exception{
		// écriture dans le fichier .CSV
	    File fichier = new File("./result.csv");
	    FileWriter fichier_resultats;
	    fichier_resultats = new FileWriter("result.csv",false);
    	fichier_resultats.write("sep=;\n");
    	fichier_resultats.write("Toughness;Winrate\n");
	    
	    // parsing des réseaux
		File reseaux = new File("reseaux/"); // fichiers des reseaux
		File ficNames[] = reseaux.listFiles(); // liste des fichiers
		int nbDiff = ficNames.length; // nb de fichiers
		int nbRes=10;
		for (int i=0; i<nbDiff; i++) { // pour chaque réseau
			String fic = ficNames[i].getName();
			BufferedReader readFile = new BufferedReader(new FileReader("reseaux/"+fic));
			System.out.println("\n" + fic + " :\n");
			int nbSoluce = 0; // nombre de reseaux avec au moins une solution
			for(int nb=1 ; nb<=nbRes; nb++) { // pour chaque reseau
				Model model=lireReseau(readFile);
				if(model==null) { // si ça bug oops
					System.out.println("Problème de lecture de fichier !\n");
					return;
				}
				System.out.println("Réseau lu "+nb);
				if (model.getSolver().solve()) { //si le modèle à au moins une solution
					System.out.println("Solution trouvée\n");
					nbSoluce++;
				}
				else {System.out.println("Pas de solution\n");}
			}
		    
		    String donnees = fic + ";" + 100*nbSoluce/nbRes + "%";
		    System.out.println(donnees);
		    fichier_resultats.write(donnees+"\n");
		    
		}
		fichier_resultats.close();
		return;	
	}
	
}
