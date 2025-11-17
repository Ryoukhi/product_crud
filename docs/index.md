# 🛍️ Product Management - API de Gestion de Produits

Bienvenue dans la documentation complète de l'**API de Gestion de Produits**. Cette application est une solution REST moderne construite avec Spring Boot pour gérer un catalogue de produits complet.

---

## 📚 Vue d'ensemble

**Product Management** est une application **microservice** de gestion de produits qui fournit une API REST complète avec les capacités suivantes :

- ✅ **Opérations CRUD** complètes sur les produits
- ✅ **API REST** moderne et bien documentée
- ✅ **Validation des données** robuste
- ✅ **Documentation interactive** Swagger UI
- ✅ **Base de données** persistante MySQL
- ✅ **Architecture MVC** propre et maintenable
- ✅ **Recherche** et filtrage de produits
- ✅ **Gestion des erreurs** structurée

---

## 🎯 Objectif

L'application permet aux développeurs et utilisateurs finaux de :
- **Créer** de nouveaux produits avec validation automatique
- **Consulter** les produits de manière flexible (tous, par ID, par recherche)
- **Mettre à jour** les informations des produits existants
- **Supprimer** les produits obsolètes
- **Rechercher** rapidement par nom (insensible à la casse)
- **Gérer** l'inventaire avec quantités et disponibilité

---

## 🚀 Démarrage rapide

### Prérequis

- **Java 21** ou supérieur
- **Maven 3.6+**
- **MySQL 8.0+**
- **Git**

### Installation locale

#### 1. Cloner le repository
```bash
git clone https://github.com/Ryoukhi/product_crud.git
cd product_crud/product_management
```

#### 2. Configurer la base de données

Créer la base de données MySQL :
```sql
CREATE DATABASE product_db;
USE product_db;
```

Mettre à jour les identifiants dans `src/main/resources/application.properties` :
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/product_db
spring.datasource.username=root
spring.datasource.password=votre_mot_de_passe
```

#### 3. Compiler et exécuter

```bash
# Compiler
mvn clean package

# Exécuter l'application
mvn spring-boot:run

# Ou utiliser le wrapper Maven
./mvnw spring-boot:run  # Linux/Mac
mvnw.cmd spring-boot:run  # Windows
```

L'application démarre sur : **http://localhost:8080**

### 4. Accéder à l'API

**Documentation Swagger UI :**
```
http://localhost:8080/swagger-ui.html
```

**Spécification OpenAPI (JSON) :**
```
http://localhost:8080/v3/api-docs
```

**API Base URL :**
```
http://localhost:8080/api/products
```

---

## 📖 Documentation

### 📋 Navigation

| Section | Description |
|---------|-------------|
| [API Reference](./api.md) | Documentation complète des endpoints REST |
| [Architecture](./architecture.md) | Architecture MVC, flux de données, diagrammes |
| [Configuration](./configurations.md) | Configuration de l'application, propriétés |
| [Commits](./commits.md) | Historique des commits et changements |

---

## 🏗️ Architecture

L'application suit une architecture **MVC (Model-View-Controller)** avec une séparation claire en couches :

```
┌─────────────────────────────────────┐
│      Clients REST (HTTP)            │
└────────────────┬────────────────────┘
                 │
        ┌────────▼────────┐
        │   CONTROLLER    │  ProductController
        │  (Présentation) │  Routes HTTP, validation
        └────────┬────────┘
                 │
        ┌────────▼────────┐
        │    SERVICE      │  ProductService
        │    (Métier)     │  Logique métier
        └────────┬────────┘
                 │
        ┌────────▼────────┐
        │  REPOSITORY     │  ProductRepository
        │    (Données)    │  Accès BD
        └────────┬────────┘
                 │
        ┌────────▼────────┐
        │    DATABASE     │  MySQL
        │   (Persistance) │  Tables
        └─────────────────┘
