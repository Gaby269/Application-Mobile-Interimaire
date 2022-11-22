#include <netinet/in.h>
#include <stdio.h>
#include <sys/types.h>
#include <netdb.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <pthread.h>        //thread
#include "parseur.c"
#include <errno.h>

//SYSTEME ERREUR OU FERMETURE
#define TRUE 1
#define FALSE 0
#define ERREUR -1
#define FERMETURE 0

//NOEUD POUR LES MISES EN ECOUTE
#define NOEUDS_MAX 100          //on fixe le nombre de noeud qui peuvent être accepter en même temps par le serveur

	

////////////////////////////////////////////////////////////////
////////////////// FONCTION DES AFFICHAGES /////////////////////
////////////////////////////////////////////////////////////////

/////////////////////////
// FONCTION PRINTCOLOR //
/////////////////////////
/// Fonction qui affiche le préfixe du commentaire a savoir [NOEUD %<numeroDuNoeud>] afficher en couleurs en calculant un numéro pour la couleur
/// numero numéro du noeud dans le graphe
void printColor(int numero) {
    int couleurProd = ((numero%6)+1) + '0';      //il n'y a que 6 couleurs visibles alors on fait un modulo 6 et on ne veut pas afficher du noir alors on met + 1 et on transfomre en ASCII
    printf("\x1B[3%cm[NOEUD %d] \033[0m", couleurProd, numero);
}

/////////////////////////////
// FONCTION PRINTCOLORPLUS //
/////////////////////////////
/// Fonction qui affiche le prefixe du commentaire a savoir [NOEUD %<numeroDuNoeud>] afficher en couleurs
/// numero numéro du noeud dans le graphe
/// type type du commentaire afficher en majuscules et en couleur dans la console
void printColorPlus(int numero, char*type){
    int couleurProd = ((numero%6)+1) + '0';
    printf("\x1B[3%cm[NOEUD %d] %s \033[0m", couleurProd, numero, type);
}


//////////////////////////////
// FONCTION PRINTCOLORNOEUD //
//////////////////////////////
/// Fonction qui affiche la toute première ligne pour un noeud (pour dire qu'il commence)
/// message1 début du message ici une ligne d'étoile pour faire joli
/// numero numéro du processus pour changer de couleur
/// message2 la fin du message qui est aussi une ligne d'étoile pour faire joli
void printColorNoeud(char * message1, int numero, char * message2) {
    int couleurProd = (numero%6)+1 + '0';
    printf("\x1B[3%cm%s%d%s\033[0m", couleurProd, message1, numero, message2);
}





/////////////////////////////////////////////////////
////////////////// FONCTION TCP /////////////////////
/////////////////////////////////////////////////////


//////////////////////
// FONCTION SENDTCP //
//////////////////////
/// Fonction qui recoit un message par buffer
/// sock descripteur de l'envoie
/// info_proc message recu
/// sizeinfo_proc taille du message a recu
/// return : resultat de la reception qui est la taille du message recu
int sendTCP(int sock, void* info_proc, int taille) {

    int res;        //on a le resultat de l'appel
    int env = 0;    //le total de ce qu'on envoie

    while(env < taille) {   //tant que la taille de l'envoie est plus petite que la taille donnée

        res = send(sock, info_proc+env, taille-env, 0);   //on appelle  pour recevoir le message
        env += res;         //et on augmente la taille

        //GESTION ERREUR
        if (res <= 0) {
            return res;
        }
    }
    return env; //et on renvoie la taille
}


