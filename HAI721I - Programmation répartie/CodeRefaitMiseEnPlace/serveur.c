// Auteur : Gabrielle POINTEAU
// Date : 05/11/2022

#include <netinet/in.h>
#include <stdio.h>
#include <sys/types.h>
#include <netdb.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>


//REQUETE POSSIBLE QU'ON A MIS EN PLACE
#define ADR_PROC 1              //adresse du processus courant
#define ATTRIB_NUM 2            //atribution du numero pour chaque processus
#define ADR_VOISINS 3            //adresse du voisin
#define ELECTION 4              //election
#define TAILLE_RESEAU 5         //a taille du reseau
#define DESTRUCTION_ANNEAU 6    //destruction du graphe


//SYSTEME ERREUR OU FERMETURE
#define  TRUE 1
#define ERREUR -1               //erreur
#define FERMETURE 0             //Fermeture
#define NOEUDS_MAX 100          //on fixe le nombre de noeud maximum qu'il peut y avoir dans un graphe



/* Programme Noeud */


////////////////////////////////////////
// STRUCTURE POUR ENVOIE INFORMATIONS //
//////////////////////////////////////// 

/// @brief Structure qui répertorie le descripteur et l'adresse du processus
struct procGraphe {
    int indiceProc;                    //indice du site dans le graphe
    int dSProc;
    struct sockaddr_in adrProc;
};




/// @brief Structure des inforamtions avec la requete les inforamtions que l'on a besoin selon la requete et l'adresse du processus 
struct infos_procGraphe {
    int requete;                            //requete qu'on veut
    int indice;                             //indice du noeud courant sur le graphe
    int descripteur;                        //descripteur du noeud
    int nbVoisin;                           //nb de voisins du noeud
    int *voisins;                //tableau de la liste des adresses des voisins
    struct sockaddr_in adrProc;             //adresse du processus dont on parle
};



//////////////////////
// FONCTION SENDTCP //
//////////////////////
/// @brief Fonction qui recoit un message par buffer
/// @param sock descripteur de lenvoie
/// @param info_proc message recu
/// @param sizeinfo_proc taille du message a recu
/// @return resultat de la reception qui est la taille du message recu
int sendTCP (int sock, void* info_proc, int sizeinfo_proc){

   //VARIABLE : 
   int rest_lu = sizeinfo_proc;                  //on commence a sizeinfo_proc
   int res;
   int lu;
   
   while(rest_lu > 0){         //tant quil me reste des octets a recevoir

      res = send(sock, info_proc + (sizeinfo_proc - rest_lu), rest_lu, 0); //on tente d'envoyer mon message

      //GESTION ERREUR
      if (res <= 0){ 
         return res; 
      }  

      rest_lu = rest_lu - res;     //on rajoute res pour savoir combien on en a lu
      lu+=res;
   }
   return lu;
}




/////////////////////////////
// FONCTION SENDCompletTCP //
/////////////////////////////
/// @brief Fonction qui envoie la taille puis le message
/// @param sock descripteur pour envoie
/// @param info_proc message a envoyer
/// @param sizeinfo_proc taille du message
void sendCompletTCP(int sock, void* info_proc, int sizeinfo_proc){

    //PREMIER APPEL POUR LA TAILLE                                                //creation d'une variable qui recupere la taille du message
    int res_premier_appel = sendTCP(sock, &sizeinfo_proc, sizeinfo_proc);     //on envoie la taille du message
    
        //GESTION DES ERREURS
        if (res_premier_appel == ERREUR) {
            perror("\n[ERREUR] : Erreur lors de l'envoie de la taille du message : ");
            close(sock);
            exit(1);          // on choisis ici d'arrêter le programme car le reste
        }
        if (res_premier_appel == FERMETURE) {
            perror("\n[ERREUR] : Abandon de la socket principale : ");
            close(sock);
            exit(1);          // on choisis ici d'arrêter le programme
        }

   //DEUXIEME APPEL POUR LE MESSAGE
   int res_deuxieme_appel = sendTCP(sock, info_proc, sizeinfo_proc);     //on envoie la taille du message
   
        //GESTION DES ERREURS
        if (res_deuxieme_appel == ERREUR) {
            perror("\n[ERREUR] : Erreur lors de l'envoie du message : ");
            close(sock);
            exit(1);          // on choisis ici d'arrêter le programme cr le reste depend de cet envoie
        }
        if (res_deuxieme_appel == FERMETURE) {
            perror("\n[ERREUR] : Abandon de la socket principale : ");
            close(sock);
            exit(1);          // on choisis ici d'arrêter le programme car le reste depend de cet envoie
        }

}








