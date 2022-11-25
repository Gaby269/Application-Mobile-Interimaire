#include "fonctions.c"
#include <math.h>



///////////////////////////////
// FONCTION POUR LES THREADS //
///////////////////////////////


void* SuiteNoeud(void * p){

    //GESTION PARAMETRES DE LA STRUTURE
        //arguments
    struct paramsNoeud * args = (struct paramsNoeud *) p;      	//parametres
    //struct partage * varPartage = args->varPartage;         	//si on a des variables partagées
    
    int etreAcc = args->Acc;                                  	//recuperation du boolean qui nous dit si on a a faire a une acceptation ou si c'est une connexion
    int numeroMoi = args->numero_proc;                          //recup du numero du noeud courant
        //info voisin
    int indice_vois = args->indice_vois;                        //indice du voisin sortant
    struct infos_Graphe *Voisin = args->VoisinCourant;          //info du voisin
    int dSVois = Voisin->descripteur;                         //descripteur du noeud
    int numero_vois = Voisin->numero;                         //numero voisin
        //thread
    pthread_t threadCourant = pthread_self();                 //identifiant du thread

	
	//Si je suis pas accepteur alors je fais une connexion
    if (etreAcc == FALSE){

        //AFFICHAGE DES INFO
            //addresse
        char adrDem[INET_ADDRSTRLEN];
        inet_ntop(AF_INET, &Voisin->adrProc.sin_addr, adrDem, INET_ADDRSTRLEN);
            //port
        int portDem = htons((short) Voisin->adrProc.sin_port); 
            //affichage
        printColorThread(numeroMoi, threadCourant);printColorPlus(numeroMoi, "ADRESSE");printf("du voisin a qui je demande est %s:%d\n", adrDem, portDem);
        printColorThread(numeroMoi, threadCourant);printColorPlus(numeroMoi, "DEMANDE");printf("de connexion au %d-ème voisin réussie !\n", indice_vois);
        
/*
        //ETAPE 11 : ENVOIE D'UN MESSAGE JE SUIS MOI POUR PLUS TARD
        sendCompletTCP(dSVois, &numeroMoi, sizeof(int));
            //affichage
        printColorPlus(numeroMoi, "ENVOIE");printf("de mon indice à %d\n", numero_vois);
*/
    }
	//Sinon j'ai accepté un noeud
    else{

        printColorThread(numeroMoi, threadCourant);printColorPlus(numeroMoi, "ACCEPTATION");printf("du %d-ème voisin\n", indice_vois);

/*
        //RECEPTION DES INFORMATIONS DDES VOISINS ENTRANT POUR PLUS TARD
        int numero_Voisin;                                         // entier qui va etre le numero du noeud qui se connecte
            //reception
        recvCompletTCP(dSVois, &numero_Voisin, sizeof(int));       // reception de l'entier dans numero_Voisin

        //AFFICHAGE
        printColorPlus(numeroMoi, "RECEPTION");printf("du noeud %d qui est mon voisin\n", numero_Voisin);

        args->VoisinCourant->numero = numero_Voisin;
*/
    }

    pthread_exit(NULL);         //sortie du thread

     
}