```

### Composants principaux

- **ProductController** : Route les requêtes HTTP, valide les entrées
- **ProductService** : Contient la logique métier, orchestration
- **ProductRepository** : Accès aux données via JPA
- **Product** : Entité de domaine avec validation
- **OpenApiConfig** : Configuration Swagger/OpenAPI

Pour plus de détails, consultez [Architecture](./architecture.md).

---

## 🔄 Opérations disponibles

### CRUD Operations

| Opération | Endpoint | Méthode | Description |
|-----------|----------|--------|-------------|
| Lister tous | `/api/products` | GET | Récupère tous les produits |
| Récupérer un | `/api/products/{id}` | GET | Récupère un produit par ID |
| Créer | `/api/products` | POST | Crée un nouveau produit |
| Mettre à jour | `/api/products/{id}` | PUT | Modifie un produit existant |
| Supprimer | `/api/products/{id}` | DELETE | Supprime un produit |
| Rechercher | `/api/products/search?name=...` | GET | Recherche par nom |

### Exemple d'utilisation

#### Créer un produit
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop Pro",
    "description": "Ordinateur portable haute performance",
    "price": 1299.99,
    "quantity": 10,
    "available": true
  }'
```

#### Récupérer tous les produits
```bash
curl http://localhost:8080/api/products
```

#### Rechercher des produits
```bash
curl "http://localhost:8080/api/products/search?name=laptop"
```

Pour plus d'exemples, consultez [API Reference](./api.md).

---

## 📊 Modèle de données

### Entité Product

L'entité `Product` représente un produit du catalogue avec les attributs suivants :

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

### Attributs

| Attribut | Type | Obligatoire | Constraints |
|----------|------|-------------|-------------|
| `id` | Long | Auto | Clé primaire, auto-générée |
| `name` | String | Oui | 3-100 caractères |
| `description` | String | Non | Max 500 caractères |
| `price` | BigDecimal | Oui | > 0 |
| `quantity` | Integer | Non | ≥ 0, défaut 0 |
| `available` | Boolean | Non | Défaut true |
| `createdAt` | LocalDateTime | Auto | Date de création (immutable) |
| `updatedAt` | LocalDateTime | Auto | Dernière modification |

### Timestamps automatiques

- `createdAt` : Défini automatiquement à la création, immutable
- `updatedAt` : Défini à la création, mis à jour automatiquement à chaque modification

---

## 🛠️ Technologies utilisées

### Framework & Dépendances

```
Spring Boot .......................... 3.5.7
Java ................................ 21
MySQL ............................... 8.0+
Maven ............................... 3.6+
```

### Dépendances principales

| Dépendance | Version | Utilité |
|-----------|---------|---------|
| spring-boot-starter-web | 3.5.7 | Framework REST |
| spring-boot-starter-data-jpa | 3.5.7 | ORM/Persistance |
| spring-boot-starter-validation | 3.5.7 | Validation des données |
| mysql-connector-j | - | Driver MySQL |
| lombok | - | Réduction boilerplate |
| springdoc-openapi-starter-webmvc-ui | - | Swagger UI / OpenAPI |

### Configuration

L'application est configurée via `application.properties` :

```properties
# Nom de l'application
spring.application.name=product_management

# Serveur
server.port=8080

# Base de données
spring.datasource.url=jdbc:mysql://localhost:3306/product_db
spring.datasource.username=root
spring.datasource.password=

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

---

## 📂 Structure du projet

```
product_crud/
├── product_management/              # Application Spring Boot
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/eadl/product_management/
│   │   │   │   ├── controllers/
│   │   │   │   │   └── ProductController.java
│   │   │   │   ├── services/
│   │   │   │   │   └── ProductService.java
│   │   │   │   ├── entities/
│   │   │   │   │   └── Product.java
│   │   │   │   ├── repositories/
│   │   │   │   │   └── ProductRepository.java
│   │   │   │   ├── config/
│   │   │   │   │   └── OpenApiConfig.java
│   │   │   │   └── ProductManagementApplication.java
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   │       └── java/...
│   ├── pom.xml                      # Configuration Maven
│   ├── mvnw & mvnw.cmd              # Maven Wrapper
│   └── target/                      # Build output
│
├── docs/                            # Documentation
│   ├── index.md                     # Cette page
│   ├── api.md                       # Référence API complète
│   ├── architecture.md              # Détails architecture MVC
│   └── configurations.md            # Configuration application
│
├── README.md                        # Présentation générale
├── mkdocs.yml                       # Configuration MkDocs
└── requirements.txt                 # Dépendances Python (MkDocs)
```

---

## 🔍 Points clés

### Validation des données

L'application valide toutes les données à l'entrée :

```java
@NotBlank(message = "Le nom est obligatoire")
@Size(min = 3, max = 100, message = "Le nom entre 3 et 100 caractères")
private String name;