//////////////////////
// FONCTION RECVTCP //
//////////////////////
/// @brief Fonction qui recoit un message par buffer
/// @param sock descripteur de lenvoie
/// @param info_proc message recu
/// @param sizeinfo_proc taille du message a recu
/// @return resultat de la reception qui est la taille du message recu
int recvTCP (int sock, void* info_proc, int sizeinfo_proc){
   
   //VARIABLE : 
   int rest_recv = sizeinfo_proc;                  //on commence a 0
   int res;
   int recu;
   
   while(rest_recv > 0){         //tant quil me reste des octets a recevoir

      res = send(sock, info_proc + (sizeinfo_proc - rest_recv), rest_recv, 0); //on tente d'envoyer mon message

      //GESTION ERREUR
      if (res <= 0){ 
         return res; 
      }  

      rest_recv = rest_recv - res;     //on rajoute res pour savoir combien on en a lu
      recu +=res;
   }
   return recu;
}




/////////////////////////////
// FONCTION RECVCOMPLETTCP //
/////////////////////////////
/// @brief Fonction qui recoit la taille puis le message
/// @param sock descripteur de lenvoie
/// @param info_proc message recu
/// @param sizeinfo_proc taille du message a recu
/// @return resultat de la reception qui est la taille du message recu
void recvCompletTCP(int sock, void* info_proc, int sizeinfo_proc){

   //PREMIER APPEL POUR LA TAILLE
   int taille_info_proc = 1;                                                     //creation d'une variable qui recupere la taille du message
   int res_premier_appel = recvTCP(sock, &taille_info_proc, sizeinfo_proc);        //on recoit la taille du message
   
      //GESTION DES ERREURS
      if (res_premier_appel == ERREUR) {
         perror("\n[ERREUR] : Erreur lors de la reception de la taille du message : ");
         close(sock);
         exit(1);          // on choisis ici d'arrêter le programme 
      }
      if (res_premier_appel == FERMETURE) {
         perror("\n[ERREUR] : Abandon de la socket principale : ");
         close(sock);
         exit(1);          // on choisis ici d'arrêter le programme 
      }

   //VERIFICATION DES TAILLES
   if (taille_info_proc > sizeinfo_proc){
      perror("[ERREUR] La taille du message est trop grande par rapport a celle attendu");
      exit(1);
   }

   //DEUXIEME APPEL POUR LE MESSAGE
   int res_deuxieme_appel = recvTCP(sock, info_proc, sizeinfo_proc);     //on recoit la taille du message
   
      //GESTION DES ERREURS
      if (res_deuxieme_appel == ERREUR) {
         perror("\n[ERREUR] : Erreur lors de la reception du message : ");
         close(sock);
         exit(1);          // on choisis ici d'arrêter le programme 
      }
      if (res_deuxieme_appel == FERMETURE) {
         perror("\n[ERREUR] : Abandon de la socket principale : ");
         close(sock);
         exit(1);          // on choisis ici d'arrêter le programme 
      }

}




