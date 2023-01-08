------------------------
-- VUES MATERIALISEES --
------------------------

CREATE MATERIALIZED VIEW regionVisiteur (idVisiteur, region) AS
SELECT idVisiteur, region
FROM Visiteur
GROUP BY idVisiteur, region;


CREATE MATERIALIZED VIEW ageEnclos (age, idEnclos) AS
SELECT age, idEnclos
FROM Visiteurs
JOIN Visites ON Visiteurs.idVisiteur = Visites.visiteurId
JOIN Enclos ON Visites.enclosId = Enclos.idEnclos
GROUP BY age, idEnclos;


CREATE MATERIALIZED VIEW typeStandVentes(idStand,typeStand) AS
SELECT idStand, typeStand
FROM Stand, Ventes
WHERE Stand.idStand= Ventes.standId;

CREATE MATERIALIZED View prixProduits(idProduit,prixVentes) as
SELECT idProduit, prixVentes
FROM Produit, Ventes
WHERE Produit.idProduit = Ventes.produitId;