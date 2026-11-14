# AI JARVIS LEGAL — Android

Application Android native en Kotlin + Jetpack Compose, en français, conçue comme base de travail pour la préparation au concours de la magistrature au Maroc.

## Fonctionnalités incluses
- Accueil et navigation par modules
- QCM avec validation, correction et score (questions de démonstration)
- Cas pratique avec zone de rédaction
- Fiches thématiques de jurisprudence à compléter
- Planning avec cases à cocher

## Compiler sans ordinateur personnel
Le dépôt contient un workflow GitHub Actions. Depuis ton téléphone :
1. Crée un compte GitHub si nécessaire.
2. Crée un nouveau dépôt, par exemple `ai-jarvis-legal-android`.
3. Envoie tous les fichiers de ce projet dans le dépôt en conservant leur arborescence.
4. Ouvre l'onglet **Actions** du dépôt.
5. Lance **Build Android APK** avec **Run workflow** (ou pousse un commit sur `main`).
6. Quand le workflow est terminé, ouvre son exécution et télécharge l'artefact `AI-JARVIS-LEGAL-debug-apk`.
7. Décompresse l'artefact et ouvre `app-debug.apk` sur ton téléphone Android. Autorise l'installation depuis cette source si Android le demande.

## À noter
- Ce paquet est le code source et le workflow de compilation, pas un APK déjà compilé.
- Le workflow requiert Gradle disponible sur le runner. Pour une compilation reproductible, ajoute le Gradle Wrapper (`gradlew`, `gradlew.bat`, `gradle/wrapper/gradle-wrapper.jar` et `gradle-wrapper.properties`) ou génère-le une fois avec `gradle wrapper`.
- Le contenu juridique est un jeu de démonstration non exhaustif. Vérifie les textes, la jurisprudence et les références dans les sources officielles marocaines avant tout usage académique.
- Aucune IA distante, base de données juridique ou synchronisation n'est connectée à ce stade. Les notes et tâches ne sont pas encore persistées après fermeture de l'application.
- Le fichier APK debug est destiné aux essais personnels. Une publication Google Play demande une version release signée et un compte développeur Google Play.
