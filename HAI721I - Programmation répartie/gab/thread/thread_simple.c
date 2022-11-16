#include <string.h>
#include <stdio.h>//perror
#include <sys/types.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>

#include "calcul.h"


/////////////////////////////////////////////
// STRCTURE POUR AVOIR PLUS DE 1 PARAMETRE //
/////////////////////////////////////////////

struct paramsFonctionThread {

  int idThread;       //nombre equivalent à l'identifiant pour que ce soit pratique
  int temps;          //tps de durée du thread différents pour chaquun
  int * donnee1;       //on veut passer l'adresse memoire associé a la donnee pour pouvoir etre changer par tout le monde
  int * donnee2;

};



///////////////////////////////////
// FONCTION POUR FAIRE UN THREAD //
///////////////////////////////////

void * connexionThread (void * params){
  
  struct paramsFonctionThread * args = (struct paramsFonctionThread *) params;            //si on a plusieurs arguments

  *(args->donnee1) += 1;                                                                   //je modifie la valeur qui est pointé par donnée
  
  pthread_t moi = pthread_self();                                                         //on veut identifiant du thread
  printf("[ CONNEXION ] Thread %li, Processus : %i,  %i secondes d'attente \n", moi, getpid(), args->temps);               //affichage quand debut thread
  printf("[ DONNEES 1 ] Données = %d\n", *(args->donnee1));
  calcul(args->temps);
  printf("[ CONNEXION ] La fin du thread : %li donnée 1 = %d\n", moi, *(args->donnee1));                                                //affichage quand fin thread
  //exit(1);
  pthread_exit(NULL);                                                                     //on sort du thread  
  //free(args);                                                                           //on libere la memoire si on utilise pas donnee

}

///////////////////////////////////
// FONCTION POUR FAIRE UN THREAD //
///////////////////////////////////


void * acceptationThread (void * params){
  
  struct paramsFonctionThread * args = (struct paramsFonctionThread *) params;            //si on a plusieurs arguments

  *(args->donnee2) += 1;                                                                   //je modifie la valeur qui est pointé par donnée
  
  pthread_t moi = pthread_self();                                                         //on veut identifiant du thread
  printf("[ACCEPTATION] Thread %li, Processus : %i,  %i secondes d'attente \n", moi, getpid(), args->temps);               //affichage quand debut thread
  printf("[ DONNEES 2 ] Données = %d\n", *(args->donnee2));
  calcul(args->temps);
  printf("[ACCEPTATION] La fin du thread : %li donnée 2 = %d\n", moi, *(args->donnee2));                                  //affichage quand fin thread
  //exit(1);
  pthread_exit(NULL);                                                                     //on sort du thread  
  //free(args);                                                                           //on libere la memoire si on utilise pas donnee

}






//////////
// MAIN //
//////////

int main(int argc, char * argv[]){


  ////////////////////////////
  // GESTION DES PARAM7TRES //
  ////////////////////////////

  if (argc < 2 ){
    printf("[UTILISATION] %s  nombre_threads  \n", argv[0]);
    return 1;
  }     

  int nbThread = atoi(argv[1]);           //stocker le nb de thread
  pthread_t threads[nbThread];            //stocker les identifiants des threads créé

  //FIN GESTION PARAM

  //valeur partager par tous les threads
  int val_partagee1 = 1;
  int val_partagee2 = 11;
 
  ///////////////////////////
  // CREATION DES THREADS ///
  ///////////////////////////

  for (int i = 1; i <= nbThread; i++){

    //PARAMETRES
    struct paramsFonctionThread * parametres = (struct paramsFonctionThread *)malloc(sizeof(struct paramsFonctionThread));                                     //on veut mettre les parametres
    //struct paramsFonctionThread parametres[sizeof(struct paramsFonctionThread)*nbThread]; 
    parametres->idThread = i;                                                     //ON LUI DONNE UN ID
    parametres->temps = i;                                                        //on attribut le tps
    parametres->donnee1 = &val_partagee1;                                           //je donne ladresse memoire de la variable paratage pour que tout le monde puisse y accéder
    parametres->donnee2 = &val_partagee2;                                           //je donne ladresse memoire de la variable paratage pour que tout le monde puisse y accéder

    //CREATION
    if (pthread_create(&threads[i], NULL, acceptationThread, parametres) != 0){      //creation du thread meme si dans if
      perror("Erreur lors de la creation d'un thread");                           //si parcontre il y a une erreur
      exit(1);                                                                    //on sort du programme
    }

    //CREATION
    if (pthread_create(&threads[i], NULL, connexionThread, parametres) != 0){      //creation du thread meme si dans if
      perror("Erreur lors de la creation d'un thread");                           //si parcontre il y a une erreur
      exit(1);                                                                    //on sort du programme
    }

    
  }

  //FIN CREATION THREADS


  ///////////////////////////////////////////////
  // ATTENTE DA FIN THREAD POUR CONTINUER MAIN //
  ///////////////////////////////////////////////

  for (int i = 1; i <= nbThread; i++){

    int res = pthread_join(threads[i], NULL);

    //GESTION ERREURS
    if (res < 0){                                                                //creation du thread meme si dans if
      perror("Erreur lors du join ! ");                           //si parcontre il y a une erreur
      exit(1);                                                                    //on sort du programme
    }

  }


  return 0;
 
}
 