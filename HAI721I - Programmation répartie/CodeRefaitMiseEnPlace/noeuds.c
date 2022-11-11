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
#define ADR_PROC 1
#define ATTRIB_NUM 2
#define ADR_VOISIN 3
#define ELECTION 4
#define TAILLE_RESEAU 5
#define DESTRUCTION_GRAPHE 6


//SYSTEME ERREUR OU FERMETURE
#define TRUE 1
#define FALSE 0
#define ERREUR -1
#define FERMETURE 0

#define TAILLE_MAX_STOCK 100        //maximum de stockage
#define NOEUDS_MAX 100          //on fixe le nombre de noeud maximum qu'il peut y avoir dans un graphe


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
    int voisins[NOEUDS_MAX];                //tableau de la liste des adresses des voisins
    struct sockaddr_in adrProc;             //adresse du processus dont on parle
};


//////////////////////
// FONCTION SENDTCP //
//////////////////////
/// @brief Fonction qui recoit un message par buffer
/// @param sock descripteur de lenvoie
/// @param informations_proc message recu
/// @param sizeinformations_proc taille du message a recu
/// @return resultat de la reception qui est la taille du message recu
int sendTCP (int sock, void* informations_proc, int sizeinformations_proc){

   //VARIABLE : 
   int rest_lu = sizeinformations_proc;                  //on commence a sizeinformations_proc
   int res;
   int lu;
   
   while(rest_lu > 0){         //tant quil me reste des octets a recevoir

      res = send(sock, informations_proc + (sizeinformations_proc - rest_lu), rest_lu, 0); //on tente d'envoyer mon message

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
/// @param informations_proc message a envoyer
/// @param sizeinformations_proc taille du message
void sendCompletTCP(int sock, void* informations_proc, int sizeinformations_proc){

   //PREMIER APPEL POUR LA TAILLE                                                //creation d'une variable qui recupere la taille du message
   int res_premier_appel = sendTCP(sock, &sizeinformations_proc, sizeof(int));     //on envoie la taille du message
   
      //GESTION DES ERREURS
      if (res_premier_appel == ERREUR) {
         perror("\n[ERREUR] : Erreur lors de l'envoie de la taille du message : ");
         close(sock);
         exit(1);          // on choisis ici d'arrêter le programme
      }
      if (res_premier_appel == FERMETURE) {
         perror("\n[ERREUR] : Abandon de la socket principale : ");
         close(sock);
         exit(1);          // on choisis ici d'arrêter le programme
      }

   //DEUXIEME APPEL POUR LE MESSAGE
   int res_deuxieme_appel = sendTCP(sock, informations_proc, sizeinformations_proc);     //on envoie la taille du message
   
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
/// @param informations_proc message recu
/// @param sizeinformations_proc taille du message a recu
/// @return resultat de la reception qui est la taille du message recu
int recvTCP (int sock, void* informations_proc, int sizeinformations_proc){
   
   //VARIABLE : 
   int rest_recv = sizeinformations_proc;                  //on commence a 0
   int res;
   int recu;
   
   while(rest_recv > 0){         //tant quil me reste des octets a recevoir

      res = send(sock, informations_proc + (sizeinformations_proc - rest_recv), rest_recv, 0); //on tente d'envoyer mon message

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
/// @param informations_proc message recu
/// @param sizeinformations_proc taille du message a recu
/// @return resultat de la reception qui est la taille du message recu
void recvCompletTCP(int sock, void* informations_proc, int sizeinformations_proc){

   //PREMIER APPEL POUR LA TAILLE
   int taille_informations_proc = 1;                                                     //creation d'une variable qui recupere la taille du message
   int res_premier_appel = recvTCP(sock, &taille_informations_proc, sizeof(int));        //on recoit la taille du message
   
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
    if (taille_informations_proc > sizeinformations_proc){
        perror("[ERREUR] La taille du message est trop grande par rapport a celle attendu");
        exit(1);
    }

    //DEUXIEME APPEL POUR LE MESSAGE
    int res_deuxieme_appel = recvTCP(sock, informations_proc, sizeinformations_proc);     //on recoit la taille du message
    
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
            close(dS);          //on ferme la socket
            exit(1);            //on sort du programme
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

    struct sockaddr_in adrSocket ;
    adrSocket.sin_family = AF_INET ;                            //IPv4 : famille AF_INET
    adrSocket.sin_addr.s_addr = INADDR_ANY;                     //Attache la socket àtoutes les interfaces réseaux locales : toutes les adresses de la station
    adrSocket.sin_port = htons((short) atoi(port)) ;            // on doit convertir la chaine de caractère en nombre

    int res_bind_noeud = bind(dS,                                  // descripteur de socket
                                (struct sockaddr*)&adrSocket,       // pointeur vers l'adresse
                                sizeof(adrSocket)) ;                // longueur de l'adresse


        //GESTION ERREUR
        if (res_bind_noeud == ERREUR) {
            perror("\n\n[ERREUR] lors du nommage de la socket : ");
            close(dS);
            exit(1); // on choisis ici d'arrêter le programme
        }

    return adrSocket;
    
}




//////////////////////////////
// DESIGNATION D'UNE SOCKET //
//////////////////////////////
/// @brief Fonction qui designe la socket ici du serveur
/// @param port numero de port en chaine de caractère
/// @param ip adresse ip 
/// @return adresse de la socket designer
struct sockaddr_in designationSocket(char * adresseIP, char* port){

    //DESCRIPTION DE LA SOCKET DISTANTE   
    struct sockaddr_in sock;                                                 // on declare la socket distante
    sock.sin_family = AF_INET ;                                              // famille d'adresse IPv4

    //CONVERTION DE LA SOCKET
    int res_conv = inet_pton(AF_INET, adresseIP, &(sock.sin_addr));          //convertire l'adresse
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
/// @brief Fonction qui connecte une socket et une adresse
/// @param dS descripteur de la socket qui veut se connecter
/// @param sock adresse de la socket a qui doit etre connecté
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
/// @brief Fonction qui met en ecoute une socket
/// @param dS descripteur de la socket qui se met en ecoute
/// @param nbMaxAttente nombre maximum d'attente possible
void ecouter(int dS, int nbProc){
   
    int nbmaxAttente = nbProc;                               //on doit avoir un nb max qui est le nombre de processus dans l'Graphe
    int res_listen = listen(dS, nbmaxAttente);               //met en ecoute au max pour NOEUDS_MAX noeuds

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
/// @param dS descripteur de la socket qui veut etre eccepter
/// @param adr adresse qui accepte le noeud
/// @return entier qui est le descripteur de la socket accepter
int accepter(int dS, struct sockaddr_in adr){

   socklen_t lgAdr = sizeof(struct sockaddr_in);   // sa taille

   int res_dS = accept(dS, (struct sockaddr *) &adr, &lgAdr); //nouvelle socket noeude

      //GESTION ERREUR
      if (res_dS == ERREUR) {
         perror("\n\n[PROCESSUS] : Erreur lors de l'acceptation de connexion : ");
         close(dS);
         exit(1); 
      }

   return res_dS;    //retourne l'adresse du noeud qu'on vient d'accepter

}





/////////////////////////
// PROGRAME PRINCIPAL  //
/////////////////////////
/// @brief Fonction main
/// @param argc nb d'argument
/// @param argv liste des arguments
/// @return entier
int main(int argc, char *argv[]) {

    //ETAPE 1 : GESTION DES INFORMATIONS
    if (argc != 5){
        printf("\n[UTILISATION] %s ip_serveur port_serveur port_noeud indice_proc\n\n", argv[0]);
        exit(1);
    }

    char* adresseIP = argv[1];          //adresse ip du serveur
    char* port_serveur = argv[2];       //port du serveur
    char* port_noeud = argv[3];         //port du noeud
    int indice_proc = atoi(argv[4]);    //indice du client

    printf("\n[PROCESSUS] \033[4mInforamtions données en paramètres :\033[0m\n");
    printf("\n       Adresse du serveur : %s\n       Port : %d", adresseIP, atoi(port_serveur));
    printf("\n       Port du noeud : %d", atoi(port_noeud));
    printf("\n       Indice du processus : %d", indice_proc);


    //ETAPE 2 : CREATION DE LA SOCKET QUI DISCUTE AVEC SERVEUR
    int dSProcCS = creationSocket();
    //AFFICHAGE
    //printf("\n[PROCESSUS] \033[4mSocket du noeud :\033[0m\n");
    printf("\n[PROCESSUS] Création de la socket réussie\n");
    printf("[PROCESSUS] Le descripteur du noeud est %d \n", dSProcCS);


    //ETAPE 3 : DESIGNATION DE LA SOCKET SERVEUR
    struct sockaddr_in sockServ = designationSocket(adresseIP, port_serveur);
    //AFFICHAGE
        //addresse
    char adrServ[INET_ADDRSTRLEN];
    inet_ntop(AF_INET, &sockServ.sin_addr, adrServ, INET_ADDRSTRLEN);
        //port
    int portServ = htons((short) sockServ.sin_port);
    printf("\n[PROCESSUS] Designation de la socket du serveur réussi\n");
    printf("[PROCESSUS] Le serveur a donc pour informations : %s::%d\n", adrServ, portServ);


    //ETAPE 4 : DEMANDE DE CONNECTION DE LA SOCKET A L'ADRESSE
    connexion(dSProcCS, &sockServ);
    //AFFICHAGE
    printf("\n[PROCESSUS] Connexion au serveur réussi !\n");


    //ETAPE 5 : CREATION de LA SOCKET PRECEDENT
    int dSProcJGraphe = creationSocket();
    //AFFICHAGE
    //printf("\n[PROCESSUS] \033[4mSocket du noeud :\033[0m\n");
    printf("\n[PROCESSUS] Création de la socket réussie\n");
    printf("[PROCESSUS] Le descripteur du noeud est %d \n", dSProcJGraphe);



    //ETAPE 6 : NOMMAGE DE LA SOCKET
    struct sockaddr_in sockProcJ = nommageSocket(dSProcJGraphe, port_noeud);
    //AFFICHAGE
        //adresse
    char adrProcJ[INET_ADDRSTRLEN];
    inet_ntop(AF_INET, &sockProcJ.sin_addr, adrProcJ, INET_ADDRSTRLEN);
        //port
    int portProcJ = htons((short) sockProcJ.sin_port);
        //affichage
    printf("\n[PROCESSUS] Nommage de la socket du noeud réussi\n");
    printf("[PROCESSUS] Les informations du noeud par la socket de descripteur %d : %s::%i\n", dSProcJGraphe, adrProcJ, portProcJ);



    //ETAPE 7 : MISE SOUS ECOUTE DE LA SOCKET
    int nbMaxAttente = NOEUDS_MAX;
    ecouter(dSProcJGraphe, nbMaxAttente);
        //AFFICHAGE
    printf("\n[PROCESSUS] La mise en ecoute de la socket du noeud précédent réussi !\n");



    //ETAPE 8 : ENVOIE DES INFORMATIONS AU SERVEUR
        //inforamtions du noeud
    struct infos_procGraphe informations_proc;      //declare la structure qu'on va envoyer au serveur
    informations_proc.requete = ADR_PROC;           //requete d'une adresse
    informations_proc.indice = indice_proc;         //donne l'indice du processus
    informations_proc.descripteur = dSProcJGraphe;  //donne le descripteur
    informations_proc.nbVoisin = nbVoisin;          //donne le nombre de voisins
    for (int j=0; j<nbVoisin; j++){
        informations_proc.voisins[j] = voisins[j];  //liste des voisins du processus
    }
    informations_proc.adrProc = sockProcJ;          //donner l'adresse de la socket
        //envoie
    sendCompletTCP(dSProcCS, &informations_proc, sizeof(struct infos_procGraphe));
    
    //AFFICHAGE
    printf("\n[PROCESSUS] Envoi des inforamtions réussi !\n");
    printf("[PROCESSUS] \033[4mEnvoie des inforamtions suivantes :\033[0m\n");
    printf("\n       Adresse du processus : %s\n       Port : %d", adrProcJ, portProcJ);
    printf("\n       Indice du noeud : %d\n       Descripteur de la socket du processus : %d", indice_proc, dSProcJGraphe);
    printf("\n       Nombre de voisins : %d\n       Liste des voisins : [%d",nbVoisin, voisins[0]);
    if (nbVoisin > 1){                              //si le nb de voisins est plus grand on ajoute le code suivant pour l'affichage
        for (int i=1; i<nbVoisin-1; i++){
            printf(",%d", voisins[i]);
        }
        printf(",%d]\n\n", voisins[nbVoisin-1]);
    } 
    else{                                           //sinon on affiche juste la fin du tabelau
        printf("]\n\n");
    } 
/*

    //ETAPE 10 : RECEPTION DES IFNORAMTIONS DES PROCESSUS AVEC QUI DISCUTER
    recvCompletTCP(dSProcCS, &informations_proc, sizeof(struct infos_procGraphe));  //on reutilise la structure informations_proc pour la recepetion
    printf("\n[PROCESSUS] Reception d'un message");


    //ETAPE 11 : CHANGEMENT DE DONNEES
    int numeroSousGraphe = informations_proc.info1;                   //le nuemro du sous Graphe prend la valeur de l'information 1 du message recu   
    printf("\n[PROCESSUS] J'ai reçu mon numéro de sous Graphe");


    //ETAPE 12 : CREATION DE L'ADRESSE DU PROCESSUS SUIVANT
    fd_set set, settmp;
    FD_ZERO(&set);                              //on reinitialise 
    FD_SET(dSProcCS, &set);                     //ajout de la socket serveur
    FD_SET(dSProcJGraphe, &set);                //ajout de la socket preedente
    struct sockaddr_in sockProcK;               //structure de son adresse
    //socklen_t lgAdr;                          //taille de l'adresse


    //ETAPE 13 : CALCUL DU MAXIMUM DES DESCRIPTEUR
    int maxdS = dSProcCS;                                 //declaration max des descripteur
    if (dSProcJGraphe > dSProcCS) maxdS = dSProcJGraphe;    //calcul du maximum par rapport au processus precedent
    

    //ETAEP 14 : MODIFICATION DES DONNEES
    int dSProcKGraphe = ERREUR;                                     //descripteur du processus suivant pour l'instant c'est une erreur

    int connecteAuPrecedent = FALSE;                                //declare un boolean qui dit si la connexion est ok ou non
    int taille_reseau = -1;                                         //on declare la taille du reseau
    int plus_grand = FALSE;                                         //on declare un boolean qui dit si le processus est le chef ou non

    struct infos_procGraphe stockage[TAILLE_MAX_STOCK];                 //declaration du tableau des informations avec les reuetes
    int indiceStockage = 0;                                         //et on declare l'indice du stockage


    while (TRUE) {

        //SI LA SOCKET DU PROCESSUS PROCHAIN EXISTE ET QUE L'INDICE OU STOCKER LES MESSAGES EST PLUS GRAND QUE 0
        if (indiceStockage > 0 && dSProcKGraphe != ERREUR) {    

            //ETAPE 15 : ENVOIE DES MESSAGES SOCKER DANS LE TABLEAU DES INFORMATIONS

                //GESTION ERREUR SI INDICE EST A 0
                if (indiceStockage == 0) {


                    //FEREMTURE DU DESCRIPTEUR PROCHAIN
                    close(dSProcKGraphe);

                    //FERMETURE DU DESCRIPTEUR PRECEDENT
                    close(dSProcJGraphe);

                    FD_CLR(dSProcKGraphe, &set);  //supression du processus suivant
                    FD_CLR(dSProcJGraphe, &set);  //suppression du processus precedent

                    //AFFICHAGE
                    printf("\n[PROCESSUS] Destruction des processus\n");
                    close(dSProcCS);
                    exit(1);                    //on sort du programme
                }

            //ENVOIE DU NOMBRE DE MESSAGE A ENVOYER
            printf("\n[PROCESSUS] Envoi du numéro des messages stockés");
            sendCompletTCP(dSProcCS, &indiceStockage, sizeof(int));

            //ENVOIE DE TOUS LES MESSAGES
            for (int i=0; i<indiceStockage; i++) {
                printf("\n[PROCESSUS] Envoi d'un message");
                sendCompletTCP(dSProcCS, &stockage[i], sizeof(struct infos_procGraphe));
            }

            //UNE FOIS QU'ON A TOUT ENVOYER ON RECOMMENCE EN METTANT l'INDICE A 0
            indiceStockage = 0;
            printf("\n[PROCESSUS] Tableau de message envoyé");
        }

        
        //ETAPE 16 : slectionner le maximum des données dans settmp
        settmp = set;
        if (select(maxdS+1, &settmp, NULL, NULL, NULL) == ERREUR) {
            printf("\n[PROCESSUS] Problème lors du select");
            continue;
        }
        

        //BOUCLE POUR PARCOURIR TOUTES LES SOCKETS POSSIBLES
        for (int df=2; df <= maxdS; df++) {
            
            if (!FD_ISSET(df, &settmp)) {
                continue;
            }

            //SI C'EST LA SOCKET QUI DISCUTE AVEC LE SERVEUR
            if (df == dSProcCS) {

                //DONNEES
                struct infos_procGraphe informations_proc;

                // ETAPE 17 : RECPETION DU MESSAGE DE LA PART DU SERVEUR QUI CONTIENT DONC l'ADRESSE DU PROCESSUS SUIVANT
                recvCompletTCP(dSProcCS, &informations_proc, sizeof(struct infos_procGraphe));
                //ON PEUT MAINTENANT ARRETER LA CONNECTION AVEC LE SERVEUR
                printf("\n[PROCESSUS] Deconnexion du serveur");

                // ETAPE 18 : FERMETURE DE LA SOCKET
                if (close(dSProcCS) == -1) {
                    printf("\n[PROCESSUS] Problème lors de la fermeture du descripteur");
                }
                FD_CLR(dSProcCS, &set);        //supression de la socet serveur

                //SI LA REQUETE EST BIEN ADRESSE DU VOISINS
                if (informations_proc.requete == ADR_VOISIN) {

                    // ETAPE 19 : STOCKAGE DE L'ADRESSE
                    sockProcK = informations_proc.adrProc;             //l'adresse de la socket suivante prend la valeur de l'adresse du message
                    
                    //RECUPERATION ADRESSE ET PORT
                    char adresse[INET_ADDRSTRLEN];
                    inet_ntop(AF_INET, &sockProcK.sin_addr, adresse, INET_ADDRSTRLEN);
                    int port = htons(sockProcK.sin_port);
                    
                    //AFFICHAGE
                    printf("\n[PROCESSUS] Tentative de se connecter à l'Graphe suivant: %s:%i\n", adresse, port);


                    //ETAPE 20 : CREATION DE LA SOCKET DU PROCESSUS SUIVANT
                    dSProcKGraphe = creationSocket();

                    //ETAPE 21 : CONNECTION AVEC LADRESSE DU PROCESSU PROCHAIN
                    connexion(dSProcKGraphe, &sockProcK);       //connexion du descripteur du processus k avec une adresse recu

                    //AFFICHAGE
                    printf("\n[PROCESSUS] Connexion au prochain sous-Graphe réussie");


                    // ETAPE 22 : LANCER L'ELECTION POUR SAVOIR QUI COMMENCE
                    
                    //DONNEEES  
                    struct infos_procGraphe informations_proc;                            //declaration d'une structure contenant un message correspondant à une requete
                    informations_proc.requete = ELECTION;                                 //la requete est une election
                    informations_proc.info1 = numeroSousGraphe;                           //l'information 1 est le numero du processus de l'Graphe
                    informations_proc.info2 = 1;                                          //l'information 2 est un entier 1

                    //STOCKAGE DANS LE TABLEAU stockage

                        //GESTION ERREUR DE DEPASSEMENT
                        if (indiceStockage == TAILLE_MAX_STOCK) {        //taille trop grande tableau pas assez grande
                            printf("\n[PROCESSUS] Problème, dépassement du stockage maximum de messages");
                            close(dSProcCS);
                            exit(1);
                        }

                    stockage[indiceStockage] = informations_proc;            //stocke le message à l'indice donnée en parametre dans la liste des messages
                    indiceStockage++;                                        //on incremente l'indice pour le mesages suviant
                                    
                    break;
                }
                //sinon auncune requete
                else {
                    printf("\n[PROCESSUS] Tout autre requete n'est pas possible pour cette étape");
                    break;
                }
            }


            //SI SOCKET EST LE PROCESSUS PRECEDENT
            if (df == dSProcJGraphe) {

                //SI LA CONNEION NEST PAS ENCORE EFFECTUE
                if (!connecteAuPrecedent) {

                    // ETAPE 17 : ACCEPTATION DE LA SOCKET QUI DEMANDE LA CONNECTION
                    dSProcJGraphe = accepter(dSProcJGraphe, sockProcJ);

                    //RECUPERATION DES ADRESSE
                    char adresse[INET_ADDRSTRLEN];
                    inet_ntop(AF_INET, &sockProcJ.sin_addr, adresse, INET_ADDRSTRLEN);

                    //AFFICHAGE
                    printf("\n[PROCESSUS] Connecté à l'Graphe précédent: %s", adresse);
                    FD_SET(dSProcJGraphe, &set);

                    //RECLACULATION DU MAX
                    if (maxdS < dSProcJGraphe) maxdS = dSProcJGraphe;
                    connecteAuPrecedent = TRUE;         //la connexion est effectué
                    continue;

                }
                //SI CONNEXION EST FAITE
                else{       //if (connecteAuPrecedent)

                    //ETAPE 18 : RECEPTION DES MESSAGES DU PROCESSUS PRECEDENT

                    //DONNEES
                    struct infos_procGraphe informations_proc;      //messages qui comportera une requete et des inforamtions
                    int nbReception;                                //nombre de recpetion 
                    int numeroProvenance;                           //numero du processus donnée dans informations
                    int calculDeTaille;                             //calcul de la taille 

                    //RECEPTION DU NOMBRE DE RECEPTION A RECEVOIR
                    recvCompletTCP(dSProcJGraphe, &nbReception, sizeof(int));

                    //BOUCLE POUR RECEVOIR AUTANT DE MESSAGE QUE PREVU
                    for (int i=0; i<nbReception; i++) {

                        //RECEVOIR LE MESSAGE 
                        recvCompletTCP(dSProcJGraphe, &informations_proc, sizeof(struct infos_procGraphe));
                        
                        //SELON LA REQUETE
                        switch (informations_proc.requete) {
                            case ELECTION:                              //si c'est une election
                                numeroProvenance = informations_proc.info1;           //le numero de provenance prend la valeur du numero de processus
                                calculDeTaille = informations_proc.info2;             //le calcul prend la valeur 1 pour l'instant
                                printf("\n[PROCESSUS] J'ai reçu un message d'élection du P°%i, le calcul de taille en cours est %i",numeroProvenance, calculDeTaille);

                                //SI MON NUMERO EST PLUS GRAND QUE LE NUMERO DE PROVENANCE JE NE DOIS RIEN ENVOYER
                                if (numeroProvenance < numeroSousGraphe) {
                                    printf("\n[PROCESSUS] Je suis un meilleur candidat, je ne renvoie pas");
                                } 
                                //SINON SI LES DEUX NUMEROS SONT EGAUX ALORS CEST MON NUMERO
                                else if (numeroProvenance == numeroSousGraphe) {
                                    printf("\n[PROCESSUS] J'ai reçu mon propre message, je suis donc l'élu, je renvoie la taille du réseau à tous");
                                    //MODIFICATION DONNEES
                                    plus_grand = TRUE;             //je deviens le chef car je viens de recevoir mon propre nuemro donc ca a été le plus grand pour tous
                                    taille_reseau = calculDeTaille;   //la taille du reseau est alors le calcul de la taille
                                    informations_proc.info1 = calculDeTaille;         //la nouvel 1 information c'est également le calcul de la taille
                                    informations_proc.requete = TAILLE_RESEAU;        //et on change la requete qui est la taille du reseau
                                    //STOCKAGE DANS LE TABLEAU stockage
                    
                                        //GESTION ERREUR DE DEPASSEMENT
                                        if (indiceStockage == TAILLE_MAX_STOCK) {        //taille trop grande tableau pas assez grande
                                            printf("\n[PROCESSUS] Problème, dépassement du stockage maximum de messages");
                                            close(dSProcCS);
                                            close(dSProcJGraphe);
                                            exit(1);
                                        }

                                    stockage[indiceStockage] = informations_proc;            //stocke le message à l'indice donnée en parametre dans la liste des messages
                                    indiceStockage++;                                        //on incremente l'indice pour le mesages suviant

                                } 
                                //SINON MON NUMERO EST PLUS PETIT DONC ON ENVOIE LE NUMERO PLUS grAND
                                else {
                                    printf("\n[PROCESSUS] C'est un meilleur candidat que moi, je renvoie");
                                    informations_proc.info2++;                          //on incremente l'information 2 qui correspond la taille du reseau
                                                                               
                                    //STOCKAGE DANS LE TABLEAU stockage
                    
                                        //GESTION ERREUR DE DEPASSEMENT
                                        if (indiceStockage == TAILLE_MAX_STOCK) {           //taille trop grande tableau pas assez grande
                                            printf("\n[PROCESSUS] Problème, dépassement du stockage maximum de messages");
                                            close(dSProcCS);
                                            close(dSProcJGraphe);
                                            exit(1);
                                        }

                                    stockage[indiceStockage] = informations_proc;            //stocke le message à l'indice donnée en parametre dans la liste des messages
                                    indiceStockage++;                                        //on incremente l'indice pour le mesages suviant

                                }
                                break;                                  //on a pas d'autre conditions
                            case TAILLE_RESEAU:                         //si c'est une taille du reseau
                                //SI JE SUIS LE CHEF
                                if (plus_grand) {                  //si je suis le chef alors la taille du reseau est connu de tous
                                    printf("\n[PROCESSUS] Le message de taille du reseau a bien fait le tour de l'Graphe");
                                }
                                //SINON JE SUIS PAS LE CHEF 
                                else {
                                    taille_reseau = informations_proc.info1;                              //la taille du reseau prend la valeur de la premiere informations
                                    printf("\n[PROCESSUS] La taille du reseau est: %i",taille_reseau);         //affichage
                                    //STOCKAGE DANS LE TABLEAU stockage
                    
                                        //GESTION ERREUR DE DEPASSEMENT
                                        if (indiceStockage == TAILLE_MAX_STOCK) {        //taille trop grande tableau pas assez grande
                                            printf("\n[PROCESSUS] Problème, dépassement du stockage maximum de messages");
                                            close(dSProcCS);
                                            close(dSProcJGraphe);
                                            exit(1);
                                        }

                                    stockage[indiceStockage] = informations_proc;            //stocke le message à l'indice donnée en parametre dans la liste des messages
                                    indiceStockage++;                                        //on incremente l'indice pour le mesages suviant

                                }
                                break;
                            default:                //sinon ca ne correspond pas a de resquete
                                printf("\n[PROCESSUS] Problème: requête non reconnue");
                                break;
                        }
                    }
                    //FIN ETAPE 18

                } 
            }
        }
    }
    */
}
