INSERT INTO Date VALUES (1, "01/10/2022", 1, "SAMEDI", 10, "OCTOBRE", 2022, "AUTOMNE", 4, 1, 0, 0, 0);

INSERT INTO Temps VALUES (1, "11:00:00", 11, 0, 0, "AM", "MATIN", 0, 0, 0, 0);

INSERT INTO Stands VALUES (1, "BOISSONS MAGIQUES", "SUCRERIES", "0.49845515", "156.6846161", 5, 0, 154864, 16, 1);
INSERT INTO Stands VALUES (2, "SOUVENIRS RUGISSANTS", "SOUVENIRS", "1.45654515", "155.544681", 5, 0, 154864, 16, 1);
INSERT INTO Stands VALUES (3, "RESTO DINGO", "ALIMENTATION", "0.4971355", "154.076854", 5, 0, 154864, 16, 1);

-- INSERT INTO Vendeurs VALUES (id, nom, prenom, age, sexe, salaire, type_contrat_, duree_contrat, num_talky, mobile, mail);
INSERT INTO Vendeurs VALUES (1, "PETIT", "JEAN", 46, "HOMME", 1500, "ALIMENTATION", 150, 12, "***", "***");
INSERT INTO Vendeurs VALUES (2, "QUINTON", "EMMA", 26, "FEMME", 1300, "SUCRERIE", 150, 13, "***", "***");
INSERT INTO Vendeurs VALUES (3, "LEVAIN", "YASSINE", 31, "HOMME", 1350, "SUCRERIE", 150, 2, "***", "***");

-- INSERT INTO Produits VALUES (id, nom, marque, prix, type, tranche_age, type_stockage, code_barre, auto_retour, taille_cm, poids_g, couleur);
INSERT INTO Produits VALUES (1, "BOULKIPIK", "BONBONFEU", 0.12, "SUCRERIE", "7-18", "SACHETS", "***", "NON", 1, 15, "ROUGE");
INSERT INTO Produits VALUES (2, "MAGRET DE CANARD", "RICHEPLAT", 25, "ALIMENTATION", "18-99", "REFREGIRATEUR", "***", "NON", 50, 200, "BEIGE");
INSERT INTO Produits VALUES (3, "BONNET LION", "ZOOBONNET", 15, "SOUVENIRS", "S", "CARTON", "***", "NON", 20, 100, "BEIGE");

INSERT INTO Visiteurs VALUES (id, nom, prenom, age, sexe, adresse, ville, departement, pays, nationnalité, tel, mail, nb_enfants);
INSERT INTO Visiteurs VALUES (1, "JEAN", "PRENOM", 12, "HOMME", "8 RUE HENRI", "BEZIERS", "HERAULT", "FRANCE", "FRANCAIS", "***", "***", 0);

-- INSERT INTO Ventes VALUES (stand, vendeur, visiteur, produit, heure, date, quantite_achat, montant_achat);
INSERT INTO Ventes VALUES (4, 4, 10, 14, 2847, 145, 2, 15);
INSERT INTO Ventes VALUES (6, 7, 10, 12, 2974, 145, 1, 5);
INSERT INTO Ventes VALUES (6, 8, 19, 54, 3001, 145, 10, 70);