/////////////////////////////
// FONCTION SENDCompletTCP //
/////////////////////////////
/// Fonction qui envoie la taille puis le message
/// sock descripteur pour envoi
/// info_proc message à﻿ envoyer
/// sizeinfo_proc taille du message
void sendCompletTCP(int sock, void* info_proc, int sizeinfo_proc){

    //PREMIER APPEL POUR LA TAILLE                                                          //creation d'une variable qui recupere la taille du message
    int res_premier_appel = sendTCP(sock, &sizeinfo_proc, sizeof(int));                     //on envoie la taille du message
    
        //GESTION DES ERREURS
        if (res_premier_appel == ERREUR) {
            perror("\n[ERREUR] : Erreur lors de l'envoie de la taille du message : ");
            close(sock);
            exit(1);          // on choisit ici d'arrêter le programme car le reste
        }
        if (res_premier_appel == FERMETURE) {
            perror("\n[ERREUR] : Abandon de la socket principale lors de l'envoie : ");
            close(sock);
            exit(1);          // on choisit ici d'arrêter le programme
        }

   //DEUXIEME APPEL POUR LE MESSAGE
   int res_deuxieme_appel = sendTCP(sock, info_proc, sizeinfo_proc);     //on envoie la taille du message
   
        //GESTION DES ERREURS
        if (res_deuxieme_appel == ERREUR) {
            perror("\n[ERREUR] : Erreur lors de l'envoie du message : ");
            close(sock);
            exit(1);          // on choisit ici d'arrêter le programme cr le reste depend de cet envoie
        }
        if (res_deuxieme_appel == FERMETURE) {
            perror("\n[ERREUR] : Abandon de la socket principale dans le l'envoie : ");
            close(sock);
            exit(1);          // on choisit ici d'arrêter le programme car le reste depend de cet envoie
        }

}



//////////////////////
// FONCTION RECVTCP //
//////////////////////
/// Fonction qui recoit un message par buffer
/// sock descripteur de l'envoie
/// info_proc message recu
/// sizeinfo_proc taille du message a recu
/// return : resultat de la reception qui est la taille du message recu
int recvTCP (int sock, void* info_proc, int sizeinfo_proc){
   
    //VARIABLES 
    int res;				//taille de chaque recv
    int recu = 0;			//taille final de chaque recvs

    //BOUCLE
    while(recu < sizeinfo_proc) {

        res = recv(sock, info_proc+recu, sizeinfo_proc-recu, 0);		//apel a recv pour recevoir les paquets
        recu += res;		//ajoutdes paquets

        //GESTION ERREUR
        if (res <=0){
            return res;
        }
    }
    return recu;	//envoie la taille
}




/////////////////////////////
// FONCTION RECVCOMPLETTCP //
/////////////////////////////
/// Fonction qui recoit la taille puis le message
/// sock descripteur de l'envoie
/// info_proc message recu
/// sizeinfo_proc taille du message a recu
void recvCompletTCP(int sock, void* info_proc, int sizeinfo_proc){

   //PREMIER APPEL POUR LA TAILLE
   int taille_info_proc;                                                     	//creation d'une variable qui recupere la taille du message
   int res_premier_appel = recvTCP(sock, &taille_info_proc, sizeof(int));       //on recoit la taille du message
   
      //GESTION DES ERREURS
      if (res_premier_appel == ERREUR) {
         perror("\n[ERREUR] : Erreur lors de la reception de la taille du message : ");
         close(sock);
         exit(1);          // on choisit ici d'arrêter le programme 
      }
      if (res_premier_appel == FERMETURE) {
         perror("\n[ERREUR] : Abandon de la socket principale lors du recv : ");
         close(sock);
         exit(1);          // on choisit ici d'arrêter le programme 
      }

   //VERIFICATION DES TAILLES
   if (taille_info_proc > sizeinfo_proc){
      perror("[ERREUR] La taille du message est trop grande par rapport a celle attendu dans recv");
      exit(1);
   }

   //DEUXIEME APPEL POUR LE MESSAGE
   int res_deuxieme_appel = recvTCP(sock, info_proc, sizeinfo_proc);     //on recoit la taille du message
   
      //GESTION DES ERREURS
      if (res_deuxieme_appel == ERREUR) {
         perror("\n[ERREUR] : Erreur lors de la reception du message : ");
         close(sock);
         exit(1);          // on choisit ici d'arrêter le programme 
      }
      if (res_deuxieme_appel == FERMETURE) {
         perror("\n[ERREUR] : Abandon de la socket principale lors du recv : ");
         close(sock);
         exit(1);          // on choisit ici d'arrêter le programme 
      }

}




