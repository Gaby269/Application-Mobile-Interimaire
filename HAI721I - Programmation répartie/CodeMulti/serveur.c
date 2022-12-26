#include "fonctions.c" 
    
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
	int nombre_connexion = 0;			//pour calculer le nombre de connexion au serveur

	
//A - CONSTRUCTION DU SERVEUR

    //ETAPE 1 : RECUPERATION DES INFO DU GRAPHE

		//a) recuperation des nombres d'aretes et de sommets
    struct info_nb nB;                  //on declare une structure des informations sur les nombres de sommets et d'aretes
    nB = nbAreteNbNoeud(nom_fichier);  	//on recupere les nombres important dans nB
    int nb_sommets = nB.nb_sommets;    	//on recupere le nombre de sommets
    int nb_aretes = nB.nb_aretes;      	//et le nombre d'aretes
    	//affichage de ces nombres
	printf("\n[SERVEUR] Nombre de sommets : %d\n", nb_sommets);
	printf("[SERVEUR] Nombre d'aretes : %d", nb_aretes);

    	//b) recuperer des aretes entre les noeuds
    struct aretes *liste_aretes = (struct aretes*) malloc (nb_aretes *sizeof(struct aretes));  	//on alloue de la memoire pour la liste des aretes
    Aretes(nom_fichier, liste_aretes);    														//et on recupere cette liste directement dans liste_aretes
	//listes_aretes : tableau des listes des aretes a la case i on a le numero du noeud 1 et le numero du noeud 2 contenudans une structure arete
    	//affichage de la liste des arretes
	printf("\n\n\033[4mListe des aretes :\033[0m\n");
	//boucle pour le tableau
	for(int k=0; k+2<nb_aretes; k+=3) {
		printf("    %d -> %d    %d -> %d    %d -> %d\n", liste_aretes[k].noeud1, liste_aretes[k].noeud2, liste_aretes[k+1].noeud1, liste_aretes[k+1].noeud2, liste_aretes[k+2].noeud1, liste_aretes[k+2].noeud2);
	}
    
    printf("\n************************************************\n************************************************\n");
	

    //ETAPE 2 : RECUPERATION DES INFORMATIONS SUR LE VOISINAGE 

		//a) données
    struct nbVois nbVoisin[nb_sommets];               //tableau du nombre de voisin de chaque sommets
        //initialisation du tableau
    for (int i=0; i<nb_sommets; i++){ 
        nbVoisin[i].nbVoisinDemande = 0;
        nbVoisin[i].nbVoisinTotal = 0;
		nbVoisin[i].nbNoeuds = nb_sommets;
    }

    	//b) remplissage du tableau
    for (int a=0; a<nb_aretes; a++){
            //données
        int noeud1 = liste_aretes[a].noeud1;
        int noeud2 = liste_aretes[a].noeud2;
            //augmentation
        nbVoisin[noeud1-1].nbVoisinTotal++;        //on augmente pour le noeud 1 son nombre total de voisin
        nbVoisin[noeud1-1].nbVoisinDemande++;      //on augmente le nombre de voisin a qui ont va demander de noeud1
        nbVoisin[noeud2-1].nbVoisinTotal++;        //on aumgnete pour le noeud 2 le nombre total de voisin
    }

    	//c) remplissage de la liste des voisins de chaque noeud
        //allocation de memoire
    int** liste_voisins_connexion = malloc(nb_sommets * sizeof(int*));                        //premiere allocation de memoire pour le nombre de sommets
    for (int i=0; i<nb_sommets; i++) {
        liste_voisins_connexion[i] = malloc(nbVoisin[i].nbVoisinTotal * sizeof(int));         //deuxieme allocation de memoire pour le nombre de voisin
    }
        //stockage dans un tableau tout les voisins à qui le noeud de numero b doit se connecter
    for (int b=1; b<=nb_sommets; b++) { 
        int nb_demandes = 0;                                         	//nombre de demandes du noeud b
        for (int c=0; c<nb_aretes; c++) {                            	//on parcourt toutes les arêtes
            int noeud1 = liste_aretes[c].noeud1;                     	//on récupère le noeud x de l'arête x->y
            if (noeud1 == b) {                                       	//si le noeud1 doit demanderdonc si c'est egal
                int noeud2 = liste_aretes[c].noeud2;             		//on récupère noeud2
                liste_voisins_connexion[b-1][nb_demandes] = noeud2;   	//on ajoute dans la liste des voisins à demander du noeud1 le noeud2 a b-1 car b est le numero et non l'indice
                nb_demandes++;											//incrementation du nombre de demande qui est l'indice des voisin
            }	
        }
    }
        //affichage du tableau des nombre des voisins de chaque noeud
    printf("\n[SERVEUR] \033[4mTableau des nb de voisins :\033[0m\n\n");
    for (int i=0; i<nb_sommets; i++){
        printf("    Noeud %d : %d voisins donct %d qui demande\n", i+1, nbVoisin[i].nbVoisinTotal, nbVoisin[i].nbVoisinDemande);
        nombre_connexion+=nbVoisin[i].nbVoisinDemande;
    }
    printf("\n************************************************\n************************************************\n");   
    

    //ETAPE 3 : GESTION DE LA SOCKET SERVEUR

		//a) creation de la socket
    int dSServeur = creationSocket();                  //creation de la socket
    	//affichage
    printf("\n[SERVEUR] Création de la socket réussie\n");
    printf("[SERVEUR] Le descripteur est %d \n", dSServeur);

    	//b) nommage de la socket
    struct sockaddr_in adrServeur = nommageSocket(dSServeur, portServeur);              //nommer la socket avec son port
        //adresse
    char adrServeurAff[INET_ADDRSTRLEN];
    inet_ntop(AF_INET, &adrServeur.sin_addr, adrServeurAff, INET_ADDRSTRLEN);
        //port
    int portServeurAff = htons(adrServeur.sin_port);
        //affichage
    printf("\n[SERVEUR] Le port est %d\n", portServeurAff);                	//affichage du port
    printf("[SERVEUR] L'adresse est %s\n", adrServeurAff);                	//affichage de l'adresse


    //ETAPE 4 : MISE SOUS ECOUTE
    int nbMaxEcoute = NOEUDS_MAX;                         //on fixe le nombre de processus maximum pour attente 
    ecouter(dSServeur, nbMaxEcoute);                      //on met en ecoute la socket serveur
        //affichage
    printf("\n[SERVEUR] La mise en ecoute de la socket du serveur réussi !\n");


	
