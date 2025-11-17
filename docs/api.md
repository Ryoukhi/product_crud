# Documentation API - Gestion des Produits

## Vue d'ensemble

L'API Product Management est une application REST basée sur Spring Boot permettant de gérer un catalogue de produits. Elle offre des opérations CRUD complètes ainsi que des fonctionnalités de recherche et filtrage.

**Base URL:** `http://localhost:8080/api`  
**Version:** 1.0  
**Format:** JSON

---

## Configuration

### Port serveur
- **Port:** 8080
- **Base de données:** MySQL (localhost:3306)
- **Base de données:** product_db
- **Version Java:** 21
- **Version Spring Boot:** 3.5.7

---

## Modèle de données - Product

### Structure de l'entité

```json
{
  "id": "Long",
  "name": "string",
  "description": "string",
  "price": "BigDecimal",
  "quantity": "Integer",
  "available": "Boolean",
  "createdAt": "LocalDateTime",
  "updatedAt": "LocalDateTime"
}
```

### Attributs

| Attribut | Type | Contraintes | Description |
|----------|------|-----------|-------------|
| `id` | Long | Auto-généré, PK | Identifiant unique du produit |
| `name` | String | Obligatoire, 3-100 caractères | Nom du produit |
| `description` | String | Optionnel, max 500 caractères | Description détaillée du produit |
| `price` | BigDecimal | Obligatoire, > 0 | Prix unitaire du produit |
| `quantity` | Integer | Optionnel, ≥ 0 | Quantité en stock |
| `available` | Boolean | Par défaut: true | Disponibilité du produit |
| `createdAt` | LocalDateTime | Auto-généré, immutable | Date/heure de création |
| `updatedAt` | LocalDateTime | Auto-généré, mis à jour | Date/heure de dernière modification |

---

## Endpoints API

### 1. Obtenir tous les produits

#### Requête
```
GET /api/products
```

#### Description
Récupère la liste complète de tous les produits disponibles dans la base de données.

#### Réponse de succès (200 OK)
```json
[
  {
    "id": 1,
    "name": "Laptop",
    "description": "Ordinateur portable haute performance",
    "price": 999.99,
    "quantity": 15,
    "available": true,
    "createdAt": "2025-11-17T10:30:00",
    "updatedAt": "2025-11-17T10:30:00"
  },
  {
    "id": 2,
    "name": "Souris",
    "description": "Souris sans fil ergonomique",
    "price": 29.99,
    "quantity": 50,
    "available": true,
    "createdAt": "2025-11-17T10:35:00",
    "updatedAt": "2025-11-17T10:35:00"
  }
]
```

#### Codes de réponse
- **200 OK:** Liste des produits récupérée avec succès

---

### 2. Obtenir un produit par ID

#### Requête
```
GET /api/products/{id}
```

#### Paramètres
| Paramètre | Type | Localisation | Description |
|-----------|------|-------------|-------------|
| `id` | Long | URL (path) | ID du produit à récupérer |

#### Exemple
```
GET /api/products/1
```

#### Réponse de succès (200 OK)
```json
{
  "id": 1,
  "name": "Laptop",
  "description": "Ordinateur portable haute performance",
  "price": 999.99,
  "quantity": 15,
  "available": true,
  "createdAt": "2025-11-17T10:30:00",
  "updatedAt": "2025-11-17T10:30:00"
}
```

#### Codes de réponse
- **200 OK:** Produit trouvé et retourné
- **404 Not Found:** Produit avec cet ID n'existe pas

---

### 3. Créer un nouveau produit

#### Requête
```
POST /api/products
```

#### En-têtes requis
```
Content-Type: application/json
```

#### Corps de la requête
```json
{
  "name": "Clavier",
  "description": "Clavier mécanique RGB",
  "price": 149.99,
  "quantity": 20,
  "available": true
}
```

#### Champs obligatoires
- `name` (String): 3-100 caractères, non vide
- `price` (BigDecimal): Doit être > 0

