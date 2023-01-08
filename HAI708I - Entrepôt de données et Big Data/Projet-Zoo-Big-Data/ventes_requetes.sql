-------------------------
-- REQUETES DES VENTES --
-------------------------



-- Moyene d'âge des visiteurs par jour de la semaine le matin (AM) par pays qui viennent acheter des choses
SELECT TableDate.numJourSemaine, AVG(Visiteurs.age) AS AgeMoyen, Visiteurs.pays
FROM Visiteurs
JOIN Ventes ON Visiteurs.idVisiteur = Ventes.visiteurId
JOIN TableDate ON Ventes.dateId = TableDate.idDate
JOIN Temps ON Ventes.tempsId = Temps.idTemps
WHERE Temps.AM_PM = 'AM'
GROUP BY TableDate.numJourSemaine, Visiteurs.pays;


-- Chiffre d'affaire des 3 types de stands par année
SELECT Stand.typeStand, SUM(Ventes.montantAchat) AS chiffreAffaire, TableDate.annee
FROM Ventes
JOIN Stand ON Ventes.standId = Stand.idStand
JOIN TableDate ON TableDate.idDate = Ventes.dateId
GROUP BY Stand.typeStand, TableDate.annee;


-- L'emplacement du stand (long, lat) qui a vendu le plus le derniers mois (exemple Decembre)
SELECT Stand.longitude, Stand.latitude, TableDate.mois, SUM(montantAchat) AS chiffreAffaire
FROM Ventes
JOIN Stand ON Stand.idStand = Ventes.standId
JOIN TableDate ON idDate = dateId
WHERE mois = 'DECEMBRE'
GROUP BY longitude, latitude
HAVING SUM(*) > ALL (SELECT SUM(montantAchat)
											FROM Ventes, TableDate
											WHERE Ventes.dateId = TableDate.idDate
											AND mois = 'DECEMBRE'
											GROUP BY longitude, latitude
										);


--Le produit qui marche le plus par stands et par saison
SELECT Produit.idProduit, Produit.nom, Stand.idStand, TableDate.saison
FROM Ventes
JOIN Produit ON idProduit = produitId
JOIN Stand ON idStand = standId
JOIN TableDate ON idDate = dateId
GROUP BY idProduit, Produit.nom, Stand.idStand, TableDate.saison;


-- Moyennes du chiffre d'affaires produit par les jeunes vendeurs (entre 18-30) pendant le weekend
SELECT AVG(montantAchat) AS moyChiffreAffaire
FROM Ventes
JOIN Vendeurs ON idVendeur = vendeurId
JOIN TableDate ON Ventes.dateId = TableDate.idDate
WHERE age > 18
AND age < 30
AND TableDate.isWeekend = 1;


-- Chiffre d'affaire des visiteurs par nationalité  dans les boutiques souvenirs
SELECT SUM(Ventes.montantAchat) AS ChiffreAffaire, Visiteurs.nationalite 
FROM Ventes, Visiteurs
WHERE Ventes.visiteurId = Visiteurs.idVisiteur
GROUP BY Visiteurs.nationalite;
  

-- Moyenne d'age des clients par type de stands quand par creneau horaire et par nationalité
SELECT AVG(age) AS AgeMoy, Stands.type, Temps.creneau
FROM Ventes, Stand, Temps, Visiteurs
WHERE isStand = standId
AND idTemps = tempsId
AND idVisiteur = visiteurId
GROUP BY typeStand, creneau;


-- Le prix des produits qui se vendent le plus.
SELECT Produit.idProduit, Produit.nom, COUNT(*) AS chiffre_affaire
FROM Produit, Ventes 
WHERE Produit.idProduit = Ventes.produitId
GROUP BY Produit.idProduit, Produit.nom
ORDER BY count(*) DESC
FETCH FIRST 20 ROWS ONLY;


-- Nombre moyen d'employé par type de stand a la derniere heure
SELECT Stand.typeStand, AVG(Stand.nbEmployes) AS MoyenneNombreEmploye
FROM Ventes
JOIN Stand ON Ventes.standId = Stand.idStand
JOIN Temps ON Ventes.tempsId = Temps.idTemps
WHERE Temps.isDerniereHeure = 1
GROUP BY Stand.typeStand;


-- Chiffre d'affaire par marque de produit
SELECT SUM(Ventes.montantAchat) AS ChiffreAffaire, Produit.idProduit, Produit.marque
FROM Ventes, Produit
WHERE Ventes.produitId = Produit.idProduit
GROUP BY Produit.idProduit, Produit.marque;