/////////////////////
// CREATION SOCKET //
/////////////////////
/// @brief Fonction qui crée une socket
/// @return descripteur de la socket 
int creationSocket (){

    int dS = socket(PF_INET, SOCK_STREAM, 0);  //on crée la socket enTCP

        //GESTION DES ERREUR
        if (dS == ERREUR){
            perror("[ERREUR] Problème lors de la création de la socket : ");
            close(dS);  //on ferme la socket
            exit(1);          //on sort du programme
        }

    return dS;        //on retourne le descripteur
}



//////////////////////////
// NOMMAGE DE LA SOCKET //
//////////////////////////
/// @brief Fonction qui nomme une scoket
/// @param dS descripteur de la sokcet à nommer
/// @param port port de la socket à nommer
/// @return retourner l'adresse de la socket
struct sockaddr_in nommageSocket(int dS, char * port){

    //ADRESSE
    struct sockaddr_in adrSocket ;
    adrSocket.sin_family = AF_INET ;                            //IPv4 : famille AF_INET
    adrSocket.sin_addr.s_addr = INADDR_ANY;                     //Attache la socket àtoutes les interfaces réseaux locales : toutes les adresses de la station
    adrSocket.sin_port = htons((short) atoi(port)) ;            // on doit convertir la chaine de caractère en nombre

    //NOMMAGE
    int res_bind_Noeud = bind(dS,                                  // descripteur de socket
                            (struct sockaddr*)&adrSocket,           // pointeur vers l'adresse de nommage
                            sizeof(adrSocket)) ;                    // longueur de l'adresse


      //GESTION ERREUR
      if (res_bind_Noeud == ERREUR) {
         perror("\n\n[ERREUR] lors du nommage de la socket : ");
         close(dS);
         exit(1); // on choisis ici d'arrêter le programme 
      }

   return adrSocket;

}





/////////////////////////
// SE METTRE EN ECOUTE //
/////////////////////////
/// @brief Fonction qui met en ecoute une socket
/// @param dS descripteur de la socket qui se met en ecoute
/// @param nbMaxAttente nombre maximum d'attente possible
void ecouter(int dS, int nbProc){
   
    int nbmaxAttente = nbProc;                               //on doit avoir un nb max qui est le nombre de processus du graphe
    int res_listen = listen(dS, nbmaxAttente);               //met en ecoute au max pour 10 Noeuds

        //GESTION ERREUR
        if (res_listen == ERREUR) {
            perror("\n\n[ERREUR] : Erreur lors de la mise en ecoute de la socket : ");
            close(dS);
            exit(1);
        }
}






////////////////////////////
// ACCEPTER UNE CONNEXION //
////////////////////////////
/// @brief Fonction qui accepte une socket
/// @param dS descripteur de la socket qui va accepter
/// @param adr pointeur vers l'adresse de la socket du Noeud
/// @return entier qui est le descripteur de la socket accepter
int accepter(int dS, struct sockaddr_in* adr){

    socklen_t lgAdr = sizeof(struct sockaddr_in);   // sa taille

    int res_dS = accept(dS, (struct sockaddr *) &adr, &lgAdr); //nouvelle socket Noeud

        //GESTION ERREUR
        if (res_dS == ERREUR) {
            perror("\n\n[SERVEUR] : Erreur lors de l'acceptation de connexion : ");
            close(dS);
            exit(1); 
        }

   return res_dS;    //retourne l'adresse du Noeud qu'on vient d'accepter

}







