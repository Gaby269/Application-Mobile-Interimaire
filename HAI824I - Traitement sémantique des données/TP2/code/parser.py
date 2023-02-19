import numpy as np
from Levenshtein import ratio as levenshtein_similarity
from Levenshtein import jaro as jaro_similarity


def split_property(string):
    splitted_string = []
    current_split = ""
    current_bracket = ""

    string_open = False
    bracket_open = False

    for c in string:
        if c == "\"":
            string_open = not (string_open)
        elif c == "[" and not (string_open):
            bracket_open = True
        elif c == "]" and not (string_open):
            current_split = current_split + "[" + current_bracket + "]"
            current_bracket = ""
            bracket_open = False
        elif c == ";" and not (string_open) and not (bracket_open):
            splitted_string.append(current_split)
            current_split = ""

        elif bracket_open:
            current_bracket += c
        else:
            current_split += c

    splitted_string.append(current_split)
    return splitted_string


def formater(entity):
    #on regroupe les propriétés sur une ligne, et on sépare par ";"
    props = [prop.strip() for prop in split_property(" ".join(entity[1:]))]
    proprietes = {}
    for prop in props:  #on formate les propriétés
        
        cut = prop.index(" ")
        key = prop[:cut].strip()
        object = prop[cut:].strip(". ")
        
        if object.startswith("["):  #découper la chaine en liste
            object_brckets = object.strip(" []") 
            object_brckets_splitted = split_property(object_brckets)
            object_brckets_formated = {}
            for sub_prop in object_brckets_splitted:
                sps = sub_prop.strip()
                sub_cut = sps.index(" ")
                sub_key = sps[:sub_cut].strip()
                sub_object = sps[sub_cut:].strip(". ")
                object_brckets_formated[sub_key] = sub_object
            proprietes[key] = object_brckets_formated
            
        else:
            proprietes[key] = object
        
    return proprietes


def parser(file):

    RDF_parse = {}  #tableau parsé
    prefixes = {}  #ensemble des prefixes
    entite_raw = []  #entite brute

    #parcours du fichier
    for line in open(file, "r", encoding='utf8'):
        ls = line.strip()

        if ls == "": continue  #skip des lignes vides

        elif ls.startswith("@prefix"):  #on ajoute le prefixe au dictionnaire
            prefixe = ls.split()
            prefixes[prefixe[1].split(":")[0]] = prefixe[
             2]  #separation entre le nom et le lien
            # regex : <[^>]*>
        else:
            entite_raw.append(ls)  # on ajoute la ligne à l'entité courrante
            if ls[-1] == ".":  #end de l'entité
                RDF_parse[entite_raw[0]] = formater(entite_raw)
                entite_raw = []

    return RDF_parse


# takes as input a parsed_graph from the parser() function
# returns a list of 3-tuples, one for every relationship in the graph
# problem: il y a 6 objets qui sont des listes -> pas pris en compte
def spo_formater(parsed_graph):

    list_of_triplets = []

    # pour chaque clé valeur, du parseur
    for key, value in parsed_graph.items():
        #recupère les predicats et les objets dans deux listes
        preds, objs = list(zip(*value.items()))

        #repéter les clés autant de fois qu'il faut de lignes
        keys = tuple(np.repeat(key, len(value.items())))
        #recuperer tous les éléments par 3
        sol = list(zip(keys, preds, objs))
        #ajout à la liste
        list_of_triplets.append(sol)

    triplets = [item for sublist in list_of_triplets for item in sublist]

    return triplets


#Sujet : lien entite
#Predicat : relation/caracteristique
#Object : le precision/description

####SIMILARITE DIFFERENTE####

# Jaro et Levenshtein sont importés

#si on utilise pas celles importer alors on a


####LENVENSHTEIN DISTANCE
def levenshtein_distance(chaine1, chaine2):

    # calculer les tailles des deux chaines dans m et n
    taille1 = len(chaine1)
    taille2 = len(chaine2)

    # matrice de taille (taille1+1)x(taille2+1) pour calculer la distance entre chaque sous-chaines
    distance = [[0 for _ in range(taille2 + 1)] for _ in range(taille1 + 1)]

    # initialiser la première colonne et la première ligne de la matrice
    for i in range(taille1 + 1):
        distance[i][0] = i
    for j in range(taille2 + 1):
        distance[0][j] = j

    # remplir la matrice
    for i in range(1, taille1 + 1):
        for j in range(1, taille2 + 1):
            #si les deux caracteres sont identiques
            if chaine1[i - 1] == chaine2[j - 1]:
                cout = 0  #pas de cout de substitution
            else:
                cout = 1  #sinon 1 de cout de substitution

            #calcule de la distance avec le minimum de la distance entre chaque sous chaine
            distance[i][j] = min(
             distance[i - 1][j] + 1,  # effacement du nouveau caractère de chaine1
             distance[i][j - 1] +
             1,  # insertion dans chaine2 du nouveau caractère de chaine1
             distance[i - 1][j - 1] + cout)  # substitution

    # retourner la distance
    return distance[taille1][taille2]


####LENVENSHTEIN DISTANCE NORMALISER
def normalized_levenshtein_distance(chaine1, chaine2):

    distance = levenshtein_distance(chaine1, chaine2)  #calcul de la distance
    taille_max = max(len(chaine1), len(chaine2))  #cherche le maximum des chaines
    #si le max des tailles est 0 on retourne 0 car on peut pas diviser par 0
    if taille_max == 0:
        return 0
    #sinon on donne la distance divisé par le max des chaines
    else:
        return distance / taille_max


####LENVENSHTEIN SIMILARITE NORMALISER
def normalized_levenshtein_similarity(chaine1, chaine2):
    return 1 - normalized_levenshtein_distance(chaine1, chaine2)



####SIMILARITE DE JARO
def jaro_similarity(chaine1, chaine2) :

    # calcul des tailles des chaines de caractères
    taille1 = len(chaine1)
    taille2 = len(chaine2)

    # si les chaines sont de 0 alors on renvoie 1
    if taille1 == 0 and taille2 == 0 :
        return 1;
		
	# trouver les caractères communs dans les deux chaînes
	for i in range(1, taille1+1) :
		for j in range(1, taille2+1) :
			tab_similarite[i-1][j-1] = 0

	# remplissage de la matrice
	for i in range(1, taille1+1) :
		for j in range(1, taille2+1) :
			# mettre 1 a chaque caractère pareil dans leur intersection
			if chaine1[i - 1] == chaine2[j - 1]:
                tab_similarite[i-1][j-1] = 1
			#sinon on laisse a 0

	# compter le nombre de caractère en commun
	nb_commun = 0
	for i in range(1, taille1+1) :
		for j in range(1, taille2+1) :
			if similarite[i-1][j-1] == 1 :
				nb_commun+=1
				
    # si pas de caractères communs, la similarité est nulle
    if nb_commun == 0:
        return 0
        
    # calcul des transpositions
	nb_transpo = 0
	for i in range(1, taille1+1) :
		for j in range(1, taille2+1) :
			if similarite[i-1][j-1] == 1 :
				if chaine1[i-1] != chaine2[j-1] :
					nb_transpo+=1

	similarite_jaro = ((nb_commun / taille1) + (nb_commun / taille2) + ((nb_commun - (nb_transpo / 2.0)) / nb_commun)) / 3.0
	
    return similarite_jaro

	


# Identité:
def identite_similarity(string_1, string_2):
    if string_1 == string_2:
        return 1
    return 0


