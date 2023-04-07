import requests
import json
import os
import re


#from inferences import deduction
def tri_cle(cle):
    if 'formated name' in cle:  #entite
        return 0
    elif 'ntname' in cle:  #typede entite
        return 1
    elif 'node1' in cle:  #relation
        return 2
    elif 'rthelp' in cle:  #type relation
        return 3


# Fonction qui cherche dans reseauDUMP en fonctions des infos données
#depart sert à savoir si c'est la premiere fois quon le demande comme dans le main ou on veut les relations
def rechercheDUMP(mots,
                  overwrite=False,
                  id_relation=-1):

    for mot in mots:
        # Si le mot a pas deja été trouvé
        if (not os.path.exists(f"dump_files/selected_{mot}.txt") or (overwrite == True)):
            id = ""
            if (id_relation != -1):
                id = str(id_relation)
    
            # Faire la requête HTTP
            url1 = "https://www.jeuxdemots.org/rezo-dump.php?gotermsubmit=Chercher&gotermrel=" + mot + "&rel=" + id + "&relin=norelin"
            url2 = "https://www.jeuxdemots.org/rezo-dump.php?gotermsubmit=Chercher&gotermrel=" + mot + "&rel=" + id + "&relout=norelout"

            selected1 = "MUTED_PLEASE_RESEND"
            while ("MUTED_PLEASE_RESEND" in selected1):
                # print(url)
                response1 = requests.get(url1)
                response2 = requests.get(url2)
        
                # Recuperation des données de reseauDump dans body
                body1 = response1.text
                body2 = response2.text
        
                # Recherche de la position de la première occurrence de la balise <CODE> pour récuperer les choses à l'interieur
                start_index1 = body1.find("<CODE>")
                start_index2 = body2.find("<CODE>")
        
                # Recuperation du texte du code source
                selected1 = ""
                selected2 = ""
        
                # Si le mot existe dans la base
                if start_index1 != -1:
                    end_index1 = body1.find("</CODE>", start_index1)
                    end_index2 = body2.find("</CODE>", start_index2)
        
                    if end_index1 != -1:
                        # Extraction du contenu entre les balises <CODE> et </CODE>
                        selected1 = body1[start_index1+6:end_index1] # On rajoute la longueur de la balise <CODE>
                        selected2 = body2[start_index2+6:end_index2]
        
                # Sinon on regarde s'il y a un message d'erreur qui dit que le mot n'existe pas
                else:
                    start_index_warning = body1.find(u"""<div class="jdm-warning">""")
                    if start_index_warning != -1:
                        print(f"\nLe mot {mot} n'existe pas veuillez changer la phrase")
                    else:
                        print("\nErreur lors de la requête")
                    exit(1)
    
            body1 = selected1[:-7]
            
            # Ecriture des relations sortantes
            with open(f"./dump_files/selected_{mot}.txt", "w") as f:
                f.write(body1)
    
            body2 = selected2[selected2.find("// les relations entrantes"):]
    
            # Ecriture des relations entrantes
            with open(f"./dump_files/selected_{mot}.txt", "a") as file:
                file.write(body2)
                    
            print(f"\n\nLes données du mot {mot} ont été récupérées.\n\n")
            