/// @brief Foncion main 
/// @param argc nombre d'argument
/// @param argv liste des argumenets
/// @return entier
int main(int argc, char *argv[])
{

    // ETAPE 1 : GESTION PARAMETRES
    if (argc != 3){
        printf("\n[UTILISATION] : %s port_serveur nombre_Noeuds\n\n", argv[0]);
        exit(1);
    }

    char* portServeur = argv[1];
    int nbNoeuds = atoi(argv[2]);


    // ETAPE 2 : CREATION SOCKET SERVEUR
    int dSServeur = creationSocket();                  //creation 
    //AFFICHAGE
    printf("\n[SERVEUR] Création de la socket réussie\n");
    printf("[SERVEUR] Le descripteur est %d \n", dSServeur);


    //ETAPE 3 : NOMMER SOCKET
    struct sockaddr_in adrServeur = nommageSocket(dSServeur, portServeur);              //nommer la socket avec son port
    //AFFICHAGE
        //adresse
    char adrServeurAff[INET_ADDRSTRLEN];
    inet_ntop(AF_INET, &adrServeur.sin_addr, adrServeurAff, INET_ADDRSTRLEN);
        //port
    int portServeurAff = htons(adrServeur.sin_port);
        //affichage
    printf("\n[SERVEUR] Le port est %d\n", portServeurAff);                //affichage du port
    printf("[SERVEUR] L'adresse est %s\n", adrServeurAff);                //affichage de l'adresse


    //ETAPE 4 : MISE SOUS ECOUTE
    int nbMaxEcoute = NOEUDS_MAX;                                //on fixe le nombre de processus maximum pour attente 
    ecouter(dSServeur, nbMaxEcoute);                      //on met en ecoute la socket serveur
    //AFFICHAGE
    printf("\n[SERVEUR] La mise en ecoute de la socket du serveur réussi !\n");


    // ETAPE 4 : MISE EN PLACE DU MULTIPLEXAGE
        //donnees util
    int dSNoeud;                                                                //declaration du descripteur
    //int nbMaxdS = dSServeur;                                                  //maximum des descripteurs
    int nbNoeudCourant = 0;                                                     //on declare le nombre de Noeud courant
    //int nbMaxNoeud = NOEUDS_MAX;                                              //on declare un maximum de noeuds possibles
    struct infos_procGraphe *procGraphe = (struct infos_procGraphe *)0;         //on declare un tableau de structure pour les informations des Noeuds connecté au sevreur
    struct sockaddr_in sockNoeud;                                               //on declare la socket du Noeud
    socklen_t lgAdr;                                                            //taille de l'adresse
    


    //BOUCLE POUR RECEVOIR LES CLIENT
    while(TRUE){

        //ETAPE 4 : ACCEPTATION DU NOEUD
            //acceptation
        dSNoeud = accept(dSServeur, (struct sockaddr*)&sockNoeud, &lgAdr);          //on accepte le Noeud qui demande
            //incrementation
        nbNoeudCourant++;                                   //incremente le nombre de noeud connecté actuellement

        //AFFICHAGE
            //adresse
        char adrNoeudAff[INET_ADDRSTRLEN];
        inet_ntop(AF_INET, &sockNoeud.sin_addr, adrNoeudAff, INET_ADDRSTRLEN);
            //port
        int portNoeudAff = htons(sockNoeud.sin_port);
            //affichage
        printf("\n[SERVEUR] Connexion d'un Noeud de descripteur %d : %s::%d\n", dSNoeud, adrNoeudAff, portNoeudAff);
        if (nbNoeudCourant <= 1){
            printf("[SERVEUR] %d Noeud est connecté au serveur\n", nbNoeudCourant);       //affichage du nombre de Noeud connecté
        }
        else printf("[SERVEUR] %d Noeuds sont connectés au serveur\n", nbNoeudCourant);       //affichage du nombre de Noeud connecté


        //ETAPE 5 : RECEPTION DES INFORMATIONS DU Noeud
            //donnees        
        struct infos_procGraphe info_proc;                                          // structure qui va recuperer les informations qu'un Noeud a envoyer
            //reception
        recvCompletTCP(dSNoeud, &info_proc, sizeof(struct infos_procGraphe));       // reception des informations dans info_proc
            //modification des donnees
            printf("\nje suis la\n");
        sockNoeud = info_proc.adrProc;                                           // donner a sockNoeud l'adresse recu dans info_proc
        int indice_proc = info_proc.indice;                                     //donne l'indice
        procGraphe[indice_proc].indice = indice_proc;                          //on attribut l'indice du noeud
        procGraphe[indice_proc].descripteur = info_proc.descripteur;           //on attribut le descripteur
        procGraphe[indice_proc].nbVoisin = info_proc.nbVoisin;                 //on attribut le nb de info_proc.voisins
        for (int i=0; i<procGraphe[indice_proc].nbVoisin; i++){
            procGraphe[indice_proc].voisins[i] = info_proc.voisins[i];        //donne les voisins
        }
        procGraphe[indice_proc].adrProc = sockNoeud;                           //on attribut l' adresse

        //AFFICHAGE
            //adresse
        char adrProcAff[INET_ADDRSTRLEN];             //on va stocker l'adresse du sous anneau dedans
        inet_ntop(AF_INET, &sockNoeud.sin_addr, adrProcAff, INET_ADDRSTRLEN);     //adresse du Noeud    
            //port
        int portProcAff = htons(sockNoeud.sin_port);                                 //port du Noeud
            //Affichage
        /*
        printf("\n[SERVEUR] \033[4mLe processus a comme informations après réception :\033[0m\n");
        printf("\n       Adresse du processus : %s\n       Port : %d", adrProcAff, portProcAff);
        printf("\n       Indice du noeud : %d\n       Descripteur de la socket du processus : %d", indice_proc, procGraphe[indice_proc].descripteur);
        printf("\n       Nombre de voisins : %d\n       Liste des voisins : [%d", procGraphe[indice_proc].nbVoisin, procGraphe[indice_proc].voisins[0]);
        if (procGraphe[indice_proc].nbVoisin > 1){                              //si le nb de voisins est plus grand on ajoute le code suivant pour l'affichage
            for (int i=1; i< procGraphe[indice_proc].nbVoisin-1; i++){
                printf(",%d", procGraphe[indice_proc].voisins[i]);
            }
            printf(",%d]\n\n", procGraphe[indice_proc].voisins[ procGraphe[indice_proc].nbVoisin-1]);
        } 
        else{                                           //sinon on affiche juste la fin du tabelau
            printf("]\n\n");
        } 
            
        //MODIFICATION DES INFORMATIONS
        procGraphe[indice_proc].indice = indice_proc;                          //on attribut l'indice du noeud
        procGraphe[indice_proc].descripteur = info_proc.descripteur;           //on attribut le descripteur
        procGraphe[indice_proc].nbVoisin = info_proc.nbVoisin;                 //on attribut le nb de info_proc.voisins
        //procGraphe[indice_proc].voisins = info_proc.voisins;                   // donner la liste des voisins
        procGraphe[indice_proc].adrProc = sockNoeud;                           //on attribut l' adresse

*/

        //ETAPE 6 : AFFICHAGE DE LA LISTE DES NOEUDS CONNECTE
            //affichage
        printf("\n[SERVEUR] \033[4mListe des sous-anneaux:\033[0m\n");
            //adresse
        char adrNoeudCoAff[INET_ADDRSTRLEN];                                       //on va stocker l'adresse du sous anneau dedans
            //parcourt des noeuds connectés au serveur                        
        for (int i=0; i<nbNoeudCourant; i++) { 
                //recuperation adresse
            inet_ntop(AF_INET, &procGraphe[i].adrProc.sin_addr, adrNoeudCoAff, INET_ADDRSTRLEN);     //adresse du Noeud    
                //port
            int portNoeudCoAff = htons(procGraphe[i].adrProc.sin_port);                                 //port du Noeud
            printf("\n      Noeud n°%i de descripteur %i : %s::%i\n", i, procGraphe[i].descripteur, adrNoeudCoAff, portNoeudCoAff);
        }
        printf("\n***********************************\n");
        


        //UNE FOIS QUE TOUS LES NOEUDS SONT LA
        if (nbNoeudCourant == nbNoeuds){        //si le nombre courant de noeud connecté est égal au nombre de noeud du graphe 
            
            printf("\n[SERVEUR] Tous les Noeuds sont connectés !\n\n***********************************\n");

            //ETAPE 7: ENVOIE DES INFORMATIONS AU PROCESSUS VOISINS
                //donnees
            struct infos_procGraphe info_proc;                          //on declare la structure qui dit ce qu'on veut
            info_proc.requete = ADR_VOISINS;                            //on donne la requete ADR_VOISIN
                //boucle
            for (int i=0; i<nbNoeudCourant; i++) {                      //pour chaque noeuds
                
                struct infos_procGraphe info_voisin[procGraphe[i].nbVoisin];   //structure contenant les voisins a envoyer

                for (int v=0; v<procGraphe[i].nbVoisin; v++){              //parcourt des voisins v est donc 

                    int indice_proc = procGraphe[i].voisins[v];                 //dns le tableau voisins du noeud i 
                    
                    info_proc.descripteur = procGraphe[indice_proc].descripteur;        //Recuperation du descripteur du voisins
                    info_proc.indice = procGraphe[indice_proc].indice;                  //recuperation de l'indice du voisin
                    info_proc.nbVoisin = procGraphe[indice_proc].nbVoisin;              //recuperation du nb de voisins
                    for (int k=0; k<sizeof(info_proc.voisins); k++){
                        info_proc.voisins[k] = procGraphe[indice_proc].voisins[k];                                                // donner la liste des voisins
                    }
                    info_proc.adrProc = procGraphe[indice_proc].adrProc;                //donne à la structure l'adresse de la liste du processus i+1

                    info_voisin[indice_proc] = info_proc;                              //donne alors la structure
                }
                    //envoie des inforamtions
                sendCompletTCP(info_proc.descripteur, info_voisin, sizeof(struct infos_procGraphe));     //on envoie les inforamtions ici adresse des voisins
                
                
                //ETAPE 8 : AFFICHAGE DE LA LISTE DES NOEUDS VOISINS
                    //adresse
                char adrNoeudAff[INET_ADDRSTRLEN];
                inet_ntop(AF_INET, &info_proc.adrProc.sin_addr, adrNoeudAff, INET_ADDRSTRLEN);
                    //port
                int portNoeudAff = htons(info_proc.adrProc.sin_port); 
                    //affichage
                printf("\n[SERVEUR] Le Noeud n°%i de descripteur %i a pour adresse et port : %s:%i\n", i, procGraphe[i].descripteur, adrNoeudAff, portNoeudAff);
                printf("\n***********************************\n");
                printf("\n[SERVEUR] \033[4mListe des adresses des voisins envoyés:\033[0m\n\n");
                    //adresse
                char adrNoeudVois[INET_ADDRSTRLEN];
                    //parcourt des noeuds connectés au serveur                        
                for (int j=0; j<procGraphe[i].nbVoisin; j++) { 
                        //recup adresse
                    inet_ntop(AF_INET, &info_proc.adrProc.sin_addr, adrNoeudVois, INET_ADDRSTRLEN);
                        //port
                    int portNoeudVois = htons(info_proc.adrProc.sin_port);   
                        //affichage               
                    printf("Noeud voisin n°%i de descripteur %i : %s::%i\n", j, procGraphe[j].descripteur, adrNoeudVois, portNoeudVois);
                }
                printf("\n***********************************\n");
            }//fin du for des noeuds


            //ETAPE 8 : FERMETURE DE LA SOCKET SERVEUR CAR PLUS BESOIN
                //fermeture
            close(dSServeur);
                //affichage
            printf("\n[SERVEUR] Je peux m'en aller !\n");
                //on sort du programme
            exit(0);
        
        }

    }
}//fin du main


