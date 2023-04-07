import os
from parser import rechercheDUMP, formaterDico, parserDico, rechercheASK
from utilities import *


def tri_relations(tab):
    return (-tab[5], -tab[4])


def deduction(mot1, relation, mot2):  # A relation B

    print("déduction en cours")
    dico1 = {}
    dico2 = {}

    # Si les deux fichiers n'existent pas
    rechercheDUMP([mot1, mot2])
    print("recherche fini dump")

    # je forme en dico les mots 1 et deux
    dico_courant = formaterDico([mot1, mot2])
    # Puis je recupere le dico de chacun
    dico1 = dico_courant[mot1]
    dico2 = dico_courant[mot2]

    print("dico fini")
    # Recuperer les r_isa du mot1 en parsant le dico pour obtenir que les r_isa id=6 DE A vers C
    # que les relations sortantes qui correspondent a r_isa donc on enleve les entrante
    dico1_form = parserDico(dico1, "relation", '6', is_entrante=False)
    #print("RELATIONS SORTANTES : ",dico1_form["4;r;rid;node1;node2;type;w"])
    #print("RELATION ENTRANTES : ",dico1_form["5;r;rid;node1;node2;type;w"])
    # print("\n\n")
    print("dico1 formaté")

    # Recuperer tous les mots trouvé et voir si relation vers mot2 = realtion DE C vers B
    # Recup les élements de b que les relations entrante qui corresponde a la relation donnée donc on enleve les sortantes
    dico2_form = parserDico(dico2, "relation", relation, is_sortante=False)
    #print("DICO2 FORMATER : ",dico2_form)
    print("dico2 formaté")

    # Recup les elements c de dico1
    entiteC_dico1 = dico1_form["2;e;eid;'name';type;w;'formated name'"]
    entiteC_dico2 = dico2_form["2;e;eid;'name';type;w;'formated name'"]
    #print("ENTITE 1 :",entiteC_dico1)
    # print("\n\n")
    #print("ENTITE 2 : ",entiteC_dico2)
    print("entite fini")

    #print("\n\n")
    # Intersection
    intersection = calculIntersection(entiteC_dico1, entiteC_dico2)
    print("INTERSECTION : ", intersection)
    print("intersection")

    if len(intersection) != 0:

        # traduction de la relation
        relationString = traductionChiffreToRelation(relation)

        tab_triplet1 = []
        tab_triplet2 = []
        tab_iprim = []

        for i in intersection:
            iprim = i[2].strip("'")
            r1, w1, anot1 = rechercheASK(mot1, "r_isa", iprim)
            r2, w2, anot2 = rechercheASK(iprim, relationString, mot2)
            tab_triplet1.append([r1, w1, anot1])
            tab_triplet2.append([r2, w2, anot2])
            tab_iprim.append(iprim)

        tab_note = calculNoteRelation(tab_triplet2, [1]*len(tab_triplet2))

        # enlever les doublons
        relations_triees = []
        #[mot1, iprim, relation, mot2]
        for iprim, poid_anotation, tab2 in zip(tab_iprim, tab_note, tab_triplet2):
            poid = int(tab2[1])
            relations_triees.append([mot1, iprim, relationString, mot2, poid, poid_anotation])
            
        # tri du tableau en fonction des poids d'anotation, puis en fonction des poids
        relations_triees.sort(key=tri_relations)
        
        print("\n\n")
        print("Tableau des relations triées : ", relations_triees)
        print("\n\n")
        # CHANGER CA POUR FAIRE UN CLASSEMENT DES MEILLEURES REPONSES

        print(f"\n\nOuiii un(e) {mot1} {traductionChiffreToFrancais(relation)} {mot2} parce que : \n")

        
        for r in relations_triees :
            print(f"   * {relations_triees.index(r)} * un(e) {r[0]} est un(e) {r[1]}, et un {r[1]} {traductionChiffreToFrancais(relation)} {r[3]} !!!")

        print("\n\n")

    else:
        print(f"\n\nNon un(e) {mot1} {traductionChiffreToFrancaisNeg(relation)} {mot2} parce que : \n")


