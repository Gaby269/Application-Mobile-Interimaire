def traductionFrancaisToChiffre(mot):
    mot = mot.lower().strip()
    try:
        int(mot)
        return mot
    except:
        dictionnaire = {
            '0': ["associe", "associé"],
            '6': ["est", "sont", "etre", "être"],
            '9': ["possède", "possede", "a", "à", "ont", "posseder", "partie de"],
            '13': ["r_agent"],
            '24': ["r_agent-1", "peut", "peuvent", "pouvoir"],
            '41': ["conséquence", "consequence"]               # r_has_conseq
        }
        for (id, relations) in dictionnaire.items():
            for relation in relations:
                if relation in mot:
                    return id
                    
        relation = input("Veuillez donner une bonne relation :")
        return traductionFrancaisToChiffre(relation)



def traductionChiffreToRelation(mot):
    mot = mot.lower().strip()
    
    dictionnaire = {
        "r_associated": ['0'],
        "r_isa": ['6'],
        "r_has_part": ['9'],
        "r_agent": ['13'],
        "r_agent-1": ['24'],
        "r_has_conseq": ['41']               # r_has_conseq
    }
    for (id, relations) in dictionnaire.items():
        for relation in relations:
            if relation == mot:
                return id
                
    relation = input(f"{relation} Veuillez donner une bonne relation (CtoR) :")
    return traductionChiffreToRelation(relation)



def traductionChiffreToFrancais(mot):
    mot = mot.lower().strip()
    
    dictionnaire = {
        "est associé à": ['0'],
        "est un(e)": ['6'],
        "a/ont un(e)/des": ['9'],
        "est possible par": ['13'],
        "peut": ['24'],
        "a pour conséquence": ['41']               # r_has_conseq
    }
    for (id, relations) in dictionnaire.items():
        for relation in relations:
            if relation == mot:
                return id
                
    relation = input(f"{mot} Veuillez donner une bonne relation (chiffre to francais) :")
    return traductionChiffreToRelation(relation)




def traductionChiffreToFrancaisNeg(mot):
    mot = mot.lower().strip()
    
    dictionnaire = {
        "n'est pas associé à": ['0'],
        "n'est pas un(e)": ['6'],
        "n'a/ont pas un(e)/des": ['9'],
        "n'est pas possible par": ['13'],
        "ne peut pas": ['24'],
        "n'a pas pour conséquence": ['41']               # r_has_conseq
    }
    for (id, relations) in dictionnaire.items():
        for relation in relations:
            if relation == mot:
                return id
                
    relation = input(f"{relation} Veuillez donner une bonne relation (CtoFN):")
    return traductionChiffreToRelation(relation)




# Intersection un peu null
def calculIntersection(ens1, ens2) :
    intersection = []
    for e1 in ens1:
        for e2 in ens2:
            if (e1[1] == e2[1]):
                intersection.append(e1)

    return intersection


def calculNoteRelation(tab_ask, tab_note) :

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
# Ajouter dans intersection un poit de 1
# Si ya tjs vrai alors on multiplie 2
# Si ya peu pertinent ou non pertinent divise par 3
# Si ya possible divise par 2
# Si ya fréquent 
# Si ya contrastif contraste (ya plusieurs sens et faux pour l'un et vrai pour l'autre)  voir comment prendre en compte ce critère
# Si juste vide ou non spécifique our faire rien 
# constitutif : etre dns la definition
# rare
# [pertinent - , peu pertinent, non pertinent, possible, frequent, rare, contrastif, non spécifique]
