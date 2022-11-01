# PictureLabelizer

## Collaborateurs

* **Jonathan Friedli** : Travaille sur la partie labelisation des images et création de la suite de tests.

* **Lazar Pavicevic** : Travaille sur la partie du stockage des images et documentation du repository.

## Description

Ce projet est une application en ligne de commande Java permettant de labéliser en utilisant des services clouds.

Providers cloud supportés :
- [x] [AWS](https://aws.amazon.com/fr/) (développement en cours)
- [ ] [Azure](https://azure.microsoft.com/fr-fr/)
- [ ] [Google Cloud](https://cloud.google.com/?hl=fr)

## Prérequis

- [Java 17 (LTS)](https://adoptium.net/temurin/releases)
- [Maven](https://adoptium.net/temurin/releases)
- [IntelliJ IDEA](https://www.jetbrains.com/fr-fr/idea/download/#section=windows) (fortement recommandé)

#### Pour AWS

Il vous faut un compte AWS pour la région `eu-west-2` (Londres) et préciser les variables d'environnement suivantes :

- `AWS_PROFILE` nom du profile à utiliser
- `AWS_BUCKET` nom du bucket S3 à utiliser

Outils à installer :
- [AWS CLI Installation](https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html)
- [AWS CLI Setup](https://docs.aws.amazon.com/cli/latest/userguide/getting-started-quickstart.html)
- [AWS Toolkit](https://docs.aws.amazon.com/toolkit-for-jetbrains/latest/userguide/welcome.html) (fortement recommandé)

## Mise en route

Chaque étape de la mise en route est faisable sur IntelliJ ou en ligne de commande.
Le guide se concentre que sur la partie en ligne de commande.

1. Créer le fichier `.env` avec les variables d'environnement du provider cloud de votre choix
```bash
# A la racine du projet
cp .env.example .env

# Editer le fichier .env selon les prérequis de votre provider cloud
```

2. Installer les dépendances
```bash
# A la racine du projet
mvn clean install
```

3. Exécuter les tests
```bash
# A la racine du projet
mvn test
```

4. Exécuter l'application
```bash
# TODO
```
