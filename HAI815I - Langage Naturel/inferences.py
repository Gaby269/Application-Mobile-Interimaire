import os
from parser import rechercheRESEAU, formaterDico, parserDico


def deduction(mot1, relation, mot2):  # A relation B

	dico1 = {}
	dico2 = {}

	# Si les deux fichiers n'existe pas
	if (not os.path.exists(f"selected_files/selected_{mot1}.txt")
	    and not os.path.exists(f"selected_files/selected_{mot2}.txt")):
		rechercheRESEAU([mot1],
		                depart=True,
		                not_sortante=False,
		                not_entrante=False,
		                id_relation=-1)  # recuperation des données de mot1
		rechercheRESEAU([mot2])  # recuperation des données de mot2
	# Si le fichier 1 existe mais pas le deux
	elif (os.path.exists(f"selected_files/selected_{mot1}.txt")
	      and not os.path.exists(f"selected_files/selected_{mot2}.txt")):
		rechercheRESEAU([mot2])  # recuperation des données de mot2
	# Si le fichier 2 existe mais pas le 1
	elif (not os.path.exists(f"selected_files/selected_{mot1}.txt")
	      and os.path.exists(f"selected_files/selected_{mot2}.txt")):
		rechercheRESEAU([mot1])  # recuperation des données de mot1

	# je forme en dico les mots 1 et deux
	dico_courant = formaterDico([mot1, mot2])
	# Puis je recupere le dico de chacun
	dico1 = dico_courant[mot1]
	dico2 = dico_courant[mot2]

	# Recuperer les r_isa du mot1 en parsant le dico pour obtenir que les r_isa id=6 DE A vers C
	# que les relations sortantes qui correspondent a r_isa donc on enleve les entrante
	dico1_form = parserDico(dico1, "relation", '6', is_entrante=False)
	#print("RELATIONS SORTANTES : ",dico1_form["4;r;rid;node1;node2;type;w"])
	#print("RELATION ENTRANTES : ",dico1_form["5;r;rid;node1;node2;type;w"])
	print("\n\n")

	# Recuperer tous les mots trouvé et voir si relation vers mot2 = realtion DE C vers B
	# Recup les élements de b que les relations entrante qui corresponde a la relation donnée donc on enleve les sortantes
	dico2_form = parserDico(dico2, "relation", relation, is_sortante=False)
	#print("DICO2 FORMATER : ",dico2_form)
	# Recup les elements c de dico1
	entiteC_dico1 = dico1_form["2;e;eid;'name';type;w;'formated name'"]
	entiteC_dico2 = dico2_form["2;e;eid;'name';type;w;'formated name'"]
	#print("ENTITE 1 :",entiteC_dico1)
	print("\n\n")
	#print("ENTITE 2 : ",entiteC_dico2)

	#print("\n\n")
	# Intersection un peu null
	intersection = []
	for e1 in entiteC_dico1:
		for e2 in entiteC_dico2:
			if (e1[1] == e2[1]):
				intersection.append(e1)
	print("INTERSECTION : ", intersection)

	print(f"\n\nOuiii un(e) {mot1} a un/des {mot2} parce que : \n")

	for i in intersection:
		iprim = i[2].strip("'")
		# and (">" not in iprim)
		if (iprim != mot1) and ("en:" not in iprim):
			print(
			 f"   * un {mot1} est un(e) {iprim}, et un {iprim} a un(e)/des {mot2} !!!")

	print("\n\n")

	copy_dico1 = dico1.copy()
	copy_dico2 = dico2.copy()

	for cle in copy_dico2.keys():
		copy_dico1[cle] = copy_dico1.get(cle, "") + copy_dico2[cle]

	dico3_form = parserDico(copy_dico1, "entite", intersection)
	print(dico3_form)


def induction2(mot1, relation, mot2):  # A relation B

	dico1 = {}
	dico2 = {}

	# A vers C par r_isa par sortante
	# A vers B avec relation par sortante
	# C vers B avec relation par entrante

	# Si les deux fichiers n'existe pas
	if (not os.path.exists(f"selected_files/selected_{mot1}.txt")
	    and not os.path.exists(f"selected_files/selected_{mot2}.txt")):
		rechercheRESEAU([mot1],
		                depart=True,
		                not_sortante=False,
		                not_entrante=False,
		                id_relation=0)  # recuperation des données de mot1
		rechercheRESEAU([mot2])  # recuperation des données de mot2
	# Si le fichier 1 existe mais pas le deux
	if (os.path.exists(f"selected_files/selected_{mot1}.txt")
	    and not os.path.exists(f"selected_files/selected_{mot2}.txt")):
		rechercheRESEAU([mot2])  # recuperation des données de mot2
	# Si le fichier 2 existe mais pas le 1
	if (not os.path.exists(f"selected_files/selected_{mot1}.txt")
	    and os.path.exists(f"selected_files/selected_{mot2}.txt")):
		rechercheRESEAU([mot1])  # recuperation des données de mot1

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
	for e in entites :
		if e.find('>') == -1 and e.find(':') == -1 and e.find('>') == -1 :
			entites2.append(e)
	entites = entites2
	print("entites :", entites)
	# Chercher les entites en fichier
	rechercheRESEAU(entites,
	                depart=False,
	                not_entrante=True,
	                id_relation=relation)
	# formater le dico de chaque entite
	dico_courant2 = formaterDico(entites, filter=True)
	# Ajout a la liste les dico
	dico_formater_total = []
	for e in entites:
		dico_formater_total.append(dico_courant2[e]["2;e;eid;'name';type;w;'formated name'"])

	print("dico formater total : ", dico_formater_total)
	# Recuperer tous les mots trouvé et voir si relation vers mot2 = realtion DE C vers B
	# Recup les élements de b que les relations entrante qui corresponde a la relation donnée donc on enleve les sortantes
	#dico2_form = parserDico(dico1, "relation", relation, is_entrante=False)
	#print("DICO2 FORMATER : ", dico2_form)

	#[1,2,3,4]
	#[_,2,_,4]
	# chercher la relation tel que on est
	#['r',_,getId(mot1),getId(mot2),relation, _]
	# quand on a le tps

	# Recup les elements c de dico1
	#entiteC_dico1 = dico1_form["2;e;eid;'name';type;w;'formated name'"]
	#entiteC_dico2 = dico2_form["2;e;eid;'name';type;w;'formated name'"]
	#print("\n\n")
	#print("\n\n")
	#print("ENTITE 1 :",entiteC_dico1)
	print("\n\n")
	#print("ENTITE 2 : ",entiteC_dico2)

	#print("\n\n")
	# Intersection un peu null
	"""
	intersection = []
	for e1 in entiteC_dico1:
		for e2 in entiteC_dico2:
			if (e1[1] == e2[1]):
				intersection.append(e1)
	print("INTERSECTION : ", intersection)

	print(f"\n\nun(e) {mot1} a un(e)/des {mot2} parce que : \n")

	for i in intersection:
		iprim = i[2].strip("'")
		# and (">" not in iprim)
		if (iprim != mot1) and ("en:" not in iprim):
			print(
			 f"   * un(e) {mot1} est un(e) {iprim}, et un(e) {iprim} a un(e)/des {mot2} !!!"
			)
"""
	print("\n\n")



