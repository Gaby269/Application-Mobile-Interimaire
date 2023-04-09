from parser import rechercheDUMP, formaterDico, parserDico, rechercheFilterDUMP, getId
from utilities import calculIntersection

def euristiqueAnnotationAsk(tab_ask, tab_note) :

    # Parcours du tableau des notes)
    for i in range(len(tab_ask)):
        if "peu pertinent" in tab_ask[i][2] or "non pertinent" in tab_ask[i][2] :
            tab_note[i] = tab_note[i] / 4
        elif "pertinent" in tab_ask[i][2] :
            tab_note[i] = tab_note[i] * 4
        
        if "toujours vrai" in tab_ask[i][2] : 
            tab_note[i] = tab_note[i] * 3
        if "rare" in tab_ask[i][2] :
            tab_note[i] = tab_note[i] / 3

        if "fréquent" in tab_ask[i][2] :
            tab_note[i] = tab_note[i] * 2
        if "possible" in tab_ask[i][2] :
            tab_note[i] = tab_note[i] / 2

        if tab_ask[i][2] == "VIDE" or "non spécifique" in tab_ask[i][2] :
            tab_note[i] = tab_note[i]
        # A changer pcq faut le prendre en compte correctement
        if "constrastif" in tab_ask[i][2] :
            tab_note[i] = tab_note[i]

    return tab_note


def euristiqueEgalite(mot1, tab_iprim, tab_note) : 
    # Parcours du tableau des notes
    for i in range(len(tab_note)):
        if tab_iprim[i] == mot1 :
            tab_note[i] = tab_note[i] / 4 # comme si c'était peu pertinent de le dire
    return tab_note


def euristiquePourcentageDump(mot1, relation, mot2, tab_iprim) :
    
    pourcentage=[]
    
    # Parcours du tableau des iprim
    for i in range(len(tab_iprim)) :

        print("\n\n")
        #print(tab_iprim[i])

        # Récuperer le poiurcentage de iprim qui peuvent rugir sur tous les animaux

        # Recuperation du nombre total d'animaux
        rechercheFilterDUMP(tab_iprim[i],id_relation=6,with_sortante=False)
        dicoFilterIprim = formaterDico([tab_iprim[i]], filter=True, id_relation=6, with_sortante=False)[tab_iprim[i]]
        entiteFilterIprim = dicoFilterIprim["2;e;eid;'name';type;w;'formated name'"]
        nb_entiteTotal_iprim = len(entiteFilterIprim)

        # Recuperation des iprim qui peuvent rugir
        # je recuper les relation entrante de rugir
        rechercheFilterDUMP(mot2,id_relation=relation,with_sortante=False)
        dicoFilterMot2 = formaterDico([mot2], filter=True, id_relation=relation, with_sortante=False)[mot2]
        entiteFilterMot2 = dicoFilterMot2["2;e;eid;'name';type;w;'formated name'"]
        # Clacul de l'intersection
        intersection = calculIntersection(entiteFilterIprim, entiteFilterMot2)
        nb_entite_iprimMot2 = len(intersection)
        
        #print(f"nbTotal de {tab_iprim[i]}  : ", nb_entiteTotal_iprim)
        #print("nbTrier : ",nb_entite_iprimMot2)
        #print("Pourcentage : ", (nb_entite_iprimMot2 / nb_entiteTotal_iprim)*100)

        pourcentage.append((nb_entite_iprimMot2 / nb_entiteTotal_iprim)*100)

    #print(pourcentage)
    
    return pourcentage
            

def calculNoteRelation(mot1, relation, mot2, tab_iprim, tab_ask, tab_note) :
    # application de chaque euristique
    tab_note = euristiqueAnnotationAsk(tab_ask, tab_note)
    tab_note = euristiqueEgalite(mot1, tab_iprim, tab_note)
    tab_pourcentage = euristiquePourcentageDump(mot1, relation, mot2, tab_iprim)
    
    return tab_note, tab_pourcentage



if __name__ == "__main__":
    euristiquePourcentageDump("lion", '24', "rugir",  ['animal', 'animal sauvage', 'félin', 'félin>17559', 'fauve', 'félidé', 'féliforme', 'féloïdé', 'homme', 'individu'], [1,1,1,1,1,1,1,1,1,1])
    #['animal', 'animal sauvage', 'félin', 'félin>17559', 'fauve', 'félidé', 'féliforme', 'féloïdé', 'homme', 'individu']