#Fonction qui forme le texte en dico
def formaterDico(phr):

    # Dictionnaire pour tous les mots de la phrase
    dico_entier = {}

    for p in phr:

        # Remplir le dico
        dico_entier[p] = {}

        # Bossage avec le fichier mtn
        donnees_par_categorie = {}

        with open(f'./dump_files/selected_{p}.txt', 'r') as file:

            categorie_actuelle = "None"  # dans quelle categorie on est
            num_categorie = 0  # id de la categorie
            a_l_interieur_de_la_balise = False  # si tes dans <def></def>
            lignes = file.readlines()

            for ligne in lignes:
                if ligne.startswith('\n'):
                    continue
                # On igonre les lignes <def></def> si on est dedans on cherche
                elif a_l_interieur_de_la_balise:
                    if ligne.strip() == "</def>":
                        a_l_interieur_de_la_balise = False
                    continue
                # On ignore si c'est la ligne
                elif ligne.strip() == "<def>":
                    a_l_interieur_de_la_balise = True
                    continue
                # Si c'est une ligne de commentaire
                if ligne.startswith('//') and not ":" in ligne:
                    continue
                if ligne.startswith('//') and ":" in ligne:
                    # on peut regardant tant quon a pas start par r
                    if ((len(donnees_par_categorie.get(categorie_actuelle, [])) == 0)
                        and len(donnees_par_categorie) !=
                        0):  # si la categorie est vide alors on saute le commentaire
                        continue
                    # Sinon on a trouvé une nouvelle catégorie
                    elif ("les relations sortantes" not in ligne) and (
                      num_categorie == 3
                    ):  # on verifie que si jamais ya pas de relations sortantes on incremente
                        # cela veut dire quon est en relations entrantes et que il n'y a pas de relations sortantes
                        num_categorie += 1
                        categorie_actuelle = str(num_categorie) + ";" + (ligne.split(
                         ":")[1]).strip()  # on peut prendre les meme chose que la ligne suivante
                        donnees_par_categorie[categorie_actuelle] = []
                        # on passe a la categorie suivante normalement
                        num_categorie += 1
                        categorie_actuelle = str(num_categorie) + ";" + (ligne.split(
                         ":")[1]).strip()  # on peut prendre les meme chose que la ligne suivante
                        donnees_par_categorie[categorie_actuelle] = []
                    elif ("les relations entrantes" not in ligne) and (
                      num_categorie == 4):  # on regarde si ya des relations entrantes
                        # cela veut dire quon est en relations entrantes mais que yen a pas
                        num_categorie += 1
                        categorie_actuelle = str(num_categorie) + ";" + (ligne.split(
                         ":")[1]).strip()  # on peut prendre les meme chose que la ligne suivante
                        donnees_par_categorie[categorie_actuelle] = []
                    else:  # sinon on est dans les sortantes et ya pas de problème
                        num_categorie += 1
                        categorie_actuelle = str(num_categorie) + ";" + (
                         ligne.split(":")[1]).strip()
                        #print("cate", categorie_actuelle)
                        donnees_par_categorie[categorie_actuelle] = []
                # Sinon
                else:
                    # On ajoute la ligne à la catégorie courante
                    if categorie_actuelle != "None":
                        donnees_par_categorie[categorie_actuelle].append(
                         ligne.strip().split(';'))

        dico_entier[p] = donnees_par_categorie

    return dico_entier


