
#include <sys/types.h>
#include <pthread.h>
#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>

#include <string.h>

#include "calcul.h"


//////////////////////////////////////////
// STRUCTURE POUR LES VARIABLES PARTAGE //
//////////////////////////////////////////

struct predicatRdv {

  // regrouoes les donn�e partag�es entres les threads participants aux RdV :
  int *cpt;                   //cpt des différents  threads qui sont mis en place en meme tps
  pthread_mutex_t verrou;     // varriable qui représente le verrou pour avoir la main
  pthread_cond_t condi;       // variable qui represente la variable conditionelle pour lattente des evenements
  
};



//////////////////////////////////////////////
// STRUCTURE POUR LES PARAMETRES DUN THREAD //
///////////////////////////////////////////////

struct paramsThread {

  // structure pour regrouper les param�tres d'un thread. 

  int idThread;                       // un identifiant de thread, de 1 � N (N le nombre total de theads secondaires)
  struct predicatRdv * varPartagee;   //les varaible paratgé  tous les threads

};



//////////////////////////////////////
// FONCTION ASSOCI2 A CHAQUE THREAD //
//////////////////////////////////////

void * participantRdv (void * param){ 

  ////////////////////////////
  // GESTION DES STRUCTURES //
  ////////////////////////////

  struct paramsThread * args = (struct paramsThread *) param;
  struct predicatRdv * predicat = args -> varPartagee;
  printf("[PARTICIPANT %d] Je commence ! \n", args->idThread);


  //////////////////////////////
  // SIMULATION LONG CALCUL : //
  //     PREMIER TRAVAIL 1    //
  //////////////////////////////

  printf("[PARTICIPANT %d] TRAVAIL 1 + simulation\n", args->idThread);
  calcul (rand()%5); // c'est un exemple.


  /////////////////////
  // PRISE DU VERROU //
  /////////////////////

  int res_lock = pthread_mutex_lock(&predicat->verrou) ;

    //GESTION ERREUR
    if (res_lock != 0){
      perror("Erreur lors de la prise du verrou\n "); //si erreur lors de la prise du verrou on ferme le thread courant
      pthread_exit(NULL);
    }

  printf("[PARTICIPANT %d] Prise du verrou\n", args->idThread);
  
  /////////////////////////////////////////
  // DECREMENTATION DU CONTEUR DU THREAD //
  /////////////////////////////////////////
  *(predicat->cpt) -= 1;
  printf("[PARTICIPANT %d] Calcul sur predicat->cpt = %d\n", args->idThread, *(predicat->cpt));



  /////////////////////////////////////////////////
  // ATTENTE QUE TOUS LES PARTICIPANTS ARRIVE>NT //
  /////////////////////////////////////////////////

  // Tant que tous les threads ne sont pas arrivé donc tant que le compteur n'est pas a 0
  while (*(predicat->cpt) != 0) {
    
    printf("[THREAD %d] Je viens d'arrivé dans la zone d'attente\n", args->idThread);

    int res_wait = pthread_cond_wait(&predicat->condi ,&predicat->verrou);    //attente du thread courant à l'aide de la variable conditionelle et u verrou paratagé
      
      //GESTION ERREUR
      if (res_wait != 0){
        perror("Erreur lors de l'attente des autres threads\n ");
        exit(1);  //si il y a une erreur au niveau de l'attente des autres threads on arrete le programme car ca faussera toutes les données 
      }

    printf("[THREAD %d] J'attent les autres\n", args->idThread);


  }



  ///////////////////////////////////////////////////
  // LIBERATION DE TOUS LES THREADS MIS EN ATTENTE //
  ///////////////////////////////////////////////////

  //Si le dernier thread est arrivé donc si le compteur est égal a zero on passe a letape suivante
  if (*(predicat->cpt) == 0) {
    
    printf("[DERNIER THREAD %d] Je suis arrivé en dernier\n", args->idThread);

    int res_broadcast = pthread_cond_broadcast(&predicat->condi); //reveille de tous les threads en attentes par la liberation de la variable conditionelle

      //GESTION ERREUR
      if (res_broadcast != 0){
        perror("Erreur lors de la liberation de la varaible conditionelle apres attente du dernier thread\n ");
        exit(1);  //ici on arrete le programme car niveau cela va poser des problème pour les threads suivants
      }
  
  }
  printf("[THREAD %d] C'est repartie pour la suite apres l'attente ! \n", args->idThread);



  ///////////////////////
  // LIBERATION VERROU //
  ///////////////////////

  //on a plus besoin du verrou donc on peut le liberer
  int res_unlock = pthread_mutex_unlock(&predicat->verrou) ;

    //GESTION ERREUR
    if (res_unlock != 0){
      perror("Erreur creation thread\n ");
      exit(1);  //si il y a un probleme au niveau de la liberation du verrou il faut arreter le programme car aucun autre thread ne pourra l'utiliser
    }

  printf("[THREAD %d] Liberation du verrou ! \n", args->idThread);


  //////////////////////////////
  // SIMULATION LONG CALCUL : //
  //     PREMIER TRAVAIL 2    //
  //////////////////////////////
  printf("[PARTICIPANT %d] TRAVAIL 2\n\n", args->idThread);
  calcul (rand()%5); // reprise et poursuite de l'execution.



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

  if (argc!=2) {
    printf("[UTILISATION] : ./prog nombre_Threads \n");
    exit(1);
  }

  int nbThreads = atoi(argv[1]);  //nombre de thread qu'on va utiliser c'est a dire le nombre de participants
  int nb = nbThreads;             //on met se nombre dans une autre varaible qui ne pas changer
  struct predicatRdv varP;        //on declare les varaibles paratagé (la structure)


  ////////////////////
  // INITIALISATION //
  ////////////////////

  pthread_t threads[nbThreads];               //creation d'un tableau pour stocker chaque thread
  struct paramsThread tabParams[nbThreads];   //et un tableau qui va stocker les parametres pour chaque threads
  
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
  

  //////////////////////
  // CREATION THREADS //
  //////////////////////

  for (int i = 0; i < nbThreads; i++){

    tabParams[i].idThread = i;  //on donne comme identifiant de thread des nombres

    ///////////////////
    // VAR PARTAGEES //
    ///////////////////
    varP.cpt = &nb;                     //le compteur devient le nombre de thread
    tabParams[i].varPartagee = &varP;

    /////////////////////
    // CREATION THREAD //
    /////////////////////
    int res_create = pthread_create(&threads[i], NULL, participantRdv, &tabParams[i]);

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
      exit(1);
    }


  //VAR COND
  int res_destroyCondi = pthread_cond_destroy(&(varP.condi));
    //GESTION ERREUR
    if (res_destroyCondi != 0){
      perror("Erreur destruction de var condi\n ");
      exit(1);
    }

  printf("\n[THREAD PRICIPALE] Fin de tous les threads secondaires \n");

  // terminer "proprement". 
  return 0;
 
}
 