/////////////////////////
//   PROGRAME NOEUDS   //
/////////////////////////
int main(int argc, char *argv[]) {

    //GESTION DES INFORMATIONS DONEES EN PARAMETRES
    if (argc != 5){
        printf("\n[UTILISATION] %s ip_serveur port_serveur port_noeud numero_noeud\n\n", argv[0]);
        exit(1);
    }

    char* adresseIP = argv[1];          	//adresse ip du serveur
    char* port_serveur = argv[2];       	//port du serveur
    char* port_noeud = argv[3];         	//port du noeud
    int numero_noeud = atoi(argv[4]);    	//numero du client


    //AFFICHAGE DES DONNEES EN PARAMETRES
    printColorNoeud("\n************************************************\n------------------- NOEUD ", numero_noeud, " -------------------\n************************************************\n\n");
	printColor(numero_noeud);printf("\033[4mInforamtions données en paramètres :\033[0m\n");
	printf("\n       Adresse du serveur : %s\n       Port : %d", adresseIP, atoi(port_serveur));
	printf("\n       Port du noeud : %d", atoi(port_noeud));
	printf("\n       Indice du processus : %d", numero_noeud);
	

//A - CONSTRUCTION D’UN SOMMET

    //ETAPE 1 : CREATION DE LA SOCKET QUI DISCUTE AVEC SERVEUR
    int dSProcCS = creationSocket();
        //affichage
    printColor(numero_noeud);printf("Création de la socket pour serveur réussie !\n");
    printColor(numero_noeud);printf("Le descripteur du noeud est %d \n\n", dSProcCS);

  
    //ETAPE 2 : DESIGNATION DE LA SOCKET SERVEUR
    struct sockaddr_in sockServ = designationSocket(adresseIP, port_serveur);
    //AFFICHAGE
        //addresse
    char adrServ[INET_ADDRSTRLEN];
    inet_ntop(AF_INET, &sockServ.sin_addr, adrServ, INET_ADDRSTRLEN);
        //port 
    int portServ = htons((short) sockServ.sin_port);
    printColor(numero_noeud);printf("Designation de la socket du serveur réussi ! \n");
    printColor(numero_noeud);printf("Le serveur a donc pour adresse %s:%d\n\n", adrServ, portServ);


    //ETAPE 3 : DEMANDE DE CONNEXION AU SERVEUR
    connexion(dSProcCS, &sockServ);
    //AFFICHAGE
    printColor(numero_noeud);printf("Connexion au serveur réussi !\n\n");


	
//B - CONSTRUCTION DE LA SOCKET DE RECEPTION : Creation d'une autre socket qui va jouer le rôle de serveur pour ses voisins qui demande une connexion

    //ETAPE 4 : GESTION DE LA SOCKET QUI JOUE LE RÔLE DE SERVEUR POUR LES AUTRES NOEUDS

		//a) creation de la socket
    int dSProcArete = creationSocket();
    	//affichage
    printColor(numero_noeud);printf("Création de la socket d'écoute réussi !\n");
    printColor(numero_noeud);printf("Le descripteur du noeud est %d \n\n", dSProcArete);

    	//b) nommage de la socket
    struct sockaddr_in sockArete = nommageSocket(dSProcArete, port_noeud);
    	//affichage
        //adresse
    char adrArete[INET_ADDRSTRLEN];
    inet_ntop(AF_INET, &sockArete.sin_addr, adrArete, INET_ADDRSTRLEN);
        //port
    int portArete = htons((short) sockArete.sin_port);
        //affichage
    printColor(numero_noeud);printf("Informations du noeud (socket) de descripteur %d : %s:%i\n\n", dSProcArete, adrArete, portArete);


    //ETAPE 5 : MISE SOUS ECOUTE DE LA SOCKET
    int nbMaxAttente = NOEUDS_MAX;
    ecouter(dSProcArete, nbMaxAttente);
        //AFFICHAGE
    printColor(numero_noeud);printf("Mise en ecoute de la socket réussie !\n\n");

    // On vient de finir de mettre en ecoute une socket qui va avoir le rôle de serveur pour ses voisins qui demande une connexion


//C - ENVOI LES INFORMATIONS AU SERVEUR

    // On renvient sur la socket qui discute avec le serveur

    //ETAPE 6 : ENVOIE DES INFORMATIONS AU SERVEUR
        //informations du noeud
    struct infos_Graphe informations_proc;      		//declare la structure qu'on va envoyer au serveur
    informations_proc.numero = numero_noeud;        	//donne l'indice du processus
    informations_proc.descripteur = dSProcArete;  		//donne le descripteur
    informations_proc.adrProc = sockArete;          	//donner l'adresse de la socket
        //envoie des informations
    sendCompletTCP(dSProcCS, &informations_proc, sizeof(struct infos_Graphe));
        //affichage
    printColor(numero_noeud);printf("Envoi des inforamtions réussi au serveur !\n");
    printColor(numero_noeud);printf("\033[4mEnvoie des informations suivantes :\033[0m\n");
    printf("\n       Adresse du processus : %s\n       Port : %d", adrArete, portArete);
    printf("\n       Numéro du noeud : %d\n       Descripteur de la socket du processus : %d\n\n\n", numero_noeud, dSProcArete);


//D - RECEPTION DES INFORMATIONS DU SERVEUR
	
    //ETAPE 7 : RECEPTION DU NB VOISIN
    struct nbVois nbVoisin;		//on declare la structure qui va contenir le nombre de voisin total et le nombre de voisin a qui il doit demander une connexion
        //reception
    recvCompletTCP(dSProcCS, &nbVoisin, sizeof(struct nbVois));  //on reutilise la structure informations_proc pour la reception
        //données recuperer
    int nbVoisinTotal = nbVoisin.nbVoisinTotal;
    int nbVoisinDemande = nbVoisin.nbVoisinDemande;
    int nbVoisinAttente = nbVoisinTotal - nbVoisinDemande;		//on peut alors déduire le nombre de voisin que la socket doit attendre
			//affichage
    printColor(numero_noeud);printf("Reception du nombre de voisin réussi ! \n");
    printf("	Nombre de voisin total : %d\n", nbVoisinTotal);
	printf("	Nombre de voisin auquels envoyer une demande de connexion : %d\n\n", nbVoisinDemande);   //ajout d'une delimitation

	printf("\n************************************************\n************************************************\n\n\n");   

	
    //ETAPE 8 : RECEPTIONS DES INFORMATIONS DES VOISINS A QUI TU DEMANDE UNE CONNEXION DONC DES SOMMETS QU'IL DOIT CONTACTER
	
    	//a) donnée tous les voisins auxquels on doit se connecter
    struct infos_Graphe* info_voisins = (struct infos_Graphe*) malloc(nbVoisinDemande * sizeof(struct infos_Graphe)); //tableau des informations des voisins du noeud courant
	
	    //b) parcours du nombre de voisin a qui on demande
    for (int v=0; v<nbVoisin.nbVoisinDemande; v++) {
            //données
        struct infos_Graphe info_voisin_courant;     						 //structure du voisin courant
            //reception des informations de l'un de tes voisins
        recvCompletTCP(dSProcCS, &info_voisin_courant, sizeof(struct infos_Graphe));
            //ajout dans le tableau
        info_voisins[v] = info_voisin_courant;			//on ajout ces informations dans le tableau prevu a cet effet
            //affichage
        printColorPlus(numero_noeud, "RECEPTION");printf("-> connexion à %d\n", info_voisin_courant.numero);	
        
    }

			//c) Fermeture de la socket qui discute avec le serveur pour pouvoir couper la communication
    close(dSProcCS);
        //affichage
	printColorPlus(numero_noeud, "FERMETURE");
	printf("Je me déconnecte du serveur !\n");
    
    
    //Fin de la communication entre le client et le serveur


//E - MISE EN CONTACT DES SOMMETS

	//On revient maintenant sur la socket qu'on a mise en écoute au début du fichier nommé dSProcArete

    //ETAPE 9 : CREATION DES STRUCTURES POUR LES CONNEXIONS
	
    	//a) creation d'un tableau pour les threads
    pthread_t threads[nbVoisinTotal];                           //crée le tableau des threads en entier
    	//init 
    for (int k=0; k<nbVoisinTotal; k++){
        threads[k] = 0;
    }
	  //struct partage partage;        //on declare les variables partagé (la structure) mais actuellement on en a pas besoin
			//arguments pour threads
    struct paramsNoeud argsCo[nbVoisinDemande];
    struct paramsNoeud argsAcc[nbVoisinAttente];

    	//b) descripteurs des sockets
	int dSVoisinDemande = 0;
    int dSVoisinEntrant = 0;
        //adresse
    struct sockaddr_in sockVoisinAccept;          //on declare la socket du Noeud
    socklen_t lgAdr;                              //taille de l'adresse

    	//c) compteurs
    int cptAcc = 0;     //compteur pour acceptation pour parcourir le tableau des voisins et les arguments de argsAcc
    int cptCo = 0;      //compteur pour connexion pour parcourir des parametres argsCo

	
     //BOUCLE pour parcourir le tableau des threads qui sera de la taille du nombre de voisin total qu'un noeud a
    for (int indiceVoisin=0; indiceVoisin<nbVoisinTotal; indiceVoisin++) {

        //partage.cptTotal = &nbVoisinTotal;			//compteur inutilisé mais peut etre plus tard

        //si je n'ai pas encore attent le nombre de connexion je continue
        if (cptCo < nbVoisinDemande){
					
			//////////////////////////
			// DEMANDE DE CONNEXION //
			//////////////////////////

			//ETAPE 10 : DEMANDE DE CONNEXION AU VOISIN

				//a) Création de la sokcet qui discute avec le processus voisin
            dSVoisinDemande = creationSocket();
                //affichage
            printColorPlus(numero_noeud, "CREATION");printf("de la socket qui demande réussi !\n");
            printColorPlus(numero_noeud, "DESCRIPTEUR");printf("du noeud créé est %d\n", dSVoisinDemande);
        
              	//b) designation de la socket du voisin
            struct sockaddr_in sockVoisin = info_voisins[cptCo].adrProc;								//recuperation des informations du voisin qui est ton voisin en fonction de son indice 
            printColorPlus(numero_noeud, "DESIGNATION");printf("de la socket du voisin réussie\n");
              
				//c) demande de connection
            connexion(dSVoisinDemande, &sockVoisin);													//connexion du descripteur qu'on vient de créer et la socket du voisin courant
            printColorPlus(numero_noeud, "CONNEXION");printf("d'un voisin réussi !\n");

            	//d) on donne les parametres pour les threads
            struct infos_Graphe VoisinCourant;							//structure des informations du voisin courant
            VoisinCourant.numero = info_voisins[cptCo].numero;			//son numero
            VoisinCourant.descripteur = dSVoisinDemande;				//son descripteur
            VoisinCourant.adrProc = sockVoisin;    						//on donne la socket du voisin
                //on fixe les parametres
            argsCo[cptCo].idThread = cptCo;								//indice pour le thread
            argsCo[cptCo].numero_proc = numero_noeud;					//le numero du noeud courant
            argsCo[cptCo].indice_vois = cptCo;							//indice du voisin
            argsCo[cptCo].VoisinCourant = &VoisinCourant;				//les info du voisin courant
            argsCo[cptCo].Acc = FALSE;                          		//donne l'info que je suis pas une acceptation
            //argsCo[cptCo].varPartage = &partage;                		//donneles partage qu'on a pas besoin actuellement

					
            //ETAPE 11 CREATION DU THREAD POUR COMMUNIQUER AVEC LE VOISIN
            printColorPlus(numero_noeud, "CREATION THREAD CO");printf("pour le noeud d'indice %d\n", cptCo);
            creationThread(&threads[indiceVoisin], argsCo, SuiteNoeud);    //creation du thread
            cptCo++;		//on incremente le compteur des connection pour passer au suivant

        }

        //si jai pas encore le bon nombre de connexion je continue
        if (cptAcc < nbVoisinAttente) {

			/////////////////////////////
			// ACCEPTER LES CONNEXIONS //
			/////////////////////////////

            //ETAPE 12 : ACCEPTATION DU NOEUD DONC RECEPTION DES CONNEXIONS
					
                //a) acceptation
            dSVoisinEntrant = accept(dSProcArete, (struct sockaddr*)&sockVoisinAccept, &lgAdr);     //on accepte le Noeud qui demande
			
				//GESTION ERREUR
				if (dSVoisinEntrant == ERREUR) {
						perror("\n\n[ERREUR] lors de l'accept d'un voisin : ");
						close(dSProcCS);
						exit(1); // on arrête le programme
				}
						
				//affichage
            printColorPlus(numero_noeud, "ACCEPT");printf("d'unn voisin réussi !\n");

            	//b) informations qu'on doit donner en parametres du threas
            struct infos_Graphe VoisinCourant;					//creation de la structure du voisin courant
            VoisinCourant.descripteur = dSVoisinEntrant;    	//on ne veut que le descripteur
                //ARGS
            argsAcc[cptAcc].idThread = cptAcc;					//on donne l'indice pour le idThread
            argsAcc[cptAcc].numero_proc = numero_noeud;			//on donne le numero du noeud courant (et non le voisin)
            argsAcc[cptAcc].indice_vois = cptAcc;				//on donne l'indice d'acceptation ou on en est
            argsAcc[cptAcc].VoisinCourant = &VoisinCourant;		//on donne les informations du voisin courant ici que le descripteur
            argsAcc[cptAcc].Acc = TRUE;							//on dit qu'on est une acceptation
            //argsAcc[cptAcc].varPartage = &partage;          	//donneles partage qu'on a pas besoin
					

			//ETAPE 13 CREATION DU THREAD POUR COMMUNIQUER AVEC LE VOISIN
			printColorPlus(numero_noeud, "CREATION THREAD ACC");printf("pour le noeud d'indice %d\n", cptAcc);
            creationThread(&threads[indiceVoisin], argsAcc, SuiteNoeud);    //creation du thread
            cptAcc++;			//incrementation du compteur de l'acceptation
        } 

    }//fin des demandes
    

    //Join des threads actuellement on en a pas besoin parce qu'on fait que des affichages qui sont assez vite a faire avant que la socket ne se ferme
    joinThreads(threads, nbVoisinTotal, numero_noeud);


    //AFFICHAGE du nombre de connexion effectuées et d'acceptation effectuées
    printColorPlus(numero_noeud, "NOMBRE CONNEXION");printf("J'ai demandé a %d noeud une connexion\n", cptCo);
    printColorPlus(numero_noeud, "NOMBRE ACCEPTATION");printf("J'ai accepté %d noeud\n", cptAcc);
    
    //liberation des pointeurs
    free(info_voisins);

    //FERMETURE DE LA SOCKET CLIENTE QUI ECOUTE ET DES SOCKET QUI ACCEPT ET QUI SE CONNECTE CAR PLUS BESOIN
    printColorPlus(numero_noeud, "FERMETURE");printf("Je peux m'en aller !\n");
        //fermeture
    close(dSVoisinDemande);
    close(dSVoisinEntrant);
    close(dSProcCS);


} // Fin du main