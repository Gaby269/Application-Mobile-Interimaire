--------------------------
-- REQUETES DES VISITES --
--------------------------


-- Les 10 enclos les moins visités par saison
SELECT Enclos.idEnclos, Enclos.nom, COUNT(*) 
FROM Visites
JOIN TableDate ON idDate = dateId
JOIN Enclos ON Enclos.idEnclos = Visites.enclosId
GROUP BY Enclos.idEnclos, Enclos.nom, saison
ORDER BY COUNT(*) ASC 
FETCH FIRST 10 ROWS ONLY;


-- la moyenne d'age des visiteurs par jour de la semaine et par enclos
SELECT TableDate.numJourSemaine, Enclos.idEnclos, AVG(Visiteurs.age) AS MoyenAge
FROM Visiteurs
JOIN Visites ON Visiteurs.idVisiteur = Visites.visiteurId
JOIN TableDate ON Visites.dateId = TableDate.idDate
JOIN Enclos ON Visites.enclosId = Enclos.idEnclos
GROUP BY ROLLUP(TableDate.numJourSemaine, Enclos.idEnclos);


-- la moyenne d'âge des visiteurs par enclos
SELECT Enclos.idEnclos, Enclos.nom, AVG(Visiteurs.age)
FROM Visites
JOIN Visiteurs ON Visites.visiteurId = Visiteurs.idVisiteur
JOIN Enclos ON Visites.enclosId = Enclos.idEnclos
GROUP BY Enclos.idEnclos, Enclos.nom;


-- Nombre d'enclos moyen visités par âge des visiteur
SELECT AVG(COUNT(*))
FROM Visiteurs
JOIN Visites ON Visiteurs.idVisiteur = Visites.visiteurId
GROUP BY Visiteurs.age
ORDER BY AVG(COUNT(*)) DESC;


-- Le nombre de visiteurs par nationalité par enclos
SELECT Visiteurs.nationalite, Enclos.idEnclos, COUNT(*) AS nbVisiteurTotal
FROM Visites
JOIN Visiteurs ON Visites.visiteurId = Visiteurs.idVisiteur
JOIN Enclos ON Enclos.idEnclos = Visites.enclosId
GROUP BY CUBE(Visiteurs.nationalite, Enclos.idEnclos);


-- Nombre de visiteurs par mois par nationalité
SELECT TableDate.mois, COUNT(*)
FROM Visites
JOIN Visiteurs ON idVisiteur = visiteurId
JOIN TableDate ON TableDate.idDate = Visites.dateId
GROUP BY TableDate.mois, nationalite
ORDER BY TableDate.mois ASC;


-- l'enclos le plus visité par saison
SELECT TableDate.saison, Enclos.idEnclos, Enclos.nom, COUNT(*) AS nbTotalVisiteur
FROM Visites
JOIN Enclos ON Visites.enclosId = Enclos.idEnclos
JOIN TableDate ON Visites.dateId = TableDate.idDate
GROUP BY TableDate.saison, Enclos.idEnclos, Enclos.nom;


-- Nombre moyen de passage par enclos par age de visiteur ayant moins de 18 ans
SELECT Visiteurs.age, AVG(nbVisites)
FROM Visites
JOIN Visiteurs ON Visites.visiteurid = Visiteurs.idVisiteur
WHERE Visiteurs.age <= 18
GROUP BY Visiteurs.age;



-- Département dont viennent les visiteurs
SELECT Visiteurs.departement
FROM Visites
JOIN Visiteurs ON Visiteurs.idVisiteur = Visites.visiteurId;


-- Fréquentation des enclos en fonction de l'heure
SELECT Temps.heure, Enclos.idEnclos, SUM(Visites.nbVisites)
FROM Visites
JOIN Temps ON Temps.idTemps = Visites.tempsId
JOIN Enclos ON Enclos.idEnclos = Visites.enclosId
GROUP BY Temps.heure, Enclos.idEnclos;