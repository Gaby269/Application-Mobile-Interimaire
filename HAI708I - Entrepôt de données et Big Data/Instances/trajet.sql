-- INSERT INTO Date VALUES (id, dateEntiere, numJour, nomJour, numMois, nomMois, annee, saison, isWeekend, isVacances, isJoursFerié, isFermeDate);
INSERT INTO Date VALUES (1, "01/10/2022", 1, "SAMEDI", 10, "OCTOBRE", 2022, "AUTOMNE", 4, 1, 0, 0, 0);


-- INSERT INTO Temps VALUES (id, tempsEntier, heure, minutes, secondes, AM_PM, creneau, isDerniereHeure, horaireOuverture, horaireFermeture, isFermeTemps);
INSERT INTO Temps VALUES (1, "11:00:00", 11, 0, 0, "AM", "MATIN", 0, "08:00:00", "20:00:00", 0);


-- INSERT INTO Visiteurs VALUES (id, nom, prenom, age, sexe, adresse, ville, departement, pays, nationnalité, tel, mail, nb_enfants);
INSERT INTO Visiteurs VALUES (1, "FOURBET", "MATHILDE", 54, "FEMME", "8 RUE HENRI", 	"BEZIERS", 	34, "FRANCE", "FRANCE", "***", "***", 0);
INSERT INTO Visiteurs VALUES (2, "AZAIS", "MANON", 12, "FEMME", "1 RUE DE LA CHARPENTE", "MONTPELLIER", 34, "FRANCE", "FRANCE", "***", "***", 0);
INSERT INTO Visiteurs VALUES (3, "DUFER", "MAXIME", 21, "HOMME", "18 RUE DES MUGUETS", 	"PARIS", 95, "FRANCE", "ISRAEL", "***", "***", 0);


-- INSERT INTO Enclos VALUES (id, nom, distance_entree, capacite, zone, type, longitude, latitude, nbAnimaux, nbEspeces);
INSERT INTO Enclos VALUES (1, "LIONS", 130, 15, "AFRIQUE", "SEC", "154.5645641", "0.6184846", 11, 2);
INSERT INTO Enclos VALUES (1, "ZEBRES", 30, 50, "AFRIQUE", "ARIDE", "154.64454641", "0.4964980", 40, 6);
INSERT INTO Enclos VALUES (1, "PARESSEUX", 350, 20, "JUNGLE", "HUMIDE", "154.489641", "0.04762846", 4, 1);


-- INSERT INTO Visites VALUES (visiteur, enclos, date, temps, nb_rentres);
INSERT INTO Visites VALUES (10, 12, 145, 4212, 1);
INSERT INTO Visites VALUES (10, 15, 145, 4275, 2);
INSERT INTO Visites VALUES (2, 15, 145, 4275, 5);