import requests
import json
import os


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
def rechercheRESEAU(phr,
                    depart=True,
                    not_sortante=False,
                    not_entrante=False,
                    id_relation=-1):

	for p in phr:

		# Pour ne pas traiter la relation qui sert a rien
		if len(phr) == 3:
			if p == phr[1]:
				continue

		# Si le mot a pas deja été trouvé
		if not (os.path.exists(f"selected_files/selected_{p}.txt") and
		        (depart == True)):
			#     print(f"\nLe fichier selected_files/selected_{p}.txt existe déjà alors on fait seulement le dictionnaire")
			# else:
			sortante = ""
			entrante = ""
			id = ""

			if (not_sortante):
				sortante = "&relout=norelout"  # enleve les sortante
			if (not_entrante):
				entrante = "&relin=norelin"  # enleve les entrantes
			if (id_relation != -1):
				id = str(id_relation)

			# Faire la requête HTTP
			url = "https://www.jeuxdemots.org/rezo-dump.php?gotermsubmit=Chercher&gotermrel=" + p + "&rel=" + id + sortante + entrante
			#print(url)
			response = requests.get(url)

			# Recuperation des données de reseauDump dans body
			body = response.text

			# Recherche de la position de la première occurrence de la balise <CODE> pour récuperer les choses à l'interieur
			start_index = body.find("<CODE>")

			# Recuperation du texte du code source
			selected = ""

			# Si le mot existe dans la base
			if start_index != -1:
				start_index += 6  # On rajoute la longueur de la balise <CODE>
				end_index = body.find("</CODE>", start_index)

				if end_index != -1:
					# Extraction du contenu entre les balises <CODE> et </CODE>
					code_content = body[start_index:end_index]
					selected = code_content

			# Sinon on regarde s'il y a un message d'erreur qui dit que le mot n'existe pas
			else:
				start_index_warning = body.find(u"""<div class="jdm-warning">""")
				if start_index_warning == -1:
					print(f"\nLe mot {p} n'existe pas veuillez changer la phrase")
					exit(1)

			# Ecriture dans un texte
			if depart == True:
				with open(f"./selected_files/selected_{p}.txt", "w") as f:
					f.write(selected)
				print(
				 f"\n\nLes données ont été écrites dans le fichier selected_{p}.txt\n\n")
			else:
				lines2 = selected.split("\n")  # split par les truc ajouté
				lines1 = []
				# Recuperationd les informations dans le fichier
				with open(f"./selected_files/selected_{p}.txt", "r") as f:
					lines1 = f.readlines()  # lit les lignes de code

				# Modification de selected
				i = 0
				for j in range(len(lines1)+1):
					print(j)
					if lines1[j] == lines2[i]:
						i += 1
					else:
						i += 1
						selected = selected[:selected.find('\n', selected.find(lines1[j])) +
						                    1] + lines1[j] + '\n' + selected[
						                     selected.find('\n', selected.find(lines1[j])) + 1:]

				# Ecriture dans le ficheir des modifications
				with open(f"./selected_files/selected_{p}.txt", "w") as file:
					file.write(selected)

				print(
				 f"\n\nLes données ont été ajouté dans le fichier selected_{p}.txt\n\n")


#Fonction qui forme le texte en dico
def formaterDico(phr):

	# Dictionnaire pour tous les mots de la phrase
	dico_entier = {}

	for p in phr:

		# Remplir le dico
		dico_entier[p] = {}

		# Bossage avec le fichier mtn
		donnees_par_categorie = {}

		with open(f'./selected_files/selected_{p}.txt', 'r') as file:

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

		#print(dico_entier)
		# Ecriture dans un texte mais pas obligé
		# Si les deux fichiers n'existe pas
		#if (not os.path.exists(f"dico_files/dico_{p}.txt") :
		#with open(f"./dico_files/dico_{p}.txt", "w") as f:
		#        json.dump(dico_entier[p], f, indent=1)
		#        #f.write(str(dico_entier))
		#print(f"\n\nLes données ont été écrites dans le fichier dico_{p}.txt\n\n")
		#else :
		#    print("\n\nElles y été deja\n\n")

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
			# Parcours des tableaux
			for tab in dico_formater[key]:
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
		for key in cle:
			#print(key)
			# Parcours des tableaux
			for tab in dico_formater[key]:
				#print(tab)
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


# // DUMP pour le terme 'pigeon' (eid=74657)
