------------
-- TABLES --
------------



BEGIN
EXECUTE IMMEDIATE 'DROP TABLE Ventes';
EXCEPTION
 WHEN OTHERS THEN
	IF SQLCODE != -942 THEN
	RAISE;
	END IF;
END;
/

BEGIN
EXECUTE IMMEDIATE 'DROP TABLE Visites';
EXCEPTION
 WHEN OTHERS THEN
	IF SQLCODE != -942 THEN
	RAISE;
	END IF;
END;
/

BEGIN
EXECUTE IMMEDIATE 'DROP TABLE TableDate';
EXCEPTION
 WHEN OTHERS THEN
	IF SQLCODE != -942 THEN
	RAISE;
	END IF;
END;
/

BEGIN
EXECUTE IMMEDIATE 'DROP TABLE Temps';
EXCEPTION
 WHEN OTHERS THEN
	IF SQLCODE != -942 THEN
	RAISE;
	END IF;
END;
/
	
BEGIN
EXECUTE IMMEDIATE 'DROP TABLE Visiteurs';
EXCEPTION
 WHEN OTHERS THEN
	IF SQLCODE != -942 THEN
	RAISE;
	END IF;
END;
/
	
BEGIN
EXECUTE IMMEDIATE 'DROP TABLE Enclos';
EXCEPTION
 WHEN OTHERS THEN
	IF SQLCODE != -942 THEN
	RAISE;
	END IF;
END;
/
	
BEGIN
EXECUTE IMMEDIATE 'DROP TABLE Produit';
EXCEPTION
 WHEN OTHERS THEN
	IF SQLCODE != -942 THEN
	RAISE;
	END IF;
END;
/
	
BEGIN
EXECUTE IMMEDIATE 'DROP TABLE Stand';
EXCEPTION
 WHEN OTHERS THEN
	IF SQLCODE != -942 THEN
	RAISE;
	END IF;
END;
/

BEGIN
EXECUTE IMMEDIATE 'DROP TABLE Vendeurs';
EXCEPTION
 WHEN OTHERS THEN
	IF SQLCODE != -942 THEN
	RAISE;
	END IF;
END;
/

DROP VIEW DateVisites;
DROP VIEW TempsVisites;
DROP VIEW VisiteursVisites;
DROP VIEW DateVentes;
DROP VIEW TempsVentes;
DROP VIEW Clients;
	
	
----------------------------
--  DIMENSIONS PARTAGEES  --
----------------------------

CREATE TABLE TableDate (
	idDate NUMBER PRIMARY KEY,
	jour NUMBER, 
	mois NUMBER,
	annee NUMBER,
	numJourSemaine NUMBER, -- 1:lundi, 7:dimanche
	dateEntiere VARCHAR2(100),
	saison NUMBER, -- 1:printemps, 2:ete, 3:automne, 4:hivers
	isWeekend NUMBER,
	isVacances NUMBER,
	isJoursFerie NUMBER,
	isOuvertDate NUMBER
);


CREATE TABLE Temps (
	idTemps NUMBER PRIMARY KEY, 
	heure NUMBER,
	minutes NUMBER,
	secondes NUMBER,
	tempsEntier VARCHAR2(100),
	AM_PM VARCHAR2(10),
	creneau NUMBER, -- 1:8h-10h ... 4:17h-20h
	isDerniereHeure NUMBER, -- boolean
	isOuvertTemps NUMBER -- boolean
);


CREATE TABLE Visiteurs (
	idVisiteur NUMBER PRIMARY KEY,
	age NUMBER,
	sexe NUMBER, -- 1 si homme, 2 si femme 
	adresse VARCHAR2(100),
	ville VARCHAR2(100),
	departement NUMBER,
	pays VARCHAR2(100), 
	nationalite VARCHAR2(32),
	telephone VARCHAR2(10),
	email VARCHAR2(100),
	nb_enfants NUMBER
);




-----------------------
--   TABLE VISITES   --
-----------------------


