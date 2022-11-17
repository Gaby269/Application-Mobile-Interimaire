
#include <sys/types.h>
#include <pthread.h>
#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>

#include <string.h>

#include "calcul.h"



////////////////////////////////////////////////////
// STRUCTURE POUR VAR PARATGEES ENTRE LES THREADS //
////////////////////////////////////////////////////

struct predicatImg {

  // regrouoes les donn�e partag�es entres les threads participants aux RdV :
  int nbZones;                // nombre de zones a traiter
  int *d;                     // tableau des zones sur lesquels les threads en sont
  pthread_mutex_t verrou;     // varriable qui représente le verrou pour avoir la main
  pthread_cond_t condi;       // variable qui represente la variable conditionelle pour lattente des evenements
  
};



//////////////////////////////////////////////
// STRUCTURE POUR LES PARAMETRES DUN THREAD //
//////////////////////////////////////////////

struct paramsThread {

  // structure pour regrouper les param�tres d'un thread. 

  int idThread;                       // un identifiant de thread, de 1 � N (N le nombre total de theads secondaires)
  struct predicatImg * varPartagee;   //les varaible paratgé  tous les threads

};





//////////////////////////////////////
// FONCTION ASSOCI2 A CHAQUE THREAD //
//////////////////////////////////////

void * traiteurImg (void * p){ 

  ////////////////////////////
  // GESTION DES STRUCTURES //
  ////////////////////////////

  struct paramsThread * args = (struct paramsThread *) p;
  struct predicatImg * predicat = args -> varPartagee;
  printf("[TRAITEMENT %d] Je commence\n\n", args->idThread);



  ////////////////////////
  // PARCOURS DUNE ZONE //
  ////////////////////////

  for (int i=1; i<= predicat -> nbZones ; i++){

    /////////////////////////////////
    //   SIMULATION LONG CALCUL :  //
    // PREMIER TRAVAIL SUR LA ZONE //
    /////////////////////////////////
    printf("[TRAITEMENT %d] TRAVAIL sur la zone %d\n\n", args->idThread, i);

    
    
    /////////////////////
    // PRISE DU VERROU //
    /////////////////////
    int res_lock = pthread_mutex_lock(&predicat->verrou) ;

      //GESTION ERREUR
      if (res_lock != 0){
        perror("Erreur prendre le verrou 1\n ");
        exit(1);
      }



    /////////////////////////////////
    // TRAVAIL DU PREMIER TRAITEUR //
    /////////////////////////////////
    if (args->idThread != 0){
      
      //////////////////////////////////////////////
      // ATTENTE QUE TOUS LES TRAITEURS ARRIVE>NT //
      //////////////////////////////////////////////
      printf("[TRAITEMENT %d] Je viens d'arriver pour la zone %d\n\n", args->idThread, i);

      //Tant que la zone que l'ont veut traiter est occupé on fait attendre
      while (predicat->d[args->idThread-1] < i) { 
         
        int res_wait = pthread_cond_wait(&predicat->condi ,&predicat->verrou);
          
          //GESTION ERREUR
          if (res_wait != 0){
            perror("Erreur lors de l'attente des autres threads\n ");
            exit(1);
          }
      }
    }

    
    ///////////////////////
    // LIBERATION VERROU //
    ///////////////////////

    //on libere le verrou pour laisser les autres threads arrivé
    int res_unlock = pthread_mutex_unlock(&predicat->verrou) ;

      //GESTION ERREUR
      if (res_unlock != 0){
        perror("Erreur deverouiller le verrou\n ");
        exit(1);
      }



    /////////////////////
    // PRISE DU VERROU //
    /////////////////////

    //on doit maintenant modifier le tableau apres avoir traiter (en simulation) la zone souhaité
    int res_lock2 = pthread_mutex_lock(&predicat->verrou) ;

      //GESTION ERREUR
      if (res_lock2 != 0){
        perror("Erreur quand je prend un verrou 2\n ");
        exit(1);
      }

    /////////////////////////////
    // MODIFICATION DU TABLEAU //
    /////////////////////////////

    //une fois qu'on a traiter la zone on doit passez a la suivante
    predicat->d[args->idThread] += 1;



    ///////////////////////////////////////////////////
    // LIBERATION DE TOUS LES THREADS MIS EN ATTENTE //
    ///////////////////////////////////////////////////

    // apres avoir modifier le tableau on est plus sur la zone courante donc on peut liberer tous les thread en attentes sur cette zone
    int res_broadcast = pthread_cond_broadcast(&predicat->condi);

      //GESTION ERREUR
      if (res_broadcast != 0){
        perror("Erreur lors du reveil du thread\n ");
      }
    


    ///////////////////////
    // LIBERATION VERROU //
    ///////////////////////

    //apres modification du tableau on peut maintenant liberer le verrou
    int res_unlock2 = pthread_mutex_unlock(&predicat->verrou) ;

      //GESTION ERREUR
      if (res_unlock2 != 0){
        perror("Erreur liberer un verrou 2\n ");
        exit(1);
      }
    
  }//fin du for



  /////////////////////////////////
  // ON ARRETE LE THREAD COURANT //
  /////////////////////////////////

  pthread_exit(NULL); // fortement recommand�.

}



