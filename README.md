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

Optionnel mais fortement recommandé:

- [IntelliJ IDEA](https://www.jetbrains.com/fr-fr/idea/download/#section=windows)

### Pour AWS

#### Outils à installer :

- AWS CLI
    - [AWS CLI Installation](https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html)
    - [AWS CLI Setup](https://docs.aws.amazon.com/cli/latest/userguide/getting-started-quickstart.html)

Optionnel mais fortement recommandé:

- [AWS Toolkit](https://docs.aws.amazon.com/toolkit-for-jetbrains/latest/userguide/welcome.html)

Tout d'abord, il faut un [credential AWS](https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-files.html).

Les credentials doivent figurer dans le fichier `.env` à la racine du projet avec les variables d'environnement
suivantes:

- `AWS_ACCESS_KEY_ID`
- `AWS_SECRET_ACCESS_KEY`
- `AWS_DEFAULT_REGION`

Ensuite, après avoir créé un bucket S3, il faut rajouter son nom dans le fichier `.env` pour la variable suivante:

- `AWS_BUCKET`

## Mise en route

Chaque étape de la mise en route est faisable sur l'interface graphique d'IntelliJ ou en ligne de commande.

Après avoir cloné le repository, à la racine du projet:

1. Créer le fichier `.env` avec les variables d'environnement du provider cloud de votre choix

```bash
cp .env.example .env

# Editer le fichier .env selon les prérequis de votre provider cloud
vi .env
```

2. Installer les dépendances

```bash
mvn clean install
```

3. Lancer les tests unitaires

```bash
# Lancer tous les tests
mvn test

# Lancer un test spécifique
mvn test -Dtest=NomDeLaClasseDeTest
```

4. Créer un exécutable JAR et lancer l'application

```bash
# Package l'application
mvn package

# Lancer l'application
java -jar target/*.jar
```
> **Warning**  
> Le fichier ``.env`` doit se trouver au même niveau que l'appel de la commande ``java -jar``. 
> Il est d'ailleurs possible d'omettre l'utilisation de ce fichier si les variables sont chargées dans l'environnement de la session actuelle.