CREATE TABLE Enclos (
	idEnclos NUMBER PRIMARY KEY,
	nom VARCHAR2(100),
	distance_entree NUMBER,
	capacite NUMBER,
	zone NUMBER,
	type VARCHAR2(100),
	longitude VARCHAR2(100),
	latitude VARCHAR2(100),
	nbAnimaux NUMBER,
	nbEspeces NUMBER
);


CREATE TABLE Visites (
	visiteurId NUMBER,
	enclosId NUMBER,
	dateId NUMBER,
	tempsId NUMBER,
	nbVisites NUMBER,
	PRIMARY KEY (visiteurId, enclosId, dateId, tempsId),
	FOREIGN KEY (visiteurId) REFERENCES Visiteurs(idVisiteur),
	FOREIGN KEY (dateId) REFERENCES TableDate(idDate),
	FOREIGN KEY (tempsId) REFERENCES Temps(idTemps),
	FOREIGN KEY (enclosId) REFERENCES Enclos(idEnclos)
);

-----------------------
--   TABLE VENTES    --
-----------------------

CREATE TABLE Vendeurs (
	idVendeur NUMBER PRIMARY KEY,
	age NUMBER,
	sexe NUMBER, -- 1:homme, 2:femme
	salaire NUMBER,
	typeContrat VARCHAR2(100),
	dureeContrat NUMBER, -- En mois
	numeroTel NUMBER,
	mail VARCHAR(100)
);

CREATE TABLE Stand (
  idStand NUMBER PRIMARY KEY, 
  nom varchar(32),
  typeStand VARCHAR2(100),
	longitude VARCHAR2(100),
	latitude VARCHAR2(100),
  nbEmployes NUMBER
);

CREATE TABLE Produit (
	idProduit NUMBER PRIMARY KEY,
	nom VARCHAR2(100),
	marque VARCHAR2(100),
	prixVente NUMBER(4,2), --
	poids NUMBER(3,1), -- kg
	limiteAge NUMBER
);


CREATE TABLE Ventes (
  vendeurId Number,
	standId NUMBER,
  produitId NUMBER,
	visiteurId NUMBER,
	dateId NUMBER,
  tempsId NUMBER,
	montantAchat NUMBER(5,2),
  CONSTRAINT PK_VENTES
    	PRIMARY KEY(vendeurId,standId,dateId,produitId,visiteurId, tempsId),
	CONSTRAINT FK_VENTES_VENDEUR
			FOREIGN KEY(vendeurId) REFERENCES Vendeurs(idVendeur),
	CONSTRAINT FK_VENTES_STAND
			FOREIGN KEY(standId) REFERENCES Stand(idStand),
	CONSTRAINT FK_VENTES_PRODUIT
			FOREIGN KEY(produitId) REFERENCES Produit(idProduit),
	CONSTRAINT FK_VENTES_VISITEUR
			FOREIGN KEY(visiteurId) REFERENCES Visiteurs(idVisiteur),
	CONSTRAINT FK_VENTES_TABLEDATE
			FOREIGN KEY(dateId) REFERENCES TableDate(idDate),
	CONSTRAINT FK_VENTES_TEMPS
			FOREIGN KEY(tempsId) REFERENCES Temps(idTemps)
);


-- Vues virtuelles pour dimensions partagées
CREATE VIEW DateVisites AS 
SELECT jour, mois, annee
FROM TableDate
WITH CHECK OPTION;

CREATE VIEW TempsVisites AS 
SELECT * 
FROM Temps
WITH CHECK OPTION;

CREATE VIEW VisiteursVisites 
AS SELECT * 
FROM Visiteurs
WITH CHECK OPTION;

CREATE VIEW DateVentes AS 
SELECT * 
FROM TableDate
WITH CHECK OPTION;

CREATE VIEW TempsVentes 
AS SELECT heure, minutes, secondes 
FROM Temps
WITH CHECK OPTION;

CREATE VIEW Clients AS 
SELECT * 
FROM Visiteurs
WITH CHECK OPTION;