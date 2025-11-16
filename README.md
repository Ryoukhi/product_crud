[![Documentation Status](https://readthedocs.org/projects/ton-projet/badge/?version=latest)](https://ton-projet.readthedocs.io/fr/latest/)

````markdown

# API de Gestion de Produits - Spring Boot

Une API REST complète pour gérer un catalogue de produits avec documentation Swagger intégrée.

## Fonctionnalités

- CRUD complet (Create, Read, Update, Delete) sur les produits
- Documentation API interactive avec Swagger UI
- Recherche de produits par nom
- Base de données MySql
- Validation des données
- Annotations OpenAPI complètes

## Démarrage rapide

### Prérequis

- Java 17 ou supérieur
- Maven 3.6+

### Installation et lancement

```bash
# Cloner le projet
git clone https://github.com/Ryoukhi/product_crud.git
cd product_management

# Compiler et lancer l'application
mvn spring-boot:run
ou
./mvnw spring-boot:run
````

L'application démarre sur :
[http://localhost:8080](http://localhost:8080)

## Accès à la documentation

* Swagger UI : [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
* Documentation OpenAPI : [http://localhost:8080/api-docs](http://localhost:8080/api-docs)

## Endpoints API


### Liste des endpoints

| Méthode | Endpoint                        | Description                    |
| ------- | ------------------------------- | ------------------------------ |
| GET     | `/api/products`                 | Récupère tous les produits     |
| GET     | `/api/products/{id}`            | Récupère un produit par ID     |
| POST    | `/api/products`                 | Crée un nouveau produit        |
| PUT     | `/api/products/{id}`            | Met à jour un produit          |
| DELETE  | `/api/products/{id}`            | Supprime un produit            |
| GET     | `/api/products/search?name=...` | Recherche par nom              |
| GET     | `/api/products/available`       | Liste les produits disponibles |

## Exemples d'utilisation

### Créer un produit

```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Ordinateur Portable",
    "description": "Laptop haute performance",
    "price": 1299.99,
    "quantity": 50,
    "available": true
  }'
```

### Récupérer tous les produits

```bash
curl http://localhost:8080/api/products
```

### Mettre à jour un produit

```bash
curl -X PUT http://localhost:8080/api/products/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Ordinateur Portable Pro",
    "description": "Laptop très haute performance",
    "price": 1499.99,
    "quantity": 45,
    "available": true
  }'
```

### Supprimer un produit

```bash
curl -X DELETE http://localhost:8080/api/products/1
```

### Rechercher un produit

```bash
curl http://localhost:8080/api/products/search?name=ordinateur
```

## Structure du projet

```
src/main/java/com/example/productmanagement/
├── config/
│   └── OpenApiConfig.java          # Configuration Swagger
├── controller/
│   └── ProductController.java      # Endpoints REST
├── model/
│   └── Product.java                # Entité JPA
├── repository/
│   └── ProductRepository.java      # Interface JPA
├── service/
│   └── ProductService.java         # Logique métier
└── ProductManagementApplication.java # Point d'entrée

src/main/resources/
└── application.properties          # Configuration
```

## Modèle de données : Product

| Champ       | Type          | Description          | Contraintes                   |
| ----------- | ------------- | -------------------- | ----------------------------- |
| id          | Long          | Identifiant unique   | Auto-généré                   |
| name        | String        | Nom du produit       | 3–100 caractères, obligatoire |
| description | String        | Description          | Max 500 caractères            |
| price       | BigDecimal    | Prix                 | > 0, obligatoire              |
| quantity    | Integer       | Quantité en stock    | >= 0                          |
| available   | Boolean       | Disponibilité        | Par défaut true               |
| createdAt   | LocalDateTime | Date de création     | Auto-généré                   |
| updatedAt   | LocalDateTime | Date de modification | Auto-mise à jour              |

## Technologies utilisées

* Spring Boot 3.5.7
* Spring Data JPA
* MySql Database
* SpringDoc OpenAPI
* Lombok
* Bean Validation

## Dépendance SpringDoc

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.3.0</version>
</dependency>
```

## Documentation API : Points clés

* `@Operation` : description des endpoints
* `@ApiResponse` : codes de réponses
* `@Parameter` : documentation des paramètres
* Configuration OpenAPI : informations générales
* Swagger UI : tests interactifs

## Tests de l'API

Swagger UI permet de :

* Visualiser tous les endpoints
* Tester les requêtes directement
* Voir les schémas de données
* Lire les exemples de réponses

## Licence

Ce projet est sous licence IUC.

## Auteur

Développé avec Spring Boot et documenté avec Swagger/OpenAPI.

```
2025
```
