// Mode Debug pour les affichages
#define DEBUG 3

  //REQUETE POSSIBLE QU'ON A MIS EN PLACE
#define ADR_PROC 1              //adresse du processus courant
#define ATTRIB_NUM 2            //atribution du numero pour chaque processus
#define ADR_VOISINS 3           //adresse du voisin
#define ELECTION 4              //election
#define NB_VOISIN 5             //on donne le nb de voisins

//SYSTEME ERREUR OU FERMETURE
#define TRUE 1
#define FALSE 0
#define ERREUR -1
#define FERMETURE 0

#define TAILLE_MAX_STOCK 100    //maximum de stockage
#define NOEUDS_MAX 100          //on fixe le nombre de noeud maximum du graphe

  
//////////////////////
// FONCTION SENDTCP //
//////////////////////
/// Fonction qui recoit un message par buffer
/// sock descripteur de lenvoie
/// info_proc message recu
/// sizeinfo_proc taille du message a recu
/// @return resultat de la reception qui est la taille du message recu
int sendTCP(int sock, void* info_proc, int taille) {

    int res;        //on a le resultat de l'appel
    int env = 0;    //le total de ce quon envoie

    while(env < taille) {   //tant que la taille de lenvoie est plus petit que la taille donnée

        res = send(sock, info_proc+env, taille-env, 0);   //on appel  pour recevoir le message
        env += res;     //et on augmente la taille

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
/// sock descripteur pour envoie
/// info_proc message a envoyer
/// sizeinfo_proc taille du message
void sendCompletTCP(int sock, void* info_proc, int sizeinfo_proc){

    //PREMIER APPEL POUR LA TAILLE                                                //creation d'une variable qui recupere la taille du message
    int res_premier_appel = sendTCP(sock, &sizeinfo_proc, sizeof(int));     //on envoie la taille du message
    
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
/// Fonction qui recoit un message par buffer
/// sock descripteur de lenvoie
/// info_proc message recu
/// sizeinfo_proc taille du message a recu
/// @return resultat de la reception qui est la taille du message recu
int recvTCP (int sock, void* info_proc, int sizeinfo_proc){
   
    //VARIABLES 
    int res;
    int recu = 0;

    //BOUCLE
    while(recu < sizeinfo_proc) {

        res = recv(sock, info_proc+recu, sizeinfo_proc-recu, 0);
        recu += res;

        //GESTION ERREUR
        if (res <=0){
            return res;
        }
    }
    return recu;
}




/////////////////////////////
// FONCTION RECVCOMPLETTCP //
/////////////////////////////
/// Fonction qui recoit la taille puis le message
/// sock descripteur de lenvoie
/// info_proc message recu
/// sizeinfo_proc taille du message a recu
/// resultat de la reception qui est la taille du message recu
void recvCompletTCP(int sock, void* info_proc, int sizeinfo_proc){

   //PREMIER APPEL POUR LA TAILLE
   int taille_info_proc;                                                     //creation d'une variable qui recupere la taille du message
   int res_premier_appel = recvTCP(sock, &taille_info_proc, sizeof(int));        //on recoit la taille du message
   
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
      perror("[ERREUR] La taille du message est trop grande par rapport a celle attendu dans recv");
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
/// Fonction qui crée une socket
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
/// Fonction qui nomme une scoket
/// dS descripteur de la sokcet à nommer
/// port port de la socket à nommer
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
/// Fonction qui designe la socket ici du serveur
/// port numero de port en chaine de caractère
/// ip adresse ip 
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
/// Fonction qui accepte une socket
/// dS descripteur de la socket qui veut etre eccepter
/// adr adresse qui accepte le noeud
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