#### Champs optionnels
- `description` (String): Max 500 caractères
- `quantity` (Integer): Par défaut 0, doit être ≥ 0
- `available` (Boolean): Par défaut true

#### Réponse de succès (201 Created)
```json
{
  "id": 3,
  "name": "Clavier",
  "description": "Clavier mécanique RGB",
  "price": 149.99,
  "quantity": 20,
  "available": true,
  "createdAt": "2025-11-17T11:00:00",
  "updatedAt": "2025-11-17T11:00:00"
}
```

#### Codes de réponse
- **201 Created:** Produit créé avec succès
- **400 Bad Request:** Données invalides ou manquantes

#### Erreurs possibles de validation

| Champ | Erreur |
|-------|--------|
| `name` | "Le nom du produit est obligatoire" |
| `name` | "Le nom doit contenir entre 3 et 100 caractères" |
| `price` | "Le prix est obligatoire" |
| `price` | "Le prix doit être supérieur à 0" |
| `description` | "La description ne peut pas dépasser 500 caractères" |
| `quantity` | "La quantité ne peut pas être négative" |

---

### 4. Mettre à jour un produit

#### Requête
```
PUT /api/products/{id}
```

#### Paramètres
| Paramètre | Type | Localisation | Description |
|-----------|------|-------------|-------------|
| `id` | Long | URL (path) | ID du produit à mettre à jour |

#### En-têtes requis
```
Content-Type: application/json
```

#### Corps de la requête
```json
{
  "name": "Laptop Pro",
  "description": "Ordinateur portable haute performance updated",
  "price": 1099.99,
  "quantity": 10,
  "available": true
}
```

#### Exemple
```
PUT /api/products/1
```

#### Réponse de succès (200 OK)
```json
{
  "id": 1,
  "name": "Laptop Pro",
  "description": "Ordinateur portable haute performance updated",
  "price": 1099.99,
  "quantity": 10,
  "available": true,
  "createdAt": "2025-11-17T10:30:00",
  "updatedAt": "2025-11-17T11:15:00"
}
```

#### Codes de réponse
- **200 OK:** Produit mis à jour avec succès
- **404 Not Found:** Produit avec cet ID n'existe pas
- **400 Bad Request:** Données invalides

---

### 5. Supprimer un produit

#### Requête
```
DELETE /api/products/{id}
```

#### Paramètres
| Paramètre | Type | Localisation | Description |
|-----------|------|-------------|-------------|
| `id` | Long | URL (path) | ID du produit à supprimer |

#### Exemple
```
DELETE /api/products/1
```

#### Réponse de succès (204 No Content)
```
(Corps vide)
```

#### Codes de réponse
- **204 No Content:** Produit supprimé avec succès
- **404 Not Found:** Produit avec cet ID n'existe pas

---

### 6. Rechercher des produits par nom

#### Requête
```
GET /api/products/search
```

#### Paramètres
| Paramètre | Type | Localisation | Description |
|-----------|------|-------------|-------------|
| `name` | String | Query | Terme de recherche (insensible à la casse) |

#### Exemple
```
GET /api/products/search?name=laptop
```

#### Réponse de succès (200 OK)
```json
[
  {
    "id": 1,
    "name": "Laptop",
    "description": "Ordinateur portable haute performance",
    "price": 999.99,
    "quantity": 15,
    "available": true,
    "createdAt": "2025-11-17T10:30:00",
    "updatedAt": "2025-11-17T10:30:00"
  },
  {
    "id": 4,
    "name": "Laptop Gaming",
    "description": "Ordinateur portable pour gaming",
    "price": 1499.99,
    "quantity": 8,
    "available": true,
    "createdAt": "2025-11-17T11:30:00",
    "updatedAt": "2025-11-17T11:30:00"
  }
]
```

#### Codes de réponse
- **200 OK:** Résultats de recherche récupérés (peut être une liste vide)