# Dictiopnnaire a structurer pour avoir les specifications demandées avec la relation, et le fait que ce soit sortant et entrant ou non
# Type de trie c'est si je trie sur entite ou relation ou autre
# valeur_trie  c'est avec quoi je trie
def parserDico(dico,
               type_trie,
               valeur_trie,
               is_sortante=True,
               is_entrante=True):

    # On récuper le dico
    dico_formater = dico.copy()

    # Tableau des clé d'un dico
    cle = list(dico_formater.keys())

    # Tableau des relations
    tab_entrantes = []
    tab_sortantes = []
    tab_typeRelat = []
    tab_Entites = []
    tab_typeEntit = []

    if type_trie == "relation":

        cle.reverse()

        # Parcours du dictionnaire
       
        for key in cle: 
            cpt = 0
            # Parcours des tableaux
            for tab in dico_formater[key]:
                
                # cpt += 1
                # if cpt % 1000 == 0:
                    # print(key, "-", cpt)
                    
                #  Choix du type de relation
                if tab[0] == 'rt' and tab[1] == valeur_trie and tab not in tab_typeRelat:
                    tab_typeRelat.append(tab)
                # Choix des relations
                if tab[0] == 'r' and tab[4] == valeur_trie:
                    # Si on choisis les relation sortantes + Si c'est bien la clé pour les relations sortantes (4)
                    if is_sortante and key[0] == '4' and tab not in tab_sortantes and int(
                      tab[5]) > 0:
                        tab_sortantes.append(tab)
                    # Si c'est une relation et que la relation est de type relation + Si c'est bien la clé pour les relations entrantes (5)
                    if is_entrante and key[0] == '5' and tab not in tab_entrantes and int(
                      tab[5]) > 0:
                        tab_entrantes.append(tab)
                # Choix les entités
                if tab[0] == 'e':
                    if is_sortante:
                        # Parcours des relations sortantes
                        for sor in tab_sortantes:
                            # Si entite aparait dans une relation en terme 1 ou en terme 2 on le garde
                            if (tab[1] == sor[2] or tab[1] == sor[3]) and tab not in tab_Entites:
                                tab_Entites.append(tab)
                    if is_entrante:
                        # Parcours des relations entrantes
                        for ent in tab_entrantes:
                            # Si entite aparait dans une relation en terme 1 ou en terme 2 on le garde
                            if (tab[1] == ent[2] or tab[1] == ent[3]) and tab not in tab_Entites:
                                tab_Entites.append(tab)
                # Choix des types d'etnités
                if tab[0] == 'nt':
                    for e in tab_Entites:
                        # Si le type aparait dans une des entites il faut le garder
                        if e[3] == tab[1] and tab not in tab_typeEntit:
                            tab_typeEntit.append(tab)

    elif type_trie == "entite":

        #entite // typeentite // relations // type de realtion
        # Tri du dictionnaire selon les clés en utilisant la fonction de tri personnalisée
        cle = sorted(cle, key=tri_cle)
        # cle = [
        #           "2;e;eid;'name';type;w;'formated name'",
        #           "1;nt;ntid;'ntname'",
        #           "4;r;rid;node1;node2;type;w",
        #           "5;r;rid;node1;node2;type;w",
        #           "3;rt;rtid;'trname';'trgpname';'rthelp'"
        #       ]
        # Parcours du dictionnaire
        cpt = 0
        for key in cle:
            #print(key)
            # Parcours des tableaux
            for tab in dico_formater[key]:
                
                #cpt += 1
                #if cpt % 100 == 0:
                    #print(cpt)
                # Choix les entités
                if tab[0] == 'e':
                    # Parcours des entites données
                    for ent in valeur_trie:
                        # Si l'entite trouvé est dedans on garde
                        if tab == ent and tab not in tab_Entites:
                            tab_Entites.append(tab)
                # Choix des types d'entités
                if tab[0] == 'nt':
                    # Parcours du tableau des entités
                    for e in tab_Entites:
                        # Si le type aparait dans une des entites il faut le garder
                        if e[3] == tab[1] and tab not in tab_typeEntit:
                            tab_typeEntit.append(tab)

                # Choix des relations
                if tab[0] == 'r':
                    # Parcours du tableau des entités
                    for e in tab_Entites:
                        # Si on choisis les relation sortantes + Si c'est bien la clé pour les relations sortantes (4)
                        if is_sortante and key[0] == '4' and tab not in tab_sortantes and (
                          tab[2] == e[1] or tab[3] == e[1]) and int(tab[5]) > 0:
                            tab_sortantes.append(tab)
                        # Si c'est une relation et que la relation est de type relation + Si c'est bien la clé pour les relations entrantes (5)
                        if is_entrante and key[0] == '5' and tab not in tab_entrantes and (
                          tab[2] == e[1] or tab[3] == e[1]) and int(tab[5]) > 0:
                            tab_entrantes.append(tab)
                #  Choix du type de relation
                if tab[0] == 'rt':
                    # Parcours du tableau des relations
                    if is_sortante:
                        for s in tab_sortantes:
                            #  Si le type existe dans la relation on garde
                            if tab[1] == s[4] and tab not in tab_typeRelat:
                                tab_typeRelat.append(tab)
                    if is_entrante:
                        for e in tab_entrantes:
                            #  Si le type existe dans la relation on garde
                            if tab[1] == s[4] and tab not in tab_typeRelat:
                                tab_typeRelat.append(tab)

    # Reconstruction du dictionnaire a partir des données de maintenant
    dico_formater["1;nt;ntid;'ntname'"] = tab_typeEntit
    #print(tab_Entites,tab_sortantes, tab_entrantes)
    dico_formater["2;e;eid;'name';type;w;'formated name'"] = tab_Entites
    dico_formater["3;rt;rtid;'trname';'trgpname';'rthelp'"] = tab_typeRelat
    dico_formater["4;r;rid;node1;node2;type;w"] = tab_sortantes
    dico_formater["5;r;rid;node1;node2;type;w"] = tab_entrantes

    return dico_formater


