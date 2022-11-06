//#include <cstdio>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#define TAILLE_MAX 1000

int main(int argc, char * argv[]) {
    if(argc != 2) {
        printf("utilisation : %s path",argv[0]);
    }

    char* nom_fichier = argv[1];
    FILE* fichier;

    //Ouverture en lecture
    fichier = fopen(nom_fichier,"r");

    //Var pour stocker une ligne
    char ligne[TAILLE_MAX] = "";

    //Compteur du nbr de lignes
    int cpt = 0;

    if (fichier != NULL) {
        char** lignes = malloc(cpt * sizeof(char*)); // Tableau qui va contenir toutes les lignes du fichier

        while(fgets(ligne, TAILLE_MAX, fichier) != NULL) {cpt++;} // on compte le nombre de ligne comme un gitan       
    
        rewind(fichier); // On retourne au debut du fichier

        
        for(int i = 0; i < cpt; i++) { // Boucle pour stocker les lignes
            fgets(ligne, TAILLE_MAX, fichier);
            lignes[i] = strdup(ligne);
        }
        
    }
    else
    {
        printf("Impossible d'ouvrir le fichier %s",nom_fichier);
    }

    fclose(fichier);

    return 0;
}