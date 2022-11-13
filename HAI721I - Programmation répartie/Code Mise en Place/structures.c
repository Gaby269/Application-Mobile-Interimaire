#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

////////////////////////////////////////
// STRUCTURES POUR ENVOI INFORMATIONS //
//////////////////////////////////////// 


/// Structure qui permet de dire pour chaque voisin combien il a de voisin en totalité et cb de voisin il va devoir envoyer une demande de connection
struct nbVois{
    int nbVoisinTotal;
    int nbVoisinDemande;
};


/// Structure des inforamtions avec la requete les inforamtions que l'on a besoin selon la requete et l'adresse du processus 
struct infos_Graphe {
    int requete;                            //requete qu'on veut
    int numero;                             //numero du noeud courant sur le graphe
    int descripteur;                        //descripteur du noeud
    struct sockaddr_in adrProc;             //adresse du processus dont on parle
};

//CREATION D'UNE STRUCTURE DES ARRETS
struct aretes{
  int noeud1;
  int noeud2;
};

//CREATION STRUCTURE POUR LES NOMBRE IMPORTANTS
struct info_nb{
  int nb_sommets;
  int nb_aretes;
};