//////////
// MAIN //
//////////

int main(int argc, char * argv[]){

  
  ////////////////////////////
  // GESTION DES PARAMETRES //
  ////////////////////////////

  if (argc!=3) {
    printf("Argument requis : nbThread nbZones \n");
    exit(1);
  }
  int nbThreads = atoi(argv[1]);    //on stocke le nombre de traitement a réaliser
  int nbZones = atoi(argv[2]);      //on stocke le nombre de zones
  //int nb = nbThreads;
  struct predicatImg varP;          //on declare les var paratagees


  ////////////////////
  // INITIALISATION //
  ////////////////////

  varP.d = (int*)malloc(sizeof(int));       //on alloue de la memoire pour le tableau de lhistorique des traitements des zones
  varP.nbZones = nbZones;                   //on donne le nombre de zones a varP
  pthread_t threads[nbThreads];             //on cree le tableau des threads
  struct paramsThread tabParams[nbThreads]; //on cree le tableau des paramatere des chaque threads
  

  ///////////////
  // VAR CONDI //
  ///////////////
  int res_condInit = pthread_cond_init(&(varP.condi), NULL);

    //GESTION ERREUR
    if (res_condInit != 0){
      perror("Erreur lors de l'initialisation de var condi\n ");
      exit(1);
    }
  

  ////////////
  // VERROU //
  ////////////
  int res_mutexInit = pthread_mutex_init(&(varP.verrou), NULL);

    //GESTION ERREUR
    if (res_mutexInit != 0){
      perror("Erreur lors de l'initialisation du verrou");
      exit(1);
    }

  srand(nbThreads);  // initialisation de rand pour la simulation de longs calculs
  

  ///////////////
  // AFFICHAGE //
  ///////////////

  printf("\n\n[THREAD PRICIPALE] Le nombre de zones est %d\n", nbZones);
  printf("[THREAD PRICIPALE] Le nombre de threads est %d \n\n", nbThreads);




  //////////////////////
  // CREATION THREADS //
  //////////////////////

  for (int i = 0; i < nbThreads; i++){

    tabParams[i].idThread = i;  //on donne comme identifiant de thread des nombres
    
    ///////////////////
    // VAR PARTAGEES //
    ///////////////////
    tabParams[i].varPartagee = &varP;


    /////////////////////
    // CREATION THREAD //
    /////////////////////
    int res_create = pthread_create(&threads[i], NULL, traiteurImg, &tabParams[i]);

      //GESTION ERREUR
      if (res_create != 0){
        perror("Erreur creation thread\n ");
        exit(1);
      }

  }

  

  ///////////////////////////////////
  // ATTENTE DE LA FIN DES THREADS //
  ///////////////////////////////////

  for (int i = 0; i < nbThreads; i++){
    printf("[TRAITEMENT %d] Je sui dans join\n\n", i);
    int res_join = pthread_join(threads[i], NULL);

      //GESTION ERREURS
      if (res_join != 0){                                                                //creation du thread meme si dans if
        perror("Erreur lors du join !\n ");                           //si parcontre il y a une erreur
        exit(1);                                                                    //on sort du programme
      }
  }


  ////////////////////////////////////
  // DESTRUCTION DES VAR PARATAGEES //
  ////////////////////////////////////
  
  //VERROU
  int res_destroyVerrou = pthread_mutex_destroy(&(varP.verrou));
    //GESTION ERREUR
    if (res_destroyVerrou != 0){
      perror("Erreur destruction du verrou\n ");
    }


  //VAR COND
  int res_destroyCondi = pthread_cond_destroy(&(varP.condi));
    //GESTION ERREUR
    if (res_destroyCondi != 0){
      perror("Erreur destruction de var condi\n ");
    }


  printf("\n[THREAD PRICIPALE] Fin de tous les threads secondaires \n\n");

  // terminer "proprement". 
  return 0;
 
}
 