/*
    //ETAPE 5 : DECLARATION DE DONNEES UTILS
    fd_set set, settmp;
    int dSNoeud;                                   //on decare la socket Noeud    
    FD_ZERO(&set);
    FD_SET(dSServeur, &set);
    int maxdS = dSServeur;                          //on fixe le maximum des descripteur en commancant par la socket serveur
    struct sockaddr_in sockNoeud;                  //on declare la socket Noeud
    int nbMaxAnneau = NOEUDS_MAX;                          //on fixe le nombre maximum d'anneau possible
    struct procGraphe procGraphe[nbMaxAnneau];     //on declare un tableau de tous les processus qui sont dans du graphe
    int nbNoeudCourant = 0;                       //on declare le maximum des anneau courant 


    while (1) {                                                  //pk un while ?    
        //MULTIPLEXAGE
        settmp = set;
        if (select(maxdS+1, &settmp, NULL, NULL, NULL) == -1) {
            printf("\n[SERVEUR] Problème lors du select : ");  
            continue;
        }
        

        for (int df=2; df <= maxdS; df++) {          //on parcourt les descripteur possible en commencant par 2 car il n'y en a pas de 0 ou 1
            if (!FD_ISSET(df, &settmp)) {
                continue;
            }

            if (df == dSServeur) {                  //si le descripteur est la socket du serveur

                //ETAPE 6 : ACCEPTATION DU Noeud
                dSNoeud = accepter(dSServeur, &sockNoeud);        //on accepte le Noeud qui demande


                //ETAPE 7 : RECPETION DES INFORMATIONS DU Noeud
                struct infos_procGraphe info_proc;                                                     //structure qui va recuperer les informations qu'un Noeud a envoyer
    
                //RECPETION DES INFORMATIONS
                recvCompletTCP(dSNoeud, &info_proc, sizeof(struct infos_procGraphe));                  //reception des informations
                sockNoeud = info_proc.adrProc;                                                         //recuperation de l'adrresse recu


                //ETAPE 8 : ATTRIBUTION DES INFORMATIONS POUR LES PROCESSUS
                
                //RECUPERATION DE LADRESSE
                char adrNoeudRecu[INET_ADDRSTRLEN];
                inet_ntop(AF_INET, &sockNoeud.sin_addr, adrNoeudRecu, INET_ADDRSTRLEN);
                printf("\n[SERVEUR] Connexion d'un sous-anneau d'adresse %s\n", adrNoeudRecu);       //affichage

                FD_SET(dSNoeud, &settmp);

                //MODIFIATION MAX DESCRIPTEUR
                if (maxdS < dSNoeud) maxdS = dSNoeud;

                //MODIFICATION DE LA SSTRUCTURE
                procGraphe[nbNoeudCourant].dSProc = dSNoeud;             //on attribut un descripteur
                procGraphe[nbNoeudCourant].adrProc = sockNoeud;          //on attribut une adresse

                //CREATION DES INFORAMTIOSN DUN Noeud
                struct infos_procGraphe attribution;        //on declare l'attibution d'un numero
                attribution.requete = ATTRIB_NUM;           //on donne la requete
                attribution.info1 = nbNoeudCourant;       //on donne l'information principale qu'on a besoin ici le maximum courant
                nbNoeudCourant++;                         //on incremente le maximum courant pour continuer l'attribution

                //ENVOIE DE LATTRIBUTION DU NUMERO D'ANNEAU AU Noeud
                sendCompletTCP(dSNoeud, &attribution, sizeof(struct infos_procGraphe));  //on envoie l'attribution à un processus

                
                //ETAPE 9 : AFFICHAGE DES PROCESSUS CONNECTE                    
                char adrNoeudEnv[INET_ADDRSTRLEN];                                       //on va stocker l'adresse du sous anneau dedans
                printf("\n***********************************\n");                      //afficher une ligne
                printf("\n[SERVEUR] \033[4mListe des sous-anneaux:.\033[0m\n");

                //BOUCLE POUR PARCOURIR LA TAILLE DES SOUS ANNEAU EXIStant                          
                for (int i=0; i<nbNoeudCourant; i++) { 

                    //RECUPERATION DES ADRESSES ET PORT
                    inet_ntop(AF_INET, &procGraphe[i].adrProc.sin_addr, adrNoeudEnv, INET_ADDRSTRLEN);   //recuperation de l'adresse dans le tableau pour le processus i
                    int portNoeud = htons(procGraphe[i].adrProc.sin_port);

                    //AFFICHAGE
                    printf("\n[SERVEUR] Le processus n°%i de descripteur %i : %s:%i\n", i, procGraphe[i].dSProc, adrNoeudEnv, portNoeud);
                }
                printf("\n***********************************\n");
                
                continue;
            }
        }//fin du for

        if (nbNoeudCourant == nbprocGraphe) {                                            //si le nombre de processus courant est égal au nombre de processus donnée
            printf("\n[SERVEUR] Tous les anneaux sont connectés, envoi des adresses\n");      //affichage
            
            //ETAPE 10 : ENVOIE DES INFORMATIONS AU PROCESSUS

            //VERIFICATION DE PLUS DE 3 PROCESSUS
            if (nbNoeudCourant < 3) {           //verification que le nombre de processus est moins de 3
                printf("\n[SERVEUR] Impossible de créer un anneau avec moins de trois sous-anneaux\n");       //aors c'est pas possible
                exit(1);
            }

            //DONNEES
            struct infos_procGraphe info_proc;                                      //on declare la structure qui dit ce qu'on veut
            info_proc.requete = ADR_VOISIN;                           //on donne la requete ADR_VOISIN
            
            //BOUCLE QUI PARCOURT LA LISTE DES INFORMATIONS DES PROCESSUS DE LANNEAU -1
            for (int i=0; i<nbNoeudCourant-1; i++) {                        //on parcours le tableau des informations des processus
                
                info_proc.adrProc = procGraphe[i+1].adrProc;            //donne à la structure l'adresse de la liste du processus i+1
                
                //RECUPERATION DE L'ADRESSE ET PORT
                char adrNoeud[INET_ADDRSTRLEN];
                inet_ntop(AF_INET, &info_proc.adrProc.sin_addr, adrNoeud, INET_ADDRSTRLEN);
                int portNoeud = htons(info_proc.adrProc.sin_port);

                //ENVOIE DES INFORMATIONS 
                sendCompletTCP(procGraphe[i].dSProc, &info_proc, sizeof(struct infos_procGraphe));     //on envoie les inforamtions ici adresse du voisin
                printf("\n[SERVEUR] Envoie de l'adresse: %s:%i\n", adrNoeud, portNoeud);
            }
            //DERNIER PROCESSUS
            info_proc.adrProc = procGraphe[0].adrProc;          //donne l'adresse du premier processus

            //RECUPERATION DE LADRESSE ET PORT
            char adrNoeud[INET_ADDRSTRLEN];
            inet_ntop(AF_INET, &info_proc.adrProc.sin_addr, adrNoeud, INET_ADDRSTRLEN);
            int portNoeud = htons(info_proc.adrProc.sin_port);

            //ENVOIE DE LADRESSE DU DERNIER PROCESSUS
            printf("\n[SERVEUR] Envoi de l'adresse: %s:%i\n", adrNoeud, portNoeud);
            sendCompletTCP(procGraphe[nbNoeudCourant-1].dSProc, &info_proc, sizeof(struct infos_procGraphe));


            //ETAPE 11 : FERMETURE DE LA SOCKET SERVEUR
            if (close(dSServeur) == -1) {                                                   //on peut donc fermer la socket du serveur car on a fait notre boulot
                printf("\n[SERVEUR] Problème lors de la fermeture du descripteur : ");
            }
            printf("\n[SERVEUR] Au revoir.\n");
            exit(0);
        }
    }
}
*/


