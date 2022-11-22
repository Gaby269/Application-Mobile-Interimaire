#SUPPRIMER LES PROCESSUS QUI SONT INUTILES
import os
from subprocess import Popen, PIPE

listeProcessus = ''.join(map(chr, ( Popen(["ps", "aux"], stdout=PIPE).communicate())[0]))			#liste de tous les processus dans le systeme
for r in listeProcessus.split('\n'):										#regarde tous les processus
	if (("bin/noeud" in r) or ("bin/serveur" in r)):			#si on a des serveur qui tourne a partir de ces neouds la
		num = r.split(' ')																	#on le decoupe
		for n in num:																				#comme il peut y avoir des processus sur la même ligne
			if len(n) > 0 and n[0] in '0123456789': 					#si n commence par un digit : c'est un processus
				os.system("kill " + n)													#on les tue
				break																						#on finit