#### Caractéristiques
- Recherche insensible à la casse
- Recherche par correspondance partielle (contient)
- Retourne une liste vide si aucun résultat

---

## Exemples d'utilisation avec cURL

### Créer un produit
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Monitor 4K",
    "description": "Écran 4K 27 pouces",
    "price": 399.99,
    "quantity": 12,
    "available": true
  }'
```

### Récupérer tous les produits
```bash
curl http://localhost:8080/api/products
```

### Récupérer un produit spécifique
```bash
curl http://localhost:8080/api/products/1
```

### Mettre à jour un produit
```bash
curl -X PUT http://localhost:8080/api/products/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Monitor 4K Pro",
    "description": "Écran 4K 27 pouces professionnel",
    "price": 449.99,
    "quantity": 10,
    "available": true
  }'
```

### Supprimer un produit
```bash
curl -X DELETE http://localhost:8080/api/products/1
```

### Rechercher des produits
```bash
curl "http://localhost:8080/api/products/search?name=monitor"
```

---

## Codes de réponse HTTP

| Code | Signification | Description |
|------|---------------|-------------|
| **200** | OK | La requête a réussi |
| **201** | Created | La ressource a été créée avec succès |
| **204** | No Content | La requête a réussi (suppression) |
| **400** | Bad Request | Données invalides ou manquantes |
| **404** | Not Found | La ressource n'existe pas |
| **500** | Internal Server Error | Erreur serveur |

---

## Gestion des erreurs

### Format d'erreur standard
Les erreurs retournent généralement un JSON avec les détails :

```json
{
  "timestamp": "2025-11-17T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Le nom doit contenir entre 3 et 100 caractères",
  "path": "/api/products"
}
```

### Erreurs de validation courantes

**Produit non trouvé (404)**
```
Produit non trouvé avec l'id: 999
```

**Données invalides (400)**
```
Le nom du produit est obligatoire
Le prix doit être supérieur à 0
La quantité ne peut pas être négative
```

---

## Architecture technique

### Technologies utilisées
- **Framework:** Spring Boot 3.5.7
- **Langage:** Java 21
- **Base de données:** MySQL 8
- **ORM:** JPA/Hibernate
- **Validation:** Jakarta Validation
- **Documentation API:** Springdoc OpenAPI (Swagger)
- **Build:** Maven

### Dépendances principales
- `spring-boot-starter-data-jpa` - Accès aux données
- `spring-boot-starter-validation` - Validation des données
- `mysql-connector-java` - Driver MySQL
- `lombok` - Réduction du boilerplate
- `springdoc-openapi-starter-webmvc-ui` - Documentation Swagger

### Structure du projet
```
product_management/
├── src/
│   ├── main/
│   │   ├── java/com/eadl/product_management/
│   │   │   ├── controllers/
│   │   │   │   └── ProductController.java
│   │   │   ├── entities/
│   │   │   │   └── Product.java
│   │   │   ├── services/
│   │   │   │   └── ProductService.java
│   │   │   ├── repositories/
│   │   │   │   └── ProductRepository.java
│   │   │   └── ProductManagementApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/...
└── pom.xml
```

---

## Notes de développement

### Timestamps automatiques
- `createdAt` est défini automatiquement à la création
- `updatedAt` est mis à jour automatiquement à chaque modification
- Ces champs ne peuvent pas être modifiés manuellement via l'API

### Connexion à la base de données
Par défaut, la connexion utilise:
- **Host:** localhost
- **Port:** 3306
- **Database:** product_db
- **Username:** root
- **Password:** (vide)

Pour modifier ces paramètres, éditez `application.properties`.

### Validation des données
La validation s'effectue au niveau de l'entité JPA et du contrôleur REST.

---

## Support et maintenance

Pour plus d'informations sur l'architecture générale du projet, consultez :
- [Architecture générale](./architecture.md)
- [Configuration](./configurations.md)
- [Commits](./commits.md)
