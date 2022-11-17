#include "fonctions.c"

//FONCTION interaction1 qui va faire recevoir les informations des noeuds et envoie des 
void * interaction1 (void * p){     //un thread représente un client

    //GESTION PARAMETRE DE LA STRUTURE
        //arguments
    struct paramsInter1 * args = (struct paramsInter1 *) p;
    struct sockaddr_in sockNoeud = args->procCourant->adrProc;     //adresse du noeud
    int dSNoeud =  args->procCourant->descripteur;                 //descripteur du noedu
        //partage
    struct partage * partage = args->varPartage;                   //Recuperation des variables paratgé
    //pthread_t threadCourant = pthread_self();                      //identifiant du threas

    //AFFICHAGE
    printf("\n[SERVEUR] Le client de dS = %d est connecté\n", dSNoeud);


    //ETAPE 6 : RECEPTION DES INFORMATIONS DU NOEUD
        //donnees        
    struct infos_Graphe info_proc;                      //structure qui va recuperer les informations qu'un Noeud a envoyer
        //reception
    recvCompletTCP(dSNoeud, &info_proc, sizeof(struct infos_Graphe));       //reception des informations dans info_proc
        //declaration variable plus simple
    int numero = info_proc.numero;
    int indice_proc = numero-1;                 //donne l'indice du tableau qu'on paratge
    sockNoeud = info_proc.adrProc;
        //modification des donnees
    args->procCourant->numero = numero;                   //recup numero
    args->procCourant->adrProc = sockNoeud;               //donner a sockNoeud l'adresse recu dans info_proc

    //PRISE DU VERROU
    priseVerrou(&(partage->verrou));     //prise du verrou
    printf("[SERVEUR] Prise du verrou pour le noeud %d\n", numero);

    //MODIFICATION DES INFORMATIONS
    partage->tabProc[indice_proc].numero = numero;                         //on attribut l'indice du noeud
    partage->tabProc[indice_proc].descripteur = dSNoeud;                          //on attribut le descripteur
    partage->tabProc[indice_proc].adrProc = sockNoeud;                            //on attribut l' adresse
        //RECUPERATION DES INFOS 
    struct nbVois nbVoisinCourant = partage->nbVois[indice_proc];                         //Recuperation du nb de voisins pour affichage

    //LIBERATION VERROU : on a plus besoin du verrou donc on peut le liberer
    liberationVerrou(&(partage->verrou));
    printf("[SERVEUR] Liberation du verrou pour le noeud %d\n", numero);

    //AFFICHAGE
        //adresse
    char adrProcAff[INET_ADDRSTRLEN];             //on va stocker l'adresse du sous anneau dedans
    inet_ntop(AF_INET, &sockNoeud.sin_addr, adrProcAff, INET_ADDRSTRLEN);     //adresse du Noeud    
        //port
    int portProcAff = htons(sockNoeud.sin_port);                                 //port du Noeud
        //affichage
    printf("\n[SERVEUR] \033[4mLe processus a comme informations après réception :\033[0m\n");
    printf("\n       Adresse du processus : %s\n       Port : %d", adrProcAff, portProcAff);
    printf("\n       Numéro du noeud dans le graphe : %d\n       Descripteur de la socket du processus : %d\n\n", indice_proc+1, dSNoeud);
        

    //ETAPE 7 : ENVOI DU NOMBRE DE VOISIN A CHAQUE NOEUDS
        //descripteur de socket courant
    int dS_courant = dSNoeud;                  //indice courant
        //envoi
    sendCompletTCP(dS_courant, &nbVoisinCourant, sizeof(struct nbVois));
        //affichage
    printf("\n[SERVEUR] \033[4mInformations envoyées :\033[0m\n\n");
    printf("      Nombre de voisin total = %d\n", nbVoisinCourant.nbVoisinTotal);
    printf("      Nombre de voisin de demande = %d\n", nbVoisinCourant.nbVoisinDemande);

    args->varPartage = partage;     //nous voulons que les valeurs soit changer donc on redonne les bonnes valeurs
    
    pthread_exit(NULL); // fortement recommand�.

}







   
/////////////////////////
//  PROGRAME SERVEUR   //
/////////////////////////
int main(int argc, char *argv[]) {

    //GESTION PARAMETRES
    if (argc != 3) {
        printf("\n[UTILISATION] : %s port_serveur fichier_graphe\n\n", argv[0]);
        exit(1);
    }

    char* portServeur = argv[1];        //port du serveur
    char* nom_fichier = argv[2];        //nom du fichier ou recuperer la structure du graphe


    //ETAPE 1 : RECUPERATION DES INFO DU GRAPHE
    struct info_nb nB;                  //on declare une structure 
    nB = nbAreteNbNoeud(nom_fichier);  //on recupere les nombre important
    int nb_sommets = nB.nb_sommets;    //on recupere le nombre de sommets
    int nb_aretes = nB.nb_aretes;      //et le nombre d'aretes

    //AFFICHAGE
    if (DEBUG > 0) { 
        printf("\n[SERVEUR] Nombre de sommets : %d\n", nb_sommets);
        printf("[SERVEUR] Nombre d'aretes : %d", nb_aretes);
    }


    //VARIABLES PARTAGE
        //allocation partage
    struct partage * part = (struct partage*) malloc(sizeof(struct partage));       //partage
    struct info_nb * nbTout = (struct info_nb*) malloc(sizeof(struct info_nb));     //nb de sommets et arretes 
        //données recuperable
    nbTout->nb_aretes = nb_aretes;      //on donne le nombre d'arretes
    nbTout->nb_sommets = nb_sommets;    //on donne le nombre de sommets
    part->info_nb = nbTout;             //donne nbTout au variables partagé
        

    //APPEL DE LA FONCTION pour recuperer les arretes
    struct aretes *liste_aretes = (struct aretes*) malloc (nb_aretes *sizeof(struct aretes));  //on alloue de la memoire pour la liste des aretes
    Arretes(nom_fichier, nb_sommets, nb_aretes, liste_aretes);    //et on recuperer cette liste directement dans liste_aretes
 
    //AFFICHAGE
    if (DEBUG > 1) { 
        printf("\n\n\033[4mListe des arretes :\033[0m\n");

        //AFFICHAGE DES ARRETES
        for(int k=0; k<nb_aretes; k+=3) {
            printf("    %d -> %d    %d -> %d    %d -> %d\n", liste_aretes[k].noeud1, liste_aretes[k].noeud2, liste_aretes[k+1].noeud1, liste_aretes[k+1].noeud2, liste_aretes[k+2].noeud1, liste_aretes[k+2].noeud2);
        }
    }
    printf("\n************************************************\n************************************************\n");
    
    //on a donc dans le tableau liste_aretes la liste des aretes on aurait pu les trier en disant à l'indice i c'est le noeud i est il est relié avec tous les elements de sont tableau


    //ETAPE 2 : RECUPERATION DES INFORMATIONS SUR LE VOISINAGE 
      //tableau des nb de voisin
    struct nbVois nbVoisin[nb_sommets];               //tableau du nombre de voisin de chaque sommets
        //initialisation
    for (int i=0; i<nb_sommets; i++){ 
        nbVoisin[i].nbVoisinDemande = 0;
        nbVoisin[i].nbVoisinTotal = 0;
    }


    //TABLEAU DES NOMBRES DE VOISINS
    for (int a=0; a<nb_aretes; a++){
            //données
        int noeud1 = liste_aretes[a].noeud1;
        int noeud2 = liste_aretes[a].noeud2;             //si l'indice du noeud est present dans l'un des deux noeuds de l'arete
            //augmentation
        nbVoisin[noeud1-1].nbVoisinTotal++;        //on augmente pour le noeud 1   
        nbVoisin[noeud1-1].nbVoisinDemande++;      //on augmente le nombre de voisin a qui ont va demander
        nbVoisin[noeud2-1].nbVoisinTotal++;        //on aumgnete pour le neoud 2
    }
    //VARIABLES PARTAGE
    part->nbVois = (struct nbVois*) malloc(nb_sommets*sizeof(struct nbVois));     //allocation memoire
    part->nbVois = nbVoisin;                    //donner les nb de voisins au paratge
    

    //TABLEAU DES LISTES DE VOISINS
        //allocation de memoire
    int** liste_voisins_connexion = malloc(nb_sommets * sizeof(int*));                        //premiere allocation de memoire
    for (int i=0; i<nb_sommets; i++) {
        liste_voisins_connexion[i] = malloc(nbVoisin[i].nbVoisinTotal * sizeof(int));           //deuxieme allocation de memoire avec le nombre de voisin
    }

        //on va stocker dans un tableau tout les voisins à qui le noeud b doit se connecter
    for (int b=1; b<=nb_sommets; b++) { 
        int nb_demandes = 0;                                         //nombre de demandes du noeud b
        for (int c=0; c<nb_aretes; c++) {                            //on parcourt toutes les arêtes
            int noeud1 = liste_aretes[c].noeud1;                       //on récupère le noeud x de l'arête x-y
            if (noeud1 == b) {                                         //si le noeud1 doit demander
                int noeud2 = liste_aretes[c].noeud2;             				 //on récupère noeud2 ici pour gagner du temps si non utilisé
                liste_voisins_connexion[b-1][nb_demandes] = noeud2;      //on ajoute dans la liste des voisins à demander du noeud1 le noeud2
                nb_demandes++;
            }
        }
    }
    
        //affichage
    printf("\n[SERVEUR] \033[4mTableau des nb de voisins :\033[0m\n\n");
    for (int i=0; i<nb_sommets; i++){
        printf("    Noeud %d : %d voisins donct %d qui demande\n", i+1, part->nbVois[i].nbVoisinTotal, part->nbVois[i].nbVoisinDemande);
    }
    printf("\n************************************************\n************************************************\n");   

    //VARIABLE PARATGE
    part->listeVoisinCo = liste_voisins_connexion;   //donner les infos des graphes au partage


    //ETAPE 3 : CREATION SOCKET SERVEUR
    int dSServeur = creationSocket();                  //creation 
    //AFFICHAGE
    printf("\n[SERVEUR] Création de la socket réussie\n");
    printf("[SERVEUR] Le descripteur est %d \n", dSServeur);


    //NOMMAGE SOCKET
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
    int nbMaxEcoute = NOEUDS_MAX;                         //on fixe le nombre de processus maximum pour attente 
    ecouter(dSServeur, nbMaxEcoute);                      //on met en ecoute la socket serveur
        //affichage
    printf("\n[SERVEUR] La mise en ecoute de la socket du serveur réussi !\n");


    //MISE EN PLACE DES DONNEES
      //donnees util
    int dSNoeud;                                                                //declaration du descripteur
    struct infos_Graphe *procGraphe = (struct infos_Graphe*) malloc(nb_sommets * sizeof(struct infos_Graphe));         //on declare un tableau de structure pour les informations des Noeuds connecté au sevreur
    struct sockaddr_in sockNoeud;                                               //on declare la socket du Noeud
    socklen_t lgAdr;                                                            //taille de l'adresse
    int numSommet;                                                              //on declare le nombre de Noeud courant
        //thread
    pthread_t threads[nb_sommets];                                              //creation d'un tableau de thread
    for (int i=0; i<nb_sommets; i++){
        threads[i] = 0;
    }

    //ALLOCATION MEMOIRE 
        //allocation paramsInter1
    struct paramsInter1 * args = (struct paramsInter1*)malloc(sizeof(struct paramsInter1)); //aloue de la memoire pour les arguments
        //aloc pour tab des processus
    struct infos_Graphe* tabProc = (struct infos_Graphe*) malloc(nb_sommets*sizeof(struct infos_Graphe));
    struct infos_Graphe * infosNoeud = (struct infos_Graphe*) malloc(nb_sommets*sizeof(struct infos_Graphe)); //aloue de la memoire pour les arguments
        
        


    //BOUCLE POUR RECEVOIR LES NOEUDS    
    for (numSommet=1; numSommet<=nb_sommets; numSommet++) {
        
        printf("\n************************************************\n************************************************\n");   
        //ETAPE 5 : ACCEPTATION DU NOEUD
            //acceptation
        dSNoeud = accept(dSServeur, (struct sockaddr*)&sockNoeud, &lgAdr);          //on accepte le Noeud qui demande

        //AFFICHAGE
            //affichage
			 if (DEBUG > 1) {
        printf("\n[SERVEUR] Connexion d'un Noeud de descripteur %d\n", dSNoeud);
			}
        if (DEBUG > 3) { 
			if (numSommet < 2) {printf("[SERVEUR] 1 Noeud est connecté au serveur\n");}         //affichage du nombre de Noeud connecté
	        else {printf("[SERVEUR] %d Noeuds sont connectés au serveur\n", numSommet);}        //affichage du nombre de Noeud connecté
		}

        //VARIABLES PARATGE
        part->tabProc = tabProc;        //allocation de memoire dans paratge
            //info dans aigs
        infosNoeud->descripteur = dSNoeud;
        infosNoeud->adrProc = sockNoeud;
        args->procCourant = infosNoeud;              //donner les infos des graphes au partage
        args->varPartage = part;                     //on donne donc les infos sur les var partage

        //CREATION DU THREAD POUR LE CLIENT
        creationThread(&threads[numSommet-1], args, interaction1);    //creation du thread
        printf("\n[SERVEUR] Création d'un thread pour le noeud  %d\n", numSommet);

    } //fin de la premieère connexion avec tous les noeuds


    //TOUT LES NOEUDS SONT OCNNECTE
    printf("\n************************************************\n************************************************\n");   
    printf("\n[SERVEUR] Tous les Noeuds sont connectés !\n");
    printf("\n************************************************\n************************************************\n");   

    //AFFICHAGE DE LA LISTE DES NOEUDS CONNECTE
        //affichage
    printf("\n[SERVEUR] \033[4mListe des noeuds connectés:\033[0m\n");
    part = args->varPartage;    //Reattribution des valeurs
        //adresse
    char adrNoeudCoAff[INET_ADDRSTRLEN];                                       //on va stocker l'adresse du sous anneau dedans
        //parcourt des noeuds connectés au serveur                        
    for (int i=0; i<nb_sommets; i++) {     //on commence a un car les indice commence a 1
            //recuperation adresse
        inet_ntop(AF_INET, &(part->tabProc[i].adrProc.sin_addr), adrNoeudCoAff, INET_ADDRSTRLEN);     //adresse du Noeud    
            //port
        int portNoeudCoAff = htons(part->tabProc[i].adrProc.sin_port);                                 //port du Noeud
            //affichage
        printf("\n      Noeud d'indice %d de descripteur %i : %s:%i", i+1, part->tabProc[i].descripteur, adrNoeudCoAff, portNoeudCoAff);
    }

    printf("\n\n************************************************\n************************************************\n");   
    //MISE EN ATTENTE DU SERVEUR POUR QU'IL EST TOUTES LES INFORAMTIONS DES NOEUDS AVANT D'ENVOYER LEURS VOISINS
    char e ;
    printf("\n[SERVEUR] : Entrez un caractère après avoir reçu toutes les informations des noeuds : ");  //on demmande au client d'entrer un message
    scanf("%c", &e);
    //FIN MISE EN ATTENTE

    printf("\n************************************************\n************************************************\n\n");   

    //ETAPE 8 : ENVOIE DES INFORMATIONS DE CONNEXION AUX NOEUDS VOISINS
    //BOUCLE SOMMET
    for (int i=0; i<nb_sommets; i++) {     //pour chaque noeuds

            //donnees
        int nbVoisinDemande = nbVoisin[i].nbVoisinDemande;			//nb voisin a qui tu dois demander du sommets courant 
        
        //BOUCLE de autant de voisin que le sommet va demander à se connecter
        for (int v=0; v<nbVoisinDemande; v++) {
                //données
            int voisinCourant = liste_voisins_connexion[i][v];												//on recupere l'insdice du voisin courant
            struct infos_Graphe info_voisin_courant;														//structure du voisin courant (-1 car l'indice commence a 1)
            info_voisin_courant.numero = procGraphe[voisinCourant-1].numero;
            info_voisin_courant.descripteur = procGraphe[voisinCourant-1].descripteur;
            info_voisin_courant.adrProc = procGraphe[voisinCourant-1].adrProc;
                //envoie
            sendCompletTCP(procGraphe[i].descripteur, &info_voisin_courant, sizeof(struct infos_Graphe));   //on envoie les inforamtions ici adresse des voisins
                //affichage ajout des voisins au sommet
            printf("[SERVEUR] Ajout du voisins %d au sommet %d\n",info_voisin_courant.numero, i+1);
                
        }//fin des voisins

    }//fin des sommets




    //ETAPE 9 : FERMETURE DE LA SOCKET SERVEUR CAR PLUS BESOIN
        //fermeture
    close(dSServeur);
        //affichage
    printf("\n[SERVEUR] Je peux m'en aller !\n\n");
        //on free les allocations mémoires
    free(liste_aretes);
    free(procGraphe);
    for (int z=0; z<nb_sommets; z++) {
        free(liste_voisins_connexion[z]);
    }
    free(liste_voisins_connexion);
        //on sort du programme
    exit(0);
      
      
    
}//fin du main