def induction2(mot1, relation, mot2):  # A relation B

    dico1 = {}
    dico2 = {}

    # A vers C par r_isa par sortante
    # A vers B avec relation par sortante
    # C vers B avec relation par entrante

    rechercheDUMP([mot1, mot2])

    # Formatage en dico du mot 1 et du mot 2
    dico_courant = formaterDico([mot1, mot2])
    #print(dico_courant)
    # Puis je recupere le dico de chacun
    dico1 = dico_courant[mot1]
    dico2 = dico_courant[mot2]

    # Recuperer les r_isa du mot1 en parsant le dico pour obtenir que les r_isa id=6 DE A vers C
    # que les relations sortantes qui correspondent a r_isa donc on enleve les entrante
    dico1_form = parserDico(dico1, "relation", '6', is_entrante=False)
    #print("RELATIONS SORTANTES : ", dico1_form["4;r;rid;node1;node2;type;w"])
    #print("RELATION ENTRANTES : ",dico1_form["5;r;rid;node1;node2;type;w"])

    print("\n\n")

    # Recuperer tous les mots en rapport
    entites = []
    # Pour chaque entite trouvé
    for entite in dico1_form["2;e;eid;'name';type;w;'formated name'"]:
        entites.append(entite[2].replace("'", ""))
    # Suppression dans les entitesles mots avec des > ou ya des numéro qui serve a rien
    entites2 = []
    for e in entites:
        if e.find('>') == -1 and e.find(':') == -1 and e.find('>') == -1:
            entites2.append(e)
    entites = entites2
    print("entites :", entites)    
    # Chercher les entites en fichier
    rechercheDUMP(entites, id_relation=relation)
    # formater le dico de chaque entite
    dico_courant2 = formaterDico(entites, filter=True)
    # Ajout a la liste les dico
    dico_formater_total = []
    for e in entites:
        dico_formater_total.append(
            dico_courant2[e]["2;e;eid;'name';type;w;'formated name'"])

    print("dico formater total : ", dico_formater_total)

    print("\n\n")


def induction(mot1, relation, mot2):  # A relation B

    dico1 = {}
    dico2 = {}

    rechercheDUMP([mot1, mot2])

    # je forme en dico les mots 1 et deux
    dico_courant = formaterDico([mot1, mot2])
    # Puis je recupere le dico de chacun
    dico1 = dico_courant[mot1]
    dico2 = dico_courant[mot2]

    # Recuperer les relations entrantes de r_isa vers mot1
    dico1_form = parserDico(dico1, "relation", '6', is_sortante=False)
    print("RELATION ENTRANTES mot1 : ",
          dico1_form["5;r;rid;node1;node2;type;w"])
    print("\n\n")

    # Recuperer les relations entrantes de relation vers mot2
    dico2_form = parserDico(dico2, "relation", relation, is_sortante=False)
    print("RELATION ENTRANTES mot2 : ",
          dico2_form["5;r;rid;node1;node2;type;w"])

    # Recup les elements c de dico1
    entiteC_dico1 = dico1_form["2;e;eid;'name';type;w;'formated name'"]
    entiteC_dico2 = dico2_form["2;e;eid;'name';type;w;'formated name'"]
    #print("ENTITE 1 :",entiteC_dico1)
    print("\n\n")
    #print("ENTITE 2 : ",entiteC_dico2)

    #print("\n\n")
    # Intersection un peu null
    intersection = calculIntersection(entiteC_dico1, entiteC_dico2)
    print("INTERSECTION : ", intersection)

    if (len(intersection) != 0):
        print(f"\n\nOuiii un(e) {mot1} a un/des {mot2} parce que : \n")
    else:
        print(f"\n\nNonnnn un(e) {mot1} n'a pas un/des {mot2} parce que : \n")

    for i in intersection:
        iprim = i[2].strip("'")
        # and (">" not in iprim)
        if (iprim != mot1) and ("en:" not in iprim):
            print(
                f"   * un {mot1} est un(e) {iprim}, et un {iprim} a un(e)/des {mot2} !!!"
            )

    print("\n\n")

    copy_dico1 = dico1.copy()
    copy_dico2 = dico2.copy()

    for cle in copy_dico2.keys():
        copy_dico1[cle] = copy_dico1.get(cle, "") + copy_dico2[cle]

    dico3_form = parserDico(copy_dico1, "entite", intersection)
    #print(dico3_form)


def transitivité(mot1, relation, mot2):
    # si A vers B et B vers C alors A vers C
    return 0


if __name__ == "__main__":
    # 9 = r_has_part
    deduction("lion", '24', "boire")
    #induction("lion", '9', "moustaches")
