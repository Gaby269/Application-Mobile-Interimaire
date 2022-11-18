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



//THREAD structure pour regrouper les paramètres d'un thread
struct paramsInter1 {
  int idThread;                       // un identifiant de thread, de 1 à N (N le nombre total de theads secondaires)
  struct infos_Graphe* procCourant;   //processus courant a qui ont donne les param avant de savoir son numero
  struct partage * varPartage;   //les varaible paratgé  tous les threads
};


// regrouoes les donnée partagées entres les threads representant les noeuds
struct partageServeur {
  int* nbCourant;                 //nombre processus courant
  struct info_nb* info_nb;          //tableau du nb de voisins
  struct infos_Graphe* tabProc;     //construction d'un tableau pour stocker tous les clients
  struct nbVois* nbVois;          //construction du tableau des voisins
  int ** listeVoisinCo;             //construction du double tableau des voisins
  pthread_mutex_t verrou;     // varriable qui représente le verrou pour avoir la main
  pthread_cond_t condi;       // variable qui represente la variable conditionelle pour lattente des evenements
  
};


//THREAD structure pour regrouper les paramétres d'un thread
struct paramsNoeud {
  int idThread;                       // un identifiant de thread, de 1 à N (N le nombre total de theads secondaires)
	int * numero_proc;									//numero dans le graphe du processus courant
	int * numero_vois;									//numero du voisin a recuperer
	int * indice_vois;									//indice du processus voisin parmis les voisins du noeud courant
	int * dSVoisin;											//descripteur du voisin
};

