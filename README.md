# PictureLabelizer

## Collaborateurs

### Product Owners

* **[Nicolas Glassey](https://github.com/NicolasGlassey)** : Enseignant pour le cours AMT à l'HEIG-VD


* **[Adrien Allemand](https://github.com/AdrienAllemand)** : Assistant pour le cours AMT à l'HEIG-VD

### Développeurs

* **[Jonathan Friedli](https://github.com/Marinlestylo)** : Etudiant en troisième année à l'HEIG-VD en ingénierie
  logicielle. Responsable de
  l'implémentation
  de création de labels.


* **[Lazar Pavicevic](https://github.com/Lazzzer)** : Etudiant en troisième année à l'HEIG-VD en ingénierie logicielle.
  Responsable de
  l'implémentation
  de manipulation de data objects.

## Description

Ce projet est une application Java permettant de détecter des labels sur une image fournie. Il s'agit d'une application
pensée pour être découpée en plusieurs
micro-services et être capable d'utiliser plusieurs providers clouds pour la reconnaissance d'images et le stockage des
données.

### Wiki

Le [wiki](https://github.com/AMT-TEAM07/PictureLabelizer/wiki) du projet regroupe toutes les informations nécessaires
pour comprendre notre méthodologie de travail, nos choix
et la documentation utilisée pour implémenter notre projet.

### Providers cloud supportés

- [x] [AWS](https://aws.amazon.com/fr/) (🚧 développement en cours 🚧)
- [ ] [Azure](https://azure.microsoft.com/fr-fr/)
- [ ] [Google Cloud](https://cloud.google.com/?hl=fr)

## Prérequis

- [Java 17 (LTS)](https://adoptium.net/temurin/releases)
- [Maven 3.8](https://maven.apache.org/download.cgi)

Optionnel mais fortement recommandé:

- [IntelliJ IDEA](https://www.jetbrains.com/fr-fr/idea/download/#section=windows)

### Pour AWS

#### Outils à installer :

- AWS CLI
    - [AWS CLI Installation](https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html)
    - [AWS CLI Configuration](https://docs.aws.amazon.com/cli/latest/userguide/getting-started-quickstart.html)

Optionnel mais fortement recommandé:

- [AWS Toolkit](https://docs.aws.amazon.com/toolkit-for-jetbrains/latest/userguide/welcome.html)

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
> Il est d'ailleurs possible d'omettre l'utilisation de ce fichier si les variables sont chargées dans l'environnement
> de la session actuelle.

## Test de validation

Pour l'étape actuelle du projet, le serveur d'intégration dispose d'un fichier `jar` lançant automatiquement
trois détections de labels. Une prenant un fichier local (ici `montreux.jpg` au même niveau que le `jar`), une prenant
une URL d'une image et une dernière utilisant un image en `base64`.

La structure sur le serveur d'intégration :

```bash
~/
|- /picture-labelizer
   |- .env
   |- montreux.jpg
   |- PictureLabelizer-1.0-SNAPSHOT.jar
```

Dans `picture-labelizer`, il faut lancer la commande suivante :

```bash
java -jar PictureLabelizer-1.0-SNAPSHOT.jar

# ou plus simplement
java -jar *.jar
```

Le résultat final devrait produire :

* Un fichier `montreux.jpg` et un fichier `montreux.jpg.json` dans le bucket S3 issue de la détection du fichier local.

* Un fichier `new-york.json` dans le bucket S3 issu de la détection de l'image à partir de l'URL.

* Un log s'affichant sur la console issu de la détection sur l'image en `base64`.
