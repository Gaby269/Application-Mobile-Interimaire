#include <netinet/in.h>
#include <stdio.h>
#include <sys/types.h>
#include <netdb.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include "parseur.c"
#include "fonctions.c"


/* Programme Noeud */
  
/////////////////////////
// PROGRAME PRINCIPAL  //
/////////////////////////
int main(int argc, char *argv[]) {

    // ETAPE 1 : GESTION PARAMETRES
    if (argc != 3) {
        printf("\n[UTILISATION] : %s port_serveur fichier_graphe\n\n", argv[0]);
        exit(1);
    }

    char* portServeur = argv[1];        //port du serveur
    char* nom_fichier = argv[2];        //nom du fichier ou recuperer la structure du graphe


    //ETAPE 1.25 : REUPERATION DES INFO DU GRAPHE
    struct info_nb nB;                  //on declare une structure 
    nB = nbAreteNbNoeud(nom_fichier);  //on recupere les nombre important
    int nb_sommets = nB.nb_sommets;    //on recupere le nombre de sommets
    int nb_aretes = nB.nb_aretes;      //et le nombre d'aretes

    // AFFICHAGE
    if (DEBUG > 0) { 
        printf("\nNombre de sommets : %d\n", nb_sommets);
        printf("Nombre d'aretes : %d", nb_aretes);
    }


    //appel de la fonction pour recuperer les arretes
    struct aretes *liste_aretes = (struct aretes*) malloc (nb_aretes *sizeof(struct aretes));  //on alloue de la memoire pour la liste des aretes
    Arretes(nom_fichier, nb_sommets, nb_aretes, liste_aretes);    //et on recuperer cette liste directement dans liste_aretes

    //AFFICHAGE
  if (DEBUG > 1) { 
    printf("\n\nListe des arretes :\n");
   }

    // AFFICHAGE DES ARRETES
    for(int k=0; k<nb_aretes; k++) {
        printf("%d -> %d\n", liste_aretes[k].noeud1, liste_aretes[k].noeud2);
    }
    
    //on a donc dans le tableau liste_aretes la liste des aretes on aurait pu les trier en disant à l'indice i c'est le noeud i est il est relié avec tous les elements de sont tableau


    // ETAPE 1.5 : LECTURE DU TABLEAU 
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

    //TABLEAU DES LISTES DE VOISINS
    int** liste_voisins_connexion = malloc(nb_sommets * sizeof(int*));                        //premiere allocation de memoire
    for (int i=0; i<nb_sommets; i++) {
        liste_voisins_connexion[i] = malloc(nbVoisin[i].nbVoisinTotal * sizeof(int));    //deuxieme allocation de memoire avec le nombre de voisin
    }

			// on va stocker dans un tableau tout les voisins à qui le noeud b doit se connecter
	  for (int b=1; b<=nb_sommets; b++) { 
			int nb_demandes = 0;                                        	 // nombre de demandes du noeud b
			for (int c=0; c<nb_aretes; c++) {                            // on parcourt toutes les arêtes
				int noeud1 = liste_aretes[c].noeud1;                       // on récupère le noeud x de l'arête x-y
				if (noeud1 == b) {                                         // si le noeud1 doit demander
					int noeud2 = liste_aretes[c].noeud2;             				 // on récupère noeud2 ici pour gagner du temps si non utilisé
					liste_voisins_connexion[b-1][nb_demandes] = noeud2; // on ajoute dans la liste des voisins à demander du noeud1 le noeud2
					nb_demandes++;
				}
			}
	  }
    
  
  
      //affichage
    printf("\n[SERVEUR] \033[4mTableau des nb de voisins :\033[0m\n");
    for (int i=0; i<nb_sommets; i++){
        printf("Le noeuds %d a %d voisins au total et %d voisin a demander\n", i+1, nbVoisin[i].nbVoisinTotal, nbVoisin[i].nbVoisinDemande);
    }





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


    // ETAPE 4 : MISE EN PLACE DES DONNES
      //donnees util
    int dSNoeud;                                                                //declaration du descripteur
    //int nbMaxNoeud = NOEUDS_MAX;                                              //on declare un maximum de noeuds possibles
    struct infos_Graphe *procGraphe = (struct infos_Graphe*) malloc(nb_sommets * sizeof(struct infos_Graphe));         //on declare un tableau de structure pour les informations des Noeuds connecté au sevreur
    struct sockaddr_in sockNoeud;                                               //on declare la socket du Noeud
    socklen_t lgAdr;                                                            //taille de l'adresse
    


    //BOUCLE POUR RECEVOIR LES CLIENT

    //int nbMaxdS = dSServeur;                                                  //maximum des descripteurs
    int numSommet;                                                     //on declare le nombre de Noeud courant
     for (numSommet=1; numSommet<=nb_sommets; numSommet++) {

        //ETAPE 4 : ACCEPTATION DU NOEUD
            //acceptation
        dSNoeud = accept(dSServeur, (struct sockaddr*)&sockNoeud, &lgAdr);          //on accepte le Noeud qui demande

        //AFFICHAGE
            //affichage
        printf("\n[SERVEUR] Connexion d'un Noeud de descripteur %d\n", dSNoeud);
       if (DEBUG > 3) { 
			 		if (numSommet < 2) {printf("[SERVEUR] 1 Noeud est connecté au serveur\n");}  //affichage du nombre de Noeud connecté
	        else {printf("[SERVEUR] %d Noeuds sont connectés au serveur\n", numSommet);}       //affichage du nombre de Noeud connecté
				}

        //ETAPE 5 : RECEPTION DES INFORMATIONS DU Noeud
            //donnees        
        struct infos_Graphe info_proc;                      // structure qui va recuperer les informations qu'un Noeud a envoyer
            //reception
        recvCompletTCP(dSNoeud, &info_proc, sizeof(struct infos_Graphe));       // reception des informations dans info_proc
            //modification des donnees
        sockNoeud = info_proc.adrProc;                                          // donner a sockNoeud l'adresse recu dans info_proc
       
			 	int indice_proc = info_proc.numero-1;                                     //donne l'indice
			 	//MODIFICATION DES INFORMATIONS
        procGraphe[indice_proc].numero = indice_proc+1;                         //on attribut l'indice du noeud
			 	procGraphe[indice_proc].descripteur = dSNoeud;                          //on attribut le descripteur
        procGraphe[indice_proc].adrProc = sockNoeud;                            //on attribut l' adresse

        //AFFICHAGE
            //adresse
        char adrProcAff[INET_ADDRSTRLEN];             //on va stocker l'adresse du sous anneau dedans
        inet_ntop(AF_INET, &sockNoeud.sin_addr, adrProcAff, INET_ADDRSTRLEN);     //adresse du Noeud    
            //port
        int portProcAff = htons(sockNoeud.sin_port);                                 //port du Noeud
            //Affichage
        printf("\n[SERVEUR] \033[4mLe processus a comme informations après réception :\033[0m\n");
        printf("\n       Adresse du processus : %s\n       Port : %d", adrProcAff, portProcAff);
        printf("\n       Numéro du noeud dans le graphe : %d\n       Descripteur de la socket du processus : %d\n\n", indice_proc+1, procGraphe[indice_proc].descripteur);
         

        //ENVOI DU NOMBRE DE VOISIN A CHAQUE NOEUDS
          //descripteur de socket courant
        int dS_courant = procGraphe[indice_proc].descripteur;                  //indice courant
          //envoi
        sendCompletTCP(dS_courant, &nbVoisin[indice_proc], sizeof(struct nbVois));
         	//AFFICHAGE
			 	printf("\n[SERVEUR] \033[4mInformations envoyées :\033[0m\n");
				printf("      [SERVEUR] Nombre de voisin total = %d\n", nbVoisin[indice_proc].nbVoisinTotal);
				printf("      [SERVEUR] Nombre de voisin de demande = %d\n", nbVoisin[indice_proc].nbVoisinDemande);
       
        printf("\n***********************************\n");
    } //fin de la premieère connexion avec tous les noeuds


      //UNE FOIS QUE TOUS LES NOEUDS SONT LA
      printf("\n[SERVEUR] Tous les Noeuds sont connectés !\n\n***********************************\n");

      //ETAPE ~6 : AFFICHAGE DE LA LISTE DES NOEUDS CONNECTE
          //affichage
      printf("\n[SERVEUR] \033[4mListe des noeuds connectés:\033[0m\n");
          //adresse
      char adrNoeudCoAff[INET_ADDRSTRLEN];                                       //on va stocker l'adresse du sous anneau dedans
          //parcourt des noeuds connectés au serveur                        
      for (int i=0; i<nb_sommets; i++) {     //on commence a un car les indice commence a 1
              //recuperation adresse
          inet_ntop(AF_INET, &procGraphe[i].adrProc.sin_addr, adrNoeudCoAff, INET_ADDRSTRLEN);     //adresse du Noeud    
              //port
          int portNoeudCoAff = htons(procGraphe[i].adrProc.sin_port);                                 //port du Noeud
          printf("\n      Noeud d'indice %d de descripteur %i : %s:%i\n", i+1, procGraphe[i].descripteur, adrNoeudCoAff, portNoeudCoAff);
			 }
		// MISE EN ATTENTE 
      char e ;
      printf("\n\n[SERVEUR %d] : Entrez un caractère après l'envoi des informations : ", dSServeur);  //on demmande au client d'entrer un message
      scanf("%c", &e);
     //FIN MISE EN ATTENTE

  
      //ETAPE X: ENVOIE DES INFORMATIONS AU PROCESSUS VOISINS
      //BOUCLE SOMMET
      for (int i=0; i<nb_sommets; i++) {     //pour chaque noeuds

					//DONNES
					//struct infos_Graphe* info_voisins = (struct infos_Graphe*) malloc( nbVoisin[i].nbVoisinDemande * sizeof(struct infos_Graphe)); 					//tableau des voisins du sommets courant
          int nbVoisinDemande = nbVoisin[i].nbVoisinDemande;			//nb voisin a qui tu dois demander du sommets courant 
          
          //BOUCLE de autant de voisin que tu a a demander
          for (int v=0; v<nbVoisinDemande; v++) {
								//données
							int voisinCourant = liste_voisins_connexion[i][v];																	//on recupere l'insdice du voisin courant
							struct infos_Graphe info_voisin_courant;																						//structure du voisin courant (-1 car l'indice commence a 1)
							info_voisin_courant.requete = procGraphe[voisinCourant-1].requete;
							info_voisin_courant.numero = procGraphe[voisinCourant-1].numero;
							info_voisin_courant.descripteur = procGraphe[voisinCourant-1].descripteur;
							info_voisin_courant.adrProc = procGraphe[voisinCourant-1].adrProc;
	            //ajout dans le tableau
							sendCompletTCP(procGraphe[i].descripteur, &info_voisin_courant, sizeof(struct infos_Graphe));     //on envoie les inforamtions ici adresse des voisins
							//AFFICHAGE ajout des voisins au sommet
						printf("[SERVEUR] Ajout du voisins %d au sommet %d\n",info_voisin_courant.numero, i+1);
							
						}//fin des voisins
			}//fin des sommets




      //ETAPE 8 : FERMETURE DE LA SOCKET SERVEUR CAR PLUS BESOIN
          //fermeture
      close(dSServeur);
          //affichage
      printf("\n[SERVEUR] Je peux m'en aller !\n");
          //on free les allocations mémoires
      free(liste_aretes);
      free(procGraphe);
      //free(info_voisins);
      for (int z=0; z<nb_sommets; z++) {
        free(liste_voisins_connexion[z]);
      }
      free(liste_voisins_connexion);
          //on sort du programme
      exit(0);
      
      
    
}//fin du main