/////////////////////
// CREATION SOCKET //
/////////////////////
/// Fonction qui crée une socket
/// return : descripteur de la socket 
int creationSocket (){

    int dS = socket(PF_INET, SOCK_STREAM, IPPROTO_TCP);  //on crée la socket enTCP

        //GESTION DES ERREUR
        if (dS == ERREUR){
            perror("[ERREUR] Problème lors de la création de la socket : ");
            close(dS);          //on ferme la socket
            exit(1);            //on sort du programme
        }

    return dS;        //on retourne le descripteur
}



//////////////////////////
// NOMMAGE DE LA SOCKET //
//////////////////////////
///  Fonction qui nomme une socket
///  dS descripteur de la socket à nommer
///  port port de la socket à nommer
/// return : retourner l'adresse de la socket
struct sockaddr_in nommageSocket(int dS, char * port){

    struct sockaddr_in adrSocket ;
    adrSocket.sin_family = AF_INET ;                            //IPv4 : famille AF_INET
    adrSocket.sin_addr.s_addr = INADDR_ANY;                     //Attache la socket à toutes les interfaces réseaux locales : toutes les adresses de la station
    adrSocket.sin_port = htons((short) atoi(port)) ;            // on doit convertir la chaine de caractères en nombre

    int res_bind_noeud = bind(dS,                                   // descripteur de socket
                                (struct sockaddr*)&adrSocket,       // pointeur vers l'adresse
                                sizeof(adrSocket)) ;                // longueur de l'adresse


        //GESTION ERREUR
        if (res_bind_noeud == ERREUR) {
            perror("\n\n[ERREUR] lors du nommage de la socket : ");
            close(dS);
            exit(1); // on choisit ici d'arrêter le programme
        }

    return adrSocket;
    
}




//////////////////////////////
// DESIGNATION D'UNE SOCKET //
//////////////////////////////
///  Fonction qui designe la socket ici du serveur
///  port numero de port en chaine de caractère
///  ip adresse ip 
/// return : adresse de la socket designer
struct sockaddr_in designationSocket(char * adresseIP, char* port){

    //DESCRIPTION DE LA SOCKET DISTANTE   
    struct sockaddr_in sock;                                                 // on declare la socket distante
    sock.sin_family = AF_INET ;                                              // famille d'adresse IPv4

    //CONVERTION DE LA SOCKET
    int res_conv = inet_pton(AF_INET, adresseIP, &(sock.sin_addr));          //convertire l'adresse

		//GESTION ERREUR
    	if (res_conv == ERREUR){
	        printf("\n[PROCESSUS] Problème lors de la convertion de l'adresse IP\n");
	        exit(1);
	    }

    //RECUPERATION PORT
    sock.sin_port = htons( (short) atoi(port)) ;                             // port du processus aussi donnée en parametre exemple "3430"

    return sock;      //on retourne la socket designer

}




///////////////////////
// CONNEXION EN TCP  //
///////////////////////
/// Fonction qui connecte une socket et une adresse
/// dS descripteur de la socket qui veut se connecter
/// sock adresse de la socket a qui doit etre connecté
void connexion(int dS, struct sockaddr_in* sock){
      
   //TAILLE DE LADRESSE SERVEUR :
   socklen_t size_addr = sizeof(struct sockaddr_in);             //on veut donc la taille de la socket du serveur

   //connexion
   int res_connect = connect(dS,                            // descripteur de socket
                            (struct sockaddr*)sock,         // pointeur vers l'adresse
                            size_addr);                     // longueur de l'adresse

        //GESTION ERREUR
        if (res_connect == ERREUR) {
            perror("\n[ERREUR] lors de la demande de connexion : ");
            close(dS);
            exit(1);
        }

}