#get.id
def getId(mot):
    f = open(f"./selected_filed/selected_{mot}.txt", "r")
    id_line = f[1]
    id_string = id_line.split("=")
    id = id_string[-1].strip(")")
    return id




def rechercheASK(mot1, relation, mot2) :

    path = f"ask_files/{mot1}_{relation}_{mot2}"
    if os.path.exists(path):
        r, w, anot = [line.strip() for line in open(path, "r")]
        return r, w, anot
        
	# URL pour chercher si la relation 
    url = "https://www.jeuxdemots.org/rezo-ask.php?gotermsubmit=Demander&term1="+mot1+"&rel="+relation+"&term2="+mot2
    #print(url)
    response = requests.get(url)

	# Recuperation des données de reseauDump dans body
    body = response.text

	# Recherche de la position de la première occurrence de la balise <CODE> pour récuperer les choses à l'interieur
    start_index = body.find("<RESULT>")

	# Recuperation du texte du code source
    selected = ""

	# Si le mot existe dans la base
    if start_index != -1:
        start_index += 8  # On rajoute la longueur de la balise <result>
        end_index = body.find("</RESULT>", start_index)

        if end_index != -1:
			# Extraction du contenu entre les balises <CODE> et </CODE>
            selected = body[start_index:end_index]

	# Sinon on regarde s'il y a un message d'erreur qui dit que le mot n'existe pas
    else:
        start_index_warning = body.find("<ERROR>")
        if start_index_warning != -1:
            print("\nUn des mots n'existe pas veuillez changer les entrées")
            exit(1)
        else : 
            print("\nErreur de la requete")
            exit(1)
	# Ecriture dans un texte
	#print(selected)

	# R
    r = ""
    start_index_r = selected.find("<R>")
    # Si le mot existe dans la base
    if start_index_r != -1:
        start_index_r += 3  # On rajoute la longueur de la balise <R>
        end_index_r = selected.find("</R>", start_index_r)

        if end_index_r != -1:
			# Extraction du contenu entre les balises <CODE> et </CODE>
            r = selected[start_index_r:end_index_r]
    #print("r : ", r)
	
    # W
    w = ""
    start_index_w = selected.find("<W>")
    # Si le mot existe dans la base
    if start_index_w != -1:
        start_index_w += 3  # On rajoute la longueur de la balise <R>
        end_index_w = selected.find("</W>", start_index_w)

        if end_index_w != -1:
			# Extraction du contenu entre les balises <CODE> et </CODE>
            w = selected[start_index_w:end_index_w]
    #print("w : ", w)
	
    # ANNOT
    anot = ""
    start_index_anot = selected.find("<ANOT>")
    # Si le mot existe dans la base
    if start_index_anot != -1:
        start_index_anot += 6  # On rajoute la longueur de la balise <R>
        end_index_anot = selected.find("</ANOT>", start_index_anot)

        if end_index_anot != -1:
            # Extraction du contenu entre les balises <CODE> et </CODE>
            anot = selected[start_index_anot:end_index_anot]

    if anot.strip() == '':
        anot = "VIDE"
    
    # écrire dans la BDD
    with open(f"ask_files/{mot1}_{relation}_{mot2}", "w") as f:
        f.write(f"{r}\n{w}\n{anot}")

    return r, w, anot



if __name__ == "__main__":
    rechercheASK("singe","r_isa", "vache")

# // DUMP pour le terme 'pigeon' (eid=74657)