/*
// CODE QUE NOUS AVONS FAIT AVANT MAIS MAINTENANT OBSOLETE
    //ETAPE 5 : DECLARATION DE DONNEES UTILS
    fd_set set, settmp;
    int dSNoeud;                                   //on decare la socket Noeud    
    FD_ZERO(&set);
    FD_SET(dSServeur, &set);
    int maxdS = dSServeur;                          //on fixe le maximum des descripteur en commancant par la socket serveur
    struct sockaddr_in sockNoeud;                  //on declare la socket Noeud
    int nbMaxAnneau = NOEUDS_MAX;                          //on fixe le nombre maximum d'anneau possible
    struct procGraphe procGraphe[nbMaxAnneau];     //on declare un tableau de tous les processus qui sont dans du graphe
    int num = 0;                       //on declare le maximum des anneau courant 


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
                struct infos_Graphe info_proc;                                                     //structure qui va recuperer les informations qu'un Noeud a envoyer
    
                //RECPETION DES INFORMATIONS
                recvCompletTCP(dSNoeud, &info_proc, sizeof(struct infos_Graphe));                  //reception des informations
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
                procGraphe[num].dSProc = dSNoeud;             //on attribut un descripteur
                procGraphe[num].adrProc = sockNoeud;          //on attribut une adresse

                //CREATION DES INFORAMTIOSN DUN Noeud
                struct infos_Graphe attribution;        //on declare l'attibution d'un numero
                attribution.requete = ATTRIB_NUM;           //on donne la requete
                attribution.info1 = num;       //on donne l'information principale qu'on a besoin ici le maximum courant
                num++;                         //on incremente le maximum courant pour continuer l'attribution

                //ENVOIE DE LATTRIBUTION DU NUMERO D'ANNEAU AU Noeud
                sendCompletTCP(dSNoeud, &attribution, sizeof(struct infos_Graphe));  //on envoie l'attribution à un processus

                
                //ETAPE 9 : AFFICHAGE DES PROCESSUS CONNECTE                    
                char adrNoeudEnv[INET_ADDRSTRLEN];                                       //on va stocker l'adresse du sous anneau dedans
                printf("\n***********************************\n");                      //afficher une ligne
                printf("\n[SERVEUR] \033[4mListe des sous-anneaux:.\033[0m\n");

                //BOUCLE POUR PARCOURIR LA TAILLE DES SOUS ANNEAU EXIStant                          
                for (int i=0; i<num; i++) { 

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

        if (num == nbprocGraphe) {                                            //si le nombre de processus courant est égal au nombre de processus donnée
            printf("\n[SERVEUR] Tous les anneaux sont connectés, envoi des adresses\n");      //affichage
            
            //ETAPE 10 : ENVOIE DES INFORMATIONS AU PROCESSUS

            //VERIFICATION DE PLUS DE 3 PROCESSUS
            if (num < 3) {           //verification que le nombre de processus est moins de 3
                printf("\n[SERVEUR] Impossible de créer un anneau avec moins de trois sous-anneaux\n");       //aors c'est pas possible
                exit(1);
            }

            //DONNEES
            struct infos_Graphe info_proc;                                      //on declare la structure qui dit ce qu'on veut
            info_proc.requete = ADR_VOISIN;                           //on donne la requete ADR_VOISIN
            
            //BOUCLE QUI PARCOURT LA LISTE DES INFORMATIONS DES PROCESSUS DE LANNEAU -1
            for (int i=0; i<num-1; i++) {                        //on parcours le tableau des informations des processus
                
                info_proc.adrProc = procGraphe[i+1].adrProc;            //donne à la structure l'adresse de la liste du processus i+1
                
                //RECUPERATION DE L'ADRESSE ET PORT
                char adrNoeud[INET_ADDRSTRLEN];
                inet_ntop(AF_INET, &info_proc.adrProc.sin_addr, adrNoeud, INET_ADDRSTRLEN);
                int portNoeud = htons(info_proc.adrProc.sin_port);

                //ENVOIE DES INFORMATIONS 
                sendCompletTCP(procGraphe[i].dSProc, &info_proc, sizeof(struct infos_Graphe));     //on envoie les inforamtions ici adresse du voisin
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
            sendCompletTCP(procGraphe[num-1].dSProc, &info_proc, sizeof(struct infos_Graphe));


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