@NotNull(message = "Le prix est obligatoire")
@DecimalMin(value = "0.0", inclusive = false)
private BigDecimal price;
```

### Gestion des erreurs

Réponses d'erreur structurées :

```json
HTTP 400 Bad Request
{
  "timestamp": "2025-11-17T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Le nom doit contenir entre 3 et 100 caractères",
  "path": "/api/products"
}
```

### Documentation interactive

Accédez à la documentation interactive via Swagger UI :
- **URL:** http://localhost:8080/swagger-ui.html
- **Testez les endpoints directement**
- **Consultez les schémas JSON**
- **Visualisez les réponses**

---

## 📝 Exemples d'utilisation

### Via cURL

#### 1. Créer un produit
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

#### 2. Récupérer tous les produits
```bash
curl http://localhost:8080/api/products
```

#### 3. Récupérer un produit spécifique
```bash
curl http://localhost:8080/api/products/1
```

#### 4. Mettre à jour un produit
```bash
curl -X PUT http://localhost:8080/api/products/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Monitor 4K Pro",
    "description": "Écran professionnel 4K 27 pouces",
    "price": 499.99,
    "quantity": 10,
    "available": true
  }'
```

#### 5. Supprimer un produit
```bash
curl -X DELETE http://localhost:8080/api/products/1
```

#### 6. Rechercher des produits
```bash
curl "http://localhost:8080/api/products/search?name=monitor"
```

### Via JavaScript/Fetch

```javascript
// Créer un produit
const product = {
  name: "Clavier Mécanique",
  description: "Clavier gaming RGB",
  price: 149.99,
  quantity: 25,
  available: true
};

fetch('http://localhost:8080/api/products', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify(product)
})
.then(res => res.json())
.then(data => console.log('Créé:', data));

// Récupérer tous les produits
fetch('http://localhost:8080/api/products')
  .then(res => res.json())
  .then(data => console.log('Produits:', data));

// Rechercher
fetch('http://localhost:8080/api/products/search?name=clavier')
  .then(res => res.json())
  .then(data => console.log('Résultats:', data));
```

---

## 🧪 Endpoints API

### Base URL
```
http://localhost:8080/api/products
```

### Tous les endpoints

```
GET    /api/products              # Lister tous
GET    /api/products/{id}         # Récupérer un
POST   /api/products              # Créer
PUT    /api/products/{id}         # Mettre à jour
DELETE /api/products/{id}         # Supprimer
GET    /api/products/search       # Rechercher par nom
```

### Codes de réponse HTTP

| Code | Signification | Exemple |
|------|---------------|---------|
| **200** | OK | GET, PUT réussis |
| **201** | Created | POST réussi |
| **204** | No Content | DELETE réussi |
| **400** | Bad Request | Données invalides |
| **404** | Not Found | Ressource inexistante |
| **500** | Server Error | Erreur serveur |

---

## 🚨 Codes de statut courants

### Succès
- **200 OK** : La requête a réussi
- **201 Created** : Le produit a été créé avec succès
- **204 No Content** : Le produit a été supprimé avec succès

### Erreurs client
- **400 Bad Request** : Données invalides ou manquantes
  - Message : "Le nom doit contenir entre 3 et 100 caractères"
  - Message : "Le prix doit être supérieur à 0"
  
- **404 Not Found** : Le produit n'existe pas
  - Message : "Produit non trouvé avec l'id: 999"

### Erreurs serveur
- **500 Internal Server Error** : Erreur serveur interne

---

## 🔐 Sécurité

### Points de sécurité

- ✅ **Validation des entrées** : Toutes les données sont validées
- ✅ **Injection SQL** : Protégée via JPA/Parameterized Queries
- ✅ **Gestion des erreurs** : Pas d'exposition de détails internes
- ✅ **Types de contenu** : JSON uniquement, pas d'exécution de scripts

### À implémenter (future)

- [ ] Authentification JWT
- [ ] Autorisation basée sur les rôles (RBAC)
- [ ] Rate limiting
- [ ] HTTPS/TLS
- [ ] CORS configuré
- [ ] Audit logging

---

## 🐛 Dépannage

### Problème : "Connection refused" lors du démarrage

**Solution :**
```bash
# Vérifiez que MySQL est démarré
# Windows
net start MySQL80

