def parser(file):
	
    RDF_parse = {}	#tableau parser
    prefixes = {}	#ensemble des prefixes
    entite = []		#ensembles des entités
    prop = []		#ensemble des propriété intermediaire
    proprietes = {} #ensemble des propriétés
	p = []
	pr = {}
	

	#parcours du fichier
    for line in open(file, "r", encoding='utf8'):
        ls = line.strip()	#supression espace avt et après
        if ls == "": continue #skip les lignes vides
        elif ls.startswith("@prefix"): #on ajoute le prefixe au dictionnaire
            prefixe = ls.split()	#separation entre chaque expression
            prefixes[prefixe[1].split(":")[0]] = prefixe[2]		#separation entre le nom et le lien
            # regex si jamais : <[^>]*>
        else:
            entite.append(ls)	 #ajouter ls à l'entité
            if ls[-1] == ".":	#si on arrive au point
                crochets = 0
                for e in entite[1:] :	#pour chaque ligne dans entite 
                    mots = e.split()
                    for mot in mots:
                        if mot == "[":
                            if mots[-1] == ";" :
		                        pr[mots[0]] = mots[1:-1]
                        else:
                            crochets -= 0
                    prop.append(mots)	#ajoute le debut
                    if mots[-1] == ";" and crochets == 0 :
                        #if mots[1:-1]
                        proprietes[mots[0]] = mots[1:-1]
                        #print(prop)
                RDF_parse[entite[0]] = proprietes	#on ajoute l'entité avec son nom comme clé
                entite = []
                prop = []
                proprietes = {}
		
    return RDF_parse