//B - RECEPTION DES INFORMATIONS DES SOMMETS
	
    //a) Mise en place des données
        //donnees utils
    int dSNoeud;                                                                //declaration du descripteur
    struct infos_Graphe *procGraphe = (struct infos_Graphe*) malloc(nb_sommets * sizeof(struct infos_Graphe));         //on declare un tableau de structure pour les informations des Noeuds connecté au sevreur
    struct sockaddr_in sockNoeud;                                               //on declare la socket du Noeud
    socklen_t lgAdr;                                                            //taille de l'adresse
    int numSommet;                                                              //on declare le nombre de Noeud courant

    //b) boucle pour recevoir les noeuds
    for (numSommet=1; numSommet<=nb_sommets; numSommet++) {
        
        printf("\n************************************************\n************************************************\n");
		
        //ETAPE 5 : ACCEPTATION DU NOEUD
            //acceptation
        dSNoeud = accept(dSServeur, (struct sockaddr*)&sockNoeud, &lgAdr);          //on accepte le Noeud qui demande
            //affichage
		printf("\n[SERVEUR] Connexion d'un Noeud de descripteur %d\n", dSNoeud);

		if (numSommet < 2) {printf("[SERVEUR] 1 Noeud est connecté au serveur\n");}         //affichage du nombre de Noeud connecté
		else {printf("[SERVEUR] %d Noeuds sont connectés au serveur\n", numSommet);}        //affichage du nombre de Noeud connecté

			
        //ETAPE 6 : RECEPTION DES INFORMATIONS DU NOEUD
            //donnees
        struct infos_Graphe info_proc;                      //structure qui va recuperer les informations qu'un Noeud a envoyer
            //reception des informations
        recvCompletTCP(dSNoeud, &info_proc, sizeof(struct infos_Graphe));       //reception des informations dans info_proc
            //modification des donnees dans le tableau des processus
        sockNoeud = info_proc.adrProc;                                          //donner a sockNoeud l'adresse recu dans info_proc
        int indice_proc = info_proc.numero-1;                                   //donne l'indice
        procGraphe[indice_proc].numero = indice_proc+1;                         //on attribut l'indice du noeud +1 car on donne le numero et non l'indice
        procGraphe[indice_proc].descripteur = dSNoeud;                          //on attribut le descripteur
        procGraphe[indice_proc].adrProc = sockNoeud;                            //on attribut l' adresse
        //AFFICHAGE
            //adresse
        char adrProcAff[INET_ADDRSTRLEN];             //on va stocker l'adresse du sous anneau dedans
        inet_ntop(AF_INET, &sockNoeud.sin_addr, adrProcAff, INET_ADDRSTRLEN);     	//adresse du Noeud    
            //port
        int portProcAff = htons(sockNoeud.sin_port);                                 //port du Noeud
            //affichage
        printf("\n[SERVEUR] \033[4mLe processus a comme informations après réception :\033[0m\n");
        printf("\n       Adresse du processus : %s\n       Port : %d", adrProcAff, portProcAff);
        printf("\n       Numéro du noeud dans le graphe : %d\n       Descripteur de la socket du processus : %d\n\n", indice_proc+1, procGraphe[indice_proc].descripteur);

			
//C - ENVOI DES INFORMATIONS AUX SOMMETS
			
        //ETAPE 7 : ENVOI DU NOMBRE DE VOISIN A CHAQUE NOEUDS
            //descripteur de socket courant
        int dS_courant = procGraphe[indice_proc].descripteur;                  //indice courant
            //envoi
        sendCompletTCP(dS_courant, &nbVoisin[indice_proc], sizeof(struct nbVois));
            //affichage
        printf("\n[SERVEUR] \033[4mInformations envoyées :\033[0m\n\n");
		printf("	  Nombre de noeuds total dns le graphe = %d\n", nbVoisin[indice_proc].nbNoeuds);
        printf("      Nombre de voisin total = %d\n", nbVoisin[indice_proc].nbVoisinTotal);
        printf("      Nombre de voisin de demande = %d\n", nbVoisin[indice_proc].nbVoisinDemande);
		
        //ETAPE : ENVOIE DE TON ORDRE POUR LA SUITE
            //envoi
        sendCompletTCP(dS_courant, &numSommet, sizeof(int));
            //affichage
        printf("\n[SERVEUR] Je t'envoie ton ordre %d\n", numSommet);


    } //fin de la premieère connexion avec tous les noeuds
	
    printf("\n************************************************\n************************************************\n");   
    printf("\n[SERVEUR] Tous les Noeuds sont connectés !\n");
    printf("\n************************************************\n************************************************\n");   

        //affichage de la liste des noeuds qui sont connectés
    printf("\n[SERVEUR] \033[4mListe des noeuds connectés:\033[0m\n");
        //adresse
    char adrNoeudCoAff[INET_ADDRSTRLEN];                                       //on va stocker l'adresse du sous anneau dedans
        //parcourt des noeuds connectés au serveur                        
    for (int i=0; i<nb_sommets; i++) {              //on commence a un car les indice commence a 1
            //recuperation adresse
        inet_ntop(AF_INET, &procGraphe[i].adrProc.sin_addr, adrNoeudCoAff, INET_ADDRSTRLEN);     //adresse du Noeud    
            //port
        int portNoeudCoAff = htons(procGraphe[i].adrProc.sin_port);                              //port du Noeud
            //affichage
        printf("\n      Noeud d'indice %d de descripteur %i : %s:%i", i+1, procGraphe[i].descripteur, adrNoeudCoAff, portNoeudCoAff);
    }
    printf("\n\n************************************************\n************************************************\n");   
	
    //MISE EN ATTENTE DU SERVEUR POUR QU'IL EST TOUTES LES INFORAMTIONS DES NOEUDS AVANT D'ENVOYER LEURS VOISINS
    char pause;
    printf("\n[SERVEUR] : Appuyez sur ENTER pour continuer...");  //on fais un "pause()"
    scanf("%c", &pause);
    //FIN MISE EN ATTENTE

    printf("\n************************************************\n************************************************\n\n");   

	
    //ETAPE 8 : ENVOIE DES INFORMATIONS DE CONNEXION AUX NOEUDS VOISINS

			//a) boucle du parcourt des sommets
    for (int i=0; i<nb_sommets; i++) {    							//pour chaque noeuds
            //donnees
        int nbVoisinDemande = nbVoisin[i].nbVoisinDemande;			//nb voisin a qui tu dois demander une connection 
        
        //BOUCLE de autant de voisin que le sommet va demander à se connecter
        for (int v=0; v<nbVoisinDemande; v++) {
                //données
            int voisinCourant = liste_voisins_connexion[i][v];								//on recupere l'insdice du voisin courant
            struct infos_Graphe info_voisin_courant;										//structure du voisin courant (-1 car voisinCourant est le numero du voisin dans le graphe et non l'indice dans le tableau donc il commence a 1)
            info_voisin_courant.numero = procGraphe[voisinCourant-1].numero;				//on y attribut un numero
            info_voisin_courant.descripteur = procGraphe[voisinCourant-1].descripteur;		//un descripteur
            info_voisin_courant.adrProc = procGraphe[voisinCourant-1].adrProc;				//et une adresse
                //envoie au voisin a qui je demande
            sendCompletTCP(procGraphe[i].descripteur, &info_voisin_courant, sizeof(struct infos_Graphe));   //on envoie les inforamtions ici adresse des voisins
                //affichage ajout des voisins au sommet
            printf("[SERVEUR] Ajout du voisins %d au sommet %d\n",info_voisin_courant.numero, i+1);
                
        }//fin des voisins

    }//fin des sommets

    printf("\n[SERVEUR] Il doit y avoir %d demande de connection\n", nombre_connexion);

    sleep(3);
    printf("\n[SERVEUR] : Appuyez sur ENTER pour continuer et commencer la coloration du graphe...");  //Après la connexion entre tous les noeuds
    scanf("%c", &pause);

    
    for (int i=0; i<nb_sommets; i++) {    							//pour chaque noeuds
            //envoi du signal
        int signal = 1;
        sendCompletTCP(procGraphe[i].descripteur, &signal, sizeof(int));   
            //affichage ajout des voisins au sommet
        printf("[SERVEUR] Envoi du signal au sommet %d\n", i+1);
    }


    //ETAPE 9 : FERMETURE DE LA SOCKET SERVEUR CAR PLUS BESOIN
	
        //a) fermeture
    close(dSServeur);
    printf("\n[SERVEUR] Je peux m'en aller !\n\n");
	
        //b) liberer les allocations de memoires
    free(liste_aretes);
    free(procGraphe);
    for (int z=0; z<nb_sommets; z++) {
        free(liste_voisins_connexion[z]);
    }
    free(liste_voisins_connexion);
	
        //c) on sort du programme
    exit(0);
      
      
    
}//fin du main