/////////////////////////
// SE METTRE EN ECOUTE //
/////////////////////////
/// Fonction qui met en ecoute une socket
/// dS descripteur de la socket qui se met en ecoute
/// nbMaxAttente nombre maximum d'attente possible
void ecouter(int dS, int nbProc){
   
    int nbmaxAttente = nbProc;                               //on doit avoir un nb max qui est le nombre de processus dans l'Graphe
    int res_listen = listen(dS, nbmaxAttente);               //met en ecoute au max pour nbmaxAttentenoeuds

        //GESTION ERREUR
        if (res_listen == ERREUR) {
            perror("\n\n[ERREUR] : Erreur lors de la mise en ecoute de la socket : ");
            close(dS);
            exit(1);
        }
}












/////////////////////////////////////////////////////////////////
////////////////// FONCTION SUR LES THREADS /////////////////////
/////////////////////////////////////////////////////////////////


            /******************************/
            /*********** THREAD ***********/
            /******************************/

/////////////////////
// CREATION THREAD //
/////////////////////
/// Fonction qui crée un thread pour eviter de mettre 'erreur dans le main
/// thread pointeur vers l'emplacement du thread qu'on veut créer
/// param pointeur vers l'emplacement des parametres pour le thread
/// fonction fonction qui va etre le thread
void creationThread(pthread_t* thread, void* param, void* fonction){
    
    int res_create = pthread_create(thread, NULL, fonction, param);

        //GESTION ERREUR
        if (res_create != 0){
            perror("[ERREUR] lors de la creation thread\n ");
            exit(1);
        }

}


/////////////////////
// JOIN DES THREAD //
/////////////////////
/// Fonction qui fait attendre tous les threads pour pouvoir terminer le programme
/// threads tableau de tous les threads crées
/// nbThreads nombre de threads que l'on a créés
void joinThreads(pthread_t* threads, int nbThreads, int noeud){

    //pour tous les threas
    for (int i = 0; i < nbThreads; i++){

        //printf("[TRAITEMENT %d] Je suis dans join\n\n", i);
        int res_join = pthread_join(threads[i], NULL);

            //GESTION ERREURS
            if (res_join != 0){
                printf("resjoin = %d", res_join);
                perror("[ERREUR] lors du join !\n ");          //si parcontre il y a une erreur
            }

        printColorPlus(noeud, "JOIN");printf("j'arrive dans le join \n");
    }
}



//Les fonctions suivantes, on ne les utilise pas encore ce sera pour la deuxieme partie du projet


            /******************************/
            /*********** VERROU ***********/
            /******************************/

            
//////////////////////////////
// INITIALISATION DU VERROU //
//////////////////////////////
/// Fonction qui  initialise le verrou
/// verrou verrou qui doit etre initialisé
void initalisationVerrou(pthread_mutex_t* verrou){
    
    int res_mutexInit = pthread_mutex_init(verrou, NULL);

    //GESTION ERREUR
    if (res_mutexInit != 0){
		//printf("[ERREUR NUMERO] res_mutexInit = %d\n", res_mutexInit);
      	perror("[ERREUR] lors de l'initialisation du verrou");
      	exit(1);
    }

}


/////////////////////
// PRISE DU VERROU //
/////////////////////
/// Fonction qui va prendre le verrou donc utiliser les resources commune
/// verrou verrou qui doit être pris au prealable initialisé
void priseVerrou(pthread_mutex_t* verrou){

    int res_lock = pthread_mutex_lock(verrou) ; //prise du verrou

        //GESTION ERREUR
        if (res_lock != 0){
            //printf("[ERREUR NUMERO] res_lock = %d\n", res_lock);
            perror("[ERREUR] lors de la prise du verrou\n "); //si erreur lors de la prise du verrou on ferme le thread courant
            pthread_exit(NULL);
        }
}