def induction(mot1, relation, mot2):  # A relation B

	dico1 = {}
	dico2 = {}

	# Si les deux fichiers n'existe pas
	if (not os.path.exists(f"selected_files/selected_{mot1}.txt")
	    and not os.path.exists(f"selected_files/selected_{mot2}.txt")):
		rechercheRESEAU([mot1],
		                depart=True,
		                not_sortante=False,
		                not_entrante=False,
		                id_relation=-1)  # recuperation des données de mot1
		rechercheRESEAU([mot2])  # recuperation des données de mot2
	# Si le fichier 1 existe mais pas le deux
	elif (os.path.exists(f"selected_files/selected_{mot1}.txt")
	      and not os.path.exists(f"selected_files/selected_{mot2}.txt")):
		rechercheRESEAU([mot2])  # recuperation des données de mot2
	# Si le fichier 2 existe mais pas le 1
	elif (not os.path.exists(f"selected_files/selected_{mot1}.txt")
	      and os.path.exists(f"selected_files/selected_{mot2}.txt")):
		rechercheRESEAU([mot1])  # recuperation des données de mot1

	# je forme en dico les mots 1 et deux
	dico_courant = formaterDico([mot1, mot2])
	# Puis je recupere le dico de chacun
	dico1 = dico_courant[mot1]
	dico2 = dico_courant[mot2]

	# Recuperer les relations entrantes de r_isa vers mot1
	dico1_form = parserDico(dico1, "relation", '6', is_sortante=False)
	print("RELATION ENTRANTES mot1 : ",dico1_form["5;r;rid;node1;node2;type;w"])
	print("\n\n")

	# Recuperer les relations entrantes de relation vers mot2
	dico2_form = parserDico(dico2, "relation", relation, is_sortante=False)
	if len(dico2_form["5;r;rid;node1;node2;type;w"]) == 0 :
		rechercheRESEAU([mot2],
						depart=False,
		                not_sortante=True)
		
	print("RELATION ENTRANTES mot2 : ",dico2_form["5;r;rid;node1;node2;type;w"])
	
	# Recup les elements c de dico1
	entiteC_dico1 = dico1_form["2;e;eid;'name';type;w;'formated name'"]
	entiteC_dico2 = dico2_form["2;e;eid;'name';type;w;'formated name'"]
	#print("ENTITE 1 :",entiteC_dico1)
	print("\n\n")
	#print("ENTITE 2 : ",entiteC_dico2)
	
	#print("\n\n")
	# Intersection un peu null
	intersection = []
	for e1 in entiteC_dico1:
		for e2 in entiteC_dico2:
			if (e1[1] == e2[1]):
				intersection.append(e1)
	print("INTERSECTION : ", intersection)

	if (len(intersection) != 0) :
		print(f"\n\nOuiii un(e) {mot1} a un/des {mot2} parce que : \n")
	else : 
		print(f"\n\nNonnnn un(e) {mot1} n'a pas un/des {mot2} parce que : \n")
		
	for i in intersection:
		iprim = i[2].strip("'")
		# and (">" not in iprim)
		if (iprim != mot1) and ("en:" not in iprim):
			print(
			 f"   * un {mot1} est un(e) {iprim}, et un {iprim} a un(e)/des {mot2} !!!")
	
	print("\n\n")
	
	copy_dico1 = dico1.copy()
	copy_dico2 = dico2.copy()
	
	for cle in copy_dico2.keys():
		copy_dico1[cle] = copy_dico1.get(cle, "") + copy_dico2[cle]
	
	dico3_form = parserDico(copy_dico1, "entite", intersection)
	print(dico3_form)



if __name__ == "__main__":
	# 9 = r_has_part
	#deduction("lion", "9", "moustache")
	induction("oiseau", '9', "bec")
