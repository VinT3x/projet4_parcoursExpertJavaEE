# projet4_parcoursExpertJavaEE

Les tests unitaires ont permis de déceler les anomalies suivantes. Voici les corrections apportées.

	Couche MODEL :
	Dans l'entité EcritureComptable :
		- le pattern de la propriété reference n'était pas correct, il faut en début de chaînes 2 lettres majuscules et non pas de 1 à 5 lettres.
		- la méthode getTotalCredit() utilisait la méthode getDebit() au lieu de getCredit(),
		- la méthode isEquilibree() comparait le résultat d'une égalité de BigDecimal à l'aide de equals() au lieu de compareTo()

	Couche BUSINESS :
	Dans la classe ComptabiliteManagerImpl:
		- suppression de l'héritage de la classe AbstractBusinessManager, pour "mocké" plus facilement dans les tests unitaires.
		- la méthode updateEcritureComptable(), ne contrôlait pas l'écriture comptable passée en paramètre. Ajout du contrôle en appelant la méthode checkEcritureComptable(pEcritureComptable).
		
	Couche CONSUMER :
	Dans le fichier sqlContext.xml :
		- sur la requête Insert de la propriété SQLinsertListLigneEcritureComptable, il manquait une virgule entre les colonnes debit et credit.
