// solveur
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.extension.Tuples;
import org.chocosolver.solver.variables.IntVar;
// Ecriture de fichiers
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
// time
// import java.lang.management.ManagementFactory;
// import java.lang.management.ThreadMXBean;
// import java.util.Scanner; // Scanner pour les entrées
import java.time.LocalTime; // Current time

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
		long fullTime = System.nanoTime();
		int nbRes = 10;
		int tailleDom = 17;
	    FileWriter fichier_resultats;
	    String[] benchs = {"test"}; 		// si on veut tester plusieurs Benchmarks
	    
	    //PARCOURT des Benchmarks
	    for (String bench : benchs) {
	    	String[] heure = (LocalTime.now().toString()).split("\\.")[0].split(":");
		    fichier_resultats = new FileWriter("../resultats/result_bench"+bench+"_"+heure[0]+heure[1]+heure[2]+".csv",false); // créer le fichier csv de sortie	    	fichier_resultats.write("sep=;\n");
	    	fichier_resultats.write("Durete;% solutions;temps moyen (s)\n"); // en-tête du fichier
		    
		    // Parsing des réseaux
	    	String path = "../reseaux/bench"+bench+"/";	//recuperation du chemin dans le dossier reseaux
			File reseaux = new File(path); 				// fichiers des reseaux
			File ficNames[] = reseaux.listFiles(); 		// liste des fichiers
			int nbDiff = ficNames.length; 				// nb de fichiers
			
			//PARCOURT DES RESEAUX
			for (int i=0; i<nbDiff; i++) { 					// pour chaque réseau
				String fic = ficNames[i].getName();			//recuperer le nom du fichier
				BufferedReader readFile = new BufferedReader(new FileReader(path+fic));
				System.out.println("\n" + fic + " :\n");	//affichage du fichier
				
				int nbSoluce = 0; 						// nombre de reseaux avec au moins une solution
				int nbTO = 0;
				double tempsMoyen = 0;
				
				for(int nb=1 ; nb<=nbRes; nb++) { 		// pour chaque reseau
					Model model=lireReseau(readFile);
					Solver solver = model.getSolver();
					solver.limitTime("30s");
					
					System.out.println("Résolution du réseau "+nb);
					long startTime = System.nanoTime();
					if (solver.solve()) { 					//si le modèle à au moins une solution
						tempsMoyen += System.nanoTime() - startTime;
						System.out.println("Solution trouvée\n");
						nbSoluce++;
					}
					else if (solver.isStopCriterionMet()){
						System.out.println("Time out !\n");
						nbTO++;
					}
					else {
						tempsMoyen += System.nanoTime() - startTime;
						System.out.println("Pas de solution\n");
					}
				}
				
			    // écriture du resultat dans le fichier
				double durete = (double) (tailleDom*tailleDom - Integer.parseInt((fic.split("\\.")[0]).split("\\_")[1])) / (tailleDom*tailleDom);
				double pourcentage = (nbTO==nbRes)? 0 : (100*nbSoluce/(nbRes-nbTO));
				double tempsMoy = (nbTO==nbRes)? 0 : ((double)tempsMoyen/(nbRes-nbTO))/1000000000;
			    fichier_resultats.write(durete + ";" + pourcentage + "%;" + tempsMoy + "\n");
			    
			} // fin du parsing de tous les fichiers
			fichier_resultats.close();
			double tempsS = (double)(System.nanoTime()-fullTime) / 1000000000;
			System.out.println("Temps total du benchmark : " + tempsS + "s");
		}
		return;	
	}
	
}
