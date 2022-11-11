//#include <cstdio>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#define TAILLE_MAX 1000

int main(int argc, char * argv[]) {
    if(argc != 2) {
        printf("\n[UTILISATION] ./parseur chemin_fichier\n");
        exit(1);
    }

    char* nom_fichier = argv[1];
    FILE* fichier;

    //Ouverture en lecture
    fichier = fopen(nom_fichier,"r");

    //Var pour stocker une ligne
    char ligne[TAILLE_MAX] = "";
    //nombre de varaibles
    int nbNoeud = 0;        //Var pour stocker le nombre de noeuds
    int nbArretes = 0;      //Var pour stocker le nombre de lignes (arretes)
    //liste de toutes les relations
    //int listeNoeuds1[TAILLE_MAX];   //on a la liste des noeuds a gauche
    //int listeNoeuds2[TAILLE_MAX];   //on a la liste des noeuds a droite
    //int **listeNoeudsRelie = (int**) malloc(sizeof(int**));

    char c = '0';
    int nbC = 0;
    int nbE = 0;
    char motEdge[4] = "";

    //Compteur du nbr de lignes
    int cpt = 0;

    if (fichier != NULL) {
        
        //RECUPERATION DES INFORAMTIONS DE BASES

        //LECTURE pour compter le nombre de ligne avant la ligne p
        while(fgets(ligne, TAILLE_MAX, fichier) != NULL && ligne[0] != 'p'){             //tant que pas la ligne avec p
            cpt++;                  //on donne un conteur pour pouvoir reparcourir apres          
        }
        nbC = cpt;
        printf(ligne);
        //RECOMMENCE
        rewind(fichier); // On retourne au debut du fichier
        //LECTURE des lignes jusqua avant p
        while (cpt!=0){         //tant qu'on est aps arriver juste avant la ligne ou il y a p
            if (fgets(ligne, TAILLE_MAX, fichier) != NULL){     //on lit les ligne
                cpt--;      //et on decremente
            }
        }
        //LECTURE de la derniere ligne
        fscanf(fichier, "%c %s %d %d", &c, motEdge, &nbNoeud, &nbArretes);  //on lit la derniere ligne
        printf("\nnbNoeud = %d\n", nbNoeud);
        printf("nbArretes = %d\n\n", nbArretes);    
        nbE = nbArretes;        //on associe le nombre d'arretes

        //RECOMMENCE
        rewind(fichier);

        //RECUPERATION DE LA LISTE DES NEOUDS
        //char** lignes = (char**)malloc(cpt * sizeof(char**)); // Tableau qui va contenir toutes les lignes du fichier

        //LECTURE des lignes jusqua avant p
        int cptE = nbE;
        while (cptE!=0){         //tant qu'on est aps arriver juste avant la ligne ou il y a p
            if (fgets(ligne, TAILLE_MAX, fichier) != NULL){     //on lit les ligne
                cptE--;      //et on decremente
            }
        }
        //tableau de valeurs
        
        int noeuds1;        //element du noeud 1
        int noeuds2;        //element du noeud 2
        //LECTURE des dernieres lignes
        while (fscanf(fichier, "%c %d %d", &c, &noeuds1, &noeuds2) != EOF){  //on lit la derniere ligne){

            //MISE DES DONNEES DANS LE TABLEAU
            printf("\nAFFICHAGE 2 : Le noeud %d est ", noeuds1);
            printf("relie a %d\n\n", noeuds2);

        }
/*
        for(int i = 0; i < cpt; i++) { // Boucle pour stocker les lignes
            fgets(ligne, TAILLE_MAX, fichier);
            lignes[i] = strdup(ligne);
        }
        */

        
    }
    else
    {
        printf("Impossible d'ouvrir le fichier %s",nom_fichier);
    }

    fclose(fichier);
    

    return 0;
}