# Linux/Mac
sudo systemctl start mysql
# ou
brew services start mysql
```

### Problème : "Access denied for user 'root'"

**Solution :**
Vérifiez les identifiants dans `application.properties` :
```properties
spring.datasource.username=root
spring.datasource.password=votre_mot_de_passe
```

### Problème : "Column 'created_at' not found"

**Solution :**
Les tables sont créées automatiquement. Sinon, créez-les manuellement :
```sql
USE product_db;

CREATE TABLE products (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  description VARCHAR(500),
  price DECIMAL(10, 2) NOT NULL,
  quantity INT DEFAULT 0,
  available BOOLEAN DEFAULT true,
  created_at DATETIME NOT NULL,
  updated_at DATETIME
);
```

### Logs

Les logs sont affichés dans la console. Pour plus de détails :

```properties
# application.properties
spring.jpa.show-sql=true
logging.level.root=INFO
logging.level.com.eadl.product_management=DEBUG
```

---

## 📚 Documentation complète

Pour des informations détaillées, consultez :

- **[API Reference](./api.md)** - Tous les endpoints avec exemples
- **[Architecture MVC](./architecture.md)** - Design et flux de données
- **[Configuration](./configurations.md)** - Propriétés et paramétrage
- **[Commits](./commits.md)** - Historique du développement

---

## 👥 Équipe

**Développeur:** Steph DevOps  
**Email:** stephen.deutou@gmail.com  
**Site:** https://www.stephdeutou.com  

**Licence:** IUC License  
**Plus d'infos:** https://kmergenius.com/licenses/iuc/

---

## 🤝 Contribution

Les contributions sont bienvenues ! 

1. Fork le repository
2. Créez une branche pour votre feature (`git checkout -b feature/AmazingFeature`)
3. Committez vos changements (`git commit -m 'Add some AmazingFeature'`)
4. Poussez vers la branche (`git push origin feature/AmazingFeature`)
5. Ouvrez une Pull Request

---

## 📋 Checklist de démarrage

- [ ] Cloner le repository
- [ ] Installer Java 21
- [ ] Installer MySQL 8+
- [ ] Créer la base de données `product_db`
- [ ] Configurer les identifiants MySQL dans `application.properties`
- [ ] Compiler avec `mvn clean package`
- [ ] Lancer l'application avec `mvn spring-boot:run`
- [ ] Accéder à http://localhost:8080/swagger-ui.html
- [ ] Tester les endpoints dans Swagger UI
- [ ] Lire la [documentation API](./api.md)

---

## 📞 Support

Pour toute question ou problème :

1. **Consultez la documentation** : [Documentation complète](./architecture.md)
2. **Vérifiez les erreurs** : Consultez les logs dans la console
3. **Testez avec Swagger** : http://localhost:8080/swagger-ui.html
4. **Contactez l'équipe** : stephen.deutou@gmail.com

---

## 📅 Versions

| Version | Date | Notes |
|---------|------|-------|
| 1.0.0 | 2025-11-17 | Version initiale |

---

## 🎓 Ressources utiles

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [OpenAPI 3.0 Specification](https://spec.openapis.org/oas/v3.0.3)
- [Swagger UI](https://swagger.io/tools/swagger-ui/)
- [MySQL Documentation](https://dev.mysql.com/doc/)
- [REST API Best Practices](https://restfulapi.net/)

---

## 📄 Licence

Ce projet est licencié sous la **IUC License**. Voir [IUC License](https://kmergenius.com/licenses/iuc/) pour plus de détails.

---

**Dernière mise à jour:** 17 novembre 2025  
**Version documentation:** 1.0.0  
**État:** Production ✅