//////////////////////////
// LIBERATION DU VERROU //
//////////////////////////
/// Fonction qui va liberer le verrou lorsqu'on en a plus besoin
/// verrou verrou qui va etre liberé
void liberationVerrou(pthread_mutex_t* verrou){

    int res_unlock = pthread_mutex_unlock(verrou) ;  //liberation du verrou car plus besoin

        //GESTION ERREUR
        if (res_unlock != 0){
            perror("[ERREUR] Libération du verrou\n ");
            exit(1);  //si il y a un probleme au niveau de la liberation du verrou il faut arreter le programme car aucun autre thread ne pourra l'utiliser
        }
}


///////////////////////////
// DESTRUCTION DU VERROU //
///////////////////////////
/// Fonction qui detruit le verrou
/// verrou verrou qu'on doit detruire
void destruireVerrou(pthread_mutex_t* verrou){

  	int res_destroyVerrou = pthread_mutex_destroy(verrou);
	    
	    //GESTION ERREUR
	    if (res_destroyVerrou != 0){
	      	//printf("[ERREUR NUMERO] res_destroyVerrou = %d\n",res_destroyVerrou);
		      perror("[ERREUR] lors de la destruction du verrou\n ");
		      exit(1);
	    }
}




            /********************************/
            /*********** VAR COND ***********/
            /********************************/


////////////////////////////////
// INITIALISATION DU VAR COND //
////////////////////////////////
/// Fonction qui detruit la variable conditionnelle
/// condi variable conditionnelle à detruire
void initialisationVarCond(pthread_cond_t* condi){

  	int res_condInit = pthread_cond_init(condi, NULL);

    	//GESTION ERREUR
	    if (res_condInit != 0){
	      perror("[ERREUR] lors de l'initialisation de var condi\n ");
	      exit(1);
	    }

}


//////////////////////////////////////////////////////
// ATTENTE DES AUTRES THREAD PAR VERROU ET VAR COND //
//////////////////////////////////////////////////////
/// Fonction qui attend les autres threads car on a une variable conditionnelle qui nous fait attendre
/// condi variable conditionnelle qui doit etre liberée
void attentVarCond(pthread_mutex_t* verrou, pthread_cond_t* condi){

    int res_wait = pthread_cond_wait(condi ,verrou);    //attente du thread courant à l'aide de la variable conditionelle et u verrou paratagé
      
        //GESTION ERREUR
        if (res_wait != 0){
            perror("[ERREUR] lors de l'attente des autres threads\n ");
            exit(1);  //si il y a une erreur au niveau de l'attente des autres threads on arrete le programme car ca faussera toutes les données 
        }
}



//////////////////////////////////////////////
// LIBERATION DE LA VARIABLE CONDITIONNELLE //
//////////////////////////////////////////////
/// Fonction qui libere la variable conditionelle
/// condi variable conditionnelle qui doit etre liberée
void liberationVarCond(pthread_cond_t* condi){

    int res_broadcast = pthread_cond_broadcast(condi);  //reveille de tous les threads en attentes par la liberation de la variable conditionelle

      	//GESTION ERREUR
	      if (res_broadcast != 0){
	        perror("[ERREUR] lors de la liberation de la varaible conditionelle apres attente du dernier thread\n ");
	        exit(1);  //ici on arrete le programme car niveau cela va poser des problème pour les threads suivants
	      }
}



/////////////////////////////
// DESTRUCTION DE VAR COND //
/////////////////////////////
/// Fonction qui detruit la variable conditionnelle
/// condi variable conditionnelle a detruire
void detruireVarCond(pthread_cond_t* condi){
  	//VAR COND
	int res_destroyCondi = pthread_cond_destroy(condi);

    	//GESTION ERREUR
	    if (res_destroyCondi != 0){
	      perror("[ERREUR] lors de la destruction de var condi\n ");
	      exit(1);
	    }

}



